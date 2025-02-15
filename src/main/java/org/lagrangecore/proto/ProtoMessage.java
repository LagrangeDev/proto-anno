package org.lagrangecore.proto;

import it.unimi.dsi.fastutil.ints.Int2IntMap;

/**
 * A message that can be serialized and deserialized using the protobuf format.
 */
public abstract class ProtoMessage {
    int serializedSize = 0;

    Int2IntMap lengthDelimitedFieldSizes = null;
}
