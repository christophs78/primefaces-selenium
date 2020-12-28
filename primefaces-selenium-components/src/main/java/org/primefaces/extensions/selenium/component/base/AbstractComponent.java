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

import org.json.JSONObject;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.AbstractPrimePageFragment;
import org.primefaces.extensions.selenium.PrimeSelenium;

public abstract class AbstractComponent extends AbstractPrimePageFragment {

    /**
     * Gets the widget by component id JS function.
     *
     * @return the JS script
     */
    public String getWidgetByIdScript() {
        return ComponentUtils.getWidgetByIdScript(getId());
    }

    /**
     * Gets the current widget's configuration e.g. widget.cfg as a String.
     *
     * @return the String representation of the widget configuration
     */
    public String getWidgetConfigurationAsString() {
        return ComponentUtils.getWidgetConfiguration(getRoot());
    }

    /**
     * Gets the current widget's configuration e.g. widget.cfg as a JSON object.
     *
     * @return the {@link JSONObject} representing the config, useful for assertions
     */
    public JSONObject getWidgetConfiguration() {
        String cfg = getWidgetConfigurationAsString();
        if (cfg == null || cfg.length() == 0) {
            return null;
        }
        return new JSONObject(cfg);
    }

    /**
     * Is the event for the root-element ajaxified?
     *
     * @param event Event with the `on` prefix, such as `onclick` or `onblur`.
     * @return
     */
    protected boolean isAjaxified(String event) {
        return isAjaxified(getRoot(), event);
    }

    /**
     * Is the event ajaxified?
     *
     * @param element Element for which to do the check. (May be a child element of a complex component.) If no element is passed it defaults to getRoot().
     * @param event Event with the `on` prefix, such as `onclick` or `onblur`.
     * @return
     */
    protected boolean isAjaxified(WebElement element, String event) {
        if (element == null) {
            element = getRoot();
        }
        Boolean hasCspRegisteredEvent = false;
        try {
            hasCspRegisteredEvent = PrimeSelenium.executeScript("return PrimeFaces.csp.hasRegisteredAjaxifiedEvent('" +
                        element.getAttribute("id") + "', '" + event + "')");
        }
        catch (JavascriptException ex) {
            if (ex.getMessage().contains("javascript error: PrimeFaces.csp.hasRegisteredAjaxifiedEvent is not a function")) {
                System.err.println("WARNING: 'pfselenium.core.csp.js' missing - not added to the page");
            }
            else {
                throw ex;
            }
        }
        return (ComponentUtils.isAjaxScript(element.getAttribute(event)) || hasCspRegisteredEvent);
    }
}
