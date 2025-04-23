package com.example.chatsdk.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.rsocket.Payload;
import io.rsocket.metadata.CompositeMetadataCodec;
import io.rsocket.metadata.RoutingMetadata;
import io.rsocket.metadata.TaggingMetadataCodec;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.util.DefaultPayload;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class RSocketUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * בונה Payload תקני לפי Composite Metadata
     */
    public static Payload buildPayload(String route, Object data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            ByteBuf dataBuf = Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8));

            RoutingMetadata routingMetadata = TaggingMetadataCodec.createRoutingMetadata(
                    ByteBufAllocator.DEFAULT,
                    Collections.singletonList(route)
            );

            CompositeByteBuf compositeMetadata = ByteBufAllocator.DEFAULT.compositeBuffer();

            // ✅ הוספת route
            CompositeMetadataCodec.encodeAndAddMetadata(
                    compositeMetadata,
                    ByteBufAllocator.DEFAULT,
                    WellKnownMimeType.MESSAGE_RSOCKET_ROUTING,
                    routingMetadata.getContent()
            );

            // ✅ הוספת data mime type
            CompositeMetadataCodec.encodeAndAddMetadata(
                    compositeMetadata,
                    ByteBufAllocator.DEFAULT,
                    WellKnownMimeType.APPLICATION_JSON,
                    Unpooled.EMPTY_BUFFER
            );

            return DefaultPayload.create(dataBuf, compositeMetadata);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build RSocket payload for route: " + route, e);
        }
    }

    /**
     * ממיר Payload חזרה לאובייקט
     */
    public static <T> T parsePayload(Payload payload, Class<T> type) {
        try {
            return objectMapper.readValue(payload.getDataUtf8(), type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse payload to " + type.getSimpleName(), e);
        } finally {
            payload.release();
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
