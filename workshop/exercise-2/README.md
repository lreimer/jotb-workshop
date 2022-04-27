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