import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './LoginPage/Login.jsx';
import User from './UserPage/User.jsx';
import Admin from './AdminPage/Admin.jsx';  // Import Admin component
import AdminChat from './AdminChat/AdminChat.jsx';
import UserChat from './UserChat/UserChat.jsx';


createRoot(document.getElementById('root')).render(
    <StrictMode>
        <Router>
            <Routes>
                <Route path="/" element={<Login />} />  {/* Login Route */}
                <Route path="/user/:userId" element={<User />} />  {/* User Route */}
                <Route path="/admin/:userId" element={<Admin />} />  {/* Admin Route */}
                <Route path="/adminchat" element={<AdminChat />} />  {/* Admin Route */}
                <Route path="/userchat" element={<UserChat />} />  {/* Admin Route */}
                

            </Routes>
        </Router>
    </StrictMode>
);
