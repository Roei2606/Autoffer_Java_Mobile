package com.example.autofferandroid.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autofferandroid.R;
import com.example.autofferandroid.databinding.FragmentNewProjectBinding;

import com.example.local_project_sdk.db.LocalProjectStorage;
import com.example.local_project_sdk.models.LocalProjectEntity;
import com.example.local_project_sdk.repository.LocalProjectRepository;
import com.example.users_sdk.network.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NewProjectFragment extends Fragment {

    private FragmentNewProjectBinding binding;
    private LocalProjectRepository repository;
    private LocalProjectEntity currentProject;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewProjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        repository = new LocalProjectRepository(requireContext());

        binding.buttonAddManual.setOnClickListener(v -> {
            if (ensureProjectCreated()) {
                navigateToAddManualFragment();
            }
        });

        binding.buttonAddCamera.setOnClickListener(v -> {
            if (ensureProjectCreated()) {
                navigateToScanFragment();
            }
        });

        binding.buttonViewCurrentProject.setOnClickListener(v -> {
            if (LocalProjectStorage.getInstance().hasProject()) {
                navigateToCurrentProjectFragment();
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("No Project Found")
                        .setMessage("You haven't started a project yet. Please add at least one item first.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private boolean ensureProjectCreated() {
        String address = binding.inputProjectAddress.getText() != null
                ? binding.inputProjectAddress.getText().toString().trim() : "";

        if (TextUtils.isEmpty(address)) {
            binding.inputProjectAddress.setError("Project address is required");
            return false;
        }

        if (currentProject == null) {
            currentProject = new LocalProjectEntity();
            currentProject.setId(UUID.randomUUID().toString());
            currentProject.setProjectAddress(address);
            currentProject.setClientId(SessionManager.getInstance().getCurrentUserId());
            currentProject.setCreatedAt(getCurrentTimestamp());

            repository.insertProject(currentProject);
            LocalProjectStorage.getInstance().setCurrentProject(currentProject);

            binding.inputProjectAddress.setEnabled(false);
        }

        return true;
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void navigateToAddManualFragment() {
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.action_newProjectFragment_to_addManualFragment);
    }

    private void navigateToCurrentProjectFragment() {
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.action_newProjectFragment_to_currentProjectFragment);
    }

    private void navigateToScanFragment() {
        Toast.makeText(getContext(), "Scan flow (TODO)", Toast.LENGTH_SHORT).show();
    }

}
