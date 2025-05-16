import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function LogoutPage() {
  const navigate = useNavigate();

  useEffect(() => {
    // Remove user info from localStorage
    localStorage.removeItem('user');
    // Optionally clear all localStorage: localStorage.clear();
    // Redirect to login after a short delay
    const timer = setTimeout(() => {
      navigate('/login');
    }, 1200);
    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div className="container">
      <h1 className="page-header">Logging Out</h1>
      <p style={{ textAlign: 'center', fontSize: '1.2em', marginTop: '30px' }}>
        You have been logged out. Redirecting to login...
      </p>
    </div>
  );
}

export default LogoutPage;
