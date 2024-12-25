import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Navigate } from 'react-router-dom';

const SOCKET_URL = "http://chatapp.localhost/ws";

const api = axios.create({
  baseURL: 'http://usersapp.localhost/api/users',
});

const UserChat = () => {
  const [chatHistory, setChatHistory] = useState([]);
  const [messageInput, setMessageInput] = useState('');
  const [client, setClient] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const initialize = async () => {
      try {
        if (true) {
          setIsAuthenticated(true);
          const userId = sessionStorage.getItem("userid");
          if (!userId) {
            console.error("User ID not found.");
            return;
          }

          setUserId(userId);

          // WebSocket setup
          const socket = new SockJS(SOCKET_URL);
          const stompClient = new Client({
            webSocketFactory: () => socket,
            debug: (str) => console.log(str),
            onConnect: () => {
              console.log('Connected to WebSocket');
              stompClient.subscribe(`/user/${userId}/queue/messages`, (message) => {
                const { senderId, content } = JSON.parse(message.body);
                setChatHistory((prev) => [...prev, { sender: 'admin', content }]);
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
        } else {
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error during initialization:", error);
        setIsAuthenticated(false);
      }
    };

    initialize();
  }, []);

  const sendMessage = () => {
    if (!messageInput.trim()) return;

    setChatHistory((prev) => [...prev, { sender: 'user', content: messageInput }]);

    if (client && userId) {
      client.publish({
        destination: `/app/sentToAdmin`,
        body: JSON.stringify({ senderId: userId, content: messageInput }),
      });
    }

    setMessageInput('');
  };

  if (isAuthenticated === null) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="container mt-4">
      <div className="card shadow-sm">
        <div className="card-header bg-primary text-white">
          <h2 className="text-center mb-0">Chat with Admin</h2>
        </div>
        <div className="card-body">
          <div
            className="chat-box mb-3 bg-light"
            style={{
              maxHeight: '400px',
              overflowY: 'scroll',
              border: '1px solid #dee2e6',
              padding: '10px',
              borderRadius: '5px',
            }}
          >
            {chatHistory.length > 0 ? (
              chatHistory.map((msg, index) => (
                <div key={index} className={msg.sender === 'user' ? 'text-right' : 'text-left'}>
                  <strong>{msg.sender === 'user' ? 'You' : 'Admin'}: </strong>
                  <span>{msg.content}</span>
                </div>
              ))
            ) : (
              <p className="text-muted">No messages yet</p>
            )}
          </div>
          <textarea
            className="form-control mb-3"
            value={messageInput}
            onChange={(e) => setMessageInput(e.target.value)}
            placeholder="Type your message..."
            rows="3"
          ></textarea>
          <button className="btn btn-primary w-100" onClick={sendMessage}>
            Send
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserChat;
