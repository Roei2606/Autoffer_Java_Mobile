//package com.example.singlechatantgroupchatseminar.activities;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatsdk.models.Message;
//import com.example.chatsdk.network.ChatManager;
//import com.example.chatsdk.network.MessageManager;
//import com.example.singlechatantgroupchatseminar.R;
//import com.example.singlechatantgroupchatseminar.adapters.MessageAdapter;
//import com.google.android.material.button.MaterialButton;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import reactor.core.Disposable;
//import reactor.core.scheduler.Schedulers;
//
////public class ChatActivity extends AppCompatActivity {
////
////    private RecyclerView recyclerView;
////    private MessageAdapter messageAdapter;
////    private List<Message> messagesList;
////    private EditText messageInput;
////    private MessageManager messageManager;
////    private ChatManager chatManager;
////    private String chatId;
////    private String currentUserId;
////    private String otherUserId;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_chat);
////
////        chatId = getIntent().getStringExtra("chatId");
////        currentUserId = getIntent().getStringExtra("currentUserId");
////        otherUserId = getIntent().getStringExtra("otherUserId");
////
////        Log.d("ChatActivity", "onCreate: chatId=" + chatId + ", currentUserId=" + currentUserId + ", otherUserId=" + otherUserId);
////
////        if (chatId == null || currentUserId == null || otherUserId == null) {
////            Log.e("ChatActivity", "Chat ID or User IDs cannot be null");
////            finish();
////            return;
////        }
////
////        messageManager = new MessageManager();
////        chatManager = new ChatManager();
////
////        setupViews();
////        listenForMessages();
////        loadMessages();
////    }
////
////    private void setupViews() {
////        recyclerView = findViewById(R.id.recyclerViewMessages);
////        messageInput = findViewById(R.id.editTextMessage);
////        MaterialButton buttonSend = findViewById(R.id.buttonSend);
////
////        messagesList = new ArrayList<>();
////        messageAdapter = new MessageAdapter(messagesList, currentUserId);
////
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        recyclerView.setAdapter(messageAdapter);
////
////        buttonSend.setOnClickListener(this::onSendClicked);
////    }
////
////    private void onSendClicked(View view) {
////        String content = messageInput.getText().toString().trim();
////        if (!content.isEmpty()) {
////            sendMessage(content);
////            messageInput.setText("");
////        }
////    }
////
////    private void sendMessage(String content) {
////        Message message = new Message(
////                chatId,
////                currentUserId,
////                otherUserId,
////                content,
////                LocalDateTime.now().toString()
////        );
////
////        messageManager.sendMessage(message).thenAccept(sentMessage -> runOnUiThread(() -> {
////            messagesList.add(sentMessage);
////            messageAdapter.notifyItemInserted(messagesList.size() - 1);
////            recyclerView.scrollToPosition(messagesList.size() - 1);
////        })).exceptionally(e -> {
////            Log.e("ChatActivity", "Error sending message", e);
////            return null;
////        });
////    }
////
////    @SuppressLint("NotifyDataSetChanged")
////    private void loadMessages() {
////        chatManager.getChatMessages(chatId, 0, 50)
////                .thenAccept(messages -> runOnUiThread(() -> {
////                    messagesList.clear();
////                    messagesList.addAll(messages);
////                    messageAdapter.notifyDataSetChanged();
////                    recyclerView.scrollToPosition(messagesList.size() - 1);
////                }))
////                .exceptionally(e -> {
////                    Log.e("ChatActivity", "Error loading messages", e);
////                    return null;
////                });
////    }
////
////    private void listenForMessages() {
////        chatManager.streamMessages(chatId)
////                .subscribe(newMessage -> runOnUiThread(() -> {
////                    messagesList.add(newMessage);
////                    messageAdapter.notifyItemInserted(messagesList.size() - 1);
////                    recyclerView.smoothScrollToPosition(messagesList.size() - 1);
////                }), throwable -> Log.e("ChatActivity", "Error receiving message", throwable));
////    }
////
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        chatManager.disposeMessageStream();
////    }
////}
//public class ChatActivity extends AppCompatActivity {
//
//    private String chatId;
//    private String currentUserId;
//    private String otherUserId;
//    private RecyclerView recyclerView;
//    private EditText inputMessage;
//    private MaterialButton buttonSend;
//    private MessageAdapter messageAdapter;
//    private List<Message> messages;
//    private ChatManager chatManager;
//    private Disposable messageStreamDisposable;
//    private MessageManager messageManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        chatId = getIntent().getStringExtra("chatId");
//        currentUserId = getIntent().getStringExtra("currentUserId");
//        otherUserId = getIntent().getStringExtra("otherUserId");
//
//        Log.d("ChatActivity", "onCreate: chatId=" + chatId + ", currentUserId=" + currentUserId + ", otherUserId=" + otherUserId);
//
//        if (chatId == null || currentUserId == null || otherUserId == null) {
//            Log.e("ChatActivity", "Chat ID or User IDs cannot be null");
//            finish();
//            return;
//        }
//
//        recyclerView = findViewById(R.id.recyclerViewMessages);
//        inputMessage = findViewById(R.id.editTextMessage);
//        buttonSend = findViewById(R.id.buttonSend);
//
//        messages = new ArrayList<>();
//        messageAdapter = new MessageAdapter(messages, currentUserId);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(messageAdapter);
//
//        chatManager = new ChatManager();
//        messageManager=new MessageManager();
//
//        loadChatMessages();
//        listenToIncomingMessages();
//
//        buttonSend.setOnClickListener(v -> {
//            String content = inputMessage.getText().toString().trim();
//            if (!content.isEmpty()) {
//                Message message = new Message(chatId, currentUserId,otherUserId, content, String.valueOf(System.currentTimeMillis()));
//                messageManager.sendMessage(message);
//                inputMessage.setText("");
//
//                messages.add(message);
//                messageAdapter.notifyItemInserted(messages.size() - 1);
//                recyclerView.scrollToPosition(messages.size() - 1);
//            }
//        });
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void loadChatMessages() {
//        chatManager.getChatMessages(chatId, 0, 100)
//                .thenAccept(loadedMessages -> runOnUiThread(() -> {
//                    messages.clear();
//                    messages.addAll(loadedMessages);
//                    messageAdapter.notifyDataSetChanged();
//                    recyclerView.scrollToPosition(messages.size() - 1);
//                }))
//                .exceptionally(e -> {
//                    Log.e("ChatActivity", "Error loading chat messages", e);
//                    return null;
//                });
//    }
//
//    private void listenToIncomingMessages() {
//        messageStreamDisposable = chatManager.streamMessages(chatId)
//                .publishOn(Schedulers.boundedElastic())
//                .subscribe(newMessage -> {
//                    Log.d("ChatActivity", "Received new message: " + newMessage.getContent());
//                    messages.add(newMessage);
//                    messageAdapter.notifyItemInserted(messages.size() - 1);
//                    recyclerView.scrollToPosition(messages.size() - 1);
//                }, throwable -> {
//                    Log.e("ChatActivity", "Error in message stream", throwable);
//                });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        listenToIncomingMessages();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
//            messageStreamDisposable.dispose();
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
//            messageStreamDisposable.dispose();
//        }
//    }
//}


//package com.example.singlechatantgroupchatseminar.activities;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatsdk.models.Message;
//import com.example.chatsdk.network.ChatManager;
//import com.example.chatsdk.network.MessageManager;
//import com.example.singlechatantgroupchatseminar.R;
//import com.example.singlechatantgroupchatseminar.adapters.MessageAdapter;
//import com.google.android.material.button.MaterialButton;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import reactor.core.Disposable;
//import reactor.core.scheduler.Schedulers;
//
//public class ChatActivity extends AppCompatActivity {
//
//    private String chatId;
//    private String currentUserId;
//    private String otherUserId;
//    private RecyclerView recyclerView;
//    private EditText inputMessage;
//    private MaterialButton buttonSend;
//    private MessageAdapter messageAdapter;
//    private List<Message> messages;
//    private ChatManager chatManager;
//    private Disposable messageStreamDisposable;
//    private MessageManager messageManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        chatId = getIntent().getStringExtra("chatId");
//        currentUserId = getIntent().getStringExtra("currentUserId");
//        otherUserId = getIntent().getStringExtra("otherUserId");
//
//        Log.d("ChatActivity", "onCreate: chatId=" + chatId + ", currentUserId=" + currentUserId + ", otherUserId=" + otherUserId);
//
//        if (chatId == null || currentUserId == null || otherUserId == null) {
//            Log.e("ChatActivity", "Chat ID or User IDs cannot be null");
//            finish();
//            return;
//        }
//
//        recyclerView = findViewById(R.id.recyclerViewMessages);
//        inputMessage = findViewById(R.id.editTextMessage);
//        buttonSend = findViewById(R.id.buttonSend);
//
//        messages = new ArrayList<>();
//        messageAdapter = new MessageAdapter(messages, currentUserId);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(messageAdapter);
//
//        chatManager = new ChatManager();
//        messageManager = new MessageManager();
//
//        loadChatMessages();
//        startListeningToStream();
//
//        buttonSend.setOnClickListener(v -> {
//            String content = inputMessage.getText().toString().trim();
//            if (!content.isEmpty()) {
//                Message message = new Message(chatId, currentUserId, otherUserId, content, String.valueOf(System.currentTimeMillis()));
//                messageManager.sendMessage(message);
//                inputMessage.setText("");
//
//                messages.add(message);
//                messageAdapter.notifyItemInserted(messages.size() - 1);
//                recyclerView.scrollToPosition(messages.size() - 1);
//            }
//        });
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void loadChatMessages() {
//        chatManager.getChatMessages(chatId, 0, 100)
//                .thenAccept(loadedMessages -> runOnUiThread(() -> {
//                    messages.clear();
//                    messages.addAll(loadedMessages);
//                    messageAdapter.notifyDataSetChanged();
//                    recyclerView.scrollToPosition(messages.size() - 1);
//                }))
//                .exceptionally(e -> {
//                    Log.e("ChatActivity", "Error loading chat messages", e);
//                    return null;
//                });
//    }
//
//    private void startListeningToStream() {
//        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
//            messageStreamDisposable.dispose();
//        }
//
//        Log.d("ChatActivity", "Subscribing to stream for chatId: " + chatId);
//
//        messageStreamDisposable = chatManager.streamMessages(chatId)
//                .publishOn(Schedulers.boundedElastic())
//                .subscribe(newMessage -> {
//                    Log.d("ChatActivity", "Received new message: " + newMessage.getContent());
//                    runOnUiThread(() -> {
//                        messages.add(newMessage);
//                        messageAdapter.notifyItemInserted(messages.size() - 1);
//                        recyclerView.scrollToPosition(messages.size() - 1);
//                    });
//                }, throwable -> {
//                    Log.e("ChatActivity", "Error in message stream", throwable);
//                });
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
//            messageStreamDisposable.dispose();
//        }
//    }
//}
package com.example.singlechatantgroupchatseminar.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsdk.models.Message;
import com.example.chatsdk.network.ChatManager;
import com.example.chatsdk.network.MessageManager;
import com.example.singlechatantgroupchatseminar.R;
import com.example.singlechatantgroupchatseminar.adapters.MessageAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;

public class ChatActivity extends AppCompatActivity {

    private String chatId;
    private String currentUserId;
    private String otherUserId;
    private RecyclerView recyclerView;
    private EditText inputMessage;
    private MaterialButton buttonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private ChatManager chatManager;
    private Disposable messageStreamDisposable;
    private MessageManager messageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("chatId");
        currentUserId = getIntent().getStringExtra("currentUserId");
        otherUserId = getIntent().getStringExtra("otherUserId");

        Log.d("ChatActivity", "onCreate: chatId=" + chatId + ", currentUserId=" + currentUserId + ", otherUserId=" + otherUserId);

        if (chatId == null || currentUserId == null || otherUserId == null) {
            Log.e("ChatActivity", "Chat ID or User IDs cannot be null");
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewMessages);
        inputMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        chatManager = new ChatManager();
        messageManager = new MessageManager();

        loadChatMessages();
        startListeningToStream();

        buttonSend.setOnClickListener(v -> {
            String content = inputMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message(chatId, currentUserId, otherUserId, content, String.valueOf(System.currentTimeMillis()));

                messageManager.sendMessage(message)
                        .thenAccept(sentMessage -> runOnUiThread(() -> {
                            messages.add(sentMessage);
                            messageAdapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }))
                        .exceptionally(e -> {
                            Log.e("ChatActivity", "❌ Failed to send message", e);
                            return null;
                        });

                inputMessage.setText("");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChatMessages() {
        chatManager.getChatMessages(chatId, 0, 100)
                .thenAccept(loadedMessages -> runOnUiThread(() -> {
                    messages.clear();
                    messages.addAll(loadedMessages);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                }))
                .exceptionally(e -> {
                    Log.e("ChatActivity", "Error loading chat messages", e);
                    return null;
                });
    }

    private void startListeningToStream() {
        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
            messageStreamDisposable.dispose();
        }

        Log.d("ChatActivity", "Subscribing to stream for chatId: " + chatId);

        messageStreamDisposable = chatManager.streamMessages(chatId)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(newMessage -> {
                    Log.d("ChatActivity", "Received new message: " + newMessage.getContent());

                    runOnUiThread(() -> {
                        // אל תוסיף את ההודעה אם היא כבר קיימת (מניעת כפילויות)
                        boolean alreadyExists = messages.stream().anyMatch(m ->
                                m.getSenderId().equals(newMessage.getSenderId()) &&
                                        m.getContent().equals(newMessage.getContent()) &&
                                        m.getTimestamp().equals(newMessage.getTimestamp()));

                        if (!alreadyExists) {
                            messages.add(newMessage);
                            messageAdapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }
                    });
                }, throwable -> {
                    Log.e("ChatActivity", "Error in message stream", throwable);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
            messageStreamDisposable.dispose();
        }
    }
}






