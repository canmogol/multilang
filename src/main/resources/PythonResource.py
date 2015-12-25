from com.dhtd.restful import PythonResource


class PythonResource(PythonResource):
    def sayHi(self, name):
        print(" >>> PythonResource.py name: %s" % name)
        return "Hello Python %s " % name
