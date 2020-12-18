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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

public abstract class AbstractPrimePage {

    private WebDriver webDriver;

    public String getBaseLocation() {
        return null;
    }

    public abstract String getLocation();

    public void goTo() {
        PrimeSelenium.goTo(this);
    }

    public boolean isAt() {
        return WebDriverProvider.get().getCurrentUrl().contains(getLocation());
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebStorage getWebStorage() {
        if (webDriver instanceof WebStorage) {
            return (WebStorage) webDriver;
        }
        else if (webDriver instanceof EventFiringWebDriver) {
            EventFiringWebDriver driver = (EventFiringWebDriver) webDriver;
            return (WebStorage) driver.getWrappedDriver();
        }
        return null;
    }
}
