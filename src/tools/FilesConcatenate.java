package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.Collection;
import java.util.Map;

public class FilesConcatenate {

    public static void concatenate(Collection<String> fileNames, Map<String, String> fileFullNames, String resFileName, String rootPath) {
        try (PrintWriter writer = new PrintWriter(resFileName)) {
            fileNames.forEach(f -> {
                String fname = fileFullNames.get(f);
                try {
                    if (fname == null) return;
                    String content = Files.readString(Paths.get(String.format("%s/%s", rootPath, fname)));
                    writer.println(content);
                } catch (NoSuchFileException | InvalidPathException e) {
                    System.out.printf("Couldn't open the file %s. %s.%n", fname, e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
