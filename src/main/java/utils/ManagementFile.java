package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ManagementFile {

    public static void readTextFileShow(String pathFile) throws Exception{
        List<String> lines = Files.readAllLines(Paths.get(pathFile));
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static List<String> readTextFile(String pathFile) throws Exception{
            List<String> lines = Files.readAllLines(Paths.get(pathFile));
        return lines;
    }

    public static void deleteFile(String pathFile) throws IOException {
        Files.delete(Paths.get(pathFile));
    }
}
