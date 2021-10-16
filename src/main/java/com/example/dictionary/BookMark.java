package com.example.dictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class BookMark {
    private static HashSet<String> listWord = new HashSet<>();
    private static final String fileName = "./data/BookMark.txt";

    public static void load() {
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                listWord.add(line);
            }
            br.close();
        }
        catch (Exception e){
            System.out.println("Error read file bookmark!");
        }
    }

    public static HashSet<String> getBookMark(){
        return listWord;
    }

    public static void addWord(String s){
        listWord.add(s);
    }

    public static void deleteWord(String s){
        listWord.remove(s);
    }

    public static void deleteAll(){
        listWord.clear();
    }

    public static void Save() {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for (String s : listWord) {
                writer.println(s);
            }
            writer.close();
            System.out.println("Bookmark Saved!");
        }
        catch (Exception e){
            System.out.println("Error save file bookmark!");
        }
    }
}
