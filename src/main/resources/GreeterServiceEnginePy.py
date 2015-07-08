from com.dhtd.greeter import GreeterServiceEngine


class GreeterServiceEnginePy(GreeterServiceEngine):
    def sayHi(self, name):
        return "Hello Python %s " % name
