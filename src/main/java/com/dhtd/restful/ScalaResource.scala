package com.dhtd.restful

import javax.jws.WebService
import javax.ws.rs._


@WebService
@Path("/scala")
@Produces(Array("application/json"))
@Consumes(Array("application/json"))
class ScalaResource {
  @GET
  @Path("/sayHi/{name}")
  def sayHi(@PathParam("name") name: String): String = "Hi scala " + name
}
