package org.elections;

import java.util.*;

public abstract class Elections {
    protected List<String> candidates = new ArrayList<>();
    protected List<String> officialCandidates = new ArrayList<>();
    protected Map<String, List<String>> list;
    protected boolean withDistrict;

    protected Elections(Map<String, List<String>> list) {
        this.list = list;
    }

    public static Elections createElections(Map<String, List<String>> list, boolean withDistrict) {
        if (withDistrict) {
            return new ElectionsWithDistrict(list);
        }
        return new ElectionsWithoutDistrict(list);
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
