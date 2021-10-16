package com.example.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class History {
    private static ArrayList<String> listHistory = new ArrayList<>();
    private static final String fileName = "./data/History.txt";

    public static void load() {
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                listHistory.add(line);
            }
            br.close();
        }
        catch (Exception e){
            System.out.println("Error read file history!");
        }
    }

    public static ArrayList<String> getListHistory(){return listHistory;}

    public static void add(String s){
        if(s.length() > 1)
            listHistory.add(s);
    }

    public static void deleteAll(){
        listHistory.clear();
    }

    public static void Save() {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for (String s : listHistory) {
                writer.println(s);
            }
            writer.close();
            System.out.println("History Saved!");
        }
        catch (Exception e){
            System.out.println("Error save file history!");
        }
    }
}
