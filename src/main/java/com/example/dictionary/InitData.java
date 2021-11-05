package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
            //System.out.println(EVkeys.size());
            //System.out.println(VEkeys.size());
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
                Tdata.put(rs.getString(2), rs.getString(3));
                Tkeys.add(rs.getString(2));
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

    public static boolean addAWord(String word, String mean, String lang){
        mean = "<html>" + mean + "</html>";
        if(lang.equals(Language.ENGLISH) || lang.equals("ENGLISH")){
            if(EVkeys.contains(word)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("This word is exist!");
                alert.setContentText("Do you want to edit it?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    editWord(word, mean, lang);
                }
                return false;
            }
            else {
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
        }
        else if(lang.equals(Language.VIETNAMESE) || lang.equals("VIETNAMESE")){
            if(EVkeys.contains(word)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("This word is exist!");
                alert.setContentText("Do you want to edit it?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    editWord(word, mean, lang);
                }
                return false;
            }
            else {
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
        return true;
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

    private static void addWordWithoutCheck(String word, String mean, String table){
        word = convertS(word);
        mean = convertS(mean);
        String addquery = "INSERT INTO `"+ table +"` (`word`, `mean`) VALUES ('" + word + "', '" + mean + "');";
        try {
            statement.executeUpdate(addquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void reset(String table){
        try {
            String path = ".\\data\\E_V.zip";
            if(table.equals("vedata")) {
                path = ".\\data\\V_E.zip";
            }
            FileInputStream file = new FileInputStream(path);
            ZipInputStream zipStream = new ZipInputStream(file);
            ZipEntry entry = zipStream.getNextEntry();

            BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream, "utf-8"));

            String line, word, meaning;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf("<html>");
                if (index != -1) {
                    word = line.substring(0, index);
                    word = word.trim();
                    meaning = line.substring(index);
                    addWordWithoutCheck(word, meaning, table);
                    wordsNum++;
                }
            }
            reader.close();
            System.out.println("Number word from " + path + ": " + wordsNum);

        } catch (Exception e) {
        }
    }

    public static void reset(boolean notification) {
        //true: hien thong bao
        //false: ko hien thong bao
        try {
            statement.executeUpdate("TRUNCATE `evdata`");
            statement.executeUpdate("TRUNCATE `vedata`");
            reset("evdata");
            reset("vedata");
            if(notification){
                Notifications.create()
                        .title("Successfully")
                        .text("Reset completely!").darkStyle()
                        .showInformation();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
