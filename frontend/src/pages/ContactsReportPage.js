import React, { useState, useEffect } from 'react';
import ContactTable from '../components/ContactTable';
import { getContactsByCounty, getAllCounties } from '../services/apiContactService';

function ContactsReportPage() {
  const [selectedCounty, setSelectedCounty] = useState('');
  const [allCounties, setAllCounties] = useState([]);
  const [contacts, setContacts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchCounties = async () => {
      try {
        const counties = await getAllCounties();
        setAllCounties(counties);
      } catch (err) {
        setError('Failed to load counties. Please try again later.');
      }
    };
    fetchCounties();
  }, []);

  const handleStateChange = (e) => {
    setSelectedCounty(e.target.value);
    setContacts([]); // Clear previous results when state changes
    setError('');
  };

  const handleSearch = async () => {
    if (!selectedCounty) {
      setError('Please select a county to search.');
      setContacts([]);
      return;
    }
    setError('');
    setIsLoading(true);
    try {
      const results = await getContactsByCounty(selectedCounty);
      setContacts(results);
      if (results.length === 0) {
        setError(`No contacts found for ${selectedCounty}.`);
      }
    } catch (err) {
      console.error("Failed to fetch contacts by county:", err);
      setError('Failed to fetch contacts. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handlePrintReport = () => {
    if (contacts.length === 0) {
      setError('No contacts to print.');
      return;
    }
    setError('');
    window.print();
  };

  const reportTableColumns = [
    { header: 'Name', accessor: 'fullName' },
    { header: 'Email', accessor: 'email' },
    { header: 'Phone', accessor: 'phone' },
    { header: 'D.O.B', accessor: 'dateOfBirth' },
    { header: 'Gender', accessor: 'gender' },
    { header: 'County', accessor: 'countyName' },
  ];


  return (
    <div className="container">
      <h1 className="page-header">Contacts Report by County</h1>
      <div className="form-container" style={{maxWidth: '600px', marginBottom: '20px'}}>
        <div className="form-group" style={{ display: 'flex', gap: '10px', alignItems: 'flex-end' }}>
          <div style={{flexGrow: 1}}>
            <label htmlFor="stateSelect">Select County</label>
            <select id="stateSelect" value={selectedCounty} onChange={handleStateChange}>
              <option value="">-- Select a County --</option>
              {allCounties.map(county => <option key={county.id} value={county.name}>{county.name}</option>)}
            </select>
          </div>
          <button onClick={handleSearch} className="button" disabled={isLoading || !selectedCounty}>
            {isLoading ? 'Searching...' : 'Search Contacts'}
          </button>
          <button 
            onClick={handlePrintReport} 
            className="button button-secondary" 
            disabled={contacts.length === 0}
            style={{marginLeft: '10px'}}
          >
            Print Report
          </button>
        </div>
        {error && <p className="error" style={{marginTop: '10px'}}>{error}</p>}
      </div>

      {contacts.length > 0 && (
        <ContactTable contacts={contacts} columns={reportTableColumns} />
      )}
    </div>
  );
}

export default ContactsReportPage;
