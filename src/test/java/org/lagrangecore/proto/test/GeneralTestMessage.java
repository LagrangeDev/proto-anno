package org.lagrangecore.proto.test;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.lagrangecore.proto.ProtoMessage;
import org.lagrangecore.proto.annotations.ProtoField;

import java.util.List;

@Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class GeneralTestMessage extends ProtoMessage {
    @ProtoField(1)
    public int intField;

    @ProtoField(2)
    public double doubleField;

    @ProtoField(3)
    public String stringField;

    @ProtoField(4)
    public boolean booleanField;

    @ProtoField(5)
    public IntList intListField;

    @ProtoField(6)
    public DoubleList doubleListField;

    @ProtoField(7)
    public List<NestedMessage> nestedMessageField;

    @Builder @NoArgsConstructor @AllArgsConstructor @ToString
    public static class NestedMessage extends ProtoMessage {
        @ProtoField(2)
        public List<String> nestedStringListField;
    }
}
