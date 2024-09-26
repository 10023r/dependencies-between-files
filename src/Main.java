import tools.CustomSorter;
import tools.FileVisitorImpl;
import tools.FilesConcatenate;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String rootPath = "./src";
        Path rootDir = Paths.get(rootPath);

        FileVisitorImpl fileVisitor = new FileVisitorImpl(rootPath);
        Files.walkFileTree(rootDir, fileVisitor);

        Map<String, List<String>> dependencies = fileVisitor.getDependencies();
        List<String> files = CustomSorter.sortByNames(fileVisitor.getFiles());
        Set<String> sorted = CustomSorter.sortByIncludeOrder(files, dependencies);
        FilesConcatenate.concatenate(sorted, fileVisitor.getFilesFullNames(), "result.txt", rootPath);

    }
}

