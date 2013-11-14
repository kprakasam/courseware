package edu.sjsu.courseware.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static List<Long> toLongs(String idsSeperatedByColon) {
        if (idsSeperatedByColon == null || idsSeperatedByColon.isEmpty())
            return Collections.emptyList();

        List<Long> ids = new ArrayList<Long>();
        
        for (String id : idsSeperatedByColon.split(":"))
            ids.add(Long.valueOf(id));
        
        return ids;
    }


}
