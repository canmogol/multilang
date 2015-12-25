package com.dhtd.greeter;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.jruby.embed.ScriptingContainer;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import java.io.InputStream;
import java.util.Scanner;

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
