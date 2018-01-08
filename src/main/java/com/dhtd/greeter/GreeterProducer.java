package com.dhtd.greeter;

import com.caucho.quercus.QuercusEngine;
import com.caucho.quercus.env.*;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.jruby.embed.ScriptingContainer;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class GreeterProducer {

    @EJB(beanName = "GreeterServiceEngineJava")
    private GreeterServiceEngine greeterServiceEngineJava;

    @EJB(beanName = "GreeterServiceEngineGroovy")
    private GreeterServiceEngine greeterServiceEngineGroovy;

    @EJB(beanName = "GreeterServiceEngineScala")
    private GreeterServiceEngine greeterServiceEngineScala;

    @Produces
    @Greetings(GreetingType.JAVASCRIPT_INTERPRETED)
    public GreeterServiceEngine getGreeterServiceEngineJavascriptInterpreted() throws ScriptException, NoSuchMethodException {
        long time = System.currentTimeMillis();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GreeterServiceEngineJS.js");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String content = scanner.next();

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(content);
        Invocable invocable = (Invocable) engine;
        Object result = invocable.invokeFunction("instance");
        GreeterServiceEngine greeterServiceEngine = (GreeterServiceEngine) result;

        System.out.println("JAVASCRIPT_INTERPRETED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.PYTHON_INTERPRETED)
    public GreeterServiceEngine getGreeterServiceEnginePythonInterpreted() {
        long time = System.currentTimeMillis();

        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from GreeterServiceEnginePy import GreeterServiceEnginePy");
        PyObject oidGeneratorPy = interpreter.get("GreeterServiceEnginePy");
        PyObject oidGeneratorPyObject = oidGeneratorPy.__call__();
        GreeterServiceEngine greeterServiceEngine = (GreeterServiceEngine) oidGeneratorPyObject.__tojava__(GreeterServiceEngine.class);

        System.out.println("PYTHON_INTERPRETED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.PHP_INTERPRETED)
    public GreeterServiceEngine getGreeterServiceEnginePhpInterpreted() {
        long time = System.currentTimeMillis();

        // create a hashcode for this instance
        final int hashCode = new Random().nextInt();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GreeterServiceEnginePhp.php");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String content = scanner.next();
        String phpScript = content
            + "\n" +
            "return new GreeterServiceEnginePhp();";
        try {
            QuercusEngine quercusEngine = new QuercusEngine();
            Value phpResourceValue = quercusEngine.execute(phpScript);
            final Env currentEnv = Env.getCurrent();
            ObjectExtValue objectExtValue = (ObjectExtValue) phpResourceValue.toJavaObject();
            // create a proxy object to handle method invocations
            GreeterServiceEngine greeterServiceEngine = (GreeterServiceEngine) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{GreeterServiceEngine.class}, (proxy, method, args) -> {
                // if this is a
                if ("sayHi".equals(method.getName())) {
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

            System.out.println("PHP_INTERPRETED time consumed: " + (System.currentTimeMillis() - time));
            return greeterServiceEngine;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Produces
    @Greetings(GreetingType.RUBY_INTERPRETED)
    public GreeterServiceEngine getGreeterServiceEngineRubyInterpreted() {
        long time = System.currentTimeMillis();

        ScriptingContainer container = new ScriptingContainer();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("greeter_service_engine_ruby.rb");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String content = scanner.next();
        Object greeter = container.runScriptlet(content);
        GreeterServiceEngine greeterServiceEngine = (GreeterServiceEngine) greeter;

        System.out.println("RUBY_INTERPRETED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.GROOVY_INTERPRETED)
    public GreeterServiceEngine getGreeterServiceEngineGroovyInterpreted() {
        long time = System.currentTimeMillis();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GreeterServiceEngineGroovy.groovy");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String content = scanner.next();
        GroovyClassLoader classLoader = new GroovyClassLoader();
        Class groovy = classLoader.parseClass(content);
        GroovyObject groovyObj = null;
        try {
            groovyObj = (GroovyObject) groovy.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        GreeterServiceEngine greeterServiceEngine = (GreeterServiceEngine) groovyObj;

        System.out.println("GROOVY_INTERPRETED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.GROOVY_COMPILED)
    public GreeterServiceEngine getGreeterServiceEngineGroovyCompiled() {
        long time = System.currentTimeMillis();

        GreeterServiceEngine greeterServiceEngine = null;
        try {
            greeterServiceEngine = (GreeterServiceEngine) Class.forName("com.dhtd.greeter.GreeterServiceEngineGroovy").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("GROOVY_COMPILED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.SCALA_COMPILED)
    public GreeterServiceEngine getGreeterServiceEngineScalaCompiled() {
        long time = System.currentTimeMillis();

        GreeterServiceEngine greeterServiceEngine = null;
        try {
            greeterServiceEngine = (GreeterServiceEngine) Class.forName("com.dhtd.greeter.GreeterServiceEngineScala").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("SCALA_COMPILED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.JAVA_COMPILED)
    public GreeterServiceEngine getGreeterServiceEngineJavaCompiled() {
        long time = System.currentTimeMillis();

        GreeterServiceEngine greeterServiceEngine = null;
        try {
            greeterServiceEngine = (GreeterServiceEngine) Class.forName("com.dhtd.greeter.GreeterServiceEngineJava").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("JAVA_COMPILED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.JAVA_INJECTED)
    public GreeterServiceEngine getGreeterServiceEngineJavaInjected() {
        long time = System.currentTimeMillis();
        GreeterServiceEngine greeterServiceEngine = greeterServiceEngineJava;
        System.out.println("JAVA_INJECTED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.GROOVY_INJECTED)
    public GreeterServiceEngine getGreeterServiceEngineGroovyInjected() {
        long time = System.currentTimeMillis();
        GreeterServiceEngine greeterServiceEngine = greeterServiceEngineGroovy;
        System.out.println("GROOVY_INJECTED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

    @Produces
    @Greetings(GreetingType.SCALA_INJECTED)
    public GreeterServiceEngine getGreeterServiceEngineScalaInjected() {
        long time = System.currentTimeMillis();
        GreeterServiceEngine greeterServiceEngine = greeterServiceEngineScala;
        System.out.println("SCALA_INJECTED time consumed: " + (System.currentTimeMillis() - time));
        return greeterServiceEngine;
    }

}
