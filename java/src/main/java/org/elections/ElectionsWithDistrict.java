package org.elections;

import java.text.DecimalFormat;
import java.util.*;

public class ElectionsWithDistrict extends Elections {
    private final Map<String, ArrayList<Integer>> votesByDistrict;

    public ElectionsWithDistrict(Map<String, List<String>> list) {
        super(list);
        votesByDistrict = new HashMap<>();
        votesByDistrict.put("District 1", new ArrayList<>());
        votesByDistrict.put("District 2", new ArrayList<>());
        votesByDistrict.put("District 3", new ArrayList<>());
    }

    @Override
    protected void addVote() {
        votesByDistrict.get("District 1").add(0);
        votesByDistrict.get("District 2").add(0);
        votesByDistrict.get("District 3").add(0);
    }

    @Override
    public void voteFor(String elector, String candidate, String electorDistrict) {
        if (isElectorDistrictExisted(electorDistrict)) {
            voteForCandidate(candidate, electorDistrict);
        }
    }

    private void voteForCandidate(String candidate, String electorDistrict) {
        var currentDistrictVotes = votesByDistrict.get(electorDistrict);
        if (isCandidateExisted(candidate)) {
            voteForExistingCandidate(candidate, currentDistrictVotes);
        } else {
            voteForNotExistingCandidate(candidate, currentDistrictVotes);
        }
    }

    private void voteForNotExistingCandidate(String candidate, ArrayList<Integer> currentDistrictVotes) {
        candidates.add(candidate);
        votesByDistrict.forEach((district, votes) -> {
            votes.add(0);
        });
        currentDistrictVotes.set(candidates.size() - 1, currentDistrictVotes.get(candidates.size() - 1) + 1);
    }

    private void voteForExistingCandidate(String candidate, ArrayList<Integer> currentDistrictVotes) {
        int index = candidates.indexOf(candidate);
        currentDistrictVotes.set(index, currentDistrictVotes.get(index) + 1);
    }

    private boolean isCandidateExisted(String candidate) {
        return candidates.contains(candidate);
    }

    private boolean isElectorDistrictExisted(String electorDistrict) {
        return votesByDistrict.containsKey(electorDistrict);
    }

    @Override
    public Map<String, String> results() {
        Map<String, String> results = new HashMap<>();
        Integer nbVotes = 0;
        Integer nullVotes = 0;
        Integer blankVotes = 0;
        int nbValidVotes = 0;
        for (Map.Entry<String, ArrayList<Integer>> entry : votesByDistrict.entrySet()) {
            ArrayList<Integer> districtVotes = entry.getValue();
            nbVotes += districtVotes.stream().reduce(0, Integer::sum);
        }

        for (int i = 0; i < officialCandidates.size(); i++) {
            int index = candidates.indexOf(officialCandidates.get(i));
            for (Map.Entry<String, ArrayList<Integer>> entry : votesByDistrict.entrySet()) {
                ArrayList<Integer> districtVotes = entry.getValue();
                nbValidVotes += districtVotes.get(index);
            }
        }

        Map<String, Integer> officialCandidatesResult = new HashMap<>();
        for (int i = 0; i < officialCandidates.size(); i++) {
            officialCandidatesResult.put(candidates.get(i), 0);
        }
        for (Map.Entry<String, ArrayList<Integer>> entry : votesByDistrict.entrySet()) {
            ArrayList<Float> districtResult = new ArrayList<>();
            ArrayList<Integer> districtVotes = entry.getValue();
            for (int i = 0; i < districtVotes.size(); i++) {
                float candidateResult = 0;
                if (nbValidVotes != 0) candidateResult = ((float) districtVotes.get(i) * 100) / nbValidVotes;
                String candidate = candidates.get(i);
                if (officialCandidates.contains(candidate)) {
                    districtResult.add(candidateResult);
                } else {
                    if (candidates.get(i).isEmpty()) {
                        blankVotes += districtVotes.get(i);
                    } else {
                        nullVotes += districtVotes.get(i);
                    }
                }
            }
            int districtWinnerIndex = 0;
            for (int i = 1; i < districtResult.size(); i++) {
                if (districtResult.get(districtWinnerIndex) < districtResult.get(i)) districtWinnerIndex = i;
            }
            officialCandidatesResult.put(candidates.get(districtWinnerIndex), officialCandidatesResult.get(candidates.get(districtWinnerIndex)) + 1);
        }
        for (int i = 0; i < officialCandidatesResult.size(); i++) {
            Float ratioCandidate = ((float) officialCandidatesResult.get(candidates.get(i))) / officialCandidatesResult.size() * 100;
            results.put(candidates.get(i), String.format(Locale.FRENCH, "%.2f%%", ratioCandidate));
        }


        float blankResult = ((float) blankVotes * 100) / nbVotes;
        results.put("Blank", String.format(Locale.FRENCH, "%.2f%%", blankResult));

        float nullResult = ((float) nullVotes * 100) / nbVotes;
        results.put("Null", String.format(Locale.FRENCH, "%.2f%%", nullResult));

        int nbElectors = electorsByDistrict.values().stream().map(List::size).reduce(0, Integer::sum);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float abstentionResult = 100 - ((float) nbVotes * 100 / nbElectors);
        results.put("Abstention", String.format(Locale.FRENCH, "%.2f%%", abstentionResult));

        return results;
    }
}
