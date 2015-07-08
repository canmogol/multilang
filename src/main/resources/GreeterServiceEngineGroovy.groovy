import com.dhtd.greeter.GreeterServiceEngine

class GreeterServiceEngineGroovy implements GreeterServiceEngine {
    String sayHi(String name) {
        "Hi Groovy " + name
    }
}