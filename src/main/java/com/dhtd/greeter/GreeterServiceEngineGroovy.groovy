package com.dhtd.greeter

import com.dhtd.service.GreeterService

import javax.ejb.EJB
import javax.ejb.Local
import javax.ejb.Stateless

@Stateless(name = "GreeterServiceEngineGroovy", mappedName = "GreeterServiceEngineGroovy")
@Local(GreeterServiceEngine.class)
class GreeterServiceEngineGroovy implements GreeterServiceEngine {

    @EJB
    private GreeterService greeterService;

    String sayHi(String name) {
        if (greeterService != null) {
            return "Groovy " + greeterService.salute(name);
        } else {
            return "Groovy Hi " + name;
        }
    }

}