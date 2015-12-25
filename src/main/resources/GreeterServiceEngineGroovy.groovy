import com.dhtd.greeter.GreeterServiceEngine

class GreeterServiceEngineGroovy implements GreeterServiceEngine {
    String sayHi(String name) {
        println(" >>> " + getClass().getName() + " name: " + name)
        "Hi Groovy " + name
    }
}