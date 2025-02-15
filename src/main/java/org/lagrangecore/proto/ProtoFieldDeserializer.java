package org.lagrangecore.proto;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@FunctionalInterface
public interface ProtoFieldDeserializer {
    static ProtoFieldDeserializer create(ProtoFieldDescriptor desc) {
        return switch (desc.fieldType()) {
            case INT32 -> forInt32(desc);
            case INT64 -> forInt64(desc);
            case UINT32 -> forUInt32(desc);
            case UINT64 -> forUInt64(desc);
            case SINT32 -> forSInt32(desc);
            case SINT64 -> forSInt64(desc);
            case FIXED32 -> forFixed32(desc);
            case FIXED64 -> forFixed64(desc);
            case SFIXED32 -> forSFixed32(desc);
            case SFIXED64 -> forSFixed64(desc);
            case FLOAT -> forFloat(desc);
            case DOUBLE -> forDouble(desc);
            case BOOL -> forBool(desc);
            case STRING -> forString(desc);
            case BYTES -> forBytes(desc);
            case MESSAGE -> forMessage(desc);
            default -> throw new IllegalArgumentException("Unsupported field type: " + desc.fieldType());
        };
    }

    static ProtoFieldDeserializer forInt32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (IntList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new IntArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readInt32());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = new IntArrayList();
                    var input = CodedInputStream.newInstance(in.readByteBuffer());
                    while (!input.isAtEnd()) {
                        list.add(input.readInt32());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readInt32());
                }
            };
        }
    }

    static ProtoFieldDeserializer forInt64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (LongList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new LongArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readInt64());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = new LongArrayList();
                    var input = CodedInputStream.newInstance(in.readByteBuffer());
                    while (!input.isAtEnd()) {
                        list.add(input.readInt64());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readInt64());
                }
            };
        }
    }

    static ProtoFieldDeserializer forUInt32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (IntList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new IntArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readUInt32());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = new IntArrayList();
                    var input = CodedInputStream.newInstance(in.readByteBuffer());
                    while (!input.isAtEnd()) {
                        list.add(input.readUInt32());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readUInt32());
                }
            };
        }
    }

    static ProtoFieldDeserializer forUInt64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (LongList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new LongArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readUInt64());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = new LongArrayList();
                    var input = CodedInputStream.newInstance(in.readByteBuffer());
                    while (!input.isAtEnd()) {
                        list.add(input.readUInt64());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readUInt64());
                }
            };
        }
    }

    static ProtoFieldDeserializer forSInt32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (IntList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new IntArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readSInt32());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = new IntArrayList();
                    var input = CodedInputStream.newInstance(in.readByteBuffer());
                    while (!input.isAtEnd()) {
                        list.add(input.readSInt32());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readSInt32());
                }
            };
        }
    }

    static ProtoFieldDeserializer forSInt64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (LongList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new LongArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readSInt64());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = new LongArrayList();
                    var input = CodedInputStream.newInstance(in.readByteBuffer());
                    while (!input.isAtEnd()) {
                        list.add(input.readSInt64());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readSInt64());
                }
            };
        }
    }

    static ProtoFieldDeserializer forFixed32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED32) {
                    var list = (IntList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new IntArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readFixed32());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32() / 4;
                    var list = new IntArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readFixed32());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED32) {
                    desc.declaredField().set(draft, in.readFixed32());
                }
            };
        }
    }

    static ProtoFieldDeserializer forFixed64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED64) {
                    var list = (LongList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new LongArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readFixed64());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32() / 8;
                    var list = new LongArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readFixed64());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED64) {
                    desc.declaredField().set(draft, in.readFixed64());
                }
            };
        }
    }

    static ProtoFieldDeserializer forSFixed32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED32) {
                    var list = (IntList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new IntArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readSFixed32());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32() / 4;
                    var list = new IntArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readSFixed32());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED32) {
                    desc.declaredField().set(draft, in.readSFixed32());
                }
            };
        }
    }

    static ProtoFieldDeserializer forSFixed64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED64) {
                    var list = (LongList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new LongArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readSFixed64());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32() / 8;
                    var list = new LongArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readSFixed64());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED64) {
                    desc.declaredField().set(draft, in.readSFixed64());
                }
            };
        }
    }

    static ProtoFieldDeserializer forFloat(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED32) {
                    var list = (FloatList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new FloatArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readFloat());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32() / 4;
                    var list = new FloatArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readFloat());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED32) {
                    desc.declaredField().set(draft, in.readFloat());
                }
            };
        }
    }

    static ProtoFieldDeserializer forDouble(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED64) {
                    var list = (DoubleList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new DoubleArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readDouble());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32() / 8;
                    var list = new DoubleArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readDouble());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_FIXED64) {
                    desc.declaredField().set(draft, in.readDouble());
                }
            };
        }
    }

    static ProtoFieldDeserializer forBool(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    var list = (BooleanList) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new BooleanArrayList();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readBool());
                } else if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int size = in.readUInt32();
                    var list = new BooleanArrayList(size);
                    for (int i = 0; i < size; i++) {
                        list.add(in.readBool());
                    }
                    desc.declaredField().set(draft, list);
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_VARINT) {
                    desc.declaredField().set(draft, in.readBool());
                }
            };
        }
    }

    static ProtoFieldDeserializer forString(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = (List<String>) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new ArrayList<>();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readString());
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    desc.declaredField().set(draft, in.readString());
                }
            };
        }
    }

    static ProtoFieldDeserializer forBytes(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = (List<byte[]>) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new ArrayList<>();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(in.readByteArray());
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    desc.declaredField().set(draft, in.readByteArray());
                }
            };
        }
    }

    static ProtoFieldDeserializer forMessage(ProtoFieldDescriptor desc) {
        var deserializer = ProtobufDeserializer.of((Class<ProtoMessage>) desc.actualType());
        if (desc.isRepeated()) {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    var list = (List<ProtoMessage>) desc.declaredField().get(draft);
                    if (list == null) {
                        list = new ArrayList<>();
                        desc.declaredField().set(draft, list);
                    }
                    list.add(deserializer.deserialize(in.readByteArray()));
                }
            };
        } else {
            return (draft, in, wireType) -> {
                if (wireType == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    desc.declaredField().set(draft, deserializer.deserialize(in.readByteArray()));
                }
            };
        }
    }

    void deserialize(ProtoMessage draft, CodedInputStream in, int wireType) throws
            IOException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, InstantiationException;
}
