package com.example.autofferandroid;

import android.app.Application;

import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.ServerConfig;


public class AutofferApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ServerConfig.init(this);
        // כאן ייווצר החיבור ל-RSocket מיד כשהאפליקציה מתחילה
        RSocketClientManager.getInstance().connect();
    }
}
