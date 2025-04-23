//package com.example.singlechatantgroupchatseminar.fragments;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatsdk.models.Chat;
//import com.example.chatsdk.models.User;
//import com.example.chatsdk.network.ChatManager;
//import com.example.chatsdk.network.UserManager;
//import com.example.chatsdk.utils.SessionManager;
//import com.example.singlechatantgroupchatseminar.R;
//import com.example.singlechatantgroupchatseminar.activities.ChatActivity;
//import com.example.singlechatantgroupchatseminar.adapters.ChatListAdapter;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.CompletableFuture;
//
//public class ChatsFragment extends Fragment {
//
//    private ChatListAdapter chatAdapter;
//    private final List<ChatListAdapter.ChatDisplayItem> chatItems = new ArrayList<>();
//    private final ChatManager chatManager = new ChatManager();
//    private final UserManager userManager = new UserManager();
//
//    @SuppressLint("NotifyDataSetChanged")
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_chats, container, false);
//
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewChats);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        chatAdapter = new ChatListAdapter(chatItems, item -> {
//            Intent intent = new Intent(getContext(), ChatActivity.class);
//            intent.putExtra("chatId", item.chat.getId());
//            intent.putExtra("otherUserId", item.otherUserId);
//            startActivity(intent);
//        });
//        recyclerView.setAdapter(chatAdapter);
//
//        loadChats();
//        return view;
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void loadChats() {
//        String currentUserId = SessionManager.getInstance().getCurrentUserId();
//        Log.d("ChatsFragment", "🔍 Loading chats for currentUserId=" + currentUserId);
//
//        chatManager.getUserChats(currentUserId, 0, 50).thenAccept(chats -> {
//            Log.d("ChatsFragment", "📦 Received " + chats.size() + " chats from server");
//
//            List<CompletableFuture<ChatListAdapter.ChatDisplayItem>> futures = new ArrayList<>();
//
//            for (Chat chat : chats) {
//                Log.d("ChatsFragment", "➡️ Chat ID: " + chat.getId() + ", Participants: " + chat.getParticipants());
//
//                String otherUserId = chat.getParticipants().stream()
//                        .filter(id -> !currentUserId.equals(id))
//                        .findFirst()
//                        .orElse(null);
//
//                if (otherUserId == null) {
//                    Log.w("ChatsFragment", "⚠️ Could not find other user in participants: " + chat.getParticipants());
//                    continue;
//                }
//
//                CompletableFuture<ChatListAdapter.ChatDisplayItem> itemFuture =
//                        userManager.getUserById(otherUserId)
//                                .thenApply(user -> {
//                                    Log.d("ChatsFragment", "👤 Found user for chat: " + user.getUsername());
//                                    String formattedTime = formatTimestamp(chat.getLastMessageTimestamp());
//                                    return new ChatListAdapter.ChatDisplayItem(chat, user.getUsername(), otherUserId, formattedTime);
//                                });
//
//                futures.add(itemFuture);
//            }
//
//            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
//                    .thenRun(() -> {
//                        List<ChatListAdapter.ChatDisplayItem> result = new ArrayList<>();
//                        for (CompletableFuture<ChatListAdapter.ChatDisplayItem> future : futures) {
//                            try {
//                                result.add(future.get());
//                            } catch (Exception e) {
//                                Log.e("ChatsFragment", "❌ Error resolving user", e);
//                            }
//                        }
//
//                        requireActivity().runOnUiThread(() -> {
//                            Log.d("ChatsFragment", "🎯 Displaying " + result.size() + " chats in RecyclerView");
//                            chatItems.clear();
//                            chatItems.addAll(result);
//                            chatAdapter.notifyDataSetChanged();
//                        });
//                    });
//
//        }).exceptionally(e -> {
//            Log.e("ChatsFragment", "🔥 Error loading chats", e);
//            return null;
//        });
//    }
//
//
//    private String formatTimestamp(String timestamp) {
//        if (timestamp == null || timestamp.isEmpty()) return "";
//        try {
//            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
//            Date date = inputFormat.parse(timestamp);
//            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//            return outputFormat.format(date);
//        } catch (ParseException e) {
//            Log.e("ChatsFragment", "Failed to parse timestamp: " + timestamp, e);
//            return "";
//        }
//    }
//}
package com.example.singlechatantgroupchatseminar.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsdk.models.Chat;
import com.example.chatsdk.models.User;
import com.example.chatsdk.network.ChatManager;
import com.example.chatsdk.network.UserManager;
import com.example.chatsdk.utils.SessionManager;
import com.example.singlechatantgroupchatseminar.R;
import com.example.singlechatantgroupchatseminar.activities.ChatActivity;
import com.example.singlechatantgroupchatseminar.adapters.ChatListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ChatsFragment extends Fragment {

    private ChatListAdapter chatAdapter;
    private final List<ChatListAdapter.ChatDisplayItem> chatItems = new ArrayList<>();
    private final ChatManager chatManager = new ChatManager();
    private final UserManager userManager = new UserManager();

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatListAdapter(chatItems, item -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("chatId", item.chat.getId());
            intent.putExtra("otherUserId", item.otherUserId);
            startActivity(intent);
        });
        recyclerView.setAdapter(chatAdapter);

        loadChats();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChats() {
        String currentUserId = SessionManager.getInstance().getCurrentUserId();
        Log.d("ChatsFragment", "🔍 Loading chats for currentUserId=" + currentUserId);

        chatManager.getUserChats(currentUserId, 0, 50).thenAccept(chats -> {
            Log.d("ChatsFragment", "📦 Received " + chats.size() + " chats from server");

            List<CompletableFuture<ChatListAdapter.ChatDisplayItem>> futures = new ArrayList<>();

            for (Chat chat : chats) {
                Log.d("ChatsFragment", "➡️ Chat ID: " + chat.getId() + ", Participants: " + chat.getParticipants());

                String otherUserId = chat.getParticipants().stream()
                        .filter(id -> !currentUserId.equals(id))
                        .findFirst()
                        .orElse(null);

                if (otherUserId == null) {
                    Log.w("ChatsFragment", "⚠️ Could not find other user in participants: " + chat.getParticipants());
                    continue;
                }

                Log.d("ChatsFragment", "🔎 Looking up otherUserId: " + otherUserId);

                CompletableFuture<ChatListAdapter.ChatDisplayItem> itemFuture =
                        userManager.getUserById(otherUserId)
                                .thenApply(user -> {
                                    if (user == null) {
                                        Log.w("ChatsFragment", "❌ userManager.getUserById returned null for ID: " + otherUserId);
                                        return null;
                                    }
                                    Log.d("ChatsFragment", "👤 Found user: " + user.getUsername());
                                    String formattedTime = formatTimestamp(chat.getLastMessageTimestamp());
                                    return new ChatListAdapter.ChatDisplayItem(chat, user.getUsername(), otherUserId, formattedTime);
                                })
                                .exceptionally(e -> {
                                    Log.e("ChatsFragment", "🚫 Failed to get user by ID: " + otherUserId, e);
                                    return null;
                                });

                futures.add(itemFuture);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> {
                        List<ChatListAdapter.ChatDisplayItem> result = new ArrayList<>();
                        for (CompletableFuture<ChatListAdapter.ChatDisplayItem> future : futures) {
                            try {
                                ChatListAdapter.ChatDisplayItem item = future.get();
                                if (item != null) {
                                    result.add(item);
                                }
                            } catch (Exception e) {
                                Log.e("ChatsFragment", "❌ Error resolving future", e);
                            }
                        }

                        requireActivity().runOnUiThread(() -> {
                            Log.d("ChatsFragment", "🎯 Displaying " + result.size() + " chats in RecyclerView");
                            chatItems.clear();
                            chatItems.addAll(result);
                            chatAdapter.notifyDataSetChanged();
                        });
                    });

        }).exceptionally(e -> {
            Log.e("ChatsFragment", "🔥 Error loading chats", e);
            return null;
        });
    }

    private String formatTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e("ChatsFragment", "Failed to parse timestamp: " + timestamp, e);
            return "";
        }
    }
}
