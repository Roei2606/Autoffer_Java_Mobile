package com.example.autofferandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.chat_sdk.network.ChatManager;


import com.example.autofferandroid.R;
import com.example.autofferandroid.activities.ChatActivity;
import com.example.autofferandroid.adapters.UserAdapter;
import com.example.core_models_sdk.models.User;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UsersFragment extends Fragment {

    private UserAdapter adapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionManager = SessionManager.getInstance();

        adapter = new UserAdapter(
                new ArrayList<>(),
                requireContext(),
                user -> {
                    String currentUserId = sessionManager.getCurrentUserId();
                    String otherUserId = user.getId();

                    Log.d("UsersFragment", "Creating chat with userId=" + otherUserId + ", currentUserId=" + currentUserId);

                    ChatManager chatManager = new ChatManager();
                    chatManager.getOrCreateChat(currentUserId, otherUserId)
                            .thenAccept(chat -> {
                                Log.d("UsersFragment", "Chat created/fetched with ID: " + chat.getId());
                                Intent intent = new Intent(getContext(), ChatActivity.class);
                                intent.putExtra("chatId", chat.getId());
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("otherUserId", otherUserId);
                                startActivity(intent);
                            })
                            .exceptionally(e -> {
                                Log.e("UsersFragment", "Error creating chat", e);
                                return null;
                            });
                }
        );

        recyclerView.setAdapter(adapter);

        loadUsersFromServer();

        return view;
    }
    private void loadUsersFromServer() {
        UserManager usersManager = new UserManager();
        String currentUserId = sessionManager.getCurrentUserId();

        usersManager.getAllUsers()
                .thenAccept(users -> {
                    // סינון המשתמש המחובר עצמו
                    List<User> filteredUsers = users.stream()
                            .filter(user -> !user.getId().equals(currentUserId))
                            .collect(Collectors.toList());

                    requireActivity().runOnUiThread(() -> adapter.setUsers(filteredUsers));
                })
                .exceptionally(e -> {
                    Log.e("UsersFragment", "Error loading users", e);
                    return null;
                });
    }
}

