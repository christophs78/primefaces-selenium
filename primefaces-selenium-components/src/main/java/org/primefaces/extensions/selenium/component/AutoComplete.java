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
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeExpectedConditions;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class AutoComplete extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @FindByParentPartialId(value = "_panel", searchFromRoot = true)
    private WebElement panel;

    public boolean isOnchangeAjaxified() {
        return isAjaxified(getInput(), "onchange");
    }

    @Override
    public WebElement getInput() {
        return input;
    }

    public WebElement getItems() {
        return getWebDriver().findElement(By.className("ui-autocomplete-items"));
    }

    public List<String> getItemValues() {
        List<WebElement> itemElements = getItems().findElements(By.className("ui-autocomplete-item"));
        return itemElements.stream().map(elt -> elt.getText()).collect(Collectors.toList());
    }

    public WebElement getPanel() {
        return panel;
    }

    public String getValue() {
        return getInput().getAttribute("value");
    }

    /**
     * If using multiple mode gets the values of the tokens.
     *
     * @return the values in a list
     */
    public List<String> getValues() {
        List<WebElement> tokens = getTokens();
        return tokens.stream()
                    .map(token -> token.findElement(By.className("ui-autocomplete-token-label")).getText())
                    .collect(Collectors.toList());
    }

    /**
     * Gets the actual token elements in mutliple mode.
     *
     * @return the List of tokens
     */
    public List<WebElement> getTokens() {
        return findElements(By.cssSelector("ul li.ui-autocomplete-token"));
    }

    /**
     * Sets the value and presses tab afterwards. Attention: Pressing tab selects the first suggested value.
     *
     * @param value
     */
    public void setValue(Serializable value) {
        setValueWithoutTab(value);
        sendTabKey();
    }

    /**
     * Sets the value without pressing tab afterwards.
     */
    public void setValueWithoutTab(Serializable value) {
        WebElement input = getInput();
        input.clear();
        ComponentUtils.sendKeys(input, value.toString());
    }

    /**
     * Sends the Tab-Key to jump to the next input. Attention: Pressing tab selects the first suggested value.
     */
    public void sendTabKey() {
        if (isOnchangeAjaxified()) {
            PrimeSelenium.guardAjax(input).sendKeys(Keys.TAB);
        }
        else {
            input.sendKeys(Keys.TAB);
        }
    }

    /**
     * Waits until the AutoComplete-Panel containing the suggestions shows up. (eg after typing)
     */
    public void wait4Panel() {
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.visibleAndAnimationComplete(panel));
    }

    /**
     * Shows the AutoComplete-Panel.
     */
    public void show() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".show();");
        wait4Panel();
    }

    /**
     * Hides the AutoComplete-Panel.
     */
    public void hide() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".hide();");
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.invisibleAndAnimationComplete(panel));
    }

    /**
     * Activates search behavior
     */
    public void activate() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".activate();");
    }

    /**
     * Deactivates search behavior
     */
    public void deactivate() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".deactivate();");
    }

    /**
     * Enables the input field
     */
    public void enable() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".enable();");
    }

    /**
     * Disables the input field
     */
    public void disable() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".disable();");
    }

    /**
     * Adds an item to the input field. Especially useful in 'multiple' mode.
     *
     * @param item the item to add to the tokens
     */
    public void addItem(String item) {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".addItem('" + item + "');");
    }

    /**
     * Removes an item from the input field. Especially useful in 'multiple' mode.
     *
     * @param item the item to remove from the tokens
     */
    public void removeItem(String item) {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".removeItem('" + item + "');");
    }

    public void search(String value) {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".search(arguments[0]);", value);
        wait4Panel();
    }
}
