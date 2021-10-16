package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InitData {
    private static HashMap<String, String> EVdata = new HashMap<>(); //tu - nghia
    private static ArrayList<String> EVkeys = new ArrayList<>(); //tu

    private static HashMap<String, String> VEdata = new HashMap<>();
    private static ArrayList<String> VEkeys = new ArrayList<>();

    public static void loadData(){
        load("./data/E_V/E_V.txt", EVdata, EVkeys);
        load("./data/V_E/V_E.txt", VEdata, VEkeys);
    }

    public static void loadZipFile(String path, HashMap<String, String> Tdata, ArrayList<String> Tkeys){
        try{
            FileInputStream file = new FileInputStream(path);
            ZipInputStream zipStream = new ZipInputStream(file);
            ZipEntry entry = zipStream.getNextEntry();

            BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream,"utf-8"));

            String line, word, meaning;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf("<html>");
                if (index != -1) {
                    word = line.substring(0, index);
                    word = word.trim();
                    Tkeys.add(word);
                    meaning = line.substring(index);
                    Tdata.put(word, meaning);
                    wordsNum++;
                }
            }
            reader.close();
            System.out.println("Number Words from " + path + " : " + wordsNum );
        }
        catch (Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.show();
        }

    }

    public static void load(String path, HashMap<String, String> Tdata, ArrayList<String> Tkeys) {
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line, word, meaning;
            int wordsNum = 0;
            while ((line = br.readLine()) != null) {
                int index = line.indexOf("<html>");
                if (index != -1) {
                    word = line.substring(0, index);
                    word = word.trim();
                    Tkeys.add(word);
                    meaning = line.substring(index);
                    Tdata.put(word, meaning);
                    wordsNum++;
                }
            }
            br.close();
            System.out.println("Number Words from " + path + " : " + wordsNum );
        }
        catch (Exception e){
            System.out.println("Error read file data!");
        }
    }

    public static HashMap<String, String> getHashMapEV(){
        return EVdata;
    }

    public static ArrayList<String> getKeyEV(){ return EVkeys; }

    public static HashMap<String, String> getHashMapVE(){
        return VEdata;
    }

    public static ArrayList<String> getKeyVE(){
        return VEkeys;
    }

    public static void addAWord(String word, String mean, String lang){
        mean = "<html>" + mean + "</html>";
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            EVdata.put(word, mean);
            EVkeys.add(word);
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.put(word, mean);
            VEkeys.add(word);
        }
        System.out.println("Added new word!");
    }

    public static void editWord(String word, String mean, String lang){
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            EVdata.replace(word, mean);
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.replace(word, mean);
        }
        System.out.println(word + " is edited!");
    }

    public static void removeWord(String word, String lang){
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            EVdata.remove(word);
            if(EVkeys.remove(word)){
                System.out.println("Removed!");
            }
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.remove(word);
            if(VEkeys.remove(word)){
                System.out.println("Removed!");
            }
        }
    }

    public static void SaveFile(String path, HashMap<String, String> TData){
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            for (Map.Entry<String, String> item : TData.entrySet()) {
                writer.println(item.getKey() + item.getValue());
            }
            writer.close();
            System.out.println("Data Saved!" + path);
        }
        catch (Exception e){
            System.out.println("Error save file data!");
        }
    }

    public static void Save(){
        SaveFile("./data/E_V/E_V.txt", EVdata);
        SaveFile("./data/V_E/V_E.txt", VEdata);
    }
}
