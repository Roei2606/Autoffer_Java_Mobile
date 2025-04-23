package com.example.chatsdk.network;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.WebsocketClientTransport;

import java.net.URI;

public class RSocketClientManager {
    private static final String SERVER_HOST = "10.0.2.2";
    private static final int SERVER_PORT = 7001;
    private static final String RSOCKET_PATH = "/rsocket";
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

    public boolean connect() {
        if (rSocket != null && !rSocket.isDisposed()) {
            return true;
        }

        if (isConnecting) {
            return false;
        }

        try {
            isConnecting = true;
            URI uri = URI.create("ws://" + SERVER_HOST + ":" + SERVER_PORT + RSOCKET_PATH);
            System.out.println("Connecting to RSocket server: " + uri);

            rSocket = RSocketConnector.create()
                    .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString()) // ✅ תיקון קריטי!
                    .dataMimeType("application/json")
                    .connect(WebsocketClientTransport.create(uri))
                    .block();

            boolean isConnected = rSocket != null && !rSocket.isDisposed();

            if (isConnected) {
                System.out.println("Connected to RSocket server successfully!");
            } else {
                System.err.println("Failed to establish RSocket connection.");
            }

            return isConnected;

        } catch (Exception e) {
            System.err.println("RSocket connection error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            isConnecting = false;
        }
    }

    public RSocket getRSocket() {
        return rSocket;
    }

    public boolean isConnected() {
        return rSocket != null && !rSocket.isDisposed();
    }
}
