import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { addCounty } from '../services/apiContactService';

function AddCountyPage() {
  const [countyName, setCountyName] = useState('');
  const [countyCode, setCountyCode] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setCountyName(e.target.value);
  };

   const handleCodeChange = (e) => {
    setCountyCode(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!countyName.trim() || !countyCode.trim()) {
      setError('County name and code are required.');
      return;
    }
    setError('');
    setSuccessMessage('');
    setIsSubmitting(true);
    try {
      await addCounty(countyName, countyCode);
      setSuccessMessage(`County "${countyName}" with code "${countyCode}" added successfully!`);
      setCountyName('');
      setCountyCode('');
      setTimeout(() => {
        navigate('/'); 
      }, 2000);
    } catch (err) {
      console.error("Failed to add county:", err);
      setError(err.response?.data?.message || 'Failed to add county. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container">
      <h1 className="page-header">Add New County</h1>
      <div className="form-container">
        {error && <p className="error" style={{border: '1px solid red', padding: '10px', marginBottom: '10px'}}>{error}</p>}
        {successMessage && <p style={{border: '1px solid green', color: 'green', padding: '10px', marginBottom: '10px'}}>{successMessage}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="countyName">County Name</label>
            <input 
              type="text" 
              id="countyName" 
              name="countyName" 
              value={countyName} 
              onChange={handleChange} 
              required 
            />
          </div>
          <div className="form-group">
            <label htmlFor="countyCode">County Code</label>
            <input 
              type="number"
              id="countyCode" 
              name="countyCode" 
              value={countyCode} 
              onChange={handleCodeChange}
              required 
            />
          </div>
          <button type="submit" className="button" disabled={isSubmitting}>
            {isSubmitting ? 'Adding...' : 'Add County'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddCountyPage;
