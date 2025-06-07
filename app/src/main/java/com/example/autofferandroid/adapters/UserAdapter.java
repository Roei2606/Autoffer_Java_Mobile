package com.example.autofferandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.autofferandroid.R;
import com.example.core_models_sdk.models.User;
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

        // קביעת ערכים בתצוגה
        String fullName = (user.getFirstName() + " " + user.getLastName()).replaceAll("^\"|\"$", "").trim();
        holder.fullNameTextView.setText(fullName);

        holder.emailTextView.setText(user.getEmail() != null ? user.getEmail() : "");
        holder.phoneTextView.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        //holder.profileTypeTextView.setText(user.getProfileType() != null ? user.getProfileType() : "");
        holder.addressTextView.setText(user.getAddress() != null ? user.getAddress() : "");

        // לחיצה על כפתור צ'אט
        holder.chatButton.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }



    public static class UserViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView fullNameTextView;
        MaterialTextView emailTextView;
        MaterialTextView phoneTextView;
        MaterialTextView profileTypeTextView;
        MaterialTextView addressTextView;
        ImageButton chatButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            fullNameTextView = itemView.findViewById(R.id.textViewFullName);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            phoneTextView = itemView.findViewById(R.id.textViewPhone);
            profileTypeTextView = itemView.findViewById(R.id.textViewProfileType);
            addressTextView = itemView.findViewById(R.id.textViewAddress);
            chatButton = itemView.findViewById(R.id.buttonChat);
        }
    }
}
