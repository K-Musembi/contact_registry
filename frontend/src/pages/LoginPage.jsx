import React, { useState } from 'react';
import { loginUser } from '../services/apiContactService';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
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
      // Save JWT token
      if (res.token) {
        localStorage.setItem('jwtToken', res.token);
      }
      setTimeout(() => navigate('/'), 1500);
      localStorage.setItem('user', JSON.stringify(res));  // optional: keep user info
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please try again.');
    } finally {
      setIsSubmitting(false);
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
        <div style={{ textAlign: 'center', marginTop: 15 }}>
          <span>Forgot Password? </span>
          <span style={{ color: '#aaa', fontStyle: 'italic' }}>Reset Password (coming soon)</span>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
