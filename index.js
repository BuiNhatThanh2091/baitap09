const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server);

// Trang web giả lập cho Manager chat
app.get('/', (req, res) => {
  res.sendFile(__dirname + '/manager.html');
});

io.on('connection', (socket) => {
  console.log('User connected: ' + socket.id);

  // Sự kiện: Khách hàng/Manager gửi tin nhắn
  socket.on('chat_message', (data) => {
    // data bao gồm: { sender: 'Customer' hoặc 'Manager', msg: 'Nội dung' }
    console.log(`Message from ${data.sender}: ${data.msg}`);
    
    // Gửi lại cho TẤT CẢ mọi người (để cả 2 bên cùng thấy)
    io.emit('chat_message', data); 
  });
});

server.listen(3000, () => {
  console.log('Listening on *:3000');
});