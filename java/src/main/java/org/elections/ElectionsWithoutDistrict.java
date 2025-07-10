package org.elections;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ElectionsWithoutDistrict extends Elections {
    public ElectionsWithoutDistrict(Map<String, List<String>> list, boolean withDistrict) {
        super(list, withDistrict);

    }

    @Override
    public void voteFor(String elector, String candidate, String electorDistrict) {
        if (candidates.contains(candidate)) {
            int index = candidates.indexOf(candidate);
            votesWithoutDistricts.set(index, votesWithoutDistricts.get(index) + 1);
        } else {
            candidates.add(candidate);
            votesWithoutDistricts.add(1);
        }
    }

    @Override
    public Map<String, String> results() {
        int nbVotes = votesWithoutDistricts.stream().reduce(0, Integer::sum);
        var results = totalForEachOfficialCandidate();

        float blankResult = ((float) totalBlankVote() * 100) / nbVotes;
        results.put("Blank", String.format(Locale.FRENCH, "%.2f%%", blankResult));

        float nullResult = ((float) totalNullVote() * 100) / nbVotes;
        results.put("Null", String.format(Locale.FRENCH, "%.2f%%", nullResult));

        int nbElectors = list.values().stream().map(List::size).reduce(0, Integer::sum);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float abstentionResult = 100 - ((float) nbVotes * 100 / nbElectors);
        results.put("Abstention", String.format(Locale.FRENCH, "%.2f%%", abstentionResult));

        return results;
    }

    private Integer totalNullVote() {
        var nullVotes = 0;
        for (int i = 0; i < votesWithoutDistricts.size(); i++) {
            String candidate = candidates.get(i);
            if (!officialCandidates.contains(candidate)) {
                if (!candidates.get(i).isEmpty()) {
                    nullVotes += votesWithoutDistricts.get(i);
                }
            }
        }
        return nullVotes;
    }

    private Integer totalBlankVote() {
        var blankVotes = 0;
        for (int i = 0; i < votesWithoutDistricts.size(); i++) {
            String candidate = candidates.get(i);
            if (!officialCandidates.contains(candidate)) {
                if (candidates.get(i).isEmpty()) {
                    blankVotes += votesWithoutDistricts.get(i);
                }
            }
        }
        return blankVotes;
    }

    private Map<String, String> totalForEachOfficialCandidate() {
        var results = new HashMap<String, String>();
        var nbValidVotes = totalValidVote();
        for (int i = 0; i < votesWithoutDistricts.size(); i++) {
            Float candidatResult = ((float) votesWithoutDistricts.get(i) * 100) / nbValidVotes;
            String candidate = candidates.get(i);
            if (officialCandidates.contains(candidate)) {
                results.put(candidate, String.format(Locale.FRENCH, "%.2f%%", candidatResult));
            }
        }
        return results;
    }

    private int totalValidVote() {
        var nbValidVotes = 0;
        for (String officialCandidate : officialCandidates) {
            int index = candidates.indexOf(officialCandidate);
            nbValidVotes += votesWithoutDistricts.get(index);
        }
        return nbValidVotes;
    }
}
