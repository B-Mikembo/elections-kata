package org.elections;

import java.util.*;

public abstract class Elections {
    protected List<String> candidates = new ArrayList<>();
    protected List<String> officialCandidates = new ArrayList<>();
    protected ArrayList<Integer> votesWithoutDistricts = new ArrayList<>();
    protected Map<String, ArrayList<Integer>> votesWithDistricts;
    protected Map<String, List<String>> list;
    protected boolean withDistrict;

    protected Elections(Map<String, List<String>> list, boolean withDistrict) {
        votesWithDistricts = new HashMap<>();
        this.list = list;
        this.withDistrict = withDistrict;
        votesWithDistricts.put("District 1", new ArrayList<>());
        votesWithDistricts.put("District 2", new ArrayList<>());
        votesWithDistricts.put("District 3", new ArrayList<>());
    }

    public static Elections createElections(Map<String, List<String>> list, boolean withDistrict) {
        if (withDistrict) {
            return new ElectionsWithDistrict(list, withDistrict);
        }
        return new ElectionsWithoutDistrict(list, withDistrict);
    }

    public void addCandidate(String candidate) {
        officialCandidates.add(candidate);
        candidates.add(candidate);
        votesWithoutDistricts.add(0);
        votesWithDistricts.get("District 1").add(0);
        votesWithDistricts.get("District 2").add(0);
        votesWithDistricts.get("District 3").add(0);
    }

    public abstract Map<String, String> results();

    public abstract void voteFor(String elector, String candidate, String electorDistrict);

}
