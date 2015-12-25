from com.dhtd.greeter import GreeterServiceEngine


class GreeterServiceEnginePy(GreeterServiceEngine):
    def sayHi(self, name):
        print(" >>> GreeterServiceEnginePy name: %s" % name)
        return "Hello Python %s " % name
