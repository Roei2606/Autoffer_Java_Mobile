package com.example.rsocket_sdk.network;


public class ServerConstants {

    // Local Server Details
    public static final String EMULATOR_HOST = "10.0.2.2";
    public static final String LOCAL_DEVICE_HOST = "192.168.1.101"; // שים את ה-IP של השרת ברשת הפנימית שלך אם צריך
    public static final int LOCAL_SERVER_PORT = 7001;
    public static final String LOCAL_RSOCKET_PATH = "/rsocket";

    // Public Server (Production) - Ngrok / VPS
    public static final String PUBLIC_SERVER_URL = "wss://090b-83-130-174-183.ngrok-free.app/rsocket";


}

