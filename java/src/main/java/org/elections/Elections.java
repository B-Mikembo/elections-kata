package org.elections;

import java.util.*;

public abstract class Elections {
    protected List<String> candidates = new ArrayList<>();
    protected List<String> officialCandidates = new ArrayList<>();
    protected Map<String, List<String>> electorsByDistrict;

    protected Elections(Map<String, List<String>> electorsByDistrict) {
        this.electorsByDistrict = electorsByDistrict;
    }

    public static Elections createElections(Map<String, List<String>> electorsByDistrict, boolean withDistrict) {
        if (withDistrict) {
            return new ElectionsWithDistrict(electorsByDistrict);
        }
        return new ElectionsWithoutDistrict(electorsByDistrict);
    }

    public void addCandidate(String candidate) {
        officialCandidates.add(candidate);
        candidates.add(candidate);
        addVote();
    }

    protected abstract void addVote();

    public abstract Map<String, String> results();

    public abstract void voteFor(String elector, String candidate, String electorDistrict);

}
