package com.dhtd.restful;

import com.dhtd.greeter.GreeterServiceEngine;
import com.dhtd.greeter.GreetingType;
import com.dhtd.greeter.Greetings;

import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/greeter")
@Produces({"application/json"})
@Consumes({"application/json"})
public class GreeterResource {

    @Inject
    @Greetings(GreetingType.PYTHON_INTERPRETED)
    GreeterServiceEngine greeterServiceEnginePython;

    @Inject
    @Greetings(GreetingType.RUBY_INTERPRETED)
    GreeterServiceEngine greeterServiceEngineRuby;

    @Inject
    @Greetings(GreetingType.GROOVY_INTERPRETED)
    GreeterServiceEngine greeterServiceEngineGroovyInterpreted;

    @Inject
    @Greetings(GreetingType.GROOVY_COMPILED)
    GreeterServiceEngine greeterServiceEngineGroovyCompiled;

    @Inject
    @Greetings(GreetingType.SCALA_COMPILED)
    GreeterServiceEngine greeterServiceEngineScalaCompiled;

    @Inject
    @Greetings(GreetingType.JAVA_COMPILED)
    GreeterServiceEngine greeterServiceEngineJavaCompiled;

    @Inject
    @Greetings(GreetingType.JAVA_INJECTED)
    GreeterServiceEngine greeterServiceEngineJavaInjected;

    @Inject
    @Greetings(GreetingType.GROOVY_INJECTED)
    GreeterServiceEngine greeterServiceEngineGroovyInjected;

    @Inject
    @Greetings(GreetingType.SCALA_INJECTED)
    GreeterServiceEngine greeterServiceEngineScalaInjected;


    @GET
    @Path("/sayHi/{name}")
    public String sayHi(@PathParam("name") String name) {
        long time = System.currentTimeMillis();
        StringBuilder hi = new StringBuilder();
        hi.append("\n\n");
        hi.append(greeterServiceEnginePython != null ? greeterServiceEnginePython.sayHi(name) : "");
        hi.append("\nPI ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineRuby != null ? greeterServiceEngineRuby.sayHi(name) : "");
        hi.append("\nRI ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineGroovyInterpreted != null ? greeterServiceEngineGroovyInterpreted.sayHi(name) : "");
        hi.append("\nGI ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineGroovyCompiled != null ? greeterServiceEngineGroovyCompiled.sayHi(name) : "");
        hi.append("\nGC ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineScalaCompiled != null ? greeterServiceEngineScalaCompiled.sayHi(name) : "");
        hi.append("\nSC ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineJavaCompiled != null ? greeterServiceEngineJavaCompiled.sayHi(name) : "");
        hi.append("\nJC ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineJavaInjected != null ? greeterServiceEngineJavaInjected.sayHi(name) : "");
        hi.append("\nJI ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineGroovyInjected != null ? greeterServiceEngineGroovyInjected.sayHi(name) : "");
        hi.append("\nGI ");
        hi.append(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        hi.append("\n\n");
        hi.append(greeterServiceEngineScalaInjected != null ? greeterServiceEngineScalaInjected.sayHi(name) : "");
        hi.append("\nSI ");
        hi.append(System.currentTimeMillis() - time);
        hi.append("\n\n");
        return hi.toString();
    }

}
