/**
 * TLS-Scanner - A TLS Configuration Analysistool based on TLS-Attacker
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsscanner.report.check;

import de.rub.nds.tlsscanner.probe.CertificateProbe;
import java.io.File;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
public class CheckConfigSerializer {

    private static final Logger LOGGER = LogManager.getLogger("CheckConfigSerializer");

    public static void serialize(CheckConfig config, File file) {
        JAXB.marshal(config, file);
    }

    public static CheckConfig deserialize(String resourcePath) {
        LOGGER.debug("Loading resource from:" + resourcePath);
        InputStream stream = CheckConfigSerializer.class.getResourceAsStream(resourcePath);
        return JAXB.unmarshal(stream, CheckConfig.class);
    }
}
