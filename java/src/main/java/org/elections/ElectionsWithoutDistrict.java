package org.elections;

import java.util.List;
import java.util.Map;

public class ElectionsWithoutDistrict extends Elections {
    public ElectionsWithoutDistrict(Map<String, List<String>> list, boolean withDistrict) {
        super(list, withDistrict);

    }
}
