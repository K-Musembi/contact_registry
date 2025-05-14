import React, { useState, useEffect } from 'react';
import GenderChart from '../components/charts/GenderChart';
import CountyChart from '../components/charts/CountyChart';
import ContactTable from '../components/ContactTable';
import { getGenderStats, getTopCounties, getRecentContacts } from '../services/apiContactService';

function DashboardPage() {
  const [genderData, setGenderData] = useState([]);
  const [countyData, setCountyData] = useState([]);
  const [recentContacts, setRecentContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError('');
        const [genderRes, countyRes, recentRes] = await Promise.all([
          getGenderStats(),
          getTopCounties(),
          getRecentContacts()
        ]);
        setGenderData(genderRes);
        setCountyData(countyRes);
        setRecentContacts(recentRes);
      } catch (err) {
        console.error("Failed to fetch dashboard data:", err);
        setError('Failed to load dashboard data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <p className="loading">Loading dashboard...</p>;
  if (error) return <p className="error">{error}</p>;

  const recentContactsColumns = [
    { header: 'Name', accessor: 'fullName' },
    { header: 'Email', accessor: 'email' },
    { header: 'Phone', accessor: 'phone' },
    { header: 'D.O.B', accessor: 'dateOfBirth' },
    { header: 'Gender', accessor: 'gender' },
    { header: 'County', accessor: 'countyName' },
  ];

  return (
    <div className="container">
      <h1 className="page-header">Admin Dashboard</h1>
      <div className="dashboard-grid">
        <div className="chart-container">
          <h2>Contacts by Gender</h2>
          <GenderChart data={genderData} />
        </div>
        <div className="chart-container">
          <h2>Contacts by Top 5 Counties</h2>
          <CountyChart data={countyData} />
        </div>
      </div>
      <div className="recent-contacts-container" style={{marginTop: '20px'}}>
        <h2>Recently Added Contacts (Top 5)</h2>
        <ContactTable contacts={recentContacts} columns={recentContactsColumns} />
      </div>
    </div>
  );
}

export default DashboardPage;
