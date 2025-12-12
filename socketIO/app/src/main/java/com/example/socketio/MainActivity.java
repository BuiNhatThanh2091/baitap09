package com.example.socketio;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    private EditText etMessage;
    private Button btnSend;
    private RecyclerView rvMessages;
    private MessageAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Init UI
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        rvMessages = findViewById(R.id.rvMessages);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        // 2. Init Socket
        try {
            // Lưu ý: Dùng 10.0.2.2 nếu chạy trên Emulator, IP LAN nếu chạy máy thật
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // 3. Connect & Listeners
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> Toast.makeText(this, "Connected to Server", Toast.LENGTH_SHORT).show()));
        mSocket.on("chat_message", onNewMessage);

        // 4. Send Message Event
        btnSend.setOnClickListener(v -> {
            String content = etMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                // Tạo JSON object gửi lên server
                org.json.JSONObject data = new org.json.JSONObject();
                try {
                    data.put("sender", "Customer"); // Xác định mình là Khách hàng
                    data.put("msg", content);
                    mSocket.emit("chat_message", data);
                    etMessage.setText("");
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Listener nhận tin nhắn từ Server
    private io.socket.emitter.Emitter.Listener onNewMessage = new io.socket.emitter.Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(() -> {
                org.json.JSONObject data = (org.json.JSONObject) args[0]; // Server trả về JSON
                try {
                    String sender = data.getString("sender");
                    String msg = data.getString("msg");
                    // Thêm vào list và cập nhật giao diện
                    messageList.add(new Message(sender, msg));
                    adapter.notifyItemInserted(messageList.size() - 1);
                    rvMessages.scrollToPosition(messageList.size() - 1);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("chat_message", onNewMessage);
    }
}