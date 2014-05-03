package com.selgp.automation.core.suites;

import com.selgp.automation.core.TestRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Handle Suites configuration in the framework with its properties.
 *
 * @author guillem.hernandez
 */
public class Suites {

    private static Suites instance = null;
    public static String EMAILS;
    public static String TIMEOUT;
    public static String NUMTHREADS;
    public static String DATAPROVIDERTHREADCOUNT;
    public static String HASRETRY;
    public static String RETRIES;
    public static String SUITENAME;

    public Suites(Properties props) {
        EMAILS = props.getProperty("EMAILS");
        TIMEOUT = props.getProperty("TIMEOUT");
        NUMTHREADS = props.getProperty("NUMTHREADS");
        DATAPROVIDERTHREADCOUNT = props.getProperty("DATAPROVIDERTHREADCOUNT");
        HASRETRY = props.getProperty("HASRETRY");
        SUITENAME = props.getProperty("SUITENAME");
        RETRIES = props.getProperty("RETRIES");
    }

    public static int getTimeout() {
        return Integer.parseInt(TIMEOUT.replaceAll("\\s+", ""));
    }

    public static int getNumThreads() {
        return Integer.parseInt(NUMTHREADS.replaceAll("\\s+", ""));
    }

    public static int getNumDataProviderThreads() {
        return Integer.parseInt(DATAPROVIDERTHREADCOUNT.replaceAll("\\s+", ""));
    }

    public static boolean hasRetry() {
        return Boolean.parseBoolean(HASRETRY.replaceAll("\\s+", ""));
    }

    public static int getNumberOfRetries() {
        return Integer.parseInt(RETRIES.replaceAll("\\s+", ""));
    }

    public static String getBaseTestPackage() {
        return System.getProperty("tests.package").toString().replaceAll("\\s+", "");
    }

    public static String getSuiteName() {
        return SUITENAME.toString().replaceAll("\\s+", "");
    }

    public static String getEmailRecipients() {
        return EMAILS.toString().replaceAll("\\s+", "");
    }

    public static Suites getInstance() {
        if (instance == null) {
            try {
                Properties props = new Properties();
                props.load(new InputStreamReader(TestRunner.class.getClassLoader().getResourceAsStream("environments.properties")));
                instance = new Suites(props);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
