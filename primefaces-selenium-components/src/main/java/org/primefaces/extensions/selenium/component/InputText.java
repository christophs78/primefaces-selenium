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

import java.io.Serializable;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;

public abstract class InputText extends AbstractInputComponent {

    public boolean isOnchangeAjaxified() {
        return isAjaxified(getInput(), "onchange");
    }

    public String getValue() {
        return getInput().getAttribute("value");
    }

    public void setValue(Serializable value) {
        WebElement input = getInput();
        input.clear();
        ComponentUtils.sendKeys(input, value.toString());

        if (isOnchangeAjaxified()) {
            PrimeSelenium.guardAjax(input).sendKeys(Keys.TAB);
        }
        else {
            input.sendKeys(Keys.TAB);
        }
    }
}
