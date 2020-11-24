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
package org.primefaces.extensions.selenium.component.model.data;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;

public class Paginator {

    private WebElement webElement;
    private List<Page> pages;

    public Paginator(WebElement webElement) {
        this.webElement = webElement;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<Page> getPages() {
        if (pages == null) {
            pages = webElement.findElements(By.className("ui-paginator-page")).stream().map(pageElt -> {
                int number = Integer.parseInt(pageElt.getText());
                return new Page(number, pageElt);
            }).collect(Collectors.toList());
        }
        return pages;
    }

    public Page getPage(int index) {
        return getPages().get(index);
    }

    public Page getActivePage() {
        List<Page> pages = getPages();
        for (Page page : pages) {
            if (PrimeSelenium.hasCssClass(page.getWebElement(), "ui-state-active")) {
                return page;
            }
        }
        return null;
    }
}
