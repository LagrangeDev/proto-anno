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
        var initialMessage = new GeneralTestMessage();

        initialMessage.intField = 42;
        initialMessage.doubleField = 0.5;
        initialMessage.stringField = "Hello, World!";
        initialMessage.booleanField = true;
        initialMessage.intListField = IntList.of(1, 2, 3, 4, 5);
        initialMessage.doubleListField = DoubleList.of(0.1, 0.2, 0.3, 0.4, 0.5);

        var nestedMessage1 = new GeneralTestMessage.NestedMessage();
        nestedMessage1.nestedStringListField = List.of("Hello", "World");

        var nestedMessage2 = new GeneralTestMessage.NestedMessage();
        nestedMessage2.nestedStringListField = List.of("Goodbye", "World");

        initialMessage.nestedMessageField = List.of(nestedMessage1, nestedMessage2);

        var serialized = ProtobufSerializer.of(GeneralTestMessage.class).serialize(initialMessage);
        var deserialized = ProtobufDeserializer.of(GeneralTestMessage.class).deserialize(serialized);

        System.out.println("Deserialized message: ");
        System.out.println("intField: " + deserialized.intField);
        System.out.println("doubleField: " + deserialized.doubleField);
        System.out.println("stringField: " + deserialized.stringField);
        System.out.println("booleanField: " + deserialized.booleanField);
        System.out.println("intListField: " + deserialized.intListField);
        System.out.println("doubleListField: " + deserialized.doubleListField);
        System.out.println("nestedMessageField: ");
        for (var nestedMessage : deserialized.nestedMessageField) {
            System.out.println("  nestedStringListField: "
                    + nestedMessage.nestedStringListField.get(0)
                    + ", "
                    + nestedMessage.nestedStringListField.get(1));
        }
    }
}
