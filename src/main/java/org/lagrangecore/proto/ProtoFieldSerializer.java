package org.lagrangecore.proto;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("unchecked")
@FunctionalInterface
public interface ProtoFieldSerializer {
    static ProtoFieldSerializer create(ProtoFieldDescriptor desc) {
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
            case BOOL -> forBoolean(desc);
            case STRING -> forString(desc);
            case BYTES -> forBytes(desc);
            case MESSAGE -> forMessage(desc);
            default -> throw new IllegalArgumentException("Unexpected input");
        };
    }

    static ProtoFieldSerializer forInt32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeInt32NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeInt32(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getInt(msg);
                out.writeInt32(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forInt64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeInt64NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeInt64(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getLong(msg);
                out.writeInt64(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forUInt32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeUInt32NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeUInt32(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getInt(msg);
                out.writeUInt32(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forUInt64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeUInt64NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeUInt64(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getLong(msg);
                out.writeUInt64(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forSInt32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeSInt32NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeSInt32(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getInt(msg);
                out.writeSInt32(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forSInt64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeSInt64NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeSInt64(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getLong(msg);
                out.writeSInt64(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forFixed32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeFixed32NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeFixed32(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getInt(msg);
                out.writeFixed32(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forFixed64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeFixed64NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeFixed64(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getLong(msg);
                out.writeFixed64(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forSFixed32(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeSFixed32NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (IntList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeSFixed32(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getInt(msg);
                out.writeSFixed32(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forSFixed64(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeSFixed64NoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (LongList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeSFixed64(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getLong(msg);
                out.writeSFixed64(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forFloat(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (FloatList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeFloatNoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (FloatList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeFloat(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getFloat(msg);
                out.writeFloat(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forDouble(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (DoubleList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeDoubleNoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (DoubleList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeDouble(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getDouble(msg);
                out.writeDouble(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forBoolean(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            if (desc.isPacked()) {
                return (msg, out) -> {
                    var list = (BooleanList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(msg.lengthDelimitedFieldSizes.get(desc.fieldNumber()));
                    for (var value : list) {
                        out.writeBoolNoTag(value);
                    }
                };
            } else {
                return (msg, out) -> {
                    var list = (BooleanList) desc.declaredField().get(msg);
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    for (var value : list) {
                        out.writeBool(desc.fieldNumber(), value);
                    }
                };
            }
        } else {
            return (msg, out) -> {
                var value = desc.declaredField().getBoolean(msg);
                out.writeBool(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forString(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (msg, out) -> {
                var list = (List<String>) desc.declaredField().get(msg);
                if (list == null || list.isEmpty()) {
                    return;
                }
                for (var value : list) {
                    out.writeString(desc.fieldNumber(), value);
                }
            };
        } else {
            return (msg, out) -> {
                var value = (String) desc.declaredField().get(msg);
                if (value == null || value.isEmpty()) {
                    return;
                }
                out.writeString(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forBytes(ProtoFieldDescriptor desc) {
        if (desc.isRepeated()) {
            return (msg, out) -> {
                var list = (List<byte[]>) desc.declaredField().get(msg);
                if (list == null || list.isEmpty()) {
                    return;
                }
                for (var value : list) {
                    out.writeByteArray(desc.fieldNumber(), value);
                }
            };
        } else {
            return (msg, out) -> {
                var value = (byte[]) desc.declaredField().get(msg);
                if (value == null || value.length == 0) {
                    return;
                }
                out.writeByteArray(desc.fieldNumber(), value);
            };
        }
    }

    static ProtoFieldSerializer forMessage(ProtoFieldDescriptor desc) {
        var serializer = ProtobufSerializer.of((Class<ProtoMessage>) desc.actualType());
        if (desc.isRepeated()) {
            return (msg, out) -> {
                var list = (List<ProtoMessage>) desc.declaredField().get(msg);
                if (list == null || list.isEmpty()) {
                    return;
                }
                for (var value : list) {
                    out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    out.writeUInt32NoTag(value.serializedSize);
                    serializer.serialize(value, out);
                }
            };
        } else {
            return (msg, out) -> {
                var value = (ProtoMessage) desc.declaredField().get(msg);
                if (value == null) {
                    return;
                }
                out.writeTag(desc.fieldNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                out.writeUInt32NoTag(value.serializedSize);
                serializer.serialize(value, out);
            };
        }
    }

    void serialize(ProtoMessage msg, CodedOutputStream out) throws IOException, IllegalAccessException;
}
