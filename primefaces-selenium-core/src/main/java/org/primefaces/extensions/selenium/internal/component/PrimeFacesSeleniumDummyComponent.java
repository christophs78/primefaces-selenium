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
package org.primefaces.extensions.selenium.internal.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.primefaces.util.ResourceUtils;

public class PrimeFacesSeleniumDummyComponent extends UIComponentBase {

    public PrimeFacesSeleniumDummyComponent() {
        ResourceUtils.addComponentResource(FacesContext.getCurrentInstance(), "pfselenium.core.csp.js", "primefaces_selenium", "head");
    }

    @Override
    public String getFamily() {
        return "org.primefaces.extensions.selenium";
    }
}
