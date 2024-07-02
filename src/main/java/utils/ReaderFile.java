package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ReaderFile {
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
}
