import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { addContact, getAllCounties } from '../services/contactService';

function AddContactPage() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    gender: '',
    county: '',
  });
  const [availableCounties, setAvailableCounties] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    getAllCounties().then(setAvailableCounties);
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.email || !formData.phone || !formData.gender || !formData.county) {
      setError('Name, Email, Phone, Gender and County are required.');
      return;
    }
    setError('');
    setSuccessMessage('');
    setIsSubmitting(true);
    try {
      await addContact(formData);
      setSuccessMessage('Contact added successfully!');
      setFormData({ name: '', email: '', phone: '', gender: '', county: '' });
      // Optionally navigate away after a delay
      setTimeout(() => navigate('/'), 2000);
    } catch (err) {
      console.error("Failed to add contact:", err);
      setError('Failed to add contact. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const genderOptions = [
    { value: '', label: 'Select Gender' },
    { value: 'Male', label: 'Male' },
    { value: 'Female', label: 'Female' },
    { value: 'NotSpecified', label: 'Not Specified' },
  ];

  return (
    <div className="container">
      <h1 className="page-header">Add New Contact</h1>
      <div className="form-container">
        {error && <p className="error" style={{border: '1px solid red', padding: '10px', marginBottom: '10px'}}>{error}</p>}
        {successMessage && <p style={{border: '1px solid green', color: 'green', padding: '10px', marginBottom: '10px'}}>{successMessage}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Full Name</label>
            <input type="text" id="name" name="name" value={formData.name} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="phone">Phone</label>
            <input type="tel" id="phone" name="phone" value={formData.phone} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label htmlFor="gender">Gender</label>
            <select id="gender" name="gender" value={formData.gender} onChange={handleChange} required>
                {genderOptions.map(g => <option key={g.value} value={g.value}>{g.label}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="county">County</label>
            <select id="county" name="county" value={formData.county} onChange={handleChange} required>
              <option value="">Select County</option>
              {availableCounties.map(c => <option key={c.value} value={c.value}>{c.label}</option>)}
            </select>
          </div>
          <button type="submit" className="button" disabled={isSubmitting}>
            {isSubmitting ? 'Adding...' : 'Add Contact'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddContactPage;
