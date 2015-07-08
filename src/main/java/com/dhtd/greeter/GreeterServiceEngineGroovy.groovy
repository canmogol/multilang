package com.dhtd.greeter

class GreeterServiceEngineGroovy implements GreeterServiceEngine {
    String sayHi(String name) {
        "Hi Groovy " + name
    }
}