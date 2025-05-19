import React, { useState } from 'react';
import { loginUser, updateUser } from '../services/apiContactService';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showChangePassword, setShowChangePassword] = useState(false);
  const [changePasswordData, setChangePasswordData] = useState({ username: '', password: '' });
  const [changePasswordMsg, setChangePasswordMsg] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');
    setIsSubmitting(true);
    try {
      const res = await loginUser(formData);
      setSuccessMessage(`Welcome ${res.username}`);
      setTimeout(() => navigate('/'), 1500);
      localStorage.setItem('user', JSON.stringify(res));  // to track logged-in user
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // Change password handlers
  const handleChangePasswordInput = (e) => {
    const { name, value } = e.target;
    setChangePasswordData(prev => ({ ...prev, [name]: value }));
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    setChangePasswordMsg('');
    try {
      await updateUser(changePasswordData);
      setChangePasswordMsg('Password updated successfully!');
    } catch (err) {
      setChangePasswordMsg(err.response?.data?.message || 'Failed to update password.');
    }
  };

  return (
    <div className="container">
      <h1 className="page-header">Login</h1>
      <div className="form-container">
        {error && <p className="error">{error}</p>}
        {successMessage && <p style={{border: '1px solid green', color: 'green', padding: '10px', marginBottom: '10px'}}>{successMessage}</p>}
        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input type="text" id="username" name="username" value={formData.username} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input type="password" id="password" name="password" value={formData.password} onChange={handleChange} required />
          </div>
          <button type="submit" className="button" disabled={isSubmitting}>
            {isSubmitting ? 'Logging in...' : 'Login'}
          </button>
        </form>
        <button className="button button-secondary" style={{marginTop: 10}} onClick={() => setShowChangePassword(v => !v)}>
          {showChangePassword ? 'Hide Change Password' : 'Change Password'}
        </button>
        {showChangePassword && (
          <form onSubmit={handleChangePassword} style={{marginTop: 20}}>
            <div className="form-group">
              <label htmlFor="cp-username">Username</label>
              <input type="text" id="cp-username" name="username" value={changePasswordData.username} onChange={handleChangePasswordInput} required />
            </div>
            <div className="form-group">
              <label htmlFor="cp-password">New Password</label>
              <input type="password" id="cp-password" name="password" value={changePasswordData.password} onChange={handleChangePasswordInput} required />
            </div>
            <button type="submit" className="button">Update Password</button>
            {changePasswordMsg && <p style={{marginTop: 10}}>{changePasswordMsg}</p>}
          </form>
        )}
      </div>
    </div>
  );
}

export default LoginPage;