package com.dhtd.restful

import javax.jws.WebService
import javax.ws.rs.*

@WebService
@Path("/groovy")
@Produces("application/json")
@Consumes("application/json")
class GroovyResource {

    @GET
    @Path("/sayHi/{name}")
    String sayHi(@PathParam("name") String name) {
        return "Groovy Hi " + name;
    }
}
