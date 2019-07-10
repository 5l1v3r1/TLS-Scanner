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
import java.util.LinkedHashMap;

public class ScoreReport {

    private final double score;

    private final LinkedHashMap<AnalyzedProperty, PropertyRatingInfluencer> influencers;

    public ScoreReport(double score, LinkedHashMap<AnalyzedProperty, PropertyRatingInfluencer> influencers) {
        this.score = score;
        this.influencers = influencers;
    }

    public double getScore() {
        return score;
    }

    public LinkedHashMap<AnalyzedProperty, PropertyRatingInfluencer> getInfluencers() {
        return influencers;
    }
}
