# gRPC API and client with Quarkus

## gRPC in a Nutshell

gRPC is a modern, high performance, open source and  universal RPC framework.
- Uses HTTP/2 as modern Web-friendly transport protocol (Multiplexing, TLS, compression, …)
- Supports several types of communication: classic request-response as well as streaming from Client-side, Server-side, Uni- and Bi-Directional
- Uses Protocol Buffers as efficient binary payload format
- support various load balancing options: proxy, client-side and look-aside balancing
- flexible support for tracing, health checks and authentication
- client and server code can be generated from the IDL easily for several languages
- gRPC Gateway and gRPC  Web projects from the ecosystem enable good interoperability

## Quarkus gRPC Quickstart

Go to the Quarkus webpage at https://code.quarkus.io and select the following packages:
- gRPC
- RESTeasy Classic
- SmallRye Health

Otherwise, open the `quarkus-beer-grpc/` project in your IDE. The `quarkus-beer-grpc/README.md` contains instructions
on how to build and run the service locally.

```bash
# local development
./gradlew quarkusDev

# build and deploy microservice
# make sure to enable exercise-2 definitions in skaffold.yaml
skaffold dev --no-prune=false --cache-artifacts=false

# alternatively, use Tilt
# make sure to enable exercise-2 definitions in Tiltfile
tilt up
```

## Protocol Buffers and gRPC Service Definition

Create a file called `beer.proto` in the directory `src/main/proto` with the following content.
Once done, run the Gradle build so that the relevant stubs and POJOs are generated.

```proto
syntax = "proto3";

option java_package = "hands.on.grpc";
option java_outer_classname = "BeerProtos";
option optimize_for = SPEED;

import "google/protobuf/empty.proto";
package beer;

service BeerService {
  // Get the list of all beers
  rpc AllBeers (google.protobuf.Empty) returns (GetBeersResponse) {}
  // Get a single beer by Asin
  rpc GetBeer (GetBeerRequest) returns (GetBeerResponse) {}
  // Create a new beer
  rpc CreateBeer (CreateBeerRequest) returns (google.protobuf.Empty) {}
  // Update an existing beer
  rpc UpdateBeer (UpdateBeerRequest) returns (google.protobuf.Empty) {}
  // Delete an existing beeer
  rpc DeleteBeer (DeleteBeerRequest) returns (google.protobuf.Empty) {}
}

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
  string asin = 1;
  Beer beer = 2;
}

message DeleteBeerRequest {
  string asin = 1;
}
```

## gRPC Service and Test Client Implementation

In the application package, create a new Java file for the class `BeerGrpcService`. Override and implement
all methods from the generated service stub base class. Alternatively, use the `BeerGrpcService.java` file
from the exercise directory.

```java
@GrpcService
public class BeerGrpcService extends BeerServiceGrpc.BeerServiceImplBase {

    private Map<String, Beer> beers = new HashMap<>();

    @PostConstruct
    void initialize() {
        beers.put("B01AU6LWNC", Beer.newBuilder().setAsin("B01AU6LWNC").setName("Edelstoff Exportbier").setBrand("Augustiner Brauerei München").setCountry("Germany").setAlcohol(5.6f).setType(Beer.BeerType.Lager).build());
    }

    @Override
    public void allBeers(Empty request, StreamObserver<GetBeersResponse> responseObserver) {
        Log.info("AllBears gRPC API called");
        
        // TODO implement me

        responseObserver.onCompleted();
    }

    @Override
    public void getBeer(GetBeerRequest request, StreamObserver<GetBeerResponse> responseObserver) {
        Log.info("GetBears gRPC API called");

        // TODO implement me

        responseObserver.onCompleted();
    }

    @Override
    public void createBeer(CreateBeerRequest request, StreamObserver<Empty> responseObserver) {
        Log.info("CreateBear gRPC API called");
        
        // TODO implement me

        responseObserver.onCompleted();
    }

    @Override
    public void updateBeer(UpdateBeerRequest request, StreamObserver<Empty> responseObserver) {
        Log.info("UpdateBeer gRPC API called");
        
        // TODO implement me

        responseObserver.onCompleted();
    }

    @Override
    public void deleteBeer(DeleteBeerRequest request, StreamObserver<Empty> responseObserver) {
        Log.info("DeleteBeer gRPC API called");
        
        // TODO implement me

        responseObserver.onCompleted();
    }
}
```

To test and call the implementation we can write a Quarkus JUnit5 test. Create a new Java file for the test class `BeerGrpcServiceTest` under `src/test/java`. Alternatively, use the `BeerGrpcServiceTest.java` file
from the exercise directory.

```java
@QuarkusTest
public class BeerGrpcServiceTest {

    @GrpcClient("beer")
    BeerServiceGrpc.BeerServiceBlockingStub client;

    @Test
    public void testAllBeers() {
        BeerProtos.GetBeersResponse allBeers = client.allBeers(Empty.newBuilder().build());
        assertEquals(3, allBeers.getBeersCount());
    }

    // TODO implement more tests
}
```

## Bonus: gRPC Web UI

After you have deployed the microservice to Kubernetes using either Tilt or Skaffold, open a browser
with the following location: http://localhost:16969

Connect to the address `grpc-beer-service:9090` and upload the Protobuf files from the `src/main/proto` directory.
Then you can select and call the individual gRPC services.

## Bonus: gRPC Nginx LoadBalancer

To implement a load balancer for gRPC services, we can use an Nginx server and configure it accordingly, so that it
listens for HTTP/2 requests on a given port and forwards the calls to our gRPC beer service backend.

```
upstream grpc_server {
  server grpc-beer-service:9090;
}

server {
  listen 8888 http2;

  location /beer.BeerService/ {
    grpc_pass grpc://grpc_server;
  }
}
```

