package com.dhtd.restful;

import org.jruby.embed.ScriptingContainer;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.ws.rs.core.Application;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class BaseApplication extends Application {

    private Set<Object> singletons = new HashSet<>();
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
            props.put("python.home","path to the Lib folder");
            props.put("python.console.encoding", "UTF-8"); // Used to prevent: console: Failed to install '': java.nio.charset.UnsupportedCharsetException: cp0.
            props.put("python.security.respectJavaAccessibility", "false"); //don't respect java accessibility, so that we can access protected members on subclasses
            props.put("python.import.site","false");

            Properties preprops = System.getProperties();

            PythonInterpreter.initialize(preprops, props, new String[0]);
            PythonInterpreter interp = new PythonInterpreter();

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
