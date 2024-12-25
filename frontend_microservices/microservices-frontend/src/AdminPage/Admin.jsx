import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate, useLocation } from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';

const userApi = axios.create({
    baseURL: 'http://userapp.localhost/api/users',
});

const deviceApi = axios.create({
    baseURL: 'http://deviceapp.localhost/api/devices',
});

    

const Admin = () => {

    const navigate = useNavigate();  // Moved inside Admin component
    const location = useLocation();  // Moved inside Admin component
    const { userId } = useParams();  // useParams to get userId from the URL
    
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [userForm, setUserForm] = useState({ name: '', username: '', password: '', role: '' });
    const [deviceForm, setDeviceForm] = useState({ description: '', address: '', energyConsumption: '', userId: '' });
    const [isEditingUser, setIsEditingUser] = useState(false);
    const [isEditingDevice, setIsEditingDevice] = useState(false);
    const [currentUserId, setCurrentUserId] = useState(null);
    const [currentDeviceId, setCurrentDeviceId] = useState(null);



    useEffect(() => {
        if (!location.state || !location.state.userId || location.state.userId !== userId) {
            // Redirect to login if state userId is not present or doesn't match the URL's userId
            navigate('/');
        } else {
            console.log("am ajuns aici");
            fetchUsers();
            fetchDevices();
        }
    }, [userId, location.state, navigate]);

    const fetchUsers = async () => {
        try {
            const response = await userApi.get('', { headers: { Authorization: `Bearer ${location.state.token}` } });
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const fetchDevices = async () => {
        try {
            const response = await deviceApi.get('', { headers: { Authorization: `Bearer ${location.state.token}` } });
            setDevices(response.data);
        } catch (error) {
            console.error('Error fetching devices:', error);
        }
    };

    const handleUserSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditingUser) {
                await userApi.put(`/${currentUserId}`, userForm, { headers: { Authorization: `Bearer ${location.state.token}` } });
            } else {
                await userApi.post('', userForm, { headers: { Authorization: `Bearer ${location.state.token}` } });
            }
            fetchUsers();
            resetUserForm();
        } catch (error) {
            console.error('Error submitting user:', error);
        }
    };

    const handleDeviceSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditingDevice) {
                await deviceApi.put(`/${currentDeviceId}`, deviceForm, { headers: { Authorization: `Bearer ${location.state.token}` } });
            } else {
                await deviceApi.post('', deviceForm, { headers: { Authorization: `Bearer ${location.state.token}` } });
            }
            fetchDevices();
            resetDeviceForm();
        } catch (error) {
            console.error('Error submitting device:', error);
        }
    };

    const deleteUser = async (id) => {
        try {
            await userApi.delete(`/${id}`, { headers: { Authorization: `Bearer ${location.state.token}` } });
            fetchUsers();
            fetchDevices();
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };

    const deleteDevice = async (id) => {
        try {
            await deviceApi.delete(`/${id}`, { headers: { Authorization: `Bearer ${location.state.token}` } });
            fetchDevices();
        } catch (error) {
            console.error('Error deleting device:', error);
        }
    };

    const editUser = (user) => {
        setUserForm({ name: user.name, username: user.username, password: '', role: user.role });
        setCurrentUserId(user.id);
        setIsEditingUser(true);
    };

    const editDevice = (device) => {
        setDeviceForm({ description: device.description, address: device.address, energyConsumption: device.energyConsumption, userId: device.userId });
        setCurrentDeviceId(device.id);
        setIsEditingDevice(true);
    };

    const resetUserForm = () => {
        setUserForm({ name: '', username: '', password: '', role: '' });
        setCurrentUserId(null);
        setIsEditingUser(false);
    };

    const resetDeviceForm = () => {
        setDeviceForm({ description: '', address: '', energyConsumption: '', userId: '' });
        setCurrentDeviceId(null);
        setIsEditingDevice(false);
    };

    return (
        <div className="container my-5">
            <h1 className="text-center">Admin Dashboard</h1>
    
            {/* Section for managing users */}
            <div className="my-4">
                <h2>Manage Users</h2>
                <form onSubmit={handleUserSubmit}>
                    <div className="form-group">
                        <label>Name</label>
                        <input
                            type="text"
                            className="form-control"
                            value={userForm.name}
                            onChange={(e) => setUserForm({ ...userForm, name: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Username</label>
                        <input
                            type="text"
                            className="form-control"
                            value={userForm.username}
                            onChange={(e) => setUserForm({ ...userForm, username: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Password</label>
                        <input
                            type="password"
                            className="form-control"
                            value={userForm.password}
                            onChange={(e) => setUserForm({ ...userForm, password: e.target.value })}
                        />
                    </div>
                    <div className="form-group">
                        <label>Role</label>
                        <input
                            type="text"
                            className="form-control"
                            value={userForm.role}
                            onChange={(e) => setUserForm({ ...userForm, role: e.target.value })}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary mt-3">
                        {isEditingUser ? 'Update User' : 'Create User'}
                    </button>
                    {isEditingUser && (
                        <button className="btn btn-secondary mt-3 ms-3" onClick={resetUserForm}>
                            Cancel
                        </button>
                    )}
                </form>
    
                <ul className="list-group my-3">
                    {users.map((user) => (
                        <li key={user.id} className="list-group-item d-flex justify-content-between">
                            <span>{user.id} {user.name} ({user.username}) - {user.role}</span>
                            <div>
                                <button className="btn btn-warning me-2" onClick={() => editUser(user)}>Edit</button>
                                <button className="btn btn-danger" onClick={() => deleteUser(user.id)}>Delete</button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
    
            {/* Section for managing devices */}
            <div className="my-4">
                <h2>Manage Devices</h2>
                <form onSubmit={handleDeviceSubmit}>
                    <div className="form-group">
                        <label>Description</label>
                        <input
                            type="text"
                            className="form-control"
                            value={deviceForm.description}
                            onChange={(e) => setDeviceForm({ ...deviceForm, description: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Address</label>
                        <input
                            type="text"
                            className="form-control"
                            value={deviceForm.address}
                            onChange={(e) => setDeviceForm({ ...deviceForm, address: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Energy Consumption</label>
                        <input
                            type="number"
                            className="form-control"
                            value={deviceForm.energyConsumption}
                            onChange={(e) => setDeviceForm({ ...deviceForm, energyConsumption: e.target.value })}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>User ID</label>
                        <input
                            type="text"
                            className="form-control"
                            value={deviceForm.userId}
                            onChange={(e) => setDeviceForm({ ...deviceForm, userId: e.target.value })}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary mt-3">
                        {isEditingDevice ? 'Update Device' : 'Create Device'}
                    </button>
    
                    {/* New Action Button Below the Create Device Button */}
                    <button
                        type="button"
                        className="btn btn-info mt-3 ms-3"
                        onClick={() => window.location.href = '/adminchat'} // Example onClick action
                    >
                        Chat
                    </button>
    
                    {isEditingDevice && (
                        <button className="btn btn-secondary mt-3 ms-3" onClick={resetDeviceForm}>
                            Cancel
                        </button>
                    )}
                </form>
    
                <ul className="list-group my-3">
                    {devices.map((device) => (
                        <li key={device.id} className="list-group-item d-flex justify-content-between">
                            <span>{device.id} {device.description} ({device.address}) {device.energyConsumption} kWh - {device.userId}</span>
                            <div>
                                <button className="btn btn-warning me-2" onClick={() => editDevice(device)}>Edit</button>
                                <button className="btn btn-danger" onClick={() => deleteDevice(device.id)}>Delete</button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
    
};

export default Admin;
