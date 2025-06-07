////package com.example.rsocket_sdk.network;
////
////import io.rsocket.RSocket;
////import io.rsocket.core.RSocketConnector;
////import io.rsocket.metadata.WellKnownMimeType;
////import io.rsocket.transport.netty.client.WebsocketClientTransport;
////
////import java.net.URI;
////
////public class RSocketClientManager {
////    private static final String SERVER_HOST = "10.0.2.2";
////    private static final int SERVER_PORT = 7001;
////    private static final String RSOCKET_PATH = "/rsocket";
////    private static RSocketClientManager instance;
////    private RSocket rSocket;
////    private boolean isConnecting = false;
////
////    private RSocketClientManager() {}
////
////    public static synchronized RSocketClientManager getInstance() {
////        if (instance == null) {
////            instance = new RSocketClientManager();
////        }
////        return instance;
////    }
////
////    public boolean connect() {
////        if (rSocket != null && !rSocket.isDisposed()) {
////            return true;
////        }
////
////        if (isConnecting) {
////            return false;
////        }
////
////        try {
////            isConnecting = true;
////            URI uri = URI.create("ws://" + SERVER_HOST + ":" + SERVER_PORT + RSOCKET_PATH);
////            System.out.println("Connecting to RSocket server: " + uri);
////
////            rSocket = RSocketConnector.create()
////                    .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString()) // ✅ תיקון קריטי!
////                    .dataMimeType("application/json")
////                    .connect(WebsocketClientTransport.create(uri))
////                    .block();
////
////            boolean isConnected = rSocket != null && !rSocket.isDisposed();
////
////            if (isConnected) {
////                System.out.println("Connected to RSocket server successfully!");
////            } else {
////                System.err.println("Failed to establish RSocket connection.");
////            }
////
////            return isConnected;
////
////        } catch (Exception e) {
////            System.err.println("RSocket connection error: " + e.getMessage());
////            e.printStackTrace();
////            return false;
////        } finally {
////            isConnecting = false;
////        }
////    }
////
////    public RSocket getRSocket() {
////        return rSocket;
////    }
////
////    public boolean isConnected() {
////        return rSocket != null && !rSocket.isDisposed();
////    }
////}
//package com.example.rsocket_sdk.network;
//
//import io.rsocket.RSocket;
//import io.rsocket.core.RSocketConnector;
//import io.rsocket.metadata.WellKnownMimeType;
//import io.rsocket.transport.netty.client.WebsocketClientTransport;
//
//import java.net.URI;
//
//public class RSocketClientManager {
//    private static final String SERVER_URL_EMULATOR = "ws://10.0.2.2:8081/rsocket"; // emulator
//    //private static final String SERVER_URL_DEVICE = "ws://192.168.1.122:8081/rsocket"; //home
//    //private static final String SERVER_URL_DEVICE = "ws://172.20.30.54:8081/rsocket"; //afeka
//    private static RSocketClientManager instance;
//    private RSocket rSocket;
//    private boolean isConnecting = false;
//
//    private RSocketClientManager() {}
//
//    public static synchronized RSocketClientManager getInstance() {
//        if (instance == null) {
//            instance = new RSocketClientManager();
//        }
//        return instance;
//    }
//
//    public boolean connect() {
//        if (rSocket != null && !rSocket.isDisposed()) {
//            return true;
//        }
//
//        if (isConnecting) {
//            return false;
//        }
//
//        try {
//            isConnecting = true;
//
//            URI uri = URI.create(SERVER_URL_EMULATOR); // שינוי פה
//            System.out.println("Connecting to RSocket server: " + uri);
//
//            rSocket = RSocketConnector.create()
//                    .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString())
//                    .dataMimeType("application/json")
//                    .connect(WebsocketClientTransport.create(uri))
//                    .block();
//
//            boolean isConnected = rSocket != null && !rSocket.isDisposed();
//
//            if (isConnected) {
//                System.out.println("Connected to RSocket server successfully!");
//            } else {
//                System.err.println("Failed to establish RSocket connection.");
//            }
//
//            return isConnected;
//
//        } catch (Exception e) {
//            System.err.println("RSocket connection error: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        } finally {
//            isConnecting = false;
//        }
//    }
//
//    public RSocket getRSocket() {
//        return rSocket;
//    }
//
//    public boolean isConnected() {
//        return rSocket != null && !rSocket.isDisposed();
//    }
//}
//
package com.example.rsocket_sdk.network;

import android.util.Log;

import java.net.URI;
import java.time.Duration;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.WebsocketClientTransport;

public class RSocketClientManager {
    private static final String TAG = "RSocketClientManager";
    private static final String SERVER_URL = "ws://10.0.2.2:8081/rsocket"; // לשנות לפי הצורך

    private static RSocketClientManager instance;
    private RSocket rSocket;
    private boolean isConnecting = false;

    private RSocketClientManager() {}

    public static synchronized RSocketClientManager getInstance() {
        if (instance == null) {
            instance = new RSocketClientManager();
        }
        return instance;
    }

    /**
     * מבצע חיבור ל-RSocket רק אם אין חיבור קיים.
     * מחזיר true אם הצליח, false אם נכשל.
     */
    public synchronized boolean connect() {
        if (isConnected()) {
            Log.d(TAG, "Already connected to RSocket.");
            return true;
        }

        if (isConnecting) {
            Log.d(TAG, "Already trying to connect...");
            return false;
        }

        try {
            isConnecting = true;

            URI uri = URI.create(SERVER_URL);
            Log.d(TAG, "Connecting to RSocket server: " + uri);

            rSocket = RSocketConnector.create()
                    .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString())
                    .dataMimeType("application/json")
                    .keepAlive(Duration.ofSeconds(20), Duration.ofSeconds(90)) // ✅ תואם לגרסה שלך// 💡 מונע timeout
                    .connect(WebsocketClientTransport.create(uri))
                    .block();

            if (rSocket != null && !rSocket.isDisposed()) {
                Log.i(TAG, "✅ RSocket connected successfully.");
                return true;
            } else {
                Log.e(TAG, "❌ Failed to establish RSocket connection.");
                return false;
            }

        } catch (Exception e) {
            Log.e(TAG, "❌ RSocket connection error: " + e.getMessage(), e);
            return false;
        } finally {
            isConnecting = false;
        }
    }

    /**
     * מחזיר את החיבור הנוכחי, גם אם צריך לחדש אותו.
     */
    public synchronized RSocket getOrConnect() {
        if (!isConnected()) {
            boolean success = connect();
            if (!success) {
                throw new IllegalStateException("❌ RSocket connection could not be established.");
            }
        }
        return rSocket;
    }

    /**
     * האם יש חיבור פעיל
     */
    public boolean isConnected() {
        return rSocket != null && !rSocket.isDisposed();
    }

    /**
     * מחזיר את החיבור (ללא ניסיון חידוש)
     */
    public RSocket getRSocket() {
        return rSocket;
    }

    /**
     * סוגר את החיבור הקיים
     */
    public void disconnect() {
        if (rSocket != null && !rSocket.isDisposed()) {
            rSocket.dispose();
            rSocket = null;
            Log.i(TAG, "🔌 RSocket connection closed.");
        }
    }
}
