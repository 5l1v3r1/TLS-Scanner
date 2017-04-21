/**
 * TLS-Scanner - A TLS Configuration Analysistool based on TLS-Attacker
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsscanner.report.check;

import java.io.Serializable;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
public class CheckConfig implements Serializable {

    private String successName;
    private String unsuccessName;
    private String successDescription;
    private String unsuccessDescription;
    private boolean transparentIfPassed;

    public CheckConfig(String successName, String unsuccessName, String successDescription,
            String unsuccessDescription, boolean transparentIfPassed) {
        this.successName = successName;
        this.unsuccessName = unsuccessName;
        this.successDescription = successDescription;
        this.unsuccessDescription = unsuccessDescription;
        this.transparentIfPassed = transparentIfPassed;
    }

    public CheckConfig() {
    }

    public String getSuccessName() {
        return successName;
    }

    public void setSuccessName(String successName) {
        this.successName = successName;
    }

    public String getUnsuccessName() {
        return unsuccessName;
    }

    public void setUnsuccessName(String unsuccessName) {
        this.unsuccessName = unsuccessName;
    }

    public String getSuccessDescription() {
        return successDescription;
    }

    public void setSuccessDescription(String successDescription) {
        this.successDescription = successDescription;
    }

    public String getUnsuccessDescription() {
        return unsuccessDescription;
    }

    public void setUnsuccessDescription(String unsuccessDescription) {
        this.unsuccessDescription = unsuccessDescription;
    }

    public boolean isTransparentIfPassed() {
        return transparentIfPassed;
    }

    public void setTransparentIfPassed(boolean transparentIfPassed) {
        this.transparentIfPassed = transparentIfPassed;
    }
}
