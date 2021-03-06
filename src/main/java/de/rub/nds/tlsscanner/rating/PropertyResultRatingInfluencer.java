/**
 * TLS-Scanner - A TLS configuration and analysis tool based on TLS-Attacker.
 *
 * Copyright 2017-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsscanner.rating;

import de.rub.nds.tlsscanner.report.AnalyzedProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"result", "influence", "scoreCap", "referencedProperty", "referencedPropertyResult"})
public class PropertyResultRatingInfluencer implements Comparable<PropertyResultRatingInfluencer> {

    private TestResult result;

    private Integer influence;

    private Integer scoreCap;

    private AnalyzedProperty referencedProperty;

    private TestResult referencedPropertyResult;

    public PropertyResultRatingInfluencer() {

    }

    public PropertyResultRatingInfluencer(TestResult result, Integer influence) {
        this.result = result;
        this.influence = influence;
    }

    public PropertyResultRatingInfluencer(TestResult result, AnalyzedProperty referencedProperty, TestResult referencedPropertyResult) {
        this.result = result;
        this.referencedProperty = referencedProperty;
        this.referencedPropertyResult = referencedPropertyResult;
    }

    public PropertyResultRatingInfluencer(TestResult result, Integer influence, Integer scoreCap) {
        this.result = result;
        this.influence = influence;
        this.scoreCap = scoreCap;
    }

    public TestResult getResult() {
        return result;
    }

    @XmlElement(required = false)
    public Integer getInfluence() {
        return influence;
    }

    @XmlElement(required = false)
    public Integer getScoreCap() {
        return scoreCap;
    }

    public boolean hasScoreCap() {
        return (scoreCap != null && scoreCap != 0);
    }

    public void setResult(TestResult result) {
        this.result = result;
    }

    public void setInfluence(Integer influence) {
        this.influence = influence;
    }

    public void setScoreCap(Integer scoreCap) {
        this.scoreCap = scoreCap;
    }

    @XmlElement(required = false)
    public AnalyzedProperty getReferencedProperty() {
        return referencedProperty;
    }

    public void setReferencedProperty(AnalyzedProperty referencedProperty) {
        this.referencedProperty = referencedProperty;
    }

    @XmlElement(required = false)
    public TestResult getReferencedPropertyResult() {
        return referencedPropertyResult;
    }

    public void setReferencedPropertyResult(TestResult referencedPropertyResult) {
        this.referencedPropertyResult = referencedPropertyResult;
    }

    public boolean isBadInfluence() {
        return (influence != null && influence < 0 || scoreCap != null);
    }

    @Override
    public int compareTo(PropertyResultRatingInfluencer t) {
        if (this.getScoreCap() == t.getScoreCap()) {
            return Integer.compare(this.getInfluence(), t.getInfluence());
        }
        if (this.getScoreCap() != null && t.getScoreCap() == null) {
            return -1;
        }
        if (t.getScoreCap() != null && this.getScoreCap() == null) {
            return 1;
        }
        return this.getScoreCap().compareTo(t.getScoreCap());
    }

}
