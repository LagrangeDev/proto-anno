package org.lagrangecore.proto;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lagrangecore.proto.annotations.DisablePacking;
import org.lagrangecore.proto.annotations.ProtoField;
import org.lagrangecore.proto.annotations.TypeMappedTo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public record ProtoFieldDescriptor(
        int fieldNumber,
        WireFormat.FieldType fieldType,
        boolean isOptional,
        boolean isRepeated,
        boolean isPacked,
        Field declaredField,
        Type actualType
) implements Comparable<ProtoFieldDescriptor> {
    static ProtoFieldDescriptor fromField(Field field, ProtoField protoField) {
        var typeMappedTo = field.getAnnotation(TypeMappedTo.class);

        if (field.getType() == IntList.class) {
            return new ProtoFieldDescriptor(
                    protoField.value(),
                    typeMappedTo == null ? WireFormat.FieldType.INT32 : typeMappedTo.value().toPbFieldType(),
                    false, true,
                    !field.isAnnotationPresent(DisablePacking.class),
                    field, int.class
            );
        }

        if (field.getType() == LongList.class) {
            return new ProtoFieldDescriptor(
                    protoField.value(),
                    typeMappedTo == null ? WireFormat.FieldType.INT64 : typeMappedTo.value().toPbFieldType(),
                    false, true,
                    !field.isAnnotationPresent(DisablePacking.class),
                    field, long.class
            );
        }

        if (field.getType() == FloatList.class) {
            return new ProtoFieldDescriptor(
                    protoField.value(),
                    typeMappedTo == null ? WireFormat.FieldType.FLOAT : typeMappedTo.value().toPbFieldType(),
                    false, true,
                    !field.isAnnotationPresent(DisablePacking.class),
                    field, float.class
            );
        }

        if (field.getType() == DoubleList.class) {
            return new ProtoFieldDescriptor(
                    protoField.value(),
                    typeMappedTo == null ? WireFormat.FieldType.DOUBLE : typeMappedTo.value().toPbFieldType(),
                    false, true,
                    !field.isAnnotationPresent(DisablePacking.class),
                    field, double.class
            );
        }

        if (field.getType() == BooleanList.class) {
            return new ProtoFieldDescriptor(
                    protoField.value(),
                    typeMappedTo == null ? WireFormat.FieldType.BOOL : typeMappedTo.value().toPbFieldType(),
                    false, true,
                    !field.isAnnotationPresent(DisablePacking.class),
                    field, boolean.class
            );
        }

        if (field.getType() == List.class) {
            var genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType parameterizedType
                    && parameterizedType.getActualTypeArguments().length > 0) {
                var actualType = parameterizedType.getActualTypeArguments()[0];
                if (Number.class.isAssignableFrom((Class<?>) actualType)) {
                    throw new IllegalArgumentException("Please use fastutil types instead.");
                }
                var fieldType = typeMappedTo == null
                        ? inferFieldType(actualType) : typeMappedTo.value().toPbFieldType();
                return new ProtoFieldDescriptor(
                        protoField.value(), fieldType,
                        false, true, false,
                        field, parameterizedType.getActualTypeArguments()[0]
                );
            } else {
                throw new IllegalArgumentException("Unsupported field type: " + genericType.getTypeName());
            }
        }

        return new ProtoFieldDescriptor(
                field.getAnnotation(ProtoField.class).value(),
                typeMappedTo == null
                        ? inferFieldType(field.getType())
                        : typeMappedTo.value().toPbFieldType(),
                field.isAnnotationPresent(Nullable.class), false, false,
                field, field.getType()
        );
    }

    static WireFormat.FieldType inferFieldType(final Type type) {
        if (type == int.class) {
            return WireFormat.FieldType.INT32;
        } else if (type == long.class) {
            return WireFormat.FieldType.INT64;
        } else if (type == float.class) {
            return WireFormat.FieldType.FLOAT;
        } else if (type == double.class) {
            return WireFormat.FieldType.DOUBLE;
        } else if (type == boolean.class) {
            return WireFormat.FieldType.BOOL;
        } else if (type == String.class) {
            return WireFormat.FieldType.STRING;
        } else if (type == byte[].class) {
            return WireFormat.FieldType.BYTES;
        } else if (type instanceof Class<?> clazz
                && ProtoMessage.class.isAssignableFrom(clazz)
        ) {
            return WireFormat.FieldType.MESSAGE;
        } else {
            throw new IllegalArgumentException("Unsupported scalar field type");
        }
    }

    void computeSerializedSize(ProtoMessage message) throws IllegalAccessException {
        int tagSize = CodedOutputStream.computeTagSize(fieldNumber);
        if (isRepeated) {
            @Nullable var list = (List<?>) declaredField.get(message);
            if (list == null || list.isEmpty()) {
                return;
            }
            if (isPacked) {
                int repeatedSize = calculateRepeatedSerializedSize(list);
                int packedLengthSize = CodedOutputStream.computeUInt32SizeNoTag(repeatedSize);
                message.lengthDelimitedFieldSizes.put(fieldNumber, repeatedSize);
                message.serializedSize += tagSize + packedLengthSize + repeatedSize;
            } else {
                message.serializedSize += tagSize * list.size() + calculateRepeatedSerializedSize(list);
            }
        } else {
            message.serializedSize += calculateSingleSerializedSize(message) + tagSize;
        }
    }

    @SuppressWarnings("unchecked")
    private int calculateRepeatedSerializedSize(@NotNull List<?> list) {
        return switch (fieldType) {
            case INT32 -> ((IntList) list).intStream().map(CodedOutputStream::computeInt32SizeNoTag).sum();
            case INT64 -> ((LongList) list).longStream().mapToInt(CodedOutputStream::computeInt64SizeNoTag).sum();
            case UINT32 -> ((IntList) list).intStream().map(CodedOutputStream::computeUInt32SizeNoTag).sum();
            case UINT64 -> ((LongList) list).longStream().mapToInt(CodedOutputStream::computeUInt64SizeNoTag).sum();
            case SINT32 -> ((IntList) list).intStream().map(CodedOutputStream::computeSInt32SizeNoTag).sum();
            case SINT64 -> ((LongList) list).longStream().mapToInt(CodedOutputStream::computeSInt64SizeNoTag).sum();
            case FIXED32 -> ((IntList) list).intStream().map(CodedOutputStream::computeFixed32SizeNoTag).sum();
            case FIXED64 -> ((LongList) list).longStream().mapToInt(CodedOutputStream::computeFixed64SizeNoTag).sum();
            case SFIXED32 -> ((IntList) list).intStream().map(CodedOutputStream::computeSFixed32SizeNoTag).sum();
            case SFIXED64 -> ((LongList) list).longStream().mapToInt(CodedOutputStream::computeSFixed64SizeNoTag).sum();
            case FLOAT -> ((FloatList) list).doubleStream().mapToInt(
                    value -> CodedOutputStream.computeFloatSizeNoTag((float) value)).sum();
            case DOUBLE -> ((DoubleList) list).doubleStream().mapToInt(CodedOutputStream::computeDoubleSizeNoTag).sum();
            case BOOL -> ((BooleanList) list).stream().mapToInt(CodedOutputStream::computeBoolSizeNoTag).sum();
            case STRING -> list.stream().mapToInt(o -> CodedOutputStream.computeStringSizeNoTag((String) o)).sum();
            case BYTES -> list.stream().mapToInt(o -> CodedOutputStream.computeBytesSizeNoTag((ByteString) o)).sum();
            case MESSAGE -> {
                var serializer = ProtobufSerializer.of((Class<ProtoMessage>) actualType);
                yield list.stream().mapToInt(o -> {
                    try {
                        int bodySize = serializer.computeSize((ProtoMessage) o);
                        int lengthSize = CodedOutputStream.computeUInt32SizeNoTag(bodySize);
                        return lengthSize + bodySize;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).sum();
            }
            default -> throw new IllegalArgumentException("Unsupported field type");
        };
    }

    @SuppressWarnings("unchecked")
    private int calculateSingleSerializedSize(ProtoMessage msg) throws IllegalAccessException {
        return switch (fieldType) {
            case INT32 -> CodedOutputStream.computeInt32SizeNoTag(declaredField.getInt(msg));
            case INT64 -> CodedOutputStream.computeInt64SizeNoTag(declaredField.getLong(msg));
            case UINT32 -> CodedOutputStream.computeUInt32SizeNoTag(declaredField.getInt(msg));
            case UINT64 -> CodedOutputStream.computeUInt64SizeNoTag(declaredField.getLong(msg));
            case SINT32 -> CodedOutputStream.computeSInt32SizeNoTag(declaredField.getInt(msg));
            case SINT64 -> CodedOutputStream.computeSInt64SizeNoTag(declaredField.getLong(msg));
            case FIXED32 -> CodedOutputStream.computeFixed32SizeNoTag(declaredField.getInt(msg));
            case FIXED64 -> CodedOutputStream.computeFixed64SizeNoTag(declaredField.getLong(msg));
            case SFIXED32 -> CodedOutputStream.computeSFixed32SizeNoTag(declaredField.getInt(msg));
            case SFIXED64 -> CodedOutputStream.computeSFixed64SizeNoTag(declaredField.getLong(msg));
            case FLOAT -> CodedOutputStream.computeFloatSizeNoTag(declaredField.getFloat(msg));
            case DOUBLE -> CodedOutputStream.computeDoubleSizeNoTag(declaredField.getDouble(msg));
            case BOOL -> CodedOutputStream.computeBoolSizeNoTag(declaredField.getBoolean(msg));
            case STRING -> CodedOutputStream.computeStringSizeNoTag((String) declaredField.get(msg));
            case BYTES -> CodedOutputStream.computeByteArraySizeNoTag((byte[]) declaredField.get(msg));
            case MESSAGE -> {
                @Nullable var message = (ProtoMessage) declaredField.get(msg);
                if (message == null) {
                    yield 0;
                }
                int bodySize = ProtobufSerializer.of((Class<ProtoMessage>) actualType).computeSize(message);
                int lengthSize = CodedOutputStream.computeUInt32SizeNoTag(bodySize);
                yield lengthSize + bodySize;
            }
            default -> throw new IllegalArgumentException("Unsupported field type");
        };
    }

    @Override
    public int compareTo(@NotNull ProtoFieldDescriptor o) {
        return Integer.compare(fieldNumber, o.fieldNumber);
    }
}
