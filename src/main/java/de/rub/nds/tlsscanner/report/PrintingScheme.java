/**
 * TLS-Scanner - A TLS configuration and analysis tool based on TLS-Attacker.
 *
 * Copyright 2017-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsscanner.report;

import de.rub.nds.tlsscanner.constants.AnsiColor;
import de.rub.nds.tlsscanner.rating.TestResult;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrintingScheme {

    private static final Logger LOGGER = LogManager.getLogger();

    private HashMap<AnalyzedProperty, ColorEncoding> colorEncodings;

    private HashMap<AnalyzedPropertyCategory, TextEncoding> textEncodings;

    private TextEncoding defaultTextEncoding;
    private ColorEncoding defaultColorEncoding;
    private boolean useColors;

    public PrintingScheme() {
    }

    public PrintingScheme(HashMap<AnalyzedProperty, ColorEncoding> colorEncodings, HashMap<AnalyzedPropertyCategory, TextEncoding> textEncodings, TextEncoding defaultTextEncoding, ColorEncoding defaultColorEncoding, boolean useColors) {
        this.colorEncodings = colorEncodings;
        this.textEncodings = textEncodings;
        this.defaultTextEncoding = defaultTextEncoding;
        this.defaultColorEncoding = defaultColorEncoding;
        this.useColors = useColors;
    }

    public HashMap<AnalyzedProperty, ColorEncoding> getColorEncodings() {
        return colorEncodings;
    }

    public HashMap<AnalyzedPropertyCategory, TextEncoding> getTextEncodings() {
        return textEncodings;
    }

    public String getEncodedString(SiteReport report, AnalyzedProperty property) {
        TestResult result = report.getResult(property);
        TextEncoding textEncoding = textEncodings.getOrDefault(property.getCategory(), defaultTextEncoding);
        ColorEncoding colorEncoding = colorEncodings.getOrDefault(property, defaultColorEncoding);
        String encodedText = textEncoding.encode(result);
        if (useColors) {
            return colorEncoding.encode(result, encodedText);
        } else {
            return encodedText;
        }
    }

    public static PrintingScheme getDefaultPrintingScheme(boolean useColors) {
        HashMap<TestResult, String> textEncodingMap = new HashMap<>();
        textEncodingMap.put(TestResult.COULD_NOT_TEST, "could not test");
        textEncodingMap.put(TestResult.ERROR_DURING_TEST, "error");
        textEncodingMap.put(TestResult.FALSE, "false");
        textEncodingMap.put(TestResult.NOT_TESTED_YET, "not tested yet");
        textEncodingMap.put(TestResult.TIMEOUT, "timeout");
        textEncodingMap.put(TestResult.TRUE, "true");
        textEncodingMap.put(TestResult.UNCERTAIN, "uncertain");
        textEncodingMap.put(TestResult.UNSUPPORTED, "unsupported by tls-scanner");
        TextEncoding defaultTextEncoding = new TextEncoding(textEncodingMap);
        HashMap<TestResult, AnsiColor> ansiColorMap = new HashMap<>();
        ansiColorMap.put(TestResult.COULD_NOT_TEST, AnsiColor.BLUE);
        ansiColorMap.put(TestResult.ERROR_DURING_TEST, AnsiColor.RED_BACKGROUND);
        ansiColorMap.put(TestResult.FALSE, AnsiColor.DEFAULT_COLOR);
        ansiColorMap.put(TestResult.NOT_TESTED_YET, AnsiColor.WHITE);
        ansiColorMap.put(TestResult.TIMEOUT, AnsiColor.PURPLE_BACKGROUND);
        ansiColorMap.put(TestResult.TRUE, AnsiColor.DEFAULT_COLOR);
        ansiColorMap.put(TestResult.UNCERTAIN, AnsiColor.YELLOW_BACKGROUND);
        ansiColorMap.put(TestResult.UNSUPPORTED, AnsiColor.CYAN);
        ColorEncoding defaultColorEncoding = new ColorEncoding(ansiColorMap);

        HashMap<TestResult, String> attackEncodingMap = new HashMap<>();
        attackEncodingMap.put(TestResult.COULD_NOT_TEST, "could not test (not vulnerable)");
        attackEncodingMap.put(TestResult.ERROR_DURING_TEST, "error");
        attackEncodingMap.put(TestResult.FALSE, "not vulnerable");
        attackEncodingMap.put(TestResult.NOT_TESTED_YET, "not tested yet");
        attackEncodingMap.put(TestResult.TIMEOUT, "timeout");
        attackEncodingMap.put(TestResult.TRUE, "vulnerable");
        attackEncodingMap.put(TestResult.UNCERTAIN, "uncertain - requires manual testing");
        attackEncodingMap.put(TestResult.UNSUPPORTED, "unsupported by TLS-Scanner");

        ColorEncoding attacks = getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN);

        HashMap<AnalyzedProperty, ColorEncoding> colorMap = new HashMap<>();
        for (AnalyzedProperty prop : AnalyzedProperty.values()) {
            if (prop.getCategory() == AnalyzedPropertyCategory.ATTACKS) {
                colorMap.put(prop, attacks);
            }
        }
        colorMap.put(AnalyzedProperty.SUPPORTS_SSL_2, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_SSL_3, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_0, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_1, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_2, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_14, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_15, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_16, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_17, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_18, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_19, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_20, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_21, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_22, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_23, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_24, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_25, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_26, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_27, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_1_3_DRAFT_28, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_PFS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.SUPPORTS_NULL_CIPHERS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_FORTEZZA, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_EXPORT, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_ANON, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_DES, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_3DES, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_SEED, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_IDEA, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_RC2, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_RC4, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_CBC, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_AEAD, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.SUPPORTS_POST_QUANTUM, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_ONLY_PFS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_AES, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_CAMELLIA, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_ARIA, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_CHACHA, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_RSA, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_DH, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_ECDH, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_STATIC_ECDH, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_GOST, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_SRP, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_KERBEROS, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_PSK_PLAIN, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_PSK_RSA, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_PSK_DHE, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_PSK_ECDHE, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_NEWHOPE, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_ECMQV, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_STREAM_CIPHERS, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_BLOCK_CIPHERS, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_EXTENDED_MASTER_SECRET, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_ENCRYPT_THEN_MAC, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_TOKENBINDING, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_MONTGOMERY_CURVES, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_SESSION_TICKETS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_SESSION_IDS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_SESSION_TICKET_ROTATION_HINT, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_SECURE_RENEGOTIATION_EXTENSION, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.SUPPORTS_CLIENT_SIDE_SECURE_RENEGOTIATION, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_CLIENT_SIDE_INSECURE_RENEGOTIATION, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_FALLBACK_SCSV, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_TLS_COMPRESSION, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_COMMON_DH_PRIMES, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_ONLY_PRIME_MODULI, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.RED));
        colorMap.put(AnalyzedProperty.SUPPORTS_ONLY_SAFEPRIME_MODULI, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.SUPPORTS_INSECURE_RENEGOTIATION, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_RENEGOTIATION, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.SUPPORTS_HTTPS, getDefaultColorEncoding(AnsiColor.DEFAULT_COLOR, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_HSTS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_HSTS_PRELOADING, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_HPKP, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_HPKP_REPORTING, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.DEFAULT_COLOR));
        colorMap.put(AnalyzedProperty.SUPPORTS_HTTP_COMPRESSION, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.PREFERS_PFS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.ENFORCES_PFS, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.ENFOCRES_CS_ORDERING, getDefaultColorEncoding(AnsiColor.GREEN, AnsiColor.YELLOW));
        colorMap.put(AnalyzedProperty.HAS_VERSION_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_CIPHERSUITE_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_EXTENSION_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_CIPHERSUITE_LENGTH_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_COMPRESSION_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_ALPN_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_CLIENT_HELLO_LENGTH_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_EMPTY_LAST_EXTENSION_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_SIG_HASH_ALGORITHM_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_BIG_CLIENT_HELLO_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_NAMED_GROUP_INTOLERANCE, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_SECOND_CIPHERSUITE_BYTE_BUG, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.REFLECTS_OFFERED_CIPHERSUITES, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.IGNORES_OFFERED_CIPHERSUITES, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.IGNORES_OFFERED_NAMED_GROUPS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.IGNORES_OFFERED_SIG_HASH_ALGOS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));

        colorMap.put(AnalyzedProperty.MISSES_MAC_APPDATA_CHECKS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.MISSES_MAC_FINISHED_CHECKS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.MISSES_VERIFY_DATA_CHECKS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.MISSES_GCM_CHECKS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_CERTIFICATE_ISSUES, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.HAS_WEAK_RANDOMNESS, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.REUSES_EC_PUBLICKEY, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.REUSES_DH_PUBLICKEY, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.REUSES_GCM_NONCES, getDefaultColorEncoding(AnsiColor.RED, AnsiColor.GREEN));
        colorMap.put(AnalyzedProperty.REQUIRES_SNI, getDefaultColorEncoding(AnsiColor.YELLOW, AnsiColor.GREEN));

        HashMap<AnalyzedPropertyCategory, TextEncoding> textMap = new HashMap<>();
        textMap.put(AnalyzedPropertyCategory.ATTACKS, new TextEncoding(attackEncodingMap));
        PrintingScheme scheme = new PrintingScheme(colorMap, textMap, defaultTextEncoding, defaultColorEncoding, useColors);
        return scheme;
    }

    private static ColorEncoding getDefaultColorEncoding(AnsiColor trueColor, AnsiColor falseColor) {
        HashMap<TestResult, AnsiColor> colorMap = new HashMap<>();
        colorMap.put(TestResult.COULD_NOT_TEST, AnsiColor.BLUE);
        colorMap.put(TestResult.ERROR_DURING_TEST, AnsiColor.RED_BACKGROUND);
        colorMap.put(TestResult.FALSE, falseColor);
        colorMap.put(TestResult.NOT_TESTED_YET, AnsiColor.WHITE);
        colorMap.put(TestResult.TIMEOUT, AnsiColor.PURPLE_BACKGROUND);
        colorMap.put(TestResult.TRUE, trueColor);
        colorMap.put(TestResult.UNCERTAIN, AnsiColor.YELLOW_BACKGROUND);
        colorMap.put(TestResult.UNSUPPORTED, AnsiColor.CYAN);
        return new ColorEncoding(colorMap);
    }
}
