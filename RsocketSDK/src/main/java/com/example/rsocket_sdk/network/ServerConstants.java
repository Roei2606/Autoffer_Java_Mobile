package com.example.rsocket_sdk.network;

public class ServerConstants {
    // כתובת לשרת לוקאלי כשעובדים באמולטור
    public static final String EMULATOR_SERVER_URL = "ws://10.0.2.2:8080/rsocket";

    // כתובת לשרת ב־Cloud Run
    public static final String PUBLIC_SERVER_URL = "wss://autoffer-server-313683195324.europe-west1.run.app/rsocket";
}
