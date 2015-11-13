package com.dhtd.restful;

import javax.jws.WebService;
import javax.ws.rs.*;

@WebService
@Path("/ruby")
@Produces({"application/json"})
@Consumes({"application/json"})
public interface RubyResource {

    @GET
    @Path("/sayHi/{name}")
    String sayHi(@PathParam("name") String name);

}


