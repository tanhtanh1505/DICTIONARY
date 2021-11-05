package com.example.dictionary;

import com.example.GoogleAPI.Language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashSet;

public class BookMark {
    private static final String url = "jdbc:mysql://localhost:3307/dictiondb";

    private static Connection con;
    private static Statement statement;
    private static boolean isConnected = true;

    public static HashSet<String> getBookMark(){
        HashSet<String> listWord = new HashSet<>();
        try {
            con = DriverManager.getConnection(url, "root", "");
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery("select word from bookmark");
            while(rs.next()){
                listWord.add(rs.getString(1));
            }
        }
        catch (Exception e){
            isConnected = false;
            System.out.println("Exception in read bookmark!");
        }
        return listWord;
    }

    public static String getMean(String word){
        if(isConnected) {
            try {
                word = InitData.convertS(word);
                ResultSet rs = statement.executeQuery("select mean from bookmark where word = '" + word + "';");
                if (rs.next()) {
                    return rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void addWord(String word, String lang){
        String mean = TranslateOffline.textTranslate(lang, word);
        word = InitData.convertS(word);
        mean = InitData.convertS(mean);
        String addquery = "INSERT INTO `bookmark` (`word`, `mean`) VALUES ('" + word + "', '" + mean + "');";
        try {
            if (isConnected)
                statement.executeUpdate(addquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWord(String word){
        word = InitData.convertS(word);
        String deletequery = "DELETE FROM `bookmark` WHERE word = '"+word+"'";
        try {
            if(isConnected)
                statement.executeUpdate(deletequery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(){
        try {
            if(isConnected)
                statement.executeUpdate("TRUNCATE bookmark");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
