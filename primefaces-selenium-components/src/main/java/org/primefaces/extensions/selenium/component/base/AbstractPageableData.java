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
package org.primefaces.extensions.selenium.component.base;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.model.data.Page;
import org.primefaces.extensions.selenium.component.model.data.Paginator;

public abstract class AbstractPageableData extends AbstractComponent {

    @FindBy(className = "ui-paginator")
    private WebElement paginator;

    public abstract List<WebElement> getRowsWebElement();

    public WebElement getPaginatorWebElement() {
        return paginator;
    }

    public Paginator getPaginator() {
        return new Paginator(getPaginatorWebElement());
    }

    public void selectPage(Page page) {
        PrimeSelenium.guardAjax(page.getWebElement()).click();
    }

    public void selectPage(int number) {
        for (Page page : getPaginator().getPages()) {
            if (page.getNumber() == number) {
                selectPage(page);
                break;
            }
        }
    }
}
