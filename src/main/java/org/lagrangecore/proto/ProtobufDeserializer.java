package org.lagrangecore.proto;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.lagrangecore.proto.annotations.ProtoField;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A deserializer for protobuf messages.
 *
 * @param <T> the type of the protobuf message
 */
public final class ProtobufDeserializer<T extends ProtoMessage> {
    private static final ConcurrentMap<Class<?>, ProtobufDeserializer<? extends ProtoMessage>> deserializers
            = new ConcurrentHashMap<>();

    private final Class<T> clazz;
    private final List<ProtoFieldDescriptor> fieldDescriptors = new ArrayList<>();
    private final Int2ObjectMap<ProtoFieldDeserializer> fieldDeserializers = new Int2ObjectOpenHashMap<>();

    private ProtobufDeserializer(Class<T> clazz) {
        this.clazz = clazz;

        if (!ProtoMessage.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not a ProtoMessage");
        }

        var fields = clazz.getDeclaredFields();
        for (var field : fields) {
            var protoField = field.getAnnotation(ProtoField.class);
            if (protoField != null) {
                var fieldDescriptor = ProtoFieldDescriptor.fromField(field, protoField);
                fieldDescriptors.add(fieldDescriptor);
                fieldDeserializers.put(protoField.value(), ProtoFieldDeserializer.create(fieldDescriptor));
            }
        }
    }

    /**
     * Get a deserializer for the given class.
     *
     * @param clazz the class of the protobuf message
     * @param <T>   the type of the protobuf message
     * @return the deserializer; if it does not exist, a new one is created
     */
    @SuppressWarnings("unchecked")
    public static <T extends ProtoMessage> ProtobufDeserializer<T> of(Class<T> clazz) {
        if (!deserializers.containsKey(clazz)) {
            deserializers.putIfAbsent(clazz, new ProtobufDeserializer<>(clazz));
        }
        return (ProtobufDeserializer<T>) deserializers.get(clazz);
    }

    private static void setDefaultValueFor(ProtoMessage message, ProtoFieldDescriptor desc)
            throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, InstantiationException {
        var declaredField = desc.declaredField();
        var clazz = declaredField.getType();
        if (clazz == int.class) {
            declaredField.setInt(message, 0);
        } else if (clazz == long.class) {
            declaredField.setLong(message, 0L);
        } else if (clazz == float.class) {
            declaredField.setFloat(message, 0.0f);
        } else if (clazz == double.class) {
            declaredField.setDouble(message, 0.0);
        } else if (clazz == boolean.class) {
            declaredField.setBoolean(message, false);
        } else if (clazz == String.class) {
            declaredField.set(message, "");
        } else if (clazz == byte[].class) {
            declaredField.set(message, new byte[0]);
        } else if (clazz == IntList.class) {
            declaredField.set(message, new IntArrayList());
        } else if (clazz == LongList.class) {
            declaredField.set(message, new LongArrayList());
        } else if (clazz == FloatList.class) {
            declaredField.set(message, new FloatArrayList());
        } else if (clazz == DoubleList.class) {
            declaredField.set(message, new DoubleArrayList());
        } else if (clazz == BooleanList.class) {
            declaredField.set(message, new BooleanArrayList());
        } else if (clazz == List.class) {
            declaredField.set(message, new ArrayList<>());
        } else {
            if (desc.isOptional()) {
                declaredField.set(message, null);
            } else {
                var emptyDraft = clazz.getDeclaredConstructor().newInstance();
                setDefaultValueFor((ProtoMessage) emptyDraft, desc);
                declaredField.set(message, emptyDraft);
            }
        }
    }

    /**
     * Deserialize a protobuf message from a byte array.
     *
     * @param data the data to deserialize
     * @return the deserialized message
     */
    public T deserialize(byte[] data) {
        try {
            var message = clazz.getDeclaredConstructor().newInstance();
            var stream = CodedInputStream.newInstance(data);
            var visitedSet = new IntArraySet();

            while (!stream.isAtEnd()) {
                var tag = stream.readTag();
                var fieldNumber = WireFormat.getTagFieldNumber(tag);
                var wireType = WireFormat.getTagWireType(tag);
                var fieldDescriptor = fieldDeserializers.get(fieldNumber);
                if (fieldDescriptor == null) {
                    stream.skipField(tag);
                    continue;
                }
                visitedSet.add(fieldNumber);
                fieldDescriptor.deserialize(message, stream, wireType);
            }

            for (var fieldDescriptor : fieldDescriptors) {
                if (!visitedSet.contains(fieldDescriptor.fieldNumber())) {
                    setDefaultValueFor(message, fieldDescriptor);
                }
            }

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
