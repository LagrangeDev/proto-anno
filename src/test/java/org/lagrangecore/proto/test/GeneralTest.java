package org.lagrangecore.proto.test;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.lagrangecore.proto.ProtobufDeserializer;
import org.lagrangecore.proto.ProtobufSerializer;

import java.util.List;

/**
 * A general test for the protobuf serialization and deserialization.
 * @author Wesley Young
 */
public final class GeneralTest {
    public static void main(String[] args) {
        var initialMessage = GeneralTestMessage.builder()
                .intField(42)
                .doubleField(0.5)
                .stringField("Hello, World!")
                .booleanField(true)
                .intListField(IntList.of(1, 2, 3, 4, 5))
                .doubleListField(DoubleList.of(0.1, 0.2, 0.3, 0.4, 0.5))
                .nestedMessageField(List.of(
                        GeneralTestMessage.NestedMessage.builder()
                                .nestedStringListField(List.of("Hello", "World"))
                                .build(),
                        GeneralTestMessage.NestedMessage.builder()
                                .nestedStringListField(List.of("Goodbye", "World"))
                                .build()
                ))
                .build();

        var serialized = ProtobufSerializer.of(GeneralTestMessage.class).serialize(initialMessage);
        var deserialized = ProtobufDeserializer.of(GeneralTestMessage.class).deserialize(serialized);

        System.out.println("Deserialized message: " + deserialized);
    }
}
