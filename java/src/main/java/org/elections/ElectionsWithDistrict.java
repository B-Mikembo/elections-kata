package org.elections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElectionsWithDistrict extends Elections {

    public ElectionsWithDistrict(Map<String, List<String>> list, boolean withDistrict) {
        super(list, withDistrict);
    }

    @Override
    public void voteFor(String elector, String candidate, String electorDistrict) {
        if (votesWithDistricts.containsKey(electorDistrict)) {
            ArrayList<Integer> districtVotes = votesWithDistricts.get(electorDistrict);
            if (candidates.contains(candidate)) {
                int index = candidates.indexOf(candidate);
                districtVotes.set(index, districtVotes.get(index) + 1);
            } else {
                candidates.add(candidate);
                votesWithDistricts.forEach((district, votes) -> {
                    votes.add(0);
                });
                districtVotes.set(candidates.size() - 1, districtVotes.get(candidates.size() - 1) + 1);
            }
        }
    }
}
