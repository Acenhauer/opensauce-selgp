package com.selgp.opensauce.webdriver;

import com.selgp.opensauce.automation.core.TestRunner;
import com.selgp.opensauce.automation.core.saucelabs.SauceREST;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Base class for all WebDriver tests.
 *
 * @author guillem.hernandez@selgp.com
 */
public abstract class BaseWebDriverTest {

    private static final Logger LOGGER = Logger.getLogger(BaseWebDriverTest.class);

    public final static String USER = System.getProperty("user", "unset");

    public final static String APIKEY = System.getProperty("apikey", "unset");

    private InheritableThreadLocal<WebDriver> globalDriver = new InheritableThreadLocal<WebDriver>();

    public static final String BROWSER = System.getProperty("browser", "unset");

    private DesiredCapabilities caps;

    private String testName;

    private String sessionId;

    public SauceREST client;

    public synchronized String getTestName() {
        return testName;
    }

    public synchronized void setTestName(String testName) {
        this.testName = testName;
    }

    public synchronized String getSessionId() {
        return sessionId;
    }

    public synchronized void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public synchronized DesiredCapabilities getCapabilities() {
        return caps;
    }

    public synchronized void setCapabilities(DesiredCapabilities caps) {
        this.caps = caps;
    }

    @BeforeMethod(alwaysRun = true)
    protected void setup(Method method, Object[] testArguments) {
        setTestName(method.getName() + "_" + testArguments.hashCode());
        SelgpRemoteWebDriver remoteWD = new SelgpRemoteWebDriver();
        setCapabilities(getDesiredCapabilitiesPerBrowser());
        globalDriver.set(remoteWD.prepareRemoteWebDriver(getCapabilities()));
        globalDriver.get().manage().window().maximize();
        globalDriver.get().manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
        globalDriver.get().manage().timeouts().setScriptTimeout(25, TimeUnit.SECONDS);
        globalDriver.get().manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
        LOGGER.info("CLEANING the cookies for browser " + BROWSER + " on environment " + TestRunner.getInstance().getInstance().ENVIRONMENT);
        globalDriver.get().manage().deleteAllCookies();
        setSessionId(remoteWD.getSessionId());
        client = new SauceREST(USER, APIKEY);
    }

    public DesiredCapabilities getDesiredCapabilitiesPerBrowser() {
        caps = new DesiredCapabilities();
        if (BROWSER.equals("unset")) {
            //Default configuration
            caps = DesiredCapabilities.firefox();
            LOGGER.info("Starting local Firefox session: " + getTestName());
            return caps;
        } else {
            if (BROWSER.equals("FFSL")) {
                caps = DesiredCapabilities.firefox();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "firefox");
                caps.setCapability("platform", Platform.XP);
                caps.setCapability("version", "28");
                LOGGER.info("Starting Firefox Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("CHSL")) {
                caps = DesiredCapabilities.chrome();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");
                caps.setCapability("platform", Platform.XP);
                caps.setCapability("version", "34");
                LOGGER.info("Starting Chrome Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("SFSL")) {
                caps = DesiredCapabilities.safari();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "5");
                LOGGER.info("Starting Safari Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("OPSL")) {
                caps = DesiredCapabilities.opera();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "11");
                LOGGER.info("Starting Opera Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("IE6SL")) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "iexplore");
                caps.setCapability("platform", Platform.XP);
                caps.setCapability("version", "6");
                LOGGER.info("Starting Internet Explorer 6 Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("IE7SL")) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "iexplore");
                caps.setCapability("version", "7");
                LOGGER.info("Starting Internet Explorer 7 Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("IE8SL")) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "iexplore");
                caps.setCapability("version", "8");
                LOGGER.info("Starting Internet Explorer 8 Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("IE9SL")) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "iexplore");
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "9");
                LOGGER.info("Starting Internet Explorer 9 Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("IE10SL")) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "iexplore");
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "10");
                LOGGER.info("Starting Internet Explorer 10 Saucelabs session: " + getTestName());
                return caps;
            } else if (BROWSER.equals("IE11SL")) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("id", getTestName());
                caps.setCapability("name", getTestName());
                caps.setCapability(CapabilityType.BROWSER_NAME, "iexplore");
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "11");
                LOGGER.info("Starting Internet Explorer 11 Saucelabs session: " + getTestName());
                return caps;
            }
        }
        return caps;
    }

    @AfterMethod(alwaysRun = true)
    protected void teardown(ITestResult tr, Method method) {
        globalDriver.get().close();
        globalDriver.get().quit();
            if (tr.isSuccess()) {
                client.jobPassed(getSessionId());
            } else {
                client.jobFailed(getSessionId());
            }
    }

    protected WebDriver driver() {
        return globalDriver.get();
    }
}
