# proto-anno

Annotation-based protobuf solution for Java.

## Overview

In C#, the protobuf-net library allows for annotating classes with attributes to define how they should be serialized. This library aims to provide a similar experience for Java developers.

## Usage

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
Person person = new Person() {{
    name = "Alice";
    id = 123;
    email = "someone@example.com";
}};
```
<details>
<summary>Why double curly brackets to instantiate an object here?</summary>

The outer pair of curly brackets creates an anonymous subclass of `Person`. The inner pair of curly brackets is an instance initializer block that sets the fields of the subclass. This is an idiom in Java to create an object and set its fields in one go.
</details>

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
