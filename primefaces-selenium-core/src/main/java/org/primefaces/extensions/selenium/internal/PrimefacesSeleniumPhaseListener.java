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
package org.primefaces.extensions.selenium.internal;

import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.primefaces.PrimeFaces;
import org.primefaces.extensions.selenium.internal.component.PrimeFacesSeleniumSystemEventListener;

public class PrimefacesSeleniumPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        if (!PrimeFaces.current().isAjaxRequest()) {
            FacesContext.getCurrentInstance().getViewRoot().subscribeToViewEvent(PreRenderViewEvent.class, new PrimeFacesSeleniumSystemEventListener());

            /*
             * All these other variants to not work because we can´t insert pfselenium.core.csp.js at the right place. pfselenium.core.csp.js must be inserted
             * after core.js
             */
            // FacesContext.getCurrentInstance().getViewRoot().getChildren().add(new PrimeFacesSeleniumDummyComponent());
            // ResourceUtils.addComponentResource(FacesContext.getCurrentInstance(), "core.js", "primefaces", "head");
            // ResourceUtils.addComponentResource(FacesContext.getCurrentInstance(), "pfselenium.core.csp.js", "primefaces_selenium", "head");
        }
    }

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
