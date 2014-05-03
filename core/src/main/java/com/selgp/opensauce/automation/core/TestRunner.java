package com.selgp.opensauce.automation.core;

import com.metapossum.utils.scanner.reflect.ClassesInPackageScanner;
import com.selgp.opensauce.automation.core.environments.Environments;
import com.selgp.opensauce.automation.core.suites.Suites;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.reporters.XMLReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Runs TestNG programatically.
 *
 * @author guillem.hernandez@selgp.com
 */
public final class TestRunner {
    private static TestRunner instance = null;
    public static Suites suites;
    public static Environments environments;
    public static String SUITE_NAME = System.getProperty("suite", "unset");
    public static String ENVIRONMENT = System.getProperty("environment", "unset");
    public static String CLASS_NAME;
    public static String PARAMETER;
    public static Properties suitesProps;
    public static Properties environmentProps;

    public static TestRunner getInstance() {
        if (instance == null) {
            instance = new TestRunner();
            init();
        }
        return instance;
    }

    public static void main(String[] args) {
        getInstance();
        run();
        System.exit(0);
    }


    public static void init() {
        suitesProps = new Properties();
        environmentProps = new Properties();
        try {
            suitesProps.load(new InputStreamReader(TestRunner.class.getClassLoader().getResourceAsStream("automation.properties")));
            environmentProps.load(new InputStreamReader(TestRunner.class.getClassLoader().getResourceAsStream("environments.properties")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        suites = new Suites(suitesProps);
        environments = new Environments(environmentProps);
        SUITE_NAME = suites.getSuiteName().toLowerCase();
        ENVIRONMENT = System.getProperty("environment").toLowerCase();
        CLASS_NAME = System.getProperty("class.name", "unset");
        PARAMETER = System.getProperty("test.parameter", "unset");
    }

    public static void run() {
        List<XmlSuite> xmlSuites = new ArrayList<XmlSuite>();
        String suite = suites.getSuiteName();
        xmlSuites.add(createXmlSuite(suite));
        TestNG testng = new TestNG();
        testng.setDefaultSuiteName(suites.getSuiteName());
        testng.setSuiteThreadPoolSize(suites.getNumThreads());
        testng.setThreadCount(suites.getNumThreads());
        testng.setDataProviderThreadCount(suites.getNumDataProviderThreads());
        testng.setXmlSuites(xmlSuites);
        testng.setUseDefaultListeners(false);
        testng.setParallel("methods");
        testng.setConfigFailurePolicy("continue");
        testng.setVerbose(100);
        XMLReporter xmlReporter = new XMLReporter();
        testng.addListener(xmlReporter);
        testng.addListener(new TestListenerAdapter());
        testng.run();
    }

    private static XmlSuite createXmlSuite(String singleSuite) {
        XmlSuite suite = new XmlSuite();
        suite.setName("Selgp QA - " + singleSuite + " tests");
        suite.setParallel("methods");
        suite.setDataProviderThreadCount(suites.getNumDataProviderThreads());
        suite.setThreadCount(suites.getNumThreads());
        suite.setTimeOut(String.valueOf(suites.getTimeout()));
        suite.setConfigFailurePolicy("continue");
        suite.setVerbose(100);

        if (!TestRunner.getInstance().PARAMETER.equals("unset")) {
            String[] keyAndValue = TestRunner.getInstance().PARAMETER.split("=");
            Map<String, String> parameterMap = new HashMap<String, String>();
            parameterMap.put(keyAndValue[0], keyAndValue[1]);
            suite.setParameters(parameterMap);
        }

        List<XmlClass> xmlClasses = null;
        if (!TestRunner.getInstance().CLASS_NAME.equals("unset")) {
            xmlClasses = getClassesFromArguments();
        } else {
            xmlClasses = getClassesForSuite();
        }
        createSuiteXmlTest(suite, TestRunner.getInstance().suites, xmlClasses);
        return suite;
    }

    private static XmlTest createSuiteXmlTest(XmlSuite xmlSuite, Suites suite, List<XmlClass> xmlClasses) {
        XmlTest xmlTest = new XmlTest(xmlSuite);
        xmlTest.setName(TestRunner.getInstance().SUITE_NAME);
        xmlTest.setClasses(xmlClasses);
        return xmlTest;
    }

    private static List<XmlClass> getClassesFromArguments() {
        List<XmlClass> classes = new ArrayList<XmlClass>();
        String[] classArray = TestRunner.getInstance().CLASS_NAME.split(",");
        for (String className : classArray) {
            XmlClass singleClass = new XmlClass(className);
            classes.add(singleClass);
        }
        return classes;
    }

    private static List<XmlClass> getClassesForSuite() {
        List<XmlClass> classes = new ArrayList<XmlClass>();
        String basePackage = suites.getBaseTestPackage();
        Class<?>[] classArray = findClassesRecursively(basePackage);
        for (Class<?> className : classArray) {
            XmlClass singleClass = new XmlClass(className);
            classes.add(singleClass);
        }
        return classes;
    }

    private static Class<?>[] findClassesRecursively(String fromPackage) {
        Set<Class<?>> classesSet;
        try {
            classesSet = new ClassesInPackageScanner().scan(fromPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Class<?>> testClassesList = new ArrayList<Class<?>>();
        Iterator<Class<?>> it = classesSet.iterator();
        while (it.hasNext()) {
            Class<?> c = it.next();
            if (isTestAnnotationPresentInClass(c) || isTestAnnotationPresentInMethods(c)) {
                testClassesList.add(c);
            }
        }
        Class<?>[] classes = testClassesList.toArray(new Class<?>[testClassesList.size()]);
        return classes;
    }

    private static boolean isTestAnnotationPresentInClass(Class<?> testClass) {
        return testClass.isAnnotationPresent(org.testng.annotations.Test.class)
                || testClass.isAnnotationPresent(org.testng.annotations.Factory.class);
    }

    private static boolean isTestAnnotationPresentInMethods(Class<?> testClass) {
        for (Method method : testClass.getMethods()) {
            if ((method.isAnnotationPresent(org.testng.annotations.Test.class))
                    || method.isAnnotationPresent(org.testng.annotations.Factory.class)) {
                return true;
            }
        }
        return false;
    }
}