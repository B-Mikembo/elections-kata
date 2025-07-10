package org.elections;

import java.util.List;
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
}
