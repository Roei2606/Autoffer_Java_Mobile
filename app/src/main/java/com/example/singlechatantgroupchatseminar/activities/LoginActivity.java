package com.example.singlechatantgroupchatseminar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatsdk.network.RSocketClientManager;
import com.example.chatsdk.network.UserManager;
import com.example.chatsdk.utils.SessionManager;
import com.example.singlechatantgroupchatseminar.MainActivity;
import com.example.singlechatantgroupchatseminar.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize connection in background
        new Thread(() -> {
            if (RSocketClientManager.getInstance().connect()) {
                System.out.println("Connected to RSocket server successfully");
            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, "Failed to connect to server", Toast.LENGTH_LONG).show()
                );
            }
        }).start();

        userManager = new UserManager();
        binding.loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = Objects.requireNonNull(binding.usernameInput.getText()).toString().trim();
        if (username.isEmpty()) {
            binding.usernameInput.setError("Username required");
            return;
        }

        binding.loginButton.setEnabled(false);

        userManager.loginUser(username)
                .thenAccept(user -> runOnUiThread(() -> {
                    binding.loginButton.setEnabled(true);
                    if (user != null) {
                        SessionManager.getInstance().setCurrentUserId(user.getId());
                        Toast.makeText(this, "Welcome " + user.getUsername(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Login failed. User not found.", Toast.LENGTH_SHORT).show();
                    }
                }))
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        binding.loginButton.setEnabled(true);
                        Toast.makeText(this, "Login Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("Login Error: " + e.getMessage());
                    });
                    return null;
                });
    }
}