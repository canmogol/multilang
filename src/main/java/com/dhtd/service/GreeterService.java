package com.dhtd.service;


import javax.ejb.Local;

@Local
public interface GreeterService {

    String salute(String name);

}
