package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.*;
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

    private static final String url = "jdbc:mysql://localhost:3307/dictiondb";

    private static Connection con;
    private static Statement statement;

    public static void loadData(){
        try {
            con = DriverManager.getConnection(url, "root", "");
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery("select * from evdata");
            loadFromDatabase(rs, EVdata, EVkeys);

            rs = statement.executeQuery("select * from vedata");
            loadFromDatabase(rs, VEdata, VEkeys);
            System.out.println("Connect success to " + con.getCatalog());
            System.out.println(EVkeys.size());
            System.out.println(VEkeys.size());
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Can't connect");
            loadZipFile(".\\data\\E_V.zip", EVdata, EVkeys);
            loadZipFile(".\\data\\V_E.zip", VEdata, VEkeys);
        }
    }

    public static void loadFromDatabase(ResultSet rs, HashMap<String, String> Tdata, ArrayList<String> Tkeys){
        try {
            while (rs.next()) {
                Tdata.put(rs.getString(1), rs.getString(2));
                Tkeys.add(rs.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

    public static String convertS(String s){
        String str = s;
        str = str.replace("\\", "\\\\");
        str = str.replace("'", "\\'");
        str = str.replace("\0", "\\0");
        str = str.replace("\n", "\\n");
        str = str.replace("\r", "\\r");
        str = str.replace("\"", "\\\"");
        str = str.replace("\\x1a", "\\Z");
        return str;
    }

    public static void addAWord(String word, String mean, String lang){
        mean = "<html>" + mean + "</html>";
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            EVdata.put(word, mean);
            EVkeys.add(word);

            word = convertS(word);
            mean = convertS(mean);
            String addquery = "INSERT INTO `evdata` (`word`, `mean`) VALUES ('" + word + "', '" + mean + "');";
            try {
                statement.executeUpdate(addquery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.put(word, mean);
            VEkeys.add(word);

            word = convertS(word);
            mean = convertS(mean);
            String addquery = "INSERT INTO `vedata` (`word`, `mean`) VALUES ('" + word + "', '" + mean + "');";
            try {
                statement.executeUpdate(addquery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void editWord(String word, String mean, String lang){
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            EVdata.replace(word, mean);

            word = convertS(word);
            mean = convertS(mean);
            String editquery = "UPDATE `evdata` set `mean`='" + mean + "' WHERE word = '" + word + "'";
            try {
                statement.executeUpdate(editquery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.replace(word, mean);

            word = convertS(word);
            mean = convertS(mean);
            String editquery = "UPDATE `vedata` set `mean`='" + mean + "' WHERE word = '" + word + "'";
            try {
                statement.executeUpdate(editquery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(word + " is edited!");
    }

    public static void removeWord(String word, String lang){
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            EVdata.remove(word);
            EVkeys.remove(word);

            word = convertS(word);
            String deletequery = "DELETE FROM `evdata` WHERE word = '"+word+"'";
            try {
                statement.executeUpdate(deletequery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            VEdata.remove(word);
            VEkeys.remove(word);

            word = convertS(word);
            String deletequery = "DELETE FROM `vedata` WHERE word = '"+word+"'";
            try {
                statement.executeUpdate(deletequery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("Error remove word!!");
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
//        SaveFile(".\\data\\E_V.zip", EVdata);
//        SaveFile(".\\data\\V_E.zip", VEdata);
    }

    public static void reset(boolean showAlert){
        //true: hien thong bao
        //false: ko hien thong bao

        HashMap<String, String> Rdata = new HashMap<>();
        ArrayList<String> Rkeys = new ArrayList<>();

        //Reset EVData
        loadZipFile(".\\data\\Back up\\E_V.zip", Rdata, Rkeys);

        for(String word : EVkeys){
            //check added word
            if(!Rkeys.contains(word)){
                removeWord(word, Language.ENGLISH);
            }
        }

        for(String word : Rkeys){
            //check deleted word
            if(!EVkeys.contains(word)){
                addAWord(word, Rdata.get(word), Language.ENGLISH);
            }
            else {
                //check edited word
                if(!EVdata.get(word).equals(Rdata.get(word))){
                    editWord(word, Rdata.get(word), Language.ENGLISH);
                }
            }
        }

        Rdata.clear();
        Rkeys.clear();
        //Reset VEData
        loadZipFile(".\\data\\Back up\\V_E.zip", Rdata, Rkeys);

        for(String word : VEkeys){
            //check added word
            if(!Rkeys.contains(word)){
                removeWord(word, Language.VIETNAMESE);
            }
        }

        for(String word : Rkeys){
            //check deleted word
            if(!VEkeys.contains(word)){
                addAWord(word, Rdata.get(word), Language.VIETNAMESE);
            }
            else {
                //check edited word
                if(!VEdata.get(word).equals(Rdata.get(word))){
                    editWord(word, Rdata.get(word), Language.VIETNAMESE);
                }
            }
        }

        System.out.println("Complete");
        if(showAlert){

        }
    }
}
