package com.dhtd.restful;

import javax.jws.WebService;
import javax.ws.rs.*;

@WebService
@Path("/php")
@Produces({"application/json"})
@Consumes({"application/json"})
public interface PhpResource {

    @GET
    @Path("/sayHi/{name}")
    String sayHi(@PathParam("name") String name);

}
