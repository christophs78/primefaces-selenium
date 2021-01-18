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

/**
 * Component wrapper for the PrimeFaces {@code p:autoComplete}.
 */
public abstract class AutoComplete extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @FindByParentPartialId(value = "_panel", searchFromRoot = true)
    private WebElement panel;

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
     * Is the input using AJAX "clear" event?
     *
     * @return true if using AJAX for clear
     */
    public boolean isClearAjaxified() {
        return ComponentUtils.hasAjaxBehavior(getRoot(), "clear");
    }

    /**
     * Is the input using AJAX "itemSelect" event?
     *
     * @return true if using AJAX for itemSelect
     */
    public boolean isItemSelectAjaxified() {
        return ComponentUtils.hasAjaxBehavior(getRoot(), "itemSelect");
    }

    /**
     * Is the input using AJAX "itemUnselect" event?
     *
     * @return true if using AJAX for itemUnselect
     */
    public boolean isItemUnselectAjaxified() {
        return ComponentUtils.hasAjaxBehavior(getRoot(), "itemUnselect");
    }

    /**
     * Is the input using AJAX "query" event?
     *
     * @return true if using AJAX for query
     */
    public boolean isQueryAjaxified() {
        return ComponentUtils.hasAjaxBehavior(getRoot(), "query");
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
     * Clears the Autocomplete input and guards AJAX for "clear" event.
     */
    @Override
    public void clear() {
        WebElement input = getInput();
        selectAllText();
        if (isClearAjaxified()) {
            input = PrimeSelenium.guardAjax(input);
        }
        input.sendKeys(Keys.BACK_SPACE);
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

    /**
     * Execute the AutoComplete search.
     *
     * @param value the search to execute
     */
    public void search(String value) {
        // search always uses AJAX no matter what
        PrimeSelenium.executeScript(true, getWidgetByIdScript() + ".search(arguments[0]);", value);
        wait4Panel();
    }
}
