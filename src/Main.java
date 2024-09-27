import tools.CustomSorter;
import tools.FileVisitorImpl;
import tools.FilesConcatenate;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String rootPath = ".";
        if (args.length == 1) {
            rootPath = args[0];
        }
        Path rootDir = Paths.get(rootPath);

        FileVisitorImpl fileVisitor = new FileVisitorImpl(rootPath);
        Files.walkFileTree(rootDir, fileVisitor);

        Map<String, List<String>> dependencies = fileVisitor.getDependencies();
        List<String> sortedByNames = CustomSorter.sortByNames(fileVisitor.getFiles());
        FilesConcatenate.concatenate(sortedByNames, fileVisitor.getFilesFullNames(), "result1.txt", rootPath);
        Set<String> sortedByIncludeOrder = CustomSorter.sortByIncludeOrder(sortedByNames, dependencies);
        FilesConcatenate.concatenate(sortedByIncludeOrder, fileVisitor.getFilesFullNames(), "result2.txt", rootPath);

    }
}

