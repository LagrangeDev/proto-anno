# proto-anno

Annotation-based [Protocol Buffers](https://protobuf.dev/) solution for Java.

## Overview

In C#, the protobuf-net library allows for annotating classes with attributes to define how they should be serialized. This library aims to provide a similar experience for Java developers.

## Usage

### Defining a Message

First define a class that extends `ProtoMessage`. Then annotate fields with `@ProtoField`.
```java
public class Person extends ProtoMessage {
    @ProtoField(1)
    public String name;
    
    @ProtoField(2)
    public int id;
    
    @ProtoField(3)
    public String email;
}
```
Note that all the annotated fields should be public because the library uses reflection to access them.

You can instantiate a `Person` object like this:
```java
void doSomething() {
    Person person = new Person();
    person.name = "Alice";
    person.id = 123;
    person.email = "someone@example.com";
}
```

To serialize the object to a byte array:
```java
var serializer = ProtobufSerializer.of(Person.class);
byte[] bytes = serializer.serialize(person);
```

Also, to deserialize a byte array to an object:
```java
var deserializer = ProtobufDeserializer.of(Person.class);
Person deserializedPerson = deserializer.deserialize(bytes);
```

### Type Mapping

All the [scalar value types](https://protobuf.dev/programming-guides/proto3/#scalar) mentioned in the official protobuf documentation are supported. The types can be either inferred from the field type or explicitly specified by annotating the field with `@TypeMappedTo`. `int` and `long` can be mapped to multiple protobuf types.

| Java      | Inferred Type | Supported                                          |
|-----------|---------------|----------------------------------------------------|
| `boolean` | `bool`        | `bool`                                             |
| `int`     | `int32`       | `int32`, `uint32`, `sint32`, `fixed32`, `sfixed32` |
| `long`    | `int64`       | `int64`, `uint64`, `sint64`, `fixed64`, `sfixed64` |
| `float`   | `float`       | `float`                                            |
| `double`  | `double`      | `double`                                           |
| `String`  | `string`      | `string`                                           |
| `byte[]`  | `bytes`       | `bytes`                                            |

### Repeated Fields

[fastutil](https://fastutil.di.unimi.it/) provides a set of fast and compact implementations of type-specific maps and sets. proto-anno uses `IntList`, `LongList`, `FloatList`, `DoubleList` and `BooleanList` in fastutil to store repeated `int`, `long`, `float`, `double` and `boolean` values, and `java.util.List` to store repeated string, byte array and message values. You cannot use `List` to store repeated scalar values, vice versa. 

The annotation `@TypeMappedTo` also applies to repeated fields. For example:
```java
public class Person extends ProtoMessage {
    @ProtoField(1)
    public List<String> names;
    
    @ProtoField(2)
    @TypeMappedTo(FieldType.UINT32)
    public IntList ids;             // manually specified as uint32
    
    @ProtoField(3)
    public DoubleList scores;       // automatically inferred as double
    
    @ProtoField(4)
    public List<Person> friends;
}
```

### Packed Encoding

As is mentioned in the [official documentation](https://protobuf.dev/programming-guides/encoding/#packed), it is recommended to use packed encoding for repeated scalar fields, and proto3 uses packed encoding by default. proto-anno also uses packed encoding by default. You can disable packed encoding by annotating the field with `@DisablePacking`.

This annotation only applies to the **encoding** process of **repeated scalar fields**. When deserializing, proto-anno will automatically detect whether the field is packed or not. Also, when it is used on repeated string, byte array or message fields, it will be ignored.

### Unsigned Integers

Java does not have unsigned integer types, and you can get around this by API provided in `java.lang.Integer` and `java.lang.Long`. For example:
```java
public class UnsignedTest extends ProtoMessage {
    @ProtoField(1)
    @TypeMappedTo(FieldType.UINT32)
    public int bigNumber;
    
    @ProtoField(2)
    @TypeMappedTo(FieldType.UINT64)
    public long evenBiggerNumber;
}

UnsignedTest test = ProtobufDeserializer.of(UnsignedTest.class).deserialize(bytes);
long bigNumber = Integer.toUnsignedLong(test.bigNumber);
String evenBiggerNumber = Long.toUnsignedString(test.evenBiggerNumber);
```

## Limitations

The following features are not supported:
- `GROUP` wire type (deprecated)
- Required fields (deprecated in proto3)
- [Enumerations](https://protobuf.dev/programming-guides/proto3/#enum)
- [Extensions](https://protobuf.dev/programming-guides/proto2/#extensions)
- [Any](https://protobuf.dev/programming-guides/proto3/#any)
- [Oneof](https://protobuf.dev/programming-guides/proto3/#oneof)

[Maps](https://protobuf.dev/programming-guides/proto3/#maps) are not supported, but you can refer to the [Backward Compatibility](https://protobuf.dev/programming-guides/proto3/#backwards) section in the official documentation to see how to represent maps using repeated fields.