package com.selgp.selenium.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Create a RemoteWebDriver session
 *
 * @author guillem.hernandez@selgp.com
 */
public class SelgpRemoteWebDriver {

    public final static String HUB_URL = System.getProperty("hub");
    private String sessionId;

    public WebDriver prepareRemoteWebDriver(DesiredCapabilities capability) {
        try {
            RemoteWebDriver wd = new RemoteWebDriver(new URL(HUB_URL), capability);
            setSessionId(wd.getSessionId().toString());
            return wd;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL passed to RemoteWebDriver.", e);
        }
    }

    public synchronized String getSessionId() {
        return sessionId;
    }

    public synchronized void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}