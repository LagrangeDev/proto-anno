package org.lagrangecore.proto;

import com.google.protobuf.CodedOutputStream;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import org.lagrangecore.proto.annotations.ProtoField;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ProtobufSerializer<T extends ProtoMessage> {
    private static final ConcurrentMap<Class<?>, ProtobufSerializer<? extends ProtoMessage>> serializers
            = new ConcurrentHashMap<>();

    private final List<ProtoFieldDescriptor> fieldDescriptors = new ArrayList<>();
    private final List<ProtoFieldSerializer> fieldSerializers = new ArrayList<>();

    private ProtobufSerializer(Class<T> clazz) {
        if (!ProtoMessage.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not a ProtoMessage");
        }

        try {
            clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " does not have a no-argument constructor", e);
        }

        var fields = clazz.getDeclaredFields();
        for (var field : fields) {
            var protoField = field.getAnnotation(ProtoField.class);
            if (protoField != null) {
                var fieldDescriptor = ProtoFieldDescriptor.fromField(field, protoField);
                fieldDescriptors.add(fieldDescriptor);
                fieldSerializers.add(ProtoFieldSerializer.create(fieldDescriptor));
            }
        }
    }

    /**
     * Get a serializer for the given class.
     *
     * @param clazz the class of the protobuf message
     * @param <T>   the type of the protobuf message
     * @return the serializer; if it does not exist, a new one is created
     */
    @SuppressWarnings("unchecked")
    public static <T extends ProtoMessage> ProtobufSerializer<T> of(Class<T> clazz) {
        if (!serializers.containsKey(clazz)) {
            serializers.putIfAbsent(clazz, new ProtobufSerializer<>(clazz));
        }
        return (ProtobufSerializer<T>) serializers.get(clazz);
    }

    int computeSize(T message) throws IllegalAccessException {
        if (message.serializedSize > 0) {
            return message.serializedSize;
        }

        if (message.lengthDelimitedFieldSizes == null) {
            message.lengthDelimitedFieldSizes = new Int2IntArrayMap();
        }
        for (var fieldDescriptor : fieldDescriptors) {
            fieldDescriptor.computeSerializedSize(message);
        }
        return message.serializedSize;
    }

    /**
     * Serialize a protobuf message to a byte array.
     *
     * @param message the message to serialize
     * @return the serialized message
     */
    public byte[] serialize(T message) {
        try {
            var byteBuffer = ByteBuffer.allocate(computeSize(message));
            var stream = CodedOutputStream.newInstance(byteBuffer);
            for (var fieldSerializer : fieldSerializers) {
                fieldSerializer.serialize(message, stream);
            }
            stream.flush();
            return byteBuffer.array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void serialize(T message, CodedOutputStream stream) throws IOException, IllegalAccessException {
        for (var fieldSerializer : fieldSerializers) {
            fieldSerializer.serialize(message, stream);
        }
    }
}
