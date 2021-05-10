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
package org.primefaces.extensions.selenium.internal.junit;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

public class WebDriverExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        WebDriverProvider.get(true);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        WebDriver webDriver = WebDriverProvider.get();
        if (webDriver != null) {
            if (PrimeSelenium.isSafari()) {
                // special Safari treatment - see https://github.com/appium/appium/issues/9938
                webDriver.close();
                PrimeSelenium.wait(1000);
            }
            webDriver.quit();
            if (PrimeSelenium.isSafari()) {
                // wait a bit so Safari really get´s closed - adapted version of https://github.com/appium/appium/issues/9938
                PrimeSelenium.wait(1000);
            }
        }
        WebDriverProvider.set(null);
    }
}
