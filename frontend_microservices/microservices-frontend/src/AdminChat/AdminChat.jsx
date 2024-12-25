import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import 'bootstrap/dist/css/bootstrap.min.css';

const SOCKET_URL = "http://chatapp.localhost/ws";

const api = axios.create({
  baseURL: 'http://userapp.localhost/api/users',
});

const AdminChat = () => {
  const [users, setUsers] = useState([]);
  const [chatHistory, setChatHistory] = useState({});
  const [messageInput, setMessageInput] = useState('');
  const [client, setClient] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await api.get('', { headers: { Authorization: `Bearer ${sessionStorage.getItem("token")}` } });
        setUsers(response.data || []);
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    };

    fetchUsers();

    const socket = new SockJS(SOCKET_URL);
    const stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log('Connected to WebSocket');
        stompClient.subscribe('/user/admin/queue/messages', (message) => {
          const { senderId, content } = JSON.parse(message.body);
          setChatHistory((prev) => ({
            ...prev,
            [senderId]: [...(prev[senderId] || []), { sender: 'user', content }],
          }));
        });
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      },
      reconnectDelay: 5000,
    });

    stompClient.activate();
    setClient(stompClient);

    return () => {
      stompClient.deactivate();
    };
  }, []);

  const sendMessage = (userId) => {
    if (!messageInput.trim()) return;

    setChatHistory((prev) => ({
      ...prev,
      [userId]: [...(prev[userId] || []), { sender: 'admin', content: messageInput }],
    }));

    if (client) {
      client.publish({
        destination: `/app/sentToUser/${userId}`,
        body: JSON.stringify({ senderId: 'admin', content: messageInput }),
      });
    }

    setMessageInput('');
  };

  return (
    <div className="container-fluid p-4">
      <h2 className="text-center mb-4">Admin Chat</h2>
      <div className="row">
        {users.map((user) => (
          <div key={user.id} className="col-md-4 mb-4">
            <div className="card">
              <div className="card-header text-center">
                <h5>{user.name} / {user.username}</h5>
              </div>
              <div className="card-body">
                <div className="chat-box mb-3" style={{ maxHeight: '200px', overflowY: 'scroll', border: '1px solid #ccc', padding: '10px' }}>
                  {chatHistory[user.id]?.map((msg, index) => (
                    <div key={index} className={msg.sender === 'admin' ? 'text-right' : 'text-left'}>
                      <strong>{msg.sender === 'admin' ? 'You' : user.name}: </strong>
                      <span>{msg.content}</span>
                    </div>
                  )) || <p className="text-muted">No messages yet</p>}
                </div>
                <textarea
                  className="form-control mb-3"
                  value={messageInput}
                  onChange={(e) => setMessageInput(e.target.value)}
                  placeholder="Type your message..."
                  rows="2"
                ></textarea>
                <button className="btn btn-primary w-100" onClick={() => sendMessage(user.id)}>
                  Send
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminChat;
