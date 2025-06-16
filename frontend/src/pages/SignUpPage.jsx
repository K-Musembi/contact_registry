import React, { useState } from 'react';
import { signUpUser } from '../services/apiContactService';
import { useNavigate } from 'react-router-dom';

function SignUpPage() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.username || !formData.password) {
      setError('All fields are required.');
      return;
    }
    setError('');
    setSuccessMessage('');
    setIsSubmitting(true);
    try {
      const res = await signUpUser(formData);
      setSuccessMessage(`User ${res.username} created!`);
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Signup failed. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container">
      <h1 className="page-header">Admin</h1>
      <div className="form-container">
        {error && <p className="error">{error}</p>}
        {successMessage && <p style={{border: '1px solid green', color: 'green', padding: '10px', marginBottom: '10px'}}>{successMessage}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input type="text" id="username" name="username" value={formData.username} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input type="password" id="password" name="password" value={formData.password} onChange={handleChange} required />
          </div>
          <button type="submit" className="button" disabled={isSubmitting}>
            {isSubmitting ? 'Signing Up...' : 'Sign Up'}
          </button>
        </form>
      </div>
        <p style={{ textAlign: 'center', marginTop: '20px' }}>
            Already have an account? <a href="/login">Login here</a>.
        </p>
    </div>
  );
}

export default SignUpPage;
