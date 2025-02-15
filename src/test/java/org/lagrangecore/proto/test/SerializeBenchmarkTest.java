package org.lagrangecore.proto.test;

import org.lagrangecore.proto.ProtobufSerializer;

import java.io.IOException;

/**
 * A simple benchmark test for the protobuf serialization.
 * @author Wesley Young
 */
public final class SerializeBenchmarkTest {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        var message = new SerializeBenchmarkTestMessage();

        long start = System.currentTimeMillis();
        var serialized = ProtobufSerializer.of(SerializeBenchmarkTestMessage.class).serialize(message);
        System.out.println(toHex(serialized));
        System.out.println("First serialize time: " + (System.currentTimeMillis() - start) + "ms");

        long start2 = System.nanoTime();
        ProtobufSerializer.of(SerializeBenchmarkTestMessage.class).serialize(message);
        System.out.println("Second serialize time: " + (System.nanoTime() - start2) + "ns");

        long start3 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            ProtobufSerializer.of(SerializeBenchmarkTestMessage.class).serialize(message);
        }
        System.out.println("1000000 serialize time: " + (System.currentTimeMillis() - start3) + "ms");
    }

    public static String toHex(byte[] bytes) {
        var hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = "0123456789ABCDEF".charAt(v >>> 4);
            hexChars[i * 2 + 1] = "0123456789ABCDEF".charAt(v & 0x0F);
        }
        return new String(hexChars);
    }
}
