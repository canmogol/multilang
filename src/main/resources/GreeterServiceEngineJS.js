var GreeterServiceEngine = Java.type('com.dhtd.greeter.GreeterServiceEngine');
var System = Java.type('java.lang.System');
var GreeterServiceEngineJS = Java.extend(GreeterServiceEngine, {
    sayHi: function (name) {
        //System.out.println(' >>> GreeterServiceEngineJS name: ' + name);
        return 'Hi Javascript ' + name;
    }
});
function instance() {
    return new GreeterServiceEngineJS();
}