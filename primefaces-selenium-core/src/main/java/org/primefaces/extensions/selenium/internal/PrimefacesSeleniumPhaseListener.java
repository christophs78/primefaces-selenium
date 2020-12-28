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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.primefaces.PrimeFaces;

public class PrimefacesSeleniumPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    private static String pfSeleniumCoreCsp;

    public PrimefacesSeleniumPhaseListener() throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream("/primefaces-selenium/pfselenium.core.csp.js"),
                    StandardCharsets.UTF_8))) {
            pfSeleniumCoreCsp = buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        if (!PrimeFaces.current().isAjaxRequest()) {
            PrimeFaces.current().executeInitScript(pfSeleniumCoreCsp);
        }
    }

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
