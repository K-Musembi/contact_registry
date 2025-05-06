import React, { useState, useEffect } from 'react';
import ContactTable from '../components/ContactTable';
import { getContactsByCounty, getContactsByStatePdf, getAllCounties } from '../services/contactService';

function ContactsReportPage() {
  const [selectedCounty, setSelectedCounty] = useState('');
  const [allCounties, setAllCounties] = useState([]);
  const [contacts, setContacts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isPrinting, setIsPrinting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    getAllCounties().then(setAllCounties);
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

  const handlePrintReport = async () => {
    if (!selectedCounty || contacts.length === 0) {
      setError('No contacts to print.');
      return;
    }
    setError('');
    setIsPrinting(true);
    try {
      const pdfBlob = await getContactsByStatePdf(selectedCounty);
      const fileURL = URL.createObjectURL(pdfBlob);
      window.open(fileURL); // Opens the PDF in a new tab, browser handles print
      // Optional: revoke object URL after some time
      setTimeout(() => URL.revokeObjectURL(fileURL), 10000);
    } catch (err) {
      console.error("Failed to generate PDF report:", err);
      setError('Failed to generate PDF. Please check if the backend service is running or try again.');
    } finally {
      setIsPrinting(false);
    }
  };
  
  const reportTableColumns = [
    { header: 'Name', accessor: 'name' },
    { header: 'Email', accessor: 'email' },
    { header: 'Phone', accessor: 'phone' },
    { header: 'Gender', accessor: 'gender' },
    { header: 'County', accessor: 'county' },
  ];


  return (
    <div className="container">
      <h1 className="page-header">Contacts Report by County</h1>
      <div className="form-container" style={{maxWidth: 'none', marginBottom: '20px'}}>
        <div className="form-group" style={{ display: 'flex', gap: '10px', alignItems: 'flex-end' }}>
          <div style={{flexGrow: 1}}>
            <label htmlFor="stateSelect">Select County</label>
            <select id="stateSelect" value={selectedCounty} onChange={handleStateChange}>
              <option value="">-- Select a County --</option>
              {allCounties.map(c => <option key={c.value} value={c.value}>{c.label}</option>)}
            </select>
          </div>
          <button onClick={handleSearch} className="button" disabled={isLoading || !selectedCounty}>
            {isLoading ? 'Searching...' : 'Search Contacts'}
          </button>
          <button 
            onClick={handlePrintReport} 
            className="button button-secondary" 
            disabled={isPrinting || contacts.length === 0}
            style={{marginLeft: '10px'}}
          >
            {isPrinting ? 'Generating PDF...' : 'Print Report (PDF)'}
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
