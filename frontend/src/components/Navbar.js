import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav>
      <Link to="/">Dashboard</Link>
      <Link to="/add-contact">Add Contact</Link>
      <Link to="/add-county">Add County</Link>
      <Link to="/contacts-report">Contacts Report</Link>
    </nav>
  );
}

export default Navbar;
