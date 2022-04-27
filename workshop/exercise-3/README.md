# gRPC REST Gateway

gRPC-Gateway is a plugin of protoc. It reads a gRPC service definition and generates a reverse-proxy server which translates a RESTful JSON API into gRPC. This server is generated according to custom options in your gRPC definition.

![](https://grpc-ecosystem.github.io/grpc-gateway/assets/images/architecture_introduction_diagram.svg)

## gRPC Gateway Installation

To setup your Go environment, follow the instructions found at https://github.com/grpc-ecosystem/grpc-gateway#installation

## Code Generation using Buf

The process of generating the required Go sources from the Proto files is a bit tedious when done by hand or
calling the protoc compiler on the command line. The tool Buf greatly simplifies the process.

First we need to configure Buf with the correct dependencies and proto source roots. Create a file `buf.yaml` in the
`grpc-beer-gateway/` directory with the following content.
```yaml
version: v1beta1
name: github.com/lreimer/jotb-workshop
deps:
  # add proto definitions for Google APIs as dependency
  - buf.build/beta/googleapis
  # add proto definitions for gRPC gateway as dependency
  - buf.build/grpc-ecosystem/grpc-gateway
build:
  roots:
    - proto
```

Next we need to configure the different protoc plugins used to generate the required Go source files as well as an
OpenAPIv2 definition of the REST interface. Create a file `buf.gen.yaml` in the `grpc-beer-gateway/` directory with the following content. 
```yaml
version: v1beta1
plugins:
  - name: go
    out: proto
    opt: paths=source_relative
  - name: go-grpc
    out: proto
    opt: paths=source_relative
  - name: grpc-gateway
    out: proto
    opt: 
     - paths=source_relative
     - generate_unbound_methods=true
  - name: openapiv2
    out: openapiv2
```

## Enhance gRPC Service with Google HTTP API options

The gRPC service definition needs to be enhanced with service specific options to configure how the gRPC messages
are mapped to and from the HTTP body and path elements. Extend the `proto/beer.proto` with the following content:

```proto
import "google/api/annotations.proto";

service BeerService {
    // Get the list of all beers
    rpc AllBeers (google.protobuf.Empty) returns (GetBeersResponse) {
        option (google.api.http) = {
            // map complete gRPC response to as HTTP response
            get: "/api/beers"
        };      
    }
    // Get a single beer by Asin
    rpc GetBeer (GetBeerRequest) returns (GetBeerResponse) {
        option (google.api.http) = {
            // map asin path param to asin field in request message
            // map beer field from response message to HTTP response body
            get: "/api/beers/{asin}"
            response_body: "beer"
        };
    }
    // Create a new beer
    rpc CreateBeer (CreateBeerRequest) returns (google.protobuf.Empty) {
        option (google.api.http) = {
            // map complete HTTP body to gRPC request message fields
            post: "/api/beers"
            body: "*"
        };
    }
    // Update an existing beer
    rpc UpdateBeer (UpdateBeerRequest) returns (google.protobuf.Empty) {
        option (google.api.http) = {
            // map asin path param to asin field in request message
            // map HTTP request body to beer field in request message
            put: "/api/beers/{asin}"
            body: "beer"
        };
    }
    // Delete an existing beeer
    rpc DeleteBeer (DeleteBeerRequest) returns (google.protobuf.Empty) {
        option (google.api.http) = {
            // map asin path param to asin field in request message
            delete: "/api/beers/{asin}"
        };
    }
}
```

Additionally, we can generated an OpenAPI v2 definition as well. Again, we need to enhance the proto file with
additional options. Extend the `proto/beer.proto` with the following content:
```proto
import "protoc-gen-openapiv2/options/annotations.proto";

option (grpc.gateway.protoc_gen_openapiv2.options.openapiv2_swagger) = {
	info: {
		title: "gRPC Beer Gateway";
		version: "1.0";
		contact: {
			name: "Mario-Leander Reimer";
			url: "https://lreimer.github.io";
			email: "mario-leander.reimer@qaware.de";
		};
		license: {
			name: "MIT";
			url: "https://github.com/lreimer/jotb-workshop/blob/master/LICENSE";
		};		
	};
	external_docs: {
		url: "https://github.com/lreimer/jotb-workshop/grpc-beer-gateway";
		description: "Beer Service gRPC Gateway";
	}
	schemes: HTTP;
	schemes: HTTPS;
	consumes: "application/json";
	produces: "application/json";
};
```

## Build and Deploy gRPC Gateway

```bash
# local development with Go (requires Go 1.17)
make build

# or containerized build
make image

# build and deploy microservice
# make sure to enable exercise-3 definitions in skaffold.yaml
skaffold dev --no-prune=false --cache-artifacts=false

# alternatively, use Tilt
# make sure to enable exercise-3 definitions in Tiltfile
tilt up

# use a HTTP client to call REST endpoints
http get localhost:18090/api/beers
curl -XGET http://localhost:18090/api/beers
```
