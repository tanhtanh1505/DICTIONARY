package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class InitData {
    private static HashMap<String, String> EVdata = new HashMap<>(); //tu - nghia
    private static ArrayList<String> EVkeys = new ArrayList<>(); //tu

    private static HashMap<String, String> VEdata = new HashMap<>();
    private static ArrayList<String> VEkeys = new ArrayList<>();

    public static void loadData(){
        loadZipFile(".\\data\\E_V.zip", EVdata, EVkeys);
        loadZipFile(".\\data\\V_E.zip", VEdata, VEkeys);
    }

    public static boolean loadZipFile(String path, HashMap<String, String> Tdata, ArrayList<String> Tkeys){
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

            return true;
        }
        catch (Exception e){
            return false;
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
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setHeaderText("Add word successfully!");
        al.setContentText("You added new word: '" + word.toUpperCase() + "' into " + lang + " database");
        al.show();
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
            EVkeys.remove(word);
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.remove(word);
            VEkeys.remove(word);
        }
        else{
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("Error save file!!");
            al.show();
        }
    }

    public static void SaveFile(String path, HashMap<String, String> TData){
        try {
            FileOutputStream file = new FileOutputStream(path);
            ZipOutputStream zipStream = new ZipOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zipStream,"utf-8"));
            zipStream.putNextEntry(new ZipEntry("E_V.txt"));

            for (Map.Entry<String, String> item : TData.entrySet()) {
                writer.write(item.getKey());
                writer.write(item.getValue());
                writer.newLine();
            }

            writer.close();
        }
        catch (Exception e){
            System.out.println("Error save file data!");
        }
    }

    public static void Save(){
        SaveFile(".\\data\\E_V.zip", EVdata);
        SaveFile(".\\data\\V_E.zip", VEdata);
    }

    public static void reset(boolean showAlert){
        //true: hien thong bao
        //false: ko hien thong bao
        EVdata.clear();
        VEdata.clear();
        EVkeys.clear();
        VEkeys.clear();
        if(loadZipFile(".\\data\\Back up\\E_V.zip", EVdata, EVkeys)
        && loadZipFile(".\\data\\Back up\\V_E.zip", VEdata, VEkeys)){
            if(showAlert){
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setHeaderText("RESET SUCCESS!!");
                al.setContentText("All data is restore");
                al.show();
            }
        }
        else {
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("RESET FAILL!!");
            al.setContentText("Something went wrong :(");
            al.show();
        }
    }
}
