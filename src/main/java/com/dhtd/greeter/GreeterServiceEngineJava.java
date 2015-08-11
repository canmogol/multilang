package com.dhtd.greeter;

import com.dhtd.service.GreeterService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless(name = "GreeterServiceEngineJava", mappedName = "GreeterServiceEngineJava")
@Local(GreeterServiceEngine.class)
public class GreeterServiceEngineJava implements GreeterServiceEngine {

    @EJB
    private GreeterService greeterService;

    @Override
    public String sayHi(String name) {
        if(greeterService != null){
            return "Java " + greeterService.salute(name);
        }else{
            return "Java Hi " + name;
        }
    }
}
