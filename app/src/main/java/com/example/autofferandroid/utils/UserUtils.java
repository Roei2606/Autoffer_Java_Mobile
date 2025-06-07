package com.example.autofferandroid.utils;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;


import com.example.core_models_sdk.models.User;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;

import java.util.function.Consumer;

public class UserUtils {

    private static final String TAG = "UserUtils";

    public static void fetchCurrentUser(Context context, LifecycleOwner owner, Consumer<User> onSuccess, Consumer<Throwable> onError) {
        String currentUserId = SessionManager.getInstance().getCurrentUserId();

        if (currentUserId == null || currentUserId.isEmpty()) {
            Log.e(TAG, "Current user ID is null or empty");
            onError.accept(new IllegalStateException("User not logged in"));
            return;
        }

        UserManager userManager = new UserManager();
        userManager.getUserById(currentUserId)
                .thenAccept(user -> {
                    if (user != null) {
                        onSuccess.accept(user);
                    } else {
                        onError.accept(new NullPointerException("User not found"));
                    }
                })
                .exceptionally(e -> {
                    Log.e(TAG, "Failed to fetch user", e);
                    onError.accept(e);
                    return null;
                });
    }
}
