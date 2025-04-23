package com.example.singlechatantgroupchatseminar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chatsdk.utils.SessionManager;
import com.example.singlechatantgroupchatseminar.databinding.ActivityMainBinding;
import com.example.singlechatantgroupchatseminar.fragments.ChatsFragment;
import com.example.singlechatantgroupchatseminar.fragments.UsersFragment;

public class MainActivity extends AppCompatActivity {

    private final String currentUserId = SessionManager.getInstance().getCurrentUserId(); // Replace this with actual user ID from login/session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadFragment(new UsersFragment()); // Default Fragment

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_users) {
                selectedFragment = new UsersFragment();
            } else if (item.getItemId() == R.id.nav_chats) {
                selectedFragment = new ChatsFragment();
            }
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
