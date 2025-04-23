package com.example.singlechatantgroupchatseminar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsdk.models.Chat;
import com.example.singlechatantgroupchatseminar.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(ChatDisplayItem item);
    }

    public static class ChatDisplayItem {
        public Chat chat;
        public String otherUsername;
        public String otherUserId;
        public String lastMessageTimeFormatted;

        public ChatDisplayItem(Chat chat, String otherUsername, String otherUserId, String lastMessageTimeFormatted) {
            this.chat = chat;
            this.otherUsername = otherUsername;
            this.otherUserId = otherUserId;
            this.lastMessageTimeFormatted = lastMessageTimeFormatted;
        }
    }

    private final List<ChatDisplayItem> chatList;
    private final OnChatClickListener listener;

    public ChatListAdapter(List<ChatDisplayItem> chatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatDisplayItem item = chatList.get(position);
        holder.nameTextView.setText(item.otherUsername);
        holder.lastMessageTextView.setText(item.chat.getLastMessage());
        holder.timestampTextView.setText(item.lastMessageTimeFormatted);

        holder.cardView.setOnClickListener(v -> listener.onChatClick(item));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView lastMessageTextView;
        TextView timestampTextView;
        MaterialCardView cardView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            lastMessageTextView = itemView.findViewById(R.id.textViewLastMessage);
            timestampTextView = itemView.findViewById(R.id.textViewTimestamp);
            cardView = itemView.findViewById(R.id.chatCardView);
        }
    }
}
