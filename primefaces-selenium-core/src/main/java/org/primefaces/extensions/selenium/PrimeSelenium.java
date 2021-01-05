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
package org.primefaces.extensions.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.primefaces.extensions.selenium.internal.ConfigProvider;
import org.primefaces.extensions.selenium.internal.Guard;
import org.primefaces.extensions.selenium.spi.PrimePageFactory;
import org.primefaces.extensions.selenium.spi.PrimePageFragmentFactory;
import org.primefaces.extensions.selenium.spi.PrimeSeleniumAdapter;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

public final class PrimeSelenium {

    private static final String HEADLESS_MODE_SYSPROP_NAME = "webdriver.headless";

    private static final String HEADLESS_MODE_SYSPROP_VAL_DEFAULT = "false";

    private PrimeSelenium() {
        super();
    }

    public static WebDriver getWebDriver() {
        return WebDriverProvider.get();
    }

    /**
     * Creates the PrimeFaces Selenium component for the selector.
     *
     * @param fragmentClass the component class to create like InputText.class
     * @param by the selector to find the component by
     * @param <T> the type of component returned
     * @return the component
     */
    public static <T extends WebElement> T createFragment(Class<T> fragmentClass, By by) {
        WebElement element = getWebDriver().findElement(by);
        return createFragment(fragmentClass, element);
    }

    /**
     * Creates the PrimeFaces Selenium component for the element.
     *
     * @param fragmentClass the component class to create like InputText.class
     * @param element the WebElement to bind this component class to
     * @param <T> the type of component returned
     * @return the component
     */
    public static <T extends WebElement> T createFragment(Class<T> fragmentClass, WebElement element) {
        return PrimePageFragmentFactory.create(fragmentClass, element);
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
     * Sets a value to a hidden input.
     *
     * @param input the WebElement input to set
     * @param value the value to set
     * @see https://stackoverflow.com/questions/11858366/how-to-type-some-text-in-hidden-field-in-selenium-webdriver-using-java
     */
    public static void setHiddenInput(WebElement input, String value) {
        executeScript(" document.getElementById('" + input.getAttribute("id") + "').value='" + value + "'");
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
     * Is the current WebDriver a Safari driver?
     *
     * @return true if Safari, false if any other browser
     */
    public static boolean isSafari() {
        Capabilities cap = ((EventFiringWebDriver) getWebDriver()).getCapabilities();
        return "Safari".equalsIgnoreCase(cap.getBrowserName());
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

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty(HEADLESS_MODE_SYSPROP_NAME, HEADLESS_MODE_SYSPROP_VAL_DEFAULT));
    }
}
