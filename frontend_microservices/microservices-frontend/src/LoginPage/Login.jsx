import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://userapp.localhost/api/users',
});

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate(); // Get the navigate function

    const handleLogin = async () => {
        try {
            const response = await api.post('/login', { username, password });
            console.log('Login response:'); // Log the entire response

            // Ensure the userId and role are available in the response
            if (response.data && response.data.userId && response.data.role && response.data.token) {
                const { userId, role, token, duration } = response.data;
                sessionStorage.setItem("token", response.data.token);
                sessionStorage.setItem("userid",response.data.userId);
                // Redirect based on the role
                if (role === 'admin') {
                    
                    navigate(`/admin/${userId}`, { state: { userId, role, token } }); // Redirect to admin page
                } else if (role === 'user') {
                    navigate(`/user/${userId}`, { state: { userId, role, token } }); // Redirect to user page
                } else {
                    console.error('Unknown role received from backend.');
                }
            } else {
                console.error('User ID or role not found in response.');
            }
        } catch (error) {
            console.error('Error logging in:', error);
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4" style={{ width: '400px' }}>
                <h2 className="text-center">Login</h2>
                <form onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
                    <div className="form-group my-3">
                        <label htmlFor="username">Username:</label>
                        <input
                            type="text"
                            className="form-control"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder="Enter Username"
                        />
                    </div>
                    <div className="form-group my-3">
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter password"
                        />
                    </div>
                    <button type="submit" className="btn btn-primary w-100 my-3">
                        Login
                    </button>
                </form>
            </div>
        </div>
    );
}
