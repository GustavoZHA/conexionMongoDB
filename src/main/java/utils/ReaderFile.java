package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ReaderFile {
    public static void readTextFileShow(String pathFile) throws Exception{
        File file = new File(pathFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        while ((st = br.readLine()) != null)
            System.out.println(st);
    }

    public static List<String> readTextFile(String pathFile) throws Exception{
        File file = new File(pathFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> listResult = new ArrayList<>();

        while (br.readLine() != null) listResult.add(br.readLine());

        return listResult;
    }
}
