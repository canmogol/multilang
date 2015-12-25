package com.dhtd.greeter

import javax.ejb.{EJB, Local, Stateless}

import com.dhtd.service.GreeterService

@Stateless(name = "GreeterServiceEngineScala", mappedName = "GreeterServiceEngineScala")
@Local(Array(classOf[GreeterServiceEngine]))
class GreeterServiceEngineScala extends GreeterServiceEngine {

  @EJB private val greeterService: GreeterService = null

  override def sayHi(name: String): String = {
    if (greeterService != null) {
      "Scala " + greeterService.salute(name)
    }
    else {
      "Scala Hi " + name
    }
  }
}
