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

| Java      | Inferred  | Supported                                          |
|-----------|-----------|----------------------------------------------------|
| `boolean` | `bool`    | `bool`                                             |
| `int`     | `int32`   | `int32`, `uint32`, `sint32`, `fixed32`, `sfixed32` |
| `long`    | `int64`   | `int64`, `uint64`, `sint64`, `fixed64`, `sfixed64` |
| `float`   | `float`   | `float`                                            |
| `double`  | `double`  | `double`                                           |
| `String`  | `string`  | `string`                                           |
| `byte[]`  | `bytes`   | `bytes`                                            |

### Explicitly & Implicitly Optional Fields

"Explicitly optional" means that if not present in serialized data when deserializing, the field will be set to `null` in the output object. In contrast, "implicitly optional" means that the field must be non-nullish in the deserialized data. Chances are that the field is not present in the **serialized data**, and the field will be set to the default value of the field type.

Here is a simple table that explains the behavior of fields with different types:

| Field Type | Implicitly Optional | Explicitly Optional |
|------------|---------------------|---------------------|
| `boolean`  | `false`             | Not supported       |
| `int`      | `0`                 | Not supported       |
| `long`     | `0`                 | Not supported       |
| `float`    | `0.0f`              | Not supported       |
| `double`   | `0.0`               | Not supported       |
| `String`   | `""`                | `null`              |
| `byte[]`   | `new byte[0]`       | `null`              |

For a nested implicitly optional field, the default value of the field type will be used. For a nested explicitly optional field, the field will be set to `null`. 

<details>
<summary>Why "implicitly optional" instead of "required"?</summary>

"Implicitly optional" is not equal to "required". A "required" field must be present in the **serialized data**. If not, the deserialization process will **throw an exception**. This feature is so annoying that it is deprecated in proto3. In proto3, all the fields are implicitly optional by default. proto-anno does not support "required" fields.
</details>

`org.jetbrains:annotations` provides a set of annotations to indicate nullability. By default, all fields are implicitly optional. You can annotate a non-primitive field with `@Nullable` to make it explicitly optional, so that the field will be set to `null` instead of a non-nullish value if it is not present in the serialized data.

`@NotNull`-annotated field will not be recognized as a required field. Still, you can use it to indicate that the field should not be `null` under any circumstances, and your IDE will give you a warning if you do not initialize a default value for the field, or if you try to assign `null` to the field.

### Repeated Fields

[fastutil](https://fastutil.di.unimi.it/) provides a set of fast and compact implementations of type-specific maps and sets. proto-anno uses `IntList`, `LongList`, `FloatList`, `DoubleList` and `BooleanList` in fastutil to store repeated primitive values, and `java.util.List` to store repeated string, byte array and message values. You cannot use `List` to store repeated primitive values, vice versa. 

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

As is mentioned in the [official documentation](https://protobuf.dev/programming-guides/encoding/#packed), it is recommended to use packed encoding for repeated primitive fields, and proto3 uses packed encoding by default. proto-anno also uses packed encoding by default. You can disable packed encoding by annotating the field with `@DisablePacking`.

This annotation only applies to the **encoding** process of **repeated primitive fields**. When deserializing, proto-anno will automatically detect whether the field is packed or not. Also, when it is used on repeated non-primitive fields, it will be ignored.

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
- Optional fields for primitive types
- [Enumerations](https://protobuf.dev/programming-guides/proto3/#enum)
- [Extensions](https://protobuf.dev/programming-guides/proto2/#extensions)
- [Any](https://protobuf.dev/programming-guides/proto3/#any)
- [Oneof](https://protobuf.dev/programming-guides/proto3/#oneof)

[Maps](https://protobuf.dev/programming-guides/proto3/#maps) are not supported, but you can refer to the [Backward Compatibility](https://protobuf.dev/programming-guides/proto3/#backwards) section in the official documentation to see how to represent maps using repeated fields.