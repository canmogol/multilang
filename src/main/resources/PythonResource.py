from com.dhtd.restful import PythonResource


class PythonResource(PythonResource):
    def sayHi(self, name):
        return "Hello Python %s " % name
