package edu.sjsu.courseware.util;

import java.util.Comparator;

public class CaseInsensitiveComparator implements Comparator<String> {
    public static final CaseInsensitiveComparator INSTANCE = new CaseInsensitiveComparator(); 
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }
}
