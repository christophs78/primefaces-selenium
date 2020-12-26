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
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeExpectedConditions;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class SelectOneMenu extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @FindByParentPartialId(value = "_items", searchFromRoot = true)
    private WebElement items;

    @FindByParentPartialId(value = "_panel", searchFromRoot = true)
    private WebElement panel;

    /**
     * Either display the dropdown or select the item it if is already displayed.
     */
    public void toggleDropdown() {
        if (getPanel().isDisplayed()) {
            getLabel().click();

            PrimeSelenium.waitGui().until(PrimeExpectedConditions.invisibleAndAnimationComplete(panel));
        }
        else {
            // label.click();
            PrimeSelenium.executeScript(getWidgetByIdScript() + ".show();");

            PrimeSelenium.waitGui().until(PrimeExpectedConditions.visibleAndAnimationComplete(panel));
        }
    }

    public void deselect(String label) {
        if (!isSelected(label)) {
            return;
        }

        if (!getPanel().isDisplayed()) {
            toggleDropdown();
        }

        for (WebElement element : getItems().findElements(By.tagName("li"))) {
            if (element.getText().equalsIgnoreCase(label)) {
                click(element);
                break;
            }
        }

        if (getPanel().isDisplayed()) {
            toggleDropdown();
        }
    }

    public void select(String label) {
        if (isSelected(label)) {
            return;
        }

        if (!getPanel().isDisplayed()) {
            toggleDropdown();
        }

        for (WebElement element : getItems().findElements(By.tagName("li"))) {
            if (element.getText().equalsIgnoreCase(label)) {
                click(element);
                break;
            }
        }

        if (getPanel().isDisplayed()) {
            toggleDropdown();
        }
    }

    public String getSelectedLabel() {
        return getLabel().getText();
    }

    public boolean isSelected(String label) {
        boolean result = false;
        try {
            result = getSelectedLabel().equalsIgnoreCase(label);
        }
        catch (Exception e) {
            // do nothing
        }
        return result;
    }

    public List<String> getLabels() {
        return getInput().findElements(By.tagName("option")).stream()
                    .map(e -> e.getAttribute("innerHTML"))
                    .collect(Collectors.toList());
    }

    public void select(int index) {
        if (isSelected(index)) {
            return;
        }

        select(getLabel(index));
    }

    public void deselect(int index) {
        if (!isSelected(index)) {
            return;
        }

        deselect(getLabel(index));
    }

    public boolean isSelected(int index) {
        return getLabel(index).equals(getSelectedLabel());
    }

    public String getLabel(int index) {
        return getLabels().get(index);
    }

    @Override
    public WebElement getInput() {
        return input;
    }

    public WebElement getEditableInput() {
        return getRoot().findElement(By.name(getId() + "_editableInput"));
    }

    public WebElement getLabel() {
        return getRoot().findElement(By.id(getId() + "_label"));
    }

    public WebElement getItems() {
        return items;
    }

    public WebElement getPanel() {
        return panel;
    }

    protected void click(WebElement element) {
        if (isAjaxified(getInput(), "onchange")) {
            PrimeSelenium.guardAjax(element).click();
        }
        else {
            element.click();
        }
    }
}
