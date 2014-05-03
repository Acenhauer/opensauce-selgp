package com.selgp.automation.core.environments;

import com.selgp.automation.core.TestRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Stores test environment data.
 *
 * @author guillem.hernandez@selgp.com
 */
public class Environments {
    public static String startUrl;
    private static Environments instance = null;
    /**
     * Using DISP environments
     */
    public final String DEVELOPMENT_URL;
    public final String INTEGRATION_URL;
    public final String STAGING_URL;
    public final String PRODUCTION_URL;
    public final String environment = System.getProperty("environment");

    public Environments(Properties props) {

        DEVELOPMENT_URL = props.getProperty("DEVELOPMENT_URL");
        INTEGRATION_URL = props.getProperty("INTEGRATION_URL");
        STAGING_URL = props.getProperty("STAGING_URL");
        PRODUCTION_URL = props.getProperty("PRODUCTION_URL");

        if (environment.equals("DEVELOPMENT")) {
            startUrl = DEVELOPMENT_URL;
        } else if (environment.equals("INTEGRATION")) {
            startUrl = INTEGRATION_URL;
        } else if (environment.equals("STAGING")) {
            startUrl = STAGING_URL;
        } else {
            startUrl = PRODUCTION_URL;
        }
    }

    public static Environments getInstance() {
        if (instance == null) {
            try {
                Properties props = new Properties();
                props.load(new InputStreamReader(TestRunner.class.getClassLoader().getResourceAsStream("environments.properties")));
                instance = new Environments(props);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}