package com.dhtd.greeter;

import javax.ejb.Local;

@Local
public interface GreeterServiceEngine {
    String sayHi(String name);
}
