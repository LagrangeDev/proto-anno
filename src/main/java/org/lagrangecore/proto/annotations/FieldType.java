package org.lagrangecore.proto.annotations;

import com.google.protobuf.WireFormat;

public enum FieldType {
    INT32(WireFormat.FieldType.INT32),
    INT64(WireFormat.FieldType.INT64),
    UINT32(WireFormat.FieldType.UINT32),
    UINT64(WireFormat.FieldType.UINT64),
    SINT32(WireFormat.FieldType.SINT32),
    SINT64(WireFormat.FieldType.SINT64),
    FIXED32(WireFormat.FieldType.FIXED32),
    FIXED64(WireFormat.FieldType.FIXED64),
    SFIXED32(WireFormat.FieldType.SFIXED32),
    SFIXED64(WireFormat.FieldType.SFIXED64),
    FLOAT(WireFormat.FieldType.FLOAT),
    DOUBLE(WireFormat.FieldType.DOUBLE),
    BOOL(WireFormat.FieldType.BOOL),
    STRING(WireFormat.FieldType.STRING),
    BYTES(WireFormat.FieldType.BYTES),
    MESSAGE(WireFormat.FieldType.MESSAGE),
    ;

    private final WireFormat.FieldType pbFieldType;

    FieldType(WireFormat.FieldType pbFieldType) {
        this.pbFieldType = pbFieldType;
    }

    public WireFormat.FieldType toPbFieldType() {
        return pbFieldType;
    }
}
