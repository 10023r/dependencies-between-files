package tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileVisitorImpl implements FileVisitor<Path> {

    private final String rootPath;
    private final String regex = "require '[\\w\\s-/\\\\]+'";;
    private final Pattern pattern = Pattern.compile(regex);
    private final Map<String, List<String>> dependencies = new HashMap<>();

    private final List<String> files = new ArrayList<>();

    private final Map<String, String> filesFullNames = new HashMap<>();

    public Map<String, String> getFilesFullNames() {
        return filesFullNames;
    }

    public List<String> getFiles() {
        return this.files;
    }

    public Map<String, List<String>> getDependencies() {
        return dependencies;
    }

    public FileVisitorImpl(String rootPath) {
        Path rootDir = Paths.get(rootPath);
        this.rootPath = rootDir.toAbsolutePath().toString();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!"text/plain".equals(Files.probeContentType(file))) {
            return FileVisitResult.CONTINUE;
        }
        String absPath = String.valueOf(file.toAbsolutePath());
        String fileRelativeNameWithExt = absPath.substring(rootPath.length() + 1);
        String fileRelativeName = fileRelativeNameWithExt.substring(0, fileRelativeNameWithExt.lastIndexOf("."));
        files.add(fileRelativeName);
        filesFullNames.put(fileRelativeName, fileRelativeNameWithExt);
        String content = Files.readString(file);
        Matcher matcher = pattern.matcher(content);
        dependencies.put(fileRelativeName, new LinkedList<>());
        String dep;
        while (matcher.find()) {
            dep = content.substring(matcher.start() + 9, matcher.end() - 1);  // removing "require '" and "'"
            dep = dep.replace('/', File.separatorChar);
            dep = dep.replace('\\', File.separatorChar);
            dependencies
                    .get(fileRelativeName)
                    .add(dep);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}