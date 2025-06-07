package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ads_sdk.models.Ad;
import com.example.ads_sdk.network.AdsManager;
import com.example.autofferandroid.adapters.AdsAdapter;
import com.example.autofferandroid.databinding.FragmentHomeBinding;
import com.example.autofferandroid.transformers.DoorOpenPageTransformer;
import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;
import com.example.users_sdk.network.SessionManager;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AdsAdapter adsAdapter;
    private AdsManager adsManager;
    private final List<Ad> adList = new ArrayList<>();

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;
    private static final long AUTO_SCROLL_DELAY = 4000; // 4 seconds

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adsManager = new AdsManager();
        adsAdapter = new AdsAdapter(adList, requireContext());
        binding.viewPagerAds.setAdapter(adsAdapter);
        binding.viewPagerAds.setPageTransformer(new DoorOpenPageTransformer());

        setupUserDetails();
        loadAds();
    }

    private void setupUserDetails() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            binding.textViewUserName.setText("Hello, " + currentUser.getFirstName() + "!");
            //binding.textViewUserType.setText(currentUser.getProfileType());
        } else {
            binding.textViewUserName.setText("Hello, Guest!");
            binding.textViewUserType.setText("Unknown Type");
        }
    }

    private void loadAds() {
        showLoading(true);

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            showLoading(false);
            return;
        }

        try {
            UserType profileType = currentUser.getProfileType();

            CompletableFuture<List<Ad>> future = adsManager.getAdsForAudience(profileType);
            future.thenAccept(ads -> {
                requireActivity().runOnUiThread(() -> {
                    adList.clear();
                    adList.addAll(ads);
                    adsAdapter.notifyDataSetChanged();
                    showLoading(false);
                    startAutoScroll();
                });
            }).exceptionally(e -> {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> showLoading(false));
                return null;
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showLoading(false);
        }
    }

    private void showLoading(boolean isLoading) {
        if (binding == null) return;
        if (isLoading) {
            binding.loadingIndicator.setVisibility(View.VISIBLE);
            binding.viewPagerAds.setVisibility(View.GONE);
        } else {
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.viewPagerAds.setVisibility(View.VISIBLE);
        }
    }

    private void startAutoScroll() {
        stopAutoScroll();

        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (binding != null && binding.viewPagerAds != null && adsAdapter != null) {
                    int currentItem = binding.viewPagerAds.getCurrentItem();
                    int itemCount = adsAdapter.getItemCount();
                    if (itemCount > 0) {
                        int nextItem = (currentItem + 1) % itemCount;
                        binding.viewPagerAds.setCurrentItem(nextItem, true);
                        handler.postDelayed(this, AUTO_SCROLL_DELAY);
                    }
                }
            }
        };
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        if (autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
    }

    @Override
    public void onDestroyView() {
        stopAutoScroll();
        binding = null;
        super.onDestroyView();
    }
}
