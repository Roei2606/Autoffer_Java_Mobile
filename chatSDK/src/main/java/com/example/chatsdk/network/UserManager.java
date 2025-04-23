package com.example.chatsdk.network;

import android.util.Log;

import com.example.chatsdk.models.LoginRequest;
import com.example.chatsdk.models.User;
import com.example.chatsdk.models.UserIdRequest;
import com.example.chatsdk.utils.RSocketUtils;
import io.rsocket.Payload;
import io.rsocket.RSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserManager {
    private final RSocketClientManager rSocketClientManager;
    private static final String TAG = "UserManager";

    public UserManager() {
        this.rSocketClientManager = RSocketClientManager.getInstance();
    }

    public CompletableFuture<User> loginUser(String username) {
        CompletableFuture<User> future = new CompletableFuture<>();

        try {
            RSocket rSocket = getActiveRSocket();
            Payload payload = RSocketUtils.buildPayload("users.login", new LoginRequest(username));


            rSocket.requestResponse(payload)
                    .map(response -> RSocketUtils.parsePayload(response, User.class))
                    .subscribe(
                            future::complete,
                            error -> {
                                Log.e(TAG, "Login Error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            }
                    );

        } catch (Exception e) {
            Log.e(TAG, "RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    public CompletableFuture<List<User>> getAllUsers() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        try {
            RSocket rSocket = getActiveRSocket();
            Payload payload = RSocketUtils.buildPayload("users.getAll", "");

            rSocket.requestStream(payload)
                    .collectList()
                    .map(responses -> {
                        List<User> users = new ArrayList<>();
                        for (Payload response : responses) {
                            users.add(RSocketUtils.parsePayload(response, User.class));
                        }
                        return users;
                    })
                    .subscribe(
                            future::complete,
                            error -> {
                                Log.e(TAG, "GetAllUsers Error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            }
                    );

        } catch (Exception e) {
            Log.e(TAG, "RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    private RSocket getActiveRSocket() {
        RSocket rSocket = rSocketClientManager.getRSocket();
        if (rSocket == null || rSocket.isDisposed()) {
            throw new IllegalStateException("RSocket connection is not established");
        }
        return rSocket;
    }

    public CompletableFuture<User> getUserById(String userId) {
        try {
            UserIdRequest request = new UserIdRequest(userId);
            Payload payload = RSocketUtils.buildPayload("users.getById", request);
            return getActiveRSocket()
                    .requestResponse(payload)
                    .map(p -> RSocketUtils.parsePayload(p, User.class))
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<User> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
}
