package com.example.autofferandroid.utils;

import java.util.List;

public class ByteUtils {

    public static byte[] toPrimitive(List<Byte> byteList) {
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }
        return result;
    }
}
