/**
 * TLS-Scanner - A TLS Configuration Analysistool based on TLS-Attacker
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsscanner.probe.certificate;

import de.rub.nds.tlsattacker.core.constants.HashAlgorithm;
import de.rub.nds.tlsattacker.core.constants.SignatureAlgorithm;
import de.rub.nds.tlsscanner.config.ScannerConfig;
import de.rub.nds.tlsscanner.report.check.CheckType;
import de.rub.nds.tlsscanner.report.check.TLSCheck;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.bouncycastle.asn1.x509.Certificate;


/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
public class CertificateJudger {

    private final Certificate certificate;
    private final String domainName;
    private final CertificateReport report;
    private final ScannerConfig config;

    public CertificateJudger(Certificate certificate, ScannerConfig config, CertificateReport report) {
        this.certificate = certificate;
        this.config = config;
        this.domainName = config.createConfig().getSniHostname();
        this.report = report;
    }

    public List<TLSCheck> getChecks() {
        List<TLSCheck> tlsCheckList = new LinkedList<>();
        boolean receivedCertificate = (certificate != null);
        tlsCheckList
                .add(new TLSCheck(!receivedCertificate, CheckType.CERTIFICATE_SENT_BY_SERVER));
        if (!receivedCertificate) {
            return tlsCheckList;
        }
        // tlsCheckList.add(checkCertificateRevoked());
        tlsCheckList.add(checkExpired());
        tlsCheckList.add(checkNotYetValid());
        tlsCheckList.add(checkHashAlgorithm());
        tlsCheckList.add(checkSignAlgorithm());
        // tlsCheckList.add(checkDomainNameMatch());
        // tlsCheckList.add(checkCertificateTrusted());
        // tlsCheckList.add(checkSelfSigned());
        // tlsCheckList.add(checkBlacklistedKey());

        return tlsCheckList;
    }

    public TLSCheck checkExpired() {
        boolean result = isCertificateExpired(report);
        return new TLSCheck(result, CheckType.CERTIFICATE_EXPIRED);
    }

    public TLSCheck checkNotYetValid() {
        boolean result = isCertificateValidYet(report);
        return new TLSCheck(result, CheckType.CERTIFICATE_NOT_VALID_YET);
    }

    public TLSCheck checkCertificateRevoked() {
        boolean result = isRevoked(certificate);
        return new TLSCheck(result, CheckType.CERTIFICATE_REVOKED);
    }

    private TLSCheck checkHashAlgorithm() {
        boolean result = isWeakHashAlgo(report);
        return new TLSCheck(result, CheckType.CERTIFICATE_WEAK_HASH_FUNCTION);
    }

    private TLSCheck checkSignAlgorithm() {
        boolean result = isWeakSigAlgo(report);
        return new TLSCheck(result, CheckType.CERTIFICATE_WEAK_SIGN_ALGORITHM);
    }

    public boolean isWeakHashAlgo(CertificateReport report) {
        HashAlgorithm algo = report.getSignatureAndHashAlgorithm().getHashAlgorithm();
        return algo == HashAlgorithm.MD5 || algo == HashAlgorithm.NONE || algo == HashAlgorithm.SHA1;
    }

    public boolean isWeakSigAlgo(CertificateReport report) {
        SignatureAlgorithm algo = report.getSignatureAndHashAlgorithm().getSignatureAlgorithm();
        return algo == SignatureAlgorithm.ANONYMOUS; // TODO is this weak?
    }

    public boolean isWeakKey(CertificateReport report) {
        return report.getWeakDebianKey() == Boolean.TRUE;
    }

    public boolean isCertificateExpired(CertificateReport report) {
        return !report.getValidTo().after(new Date(System.currentTimeMillis()));
    }

    public boolean isCertificateValidYet(CertificateReport report) {
        return !report.getValidFrom().before(new Date(System.currentTimeMillis()));
    }

    public boolean isRevoked(Certificate certificate) {
        // TODO
        return false;
    }

    public boolean domainNameDoesNotMatch(Certificate certificate, String domainName) {
        // TODO
        return false;
    }

    private boolean isNotTrusted(Certificate certificate) {
        // TODO
        return false;
    }

    private boolean isSelfSigned(Certificate certificate) {
        return false;

    }

    private TLSCheck checkDomainNameMatch() {
        // if (domainNameDoesNotMatch(certificate, domainName)) {
        // tlsCheckList.add(new ConfigurationFlaw("Domain nicht zulässig",
        // FlawLevel.FATAL,
        // "Das eingesetzte Zertifikat ist für die gescannte Domain nicht gültig.",
        // "Beantrage sie ein neues Zertifikat welches ebenfalls für die Domain "
        // + domainName
        // + " gültig ist."));
        // }
        return null;
    }

    private TLSCheck checkCertificateTrusted() {
        // if (isNotTrusted(certificate)) {
        // tlsCheckList.add(new
        // ConfigurationFlaw("Zertifikat nicht vertrauenswürdig",
        // FlawLevel.FATAL,
        // "Dem Eingesetzten Zertifikat wird nicht vertraut",
        // "Beantrage sie ein neues Zertifikat welchem Vertraut werden kann."));
        // }
        return null;
    }

    private TLSCheck checkSelfSigned() {
        // if (isSelfSigned(certificate)) {
        // tlsCheckList
        // .add(new ConfigurationFlaw(
        // "Zertifikat ist selbst signiert",
        // FlawLevel.FATAL,
        // "Das eingesetzte Zertifikat legitimiert sich selbst. Besucher ihrer Seite können die Validität dieses Zertifikats nicht überprüfen.",
        // "Beantragen sie ein Zertifikat bei einer vertrauenswürdigen Zertifizierungsstelle."));
        // }
        return null;
    }

    private TLSCheck checkBlacklistedKey() {
        // if (isWeakKey(report)) {
        // tlsCheckList.add(new ConfigurationFlaw(domainName, FlawLevel.FATAL,
        // domainName, domainName));
        // }
        return null;
    }
}
