# J on the Beach - Cloud native IPC for Microservices

This workshop focuses on modern and efficient Inter Process Communication (IPC) for microservices. We start with a REST API, built using JAX-RS and Quarkus to briefly discuss the pros and cons of this approach. Then, we will extend the API with an efficient Protobuf payload representation in order to finally transform the API into a fully fledged high-performance gRPC interface definition. But that’s not all! To put some extra icing on the cake, this workshop will demonstrate how to consume the gRPC service from a JavaScript web client and also how to completely generate a matching REST API from an enhanced gRPC interface definition to ensure full interoperability in a microservice architecture.

## Workshop Outline
- Brief introduction, workshop agenda and tech check
- Discussion of REST and gRPC (and other IPC mechanisms) with pros and cons
- Define and Implement Protobuf Serialization with Quarkus and JAX-RS
- Define and Implement gRPC API with Quarkus
- Generate and build a matching REST API Gateway (in Go) for the gRPC service
- Generate and build a gRPC Web client (in TypeScript) for the gRPC service
- Wrap-up and Outlook

## What the attendees will learn

- Pros and Cons of resource-oriented APIs as well as RPC APIs
- Key attributes and strengths of Protocol Buffers and gRPC
- How to define and use Protocol Buffers as efficient serialization format with Quarkus and JAX-RS
- How to define and use gRPC services with Quarkus on client as well as server side
- Useful architectural patterns for gRPC <-> REST Integration
- Useful frameworks for seamless interoperability: gRPC Gateway and gRPC Web

## Requirements
- Solid Java programming skills, experience in languages like Go or TypeScript are not required but useful
- Working Java Development environment with IDE (IntelliJ, VS.code) and JDK 11
- Local Docker & Kubernetes Installation, e.g. Docker Desktop, Minikube, Rancher Desktop, or similar
- Additional small CLI tools (Kubectl, Skaffold or Tilt, Buf) but can be installed during workshop

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
