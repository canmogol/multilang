package com.dhtd.restful;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class APIPoint extends BaseApplication {

    public APIPoint() {
        addResource(GreeterResource.class);
        addResource(ScalaResource.class);
        addResource(GroovyResource.class);
        addRubyResource(RubyResource.class);
        addPythonResource(PythonResource.class);
        addPhpResource(PhpResource.class);
    }

}
