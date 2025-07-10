package org.elections;

import java.text.DecimalFormat;
import java.util.*;

public class ElectionsWithoutDistrict extends Elections {
    private final List<Integer> votes;

    public ElectionsWithoutDistrict(Map<String, List<String>> list) {
        super(list);
        votes = new ArrayList<>();
    }

    @Override
    protected void addVote() {
        votes.add(0);
    }

    @Override
    public void voteFor(String elector, String candidate, String electorDistrict) {
        if (candidates.contains(candidate)) {
            int index = candidates.indexOf(candidate);
            votes.set(index, votes.get(index) + 1);
        } else {
            candidates.add(candidate);
            votes.add(1);
        }
    }

    @Override
    public Map<String, String> results() {
        int nbVotes = votes.stream().reduce(0, Integer::sum);
        var resultByOfficialCandidate = totalForEachOfficialCandidate();

        float blankResult = ((float) totalBlankVote() * 100) / nbVotes;
        resultByOfficialCandidate.put("Blank", String.format(Locale.FRENCH, "%.2f%%", blankResult));

        float nullResult = ((float) totalNullVote() * 100) / nbVotes;
        resultByOfficialCandidate.put("Null", String.format(Locale.FRENCH, "%.2f%%", nullResult));

        int nbElectors = electorsByDistrict.values().stream().map(List::size).reduce(0, Integer::sum);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float abstentionResult = 100 - ((float) nbVotes * 100 / nbElectors);
        resultByOfficialCandidate.put("Abstention", String.format(Locale.FRENCH, "%.2f%%", abstentionResult));

        return resultByOfficialCandidate;
    }

    private Integer totalNullVote() {
        var nullVotes = 0;
        for (int i = 0; i < votes.size(); i++) {
            var currentCandidate = candidates.get(i);
            if (isNullVote(currentCandidate, i)) {
                nullVotes += votes.get(i);
            }
        }
        return nullVotes;
    }

    private boolean isNullVote(String candidate, int i) {
        return !officialCandidates.contains(candidate) && !candidates.get(i).isEmpty();
    }

    private Integer totalBlankVote() {
        var blankVotes = 0;
        for (int i = 0; i < votes.size(); i++) {
            var currentCandidate = candidates.get(i);
            if (isBlankVote(currentCandidate, i)) {
                blankVotes += votes.get(i);
            }
        }
        return blankVotes;
    }

    private boolean isBlankVote(String candidate, int i) {
        return !officialCandidates.contains(candidate) && candidates.get(i).isEmpty();
    }

    private Map<String, String> totalForEachOfficialCandidate() {
        var results = new HashMap<String, String>();
        var nbValidVotes = totalValidVote();
        for (int i = 0; i < votes.size(); i++) {
            var candidateResult = ((float) votes.get(i) * 100) / nbValidVotes;
            var candidate = candidates.get(i);
            if (officialCandidates.contains(candidate)) {
                results.put(candidate, String.format(Locale.FRENCH, "%.2f%%", candidateResult));
            }
        }
        return results;
    }

    private int totalValidVote() {
        var nbValidVotes = 0;
        for (String officialCandidate : officialCandidates) {
            int index = candidates.indexOf(officialCandidate);
            nbValidVotes += votes.get(index);
        }
        return nbValidVotes;
    }
}
