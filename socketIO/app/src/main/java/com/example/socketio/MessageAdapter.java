package com.example.socketio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    // Xác định loại tin nhắn (Gửi đi hay Nhận được)
    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSender().equals("Customer")) {
            return TYPE_SENT; // Tin của mình (Customer) -> Hiển thị bên phải
        } else {
            return TYPE_RECEIVED; // Tin của Manager -> Hiển thị bên trái
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).tvContent.setText(message.getContent());
        } else {
            ((ReceivedViewHolder) holder).tvContent.setText(message.getContent()); // Có thể thêm tên Manager vào đây nếu muốn
        }
    }

    @Override
    public int getItemCount() { return messageList.size(); }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        SentViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        ReceivedViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}