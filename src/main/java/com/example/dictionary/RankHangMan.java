package com.example.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class RankHangMan {
    private static ArrayList<String> listName= new ArrayList<>();
    private static ArrayList<Integer> listScore = new ArrayList<>();
    private static HashSet<String> listRank = new HashSet<>();

    private static final String fileName = ".\\data\\Rank.txt";

    public static void load() {
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                int index = line.indexOf("-");
                if (index != -1) {
                    listName.add(line.substring(0, index));
                    listScore.add(Integer.valueOf(line.substring(index + 1)));
                }
            }
            br.close();

            for(int i = 0; i < listName.size()-1; i++){
                for(int j = i+1; j < listName.size(); j++){
                    if(listScore.get(i) < listScore.get(j)){
                        int swap = listScore.get(i); listScore.set(i, listScore.get(j)); listScore.set(j, swap);
                        String swapn = listName.get(i); listName.set(i, listName.get(j)); listName.set(j, swapn);
                    }
                }
            }

            for(int i = 0; i < listName.size(); i++){
                String space = String.format("%10s %90s", listName.get(i), listScore.get(i));;
                listRank.add(space);
            }
        }
        catch (Exception e){
            System.out.println("Error read file rank!");
        }
    }

    public static HashSet<String> getListRank(){
        return listRank;
    }

    public static void add(String name, int sc){
        listName.add(name);
        listScore.add(sc);
    }

    public static void Save() {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for (int i = 0; i < listName.size(); i++) {
                writer.println(listName.get(i) + "-" + listScore.get(i));
            }
            writer.close();
            //System.out.println("Rank Saved!");
        }
        catch (Exception e){
            System.out.println("Error save file rank!");
        }
    }

    public static void deleteAll(){
        listName.clear();
        listScore.clear();
        listRank.clear();
    }
}
