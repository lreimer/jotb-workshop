# Protobuf Serialization with Quarkus and JAX-RS

## Protocol Buffers in a Nutshell

Protocol Buffers are a language-neutral, platform-neutral extensible mechanism for serializing structured data.
- https://developers.google.com/protocol-buffers
- Like XML or JSON - just smaller, faster and easier!
- Google Protobuf uses an efficient binary format to serialize data structures.
- An Interface Definition Language (IDL) is used to define data structures and message payloads. Many primitive types, enums, maps, arrays, nested types.
- Protocol Buffers supports code generation for Java, Python, Objective-C, C++, Kotlin, Dart, Go, Ruby und C#
- Protobuf supports evolution as well as extension of schemas. Backwards and forwards compatibility are supported.

## Quarkus REST Quickstart

If you want to start yourself, go to the Quarkus webpage at https://code.quarkus.io and select the following packages:
- RESTeasy Classic
- RESTeasy Classic JSON-B
- SmallRye Health 

Otherwise, open the `quarkus-beer-rest/` project in your IDE. The `quarkus-beer-rest/README.md` contains instructions
on how to build and run the service locally.

## Protocol Buffers Definition and Build Integration

First, we need to extend the Gradle build with the Protobuf Gradle plugin, its configuration and dependency.
Open the `build.gradle` file and extend it with the following content:
```groovy
plugins {
    // add this to the plugins section
    id "com.google.protobuf" version "0.8.18"
}

dependencies {
    // add this to the dependencies
    implementation 'com.google.protobuf:protobuf-java:3.19.4'
}

protobuf {
    // Configure the protoc executable
    protoc {
        // Download from repositories
        artifact = 'com.google.protobuf:protoc:3.19.4'
    }
}

sourceSets.main.java.srcDirs += ['build/generated/source/proto/main/java']
```

Next, create a file called `beer.proto` in the directory `src/main/proto` with the following content:
```proto
syntax = "proto3";

package beer;

option java_package = "hands.on.grpc";
option java_outer_classname = "BeerProtos";
option optimize_for = SPEED;

message Beer {
  string asin = 1;
  string name = 2;
  string brand = 3;
  string country = 4;
  float alcohol = 5;
  enum BeerType {
    IndianPaleAle = 0;
    SessionIpa = 1;
    Lager = 2;
  }
  BeerType type = 6;
}

message GetBeersRequest {
}

message GetBeersResponse {
  repeated Beer beers = 1;
}

message GetBeerRequest {
  string asin = 1;
}

message GetBeerResponse {
  Beer beer = 1;
}

message CreateBeerRequest {
  Beer beer = 1;
}

message UpdateBeerRequest {
  Beer beer = 1;
}

message DeleteBeerRequest {
  string asin = 1;
}
```

# 