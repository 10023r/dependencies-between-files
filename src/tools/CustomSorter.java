package tools;

import java.io.File;
import java.util.*;

public class CustomSorter {

    private CustomSorter() {}

    public static List<String> sortByNames(List<String> list) {
        list.sort((a, b) -> {
            String name1 = a.substring(a.lastIndexOf(File.separator) + 1);
            String name2 = b.substring(b.lastIndexOf(File.separator) + 1);
            return name1.compareTo(name2);
        });
        return list;
    }

    public static Set<String> sortByIncludeOrder(List<String> list, Map<String, List<String>> dependencies) {
        Set<String> result = new LinkedHashSet<>();
        while (!list.isEmpty()) {
            String curFile = list.removeFirst();
            Set<String> order = new LinkedHashSet<>();
            dfs(curFile, order, dependencies, result);
        }
        return result;
    }

    private static void dfs(String cur, Set<String> order, Map<String, List<String>> deps, Set<String> result) {
        if (order.contains(cur)) {
            String cycle = String.format("%s->%s", String.join("->", order), cur);
            throw new RuntimeException("Found cycle: " + cycle);
        }

        order.add(cur);
        if (deps.get(cur) != null && !deps.get(cur).isEmpty()) {  // first condition for cases when pointing to non-existing file
            deps.get(cur)
                    .forEach(s -> dfs(s, order, deps, result));

        }
        order.remove(cur);
        result.add(cur);
    }
}
