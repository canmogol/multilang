package com.dhtd.restful;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.env.*;
import org.jruby.embed.ScriptingContainer;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BaseApplication extends Application {

    private static final Logger logger = Logger.getLogger(BaseApplication.class.getName());

    private Set<Object> singletons = new HashSet<>();
    private static QuercusEngine quercusEngine = null;
    private static PythonInterpreter interpreter = null;
    private static ScriptingContainer scriptingContainer = null;
    private Set<Class<?>> classList = new HashSet<>();

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classList;
    }

    public void addResource(Class<?> clazz) {
        classList.add(clazz);
    }

    public void addPhpResource(Class<?> clazz) {
        addPhpResource(clazz, clazz.getSimpleName() + ".php");
    }

    private void addPhpResource(final Class<?> clazz, String fileName) {
        // read php file content
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        final String phpResourceContent = scanner.next();

        // find all declared methods in class/interface
        List<String> list = Arrays.asList(clazz.getDeclaredMethods())
            .stream()
            .map(Method::getName)
            .collect(Collectors.toList());

        // create a hashcode for this instance
        final int hashCode = new Random().nextInt();

        // create a php object from read class
        String phpScript = phpResourceContent
            + "\n" +
            "return new " + clazz.getSimpleName() + "();";

        try {
            // execute class with instantiation script
            QuercusEngine quercusEngine = getPhpInterpreter();
            Value phpResourceValue = quercusEngine.execute(phpScript);
            final Env currentEnv = Env.getCurrent();
            ObjectExtValue objectExtValue = (ObjectExtValue) phpResourceValue.toJavaObject();

            // create a proxy object to handle method invocations
            Object instance = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{PhpResource.class}, (proxy, method, args) -> {
                // if this is a
                if (list.contains(method.getName())) {
                    // get method name
                    StringValue stringValueMethod = new StringBuilderValue(method.getName());
                    // get method arguments
                    Value[] values = Arrays.asList(args)
                        .stream()
                        .map(StringValue::create)
                        .collect(Collectors.toList())
                        .toArray(new Value[args.length]);
                    // call method
                    Value responseValue = objectExtValue.callMethod(currentEnv, stringValueMethod, values);
                    // return value as java object
                    return responseValue.toJavaObject();
                } else if ("hashCode".equals(method.getName())) {
                    return hashCode;
                } else {
                    // delegate method call
                    return method.invoke(proxy, args);
                }
            });
            getSingletons().add(instance);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private QuercusEngine getPhpInterpreter() {
        if (quercusEngine == null) {
            quercusEngine = new QuercusEngine();
        }
        return quercusEngine;
    }

    public void addPythonResource(Class<?> clazz) {
        addPythonResource(clazz, clazz.getSimpleName());
    }

    public void addPythonResource(Class<?> clazz, String fileName) {
        getPythonInterpreter().exec("from " + fileName + " import " + fileName);
        PyObject foundPyObject = getPythonInterpreter().get(fileName);
        PyObject pyObject = foundPyObject.__call__();
        getSingletons().add(pyObject.__tojava__(clazz));
    }

    public void addRubyResource(Class<?> clazz) {
        addRubyResource(clazz, clazz.getSimpleName() + ".rb");
    }

    public void addRubyResource(Class<?> clazz, String resource) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String content = scanner.next();
        getSingletons().add(getScriptingContainer().runScriptlet(content));
    }

    private PythonInterpreter getPythonInterpreter() {
        if (interpreter == null) {
            Properties props = new Properties();
            props.put("python.home", "path to the Lib folder");
            props.put("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
            props.put("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
            props.put("python.import.site", "false");

            Properties properties = System.getProperties();

            PythonInterpreter.initialize(properties, props, new String[0]);

            interpreter = new PythonInterpreter();
        }
        return interpreter;
    }

    private ScriptingContainer getScriptingContainer() {
        if (scriptingContainer == null) {
            scriptingContainer = new ScriptingContainer();
        }
        return scriptingContainer;
    }

}
