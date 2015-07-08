package com.dhtd.greeter;

public class GreeterServiceEngineJava implements GreeterServiceEngine {
    @Override
    public String sayHi(String name) {
        return "Hi Java " + name;
    }
}
