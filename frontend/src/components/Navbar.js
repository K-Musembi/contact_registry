import React from 'react';
import { Link } from 'react-router-dom';

const isLoggedIn = () => {
  const user = localStorage.getItem('user');
  return user ? true : false;
}

function Navbar() {
  const loggedIn = isLoggedIn();

  return (
    <nav>
      <Link to="/">Dashboard</Link>
      <Link to="/signup">Sign In</Link>
      {loggedIn && <Link to="/add-contact">Add Contact</Link>}
      {loggedIn && <Link to="/add-county">Add County</Link>}
      {loggedIn && <Link to="/logout">Logout</Link>}
      <Link to="/contacts-report">Contacts Report</Link>
    </nav>
  );
}

export default Navbar;
