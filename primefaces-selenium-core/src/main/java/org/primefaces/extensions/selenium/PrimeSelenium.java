/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.primefaces.extensions.selenium.internal.ConfigProvider;
import org.primefaces.extensions.selenium.internal.Guard;
import org.primefaces.extensions.selenium.spi.PrimePageFactory;
import org.primefaces.extensions.selenium.spi.PrimeSeleniumAdapter;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

public final class PrimeSelenium {

    private PrimeSelenium() {
        super();
    }

    public static WebDriver getWebDriver() {
        return WebDriverProvider.get();
    }

    public static <T> T executeScript(String script, Object... args) {
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        return (T) executor.executeScript(script, args);
    }

    public static <T extends AbstractPrimePage> T goTo(Class<T> pageClass) {
        WebDriver driver = WebDriverProvider.get();

        T page = PrimePageFactory.create(pageClass, driver);
        driver.get(getUrl(page));

        return page;
    }

    public static void goTo(AbstractPrimePage page) {
        WebDriver driver = WebDriverProvider.get();
        driver.get(getUrl(page));
    }

    public static String getUrl(AbstractPrimePage page) {
        PrimeSeleniumAdapter adapter = ConfigProvider.getInstance().getAdapter();

        String baseLocation = page.getBaseLocation();
        if (adapter != null) {
            baseLocation = adapter.getBaseUrl();
        }

        return baseLocation + page.getLocation();
    }

    public static String getUrl(String url) {
        PrimeSeleniumAdapter adapter = ConfigProvider.getInstance().getAdapter();
        return adapter.getBaseUrl() + url;
    }

    public static boolean hasCssClass(WebElement element, String cssClass) {
        String classes = element.getAttribute("class");
        if (classes == null || cssClass.isEmpty()) {
            return false;
        }

        for (String currentClass : classes.split(" ")) {
            if (currentClass.equals(cssClass)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isElementPresent(By by) {
        try {
            getWebDriver().findElement(by);
            return true;
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed(); // just any method to check if NoSuchElementException will be thrown
            return true;
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isElementDisplayed(By by) {
        try {
            return getWebDriver().findElement(by).isDisplayed();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isElementEnabled(By by) {
        try {
            return getWebDriver().findElement(by).isEnabled();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static <T> T guardHttp(T target) {
        return Guard.http(target);
    }

    public static <T> T guardAjax(T target) {
        return Guard.ajax(target);
    }

    public static WebDriverWait waitGui() {
        ConfigProvider config = ConfigProvider.getInstance();
        WebDriver driver = WebDriverProvider.get();

        WebDriverWait wait = new WebDriverWait(driver, config.getGuiTimeout(), 100);

        return wait;
    }

    public static WebDriverWait waitDocumentLoad() {
        ConfigProvider config = ConfigProvider.getInstance();
        WebDriver driver = WebDriverProvider.get();

        WebDriverWait wait = new WebDriverWait(driver, config.getDocumentLoadTimeout(), 100);
        wait.until(PrimeExpectedConditions.documentLoaded());

        return wait;
    }

    public static void disableAnimations() {
        executeScript("if (window.$) { $(function() { $.fx.off = true; }); }");
    }

    public static void enableAnimations() {
        executeScript("if (window.$) { $(function() { $.fx.off = false; }); }");
    }

    /**
     * Is the current WebDriver a Chrome driver?
     *
     * @return true if Chrome, false if any other browser
     */
    public static boolean isChrome() {
        Capabilities cap = ((EventFiringWebDriver) getWebDriver()).getCapabilities();
        return "Chrome".equalsIgnoreCase(cap.getBrowserName());
    }

    /**
     * Is the current WebDriver a Firefox driver?
     *
     * @return true if Firefox, false if any other browser
     */
    public static boolean isFirefox() {
        Capabilities cap = ((EventFiringWebDriver) getWebDriver()).getCapabilities();
        return "Firefox".equalsIgnoreCase(cap.getBrowserName());
    }

    /**
     * Do we run on MacOS?
     *
     * @return true if MacOS
     */
    public static boolean isMacOs() {
        String os = System.getProperty("os.name").toUpperCase();
        if ((os.indexOf("DARWIN") >= 0) || (os.indexOf("MAC") >= 0)) {
            return true;
        }
        else {
            return false;
        }
    }
}
