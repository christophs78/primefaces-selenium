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
package org.primefaces.extensions.selenium.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class DatePicker extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @Override
    public WebElement getInput() {
        return input;
    }

    public WebElement getPanel() {
        return getWebDriver().findElement(By.id(getId() + "_panel"));
    }

    public LocalDateTime getValue() {
        Object date = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate()");

        if (date == null) {
            return null;
        }

        // TODO: take timeZone - attribute into account when set; currently we always use the default ZoneId.systemDefault()

        String utcTimeString = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate().toUTCString();");

        // Parse time string and move into server-timezone
        LocalDateTime dateTime = LocalDateTime.parse(utcTimeString, DateTimeFormatter.RFC_1123_DATE_TIME);
        dateTime = LocalDateTime.ofInstant(dateTime.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

        return dateTime;
    }

    public LocalDate getValueAsLocalDate() {
        LocalDateTime value = getValue();
        return value != null ? value.toLocalDate() : null;
    }

    public void setValue(LocalDate localDate) {
        setValue(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public void setValue(LocalDateTime dateTime) {
        long millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        setValue(millis);
    }

    public void setValue(long millis) {
        String formattedDate = millisAsFormattedDate(millis);

        // Emulate user input instead of using js, calendar.setDate() can't go beyond mindate/maxdate
        WebElement input = getInput();
        if (PrimeSelenium.isMacOs()) {
            input.sendKeys(Keys.chord(Keys.COMMAND, "a")); // select everything
        }
        else {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a")); // select everything
        }
        input.sendKeys(formattedDate); // overwrite value

        if (ComponentUtils.hasAjaxBehavior(getRoot(), "dateSelect")) {
            PrimeSelenium.guardAjax(input).sendKeys(Keys.TAB);
        }
        else {
            input.sendKeys(Keys.TAB);
        }
    }

    public String millisAsFormattedDate(long millis) {
        return PrimeSelenium.executeScript(
                    "return " + getWidgetByIdScript() + ".jq.data().primeDatePicker.formatDateTime(new Date(" + millis + "));");
    }

    public long getTimezoneOffset() {
        return (Long) PrimeSelenium.executeScript("return new Date().getTimezoneOffset();");
    }

}
