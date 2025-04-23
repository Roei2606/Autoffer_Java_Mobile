package com.example.chatsdk.network;

import android.util.Log;

import com.example.chatsdk.models.Chat;
import com.example.chatsdk.models.ChatMessagesRequest;
import com.example.chatsdk.models.ChatRequest;
import com.example.chatsdk.models.Message;
import com.example.chatsdk.models.UserChatsRequest;
import com.example.chatsdk.utils.RSocketUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChatManager {
    private final RSocketClientManager rSocketClientManager;
    private Disposable messageStreamDisposable;


    public ChatManager() {
        this.rSocketClientManager = RSocketClientManager.getInstance();
    }

    public CompletableFuture<List<Chat>> getUserChats(String userId, int page, int size) {
        try {
            UserChatsRequest request = new UserChatsRequest(userId, page, size);
            Payload payload = RSocketUtils.buildPayload("chats.getAll", request);

            return getActiveRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Chat.class))
                    .collectList()
                    .toFuture();

        } catch (Exception e) {
            CompletableFuture<List<Chat>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<List<Message>> getChatMessages(String chatId, int page, int size) {
        try {
            ChatMessagesRequest request = new ChatMessagesRequest(chatId, page, size);
            Payload payload = RSocketUtils.buildPayload("chats.getMessages", request);

            return getActiveRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Message.class))
                    .collectList()
                    .toFuture();

        } catch (Exception e) {
            CompletableFuture<List<Message>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Chat> getOrCreateChat(String user1Id, String user2Id) {
        try {
            ChatRequest request = new ChatRequest(user1Id, user2Id);
            Log.d("ChatManager", "Sending ChatRequest: " + request.toString());
            Payload payload = RSocketUtils.buildPayload("chats.getOrCreate", request);

            return getActiveRSocket()
                    .requestResponse(payload)
                    .map(p -> {
                        Log.d("ChatManager", "Received chat payload");
                        return RSocketUtils.parsePayload(p, Chat.class);
                    })
                    .toFuture();

        } catch (Exception e) {
            Log.e("ChatManager", "Error creating/opening chat", e);
            CompletableFuture<Chat> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }


    public Flux<Message> streamMessages(String chatId) {
        try {
            Payload payload = RSocketUtils.buildPayload("chats.streamMessages", chatId);
            return getActiveRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Message.class));
        } catch (Exception e) {
            return Flux.error(e);
        }
    }

    public void disposeMessageStream() {
        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
            messageStreamDisposable.dispose();
        }
    }

    private RSocket getActiveRSocket() {
        RSocket rSocket = rSocketClientManager.getRSocket();
        if (rSocket == null || rSocket.isDisposed()) {
            throw new IllegalStateException("RSocket connection is not established");
        }
        return rSocket;
    }
}
