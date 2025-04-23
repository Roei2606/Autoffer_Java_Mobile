package com.example.singlechatantgroupchatseminar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsdk.models.User;
import com.example.singlechatantgroupchatseminar.R;
import com.example.singlechatantgroupchatseminar.activities.ChatActivity;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private final OnUserClickListener listener;

    public UserAdapter(List<User> userList, Context context, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername().replaceAll("^\"|\"$", ""));


        // Handle chat button click
        holder.chatButton.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static void openChatActivity(Context context, String chatId, String currentUserId, String selectedUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatId", chatId);
        intent.putExtra("currentUserId", currentUserId);
        intent.putExtra("selectedUserId", selectedUserId);
        context.startActivity(intent);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView usernameTextView;
        ImageButton chatButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textViewUsername);
            chatButton = itemView.findViewById(R.id.buttonChat);
        }
    }
}
