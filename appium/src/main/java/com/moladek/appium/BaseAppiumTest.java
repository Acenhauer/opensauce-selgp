package com.moladek.appium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * Base class for all Appium tests.
 *
 * @author guillem.hernandez@selgp.com
 */

public abstract class BaseAppiumTest {

    public class SwipeableWebDriver extends RemoteWebDriver implements HasTouchScreen {
        private RemoteTouchScreen touch;

        public SwipeableWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
            super(remoteAddress, desiredCapabilities);
            touch = new RemoteTouchScreen(getExecuteMethod());
        }

        public TouchScreen getTouch() {
            return touch;
        }
    }
}
