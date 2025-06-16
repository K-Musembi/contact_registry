import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function LogoutPage() {
  const [confirm, setConfirm] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user'); // optional: remove user info
    setConfirm(true);
    setTimeout(() => {
      navigate('/login');
    }, 1200);
  };

  return (
    <div className="container">
      <h1 className="page-header">Logout</h1>
      {!confirm ? (
        <div style={{ textAlign: 'center', marginTop: '40px' }}>
          <p style={{ fontSize: '1.2em', marginBottom: '30px' }}>
            Do you want to logout?
          </p>
          <button className="button" onClick={handleLogout}>
            Yes, Logout
          </button>
        </div>
      ) : (
        <p style={{ textAlign: 'center', fontSize: '1.2em', marginTop: '30px' }}>
          You have been logged out. Redirecting to login...
        </p>
      )}
    </div>
  );
}

export default LogoutPage;
