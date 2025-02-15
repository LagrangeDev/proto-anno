package org.lagrangecore.proto.test;

import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.lagrangecore.proto.ProtoMessage;
import org.lagrangecore.proto.annotations.DisablePacking;
import org.lagrangecore.proto.annotations.FieldType;
import org.lagrangecore.proto.annotations.ProtoField;
import org.lagrangecore.proto.annotations.TypeMappedTo;

import java.util.List;

public class SerializeBenchmarkTestMessage extends ProtoMessage {
    @ProtoField(1)
    public int intField = 42;

    @ProtoField(2)
    public String stringField = "Hello, World!";

    @ProtoField(3)
    @DisablePacking
    @TypeMappedTo(FieldType.SINT32)
    public IntList intListField = IntList.of(1, 2, 3, 4, 5);

    @ProtoField(4)
    public DoubleList doubleListField = DoubleList.of(0.1, 0.2, 0.3, 0.4, 0.5);

    @ProtoField(5)
    public BooleanList booleanListField = BooleanList.of(true, false, true, false, true);

    @ProtoField(6)
    public List<NestedMessage> nestedMessageField = List.of(new NestedMessage());

    public static class NestedMessage extends ProtoMessage {
        @ProtoField(1)
        public int nestedIntField = 42;

        @ProtoField(2)
        public List<String> nestedStringListField = List.of("Hello", "World");
    }
}
