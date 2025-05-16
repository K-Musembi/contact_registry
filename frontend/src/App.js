import React from 'react';
import { BrowserRouter as Router, Routes, Route, Outlet } from 'react-router-dom';
import Navbar from './components/Navbar';
import LoginPage from './pages/LoginPage';
import SignUpPage from './pages/SignUpPage';
import DashboardPage from './pages/DashboardPage';
import AddContactPage from './pages/AddContactPage';
import AddCountyPage from './pages/AddCountyPage';
import ContactsReportPage from './pages/ContactsReportPage';
import './App.css';

function Layout() {
  return (
    <>
      <Navbar />
      <main>
        <Outlet /> {/* child routes rendering */}
      </main>
    </>
  );
}

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<DashboardPage />} />
          <Route path="login" element={<LoginPage />} />
          <Route path="signup" element={<SignUpPage />} />
          <Route path='logout' element={<LoginPage />} />
          <Route path="add-contact" element={<AddContactPage />} />
          <Route path="add-county" element={<AddCountyPage />} />
          <Route path="contacts-report" element={<ContactsReportPage />} />
          <Route path="*" element={
            <div className="container">
                <h1 className="page-header">404 - Page Not Found</h1>
                <p>Sorry, the page you are looking for does not exist.</p>
            </div>
            } />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
