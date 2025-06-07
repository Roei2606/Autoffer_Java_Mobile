package com.example.rsocket_sdk.network;


import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;

public class ServerConfig {

    private static final String TAG = "ServerConfig";
    private static String serverUrl = null;
    private static boolean isInitialized = false;

    public static void init(Context context) {
        if (isInitialized) return;

        try {
            InputStream is = context.getAssets().open("server_config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            serverUrl = jsonObject.getString("serverUrl");

            Log.i(TAG, "Server URL loaded from server_config.json: " + serverUrl);
        } catch (Exception e) {
            Log.e(TAG, "Failed to load server_config.json, using default PUBLIC_SERVER_URL", e);
            serverUrl = ServerConstants.PUBLIC_SERVER_URL;
        } finally {
            isInitialized = true;
        }
    }

    public static String getServerUrl() {
        if (!isInitialized) {
            Log.w(TAG, "ServerConfig not initialized. Falling back to PUBLIC_SERVER_URL.");
            return ServerConstants.PUBLIC_SERVER_URL;
        }
        return serverUrl;
    }
}



