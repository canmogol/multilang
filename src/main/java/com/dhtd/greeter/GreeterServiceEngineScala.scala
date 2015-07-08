package com.dhtd.greeter

class GreeterServiceEngineScala extends GreeterServiceEngine {
  override def sayHi(name: String): String = "Hi Scala " + name
}
