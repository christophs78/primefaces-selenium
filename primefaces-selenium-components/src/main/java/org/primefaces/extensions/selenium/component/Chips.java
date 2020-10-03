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

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class Chips extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @Override
    public WebElement getInput() {
        return input;
    }

    public List<String> getValues() {
        List<WebElement> chipTokens = getChipTokens();
        List<String> values = chipTokens.stream()
                    .map(token -> token.findElement(By.className("ui-chips-token-label")).getText())
                    .collect(Collectors.toList());

        return values;
    }

    public List<WebElement> getChipTokens() {
        return findElements(By.cssSelector("ul li.ui-chips-token"));
    }

    public void addValue(String value) {
        WebElement chipsInput = getInput();
        chipsInput.sendKeys(value);
        PrimeSelenium.guardAjax(chipsInput).sendKeys(Keys.ENTER);
    }

    public void removeValue(String value) {
        for (WebElement chipToken : getChipTokens()) {
            if (chipToken.findElement(By.className("ui-chips-token-label")).getText().equals(value)) {
                PrimeSelenium.guardAjax(chipToken.findElement(By.className("ui-icon-close"))).click();
            }
        }
    }
}
