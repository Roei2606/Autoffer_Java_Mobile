package com.example.rsocket_sdk.network;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.metadata.CompositeMetadataCodec;
import io.rsocket.metadata.TaggingMetadataCodec;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.util.DefaultPayload;


public class PingSender {

    public static void sendPing(android.content.Context context) {
        try {
            Payload payload = RSocketUtils.buildPayload("ping", "ping from android");

            RSocketClientManager.getInstance()
                    .getOrConnect()
                    .requestResponse(payload)
                    .map(Payload::getDataUtf8)
                    .doOnNext(response -> {
                        Log.i("✅ PING", "Server replied: " + response);
                        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                        handler.post(() -> Toast.makeText(context, "✅ " + response, Toast.LENGTH_LONG).show());
                    })
                    .doOnError(error -> {
                        Log.e("❌ PING", "Ping failed", error);
                        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                        handler.post(() -> Toast.makeText(context, "❌ Ping failed: " + error.getMessage(), Toast.LENGTH_LONG).show());
                    })
                    .subscribe();

        } catch (Exception e) {
            Log.e("❌ PING", "Exception while sending ping", e);
        }
    }
}
