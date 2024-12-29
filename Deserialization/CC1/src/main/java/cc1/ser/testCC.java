package cc1.ser;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class testCC {
    public static void main(String[] args) throws Exception {
        List<String> list1 = Arrays.asList("A", "B", "C", "D");
        List<String> list2 = Arrays.asList("C", "D", "E", "F");

        List<String> intersection = (List<String>) CollectionUtils.intersection(list1, list2);
        System.out.println(intersection);
    }
}
