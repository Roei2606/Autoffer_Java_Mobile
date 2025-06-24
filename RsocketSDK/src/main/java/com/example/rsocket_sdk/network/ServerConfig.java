//package com.example.rsocket_sdk.network;
//
//import android.content.Context;
//import android.os.Build;
//import android.util.Log;
//
//import org.json.JSONObject;
//
//import java.io.InputStream;
//
//public class ServerConfig {
//
//    private static final String TAG = "ServerConfig";
//    private static String serverUrl = null;
//    private static boolean isInitialized = false;
//
//    public static void init(Context context) {
//        if (isInitialized) return;
//
//        try {
//            if (isEmulator()) {
//                // חיבור לאימולטור
//                serverUrl = ServerConstants.EMULATOR_SERVER_URL;
//            } else {
//                // קריאת קובץ json עבור מכשיר פיזי
//                InputStream is = context.getAssets().open("server_config.json");
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//                String json = new String(buffer, "UTF-8");
//
//                JSONObject jsonObject = new JSONObject(json);
//                serverUrl = jsonObject.getString("serverUrl");
//            }
//
//            Log.i(TAG, "Server URL loaded: " + serverUrl);
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to load server_config.json, using default PUBLIC_SERVER_URL", e);
//            serverUrl = ServerConstants.PUBLIC_SERVER_URL;
//        } finally {
//            isInitialized = true;
//        }
//    }
//
//    public static String getServerUrl() {
//        if (!isInitialized) {
//            Log.w(TAG, "ServerConfig not initialized. Falling back to PUBLIC_SERVER_URL.");
//            return ServerConstants.PUBLIC_SERVER_URL;
//        }
//        return serverUrl;
//    }
//
//    // מזהה האם מדובר באימולטור
//    private static boolean isEmulator() {
//        return Build.FINGERPRINT.startsWith("google/sdk_gphone_")
//                || Build.FINGERPRINT.contains("generic")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.PRODUCT.contains("sdk_gphone");
//    }
//}
package com.example.rsocket_sdk.network;

import android.util.Log;

import com.example.rsocketsdk.BuildConfig;

public class ServerConfig {

    private static final String TAG = "ServerConfig";

    public static String getServerUrl() {
        if (isEmulator()) {
            return ServerConstants.EMULATOR_SERVER_URL;
        } else {
            return ServerConstants.PUBLIC_SERVER_URL;
        }
    }

    private static boolean isEmulator() {
        return android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.FINGERPRINT.toLowerCase().contains("emulator")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(android.os.Build.PRODUCT);
    }


}
