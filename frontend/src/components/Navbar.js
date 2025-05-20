import React from 'react';
import { Link } from 'react-router-dom';

const isLoggedIn = () => {
  const jwtToken = localStorage.getItem('jwtToken');
  return jwtToken ? true : false;
}

function Navbar() {
  const loggedIn = isLoggedIn();

  return (
    <nav>
      <Link to="/">Dashboard</Link>
      {!loggedIn && <Link to="/signup">Admin</Link>}
      <Link to="/contacts-report">Contacts Report</Link>
      {loggedIn && <Link to="/add-contact">Add Contact</Link>}
      {loggedIn && <Link to="/add-county">Add County</Link>}
      {loggedIn && <Link to="/logout">Logout</Link>}
    </nav>
  );
}

export default Navbar;
