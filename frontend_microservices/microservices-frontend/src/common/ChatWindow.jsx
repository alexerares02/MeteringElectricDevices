import React, { useState } from "react";

const ChatWindow = ({ messages, onSendMessage }) => {
  const [message, setMessage] = useState("");

  const handleSend = () => {
    if (message.trim()) {
      onSendMessage(message);
      setMessage("");
    }
  };

  return (
    <div className="d-flex flex-column vh-100">
      <div
        className="flex-grow-1 overflow-auto border rounded p-3 mb-3"
        style={{ backgroundColor: "#f8f9fa" }}
      >
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`mb-2 ${
              msg.senderId === "admin" ? "text-end text-primary" : "text-start text-success"
            }`}
          >
            <strong>{msg.senderId}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <div className="input-group">
        <input
          type="text"
          className="form-control"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type a message..."
        />
        <button className="btn btn-primary" onClick={handleSend}>
          Send
        </button>
      </div>
    </div>
  );
};

export default ChatWindow;
