/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.selenium.spi;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.internal.ConfigProvider;
import org.primefaces.extensions.selenium.internal.OnloadScriptsEventListener;

public class WebDriverProvider {

    private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

    public static void set(WebDriver driver) {
        WEB_DRIVER.set(driver);
    }

    public static WebDriver get() {
        return get(false);
    }

    public static WebDriver get(boolean create) {
        WebDriver driver = WEB_DRIVER.get();
        if (driver == null && create) {
            PrimeSeleniumAdapter adapter = ConfigProvider.getInstance().getAdapter();

            driver = adapter.createWebDriver();

            if (PrimeSelenium.isHeadless()) {
                /*
                 * Define window-size for headless-mode. Selenium WebDriver-default seems to be 800x600. This causes issues with modern themes (eg Saga) which
                 * use more space for some components. (eg DatePicker-popup)
                 */
                driver.manage().window().setSize(new Dimension(1920, 1080));
            }
            else if (PrimeSelenium.isSafari()) {
                driver.manage().window().setSize(new Dimension(1280, 800));
            }

            EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new OnloadScriptsEventListener());

            set(eventDriver);

            driver = eventDriver;
        }
        return driver;
    }
}
