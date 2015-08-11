package com.dhtd.service;


import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(GreeterService.class)
public class GreeterServiceImpl implements GreeterService {

    @Override
    public String salute(String name) {
        return "Injected Hi " + name;
    }

}
