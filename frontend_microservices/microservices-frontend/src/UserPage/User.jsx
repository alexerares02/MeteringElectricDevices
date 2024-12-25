import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const api = axios.create({
    baseURL: 'http://deviceapp.localhost/api/devices', 
});

const User = () => {
    const { userId } = useParams(); // userId from URL parameters
    const navigate = useNavigate(); // for redirection
    const location = useLocation(); // to access state passed from Login
    const [devices, setDevices] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!location.state || !location.state.userId || location.state.userId !== userId) {
            navigate('/'); // Redirect to login if state userId is not valid
        } else {
            fetchDevices(); // Fetch devices for the user
            setupWebSocket(); // Set up WebSocket connection
        }
    }, [userId, location.state, navigate]);

    const fetchDevices = async () => {
        try {
            console.log('Fetching devices for user ID:', userId);
            const response = await api.get(`/user/${userId}`, { headers: { Authorization: `Bearer ${location.state.token}` } });
            setDevices(response.data);
        } catch (error) {
            console.error('Error fetching devices:', error.response ? error.response.data : error.message);
            setError('Failed to load devices.');
        }
    };

    const setupWebSocket = () => {
        const socket = new SockJS('http://monitoringapp.localhost/websocket-endpoint'); // Updated backend WebSocket endpoint
        const client = new Client({
            webSocketFactory: () => socket,
            debug: (str) => console.log(str),
            reconnectDelay: 5000,
        });

        client.onConnect = () => {
            console.log('Connected to WebSocket');

            // Subscribe to user-specific alerts
            client.subscribe('/topic/reply/'+userId, (message) => {
                console.log('Received WebSocket message:', message.body);
                alert(`Alert for user ${userId}: ${message.body}`);
            });
        };

        client.onStompError = (frame) => {
            console.error('STOMP error', frame);
        };

        client.activate();
    };

    return (
        <div className="container my-5">
            <h1 className="text-center">Your Devices</h1>

            {error && <div className="alert alert-danger">{error}</div>}

            {!error && devices.length === 0 && (
                <div className="alert alert-info">No devices found.</div>
            )}

            {devices.length > 0 && (
                <div className="row">
                    {devices.map((device, index) => (
                        <div key={index} className="col-md-4 my-3">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Device Name: {device.name}</h5>
                                    <p className="card-text">
                                        <strong>ID:</strong> {device.id}<br />
                                        <strong>Description:</strong> {device.description}<br />
                                        <strong>Address:</strong> {device.address}<br />
                                        <strong>Energy Consumption:</strong> {device.energyConsumption} kWh<br />
                                        <strong>User Id:</strong> {device.userId}<br />
                                    </p>
                                

                                </div>
                            </div>
                        </div>
                    ))}
                    {/* New Action Button Below the Create Device Button */}
                    <button
                        type="button"
                        className="btn btn-info mt-3 ms-3"
                        onClick={() => window.location.href = '/userchat'} // Example onClick action
                    >
                        Chat
                    </button>
                </div>
            )}
        </div>
    );
};

export default User;
