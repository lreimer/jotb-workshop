package hands.on.grpc;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.logging.Log;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {

    @GrpcClient
    HelloGrpc stub;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Log.info("Hello REST API called");
        return stub.sayHello(HelloRequest.newBuilder().setName("J on the Beach 2022").build()).await().indefinitely().getMessage();
    }
}