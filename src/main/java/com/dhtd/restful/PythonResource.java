package com.dhtd.restful;

import javax.jws.WebService;
import javax.ws.rs.*;

@WebService
@Path("/python")
@Produces({"application/json"})
@Consumes({"application/json"})
public interface PythonResource {

    @GET
    @Path("/sayHi/{name}")
    String sayHi(@PathParam("name") String name);

}
