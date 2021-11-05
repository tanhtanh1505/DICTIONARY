package com.example.dictionary;

import java.sql.*;
import java.util.ArrayList;

public class History {
    private static final String url = "jdbc:mysql://localhost:3307/dictiondb";

    private static Connection con;
    private static Statement statement;
    private static boolean isConnected = true;

    public static ArrayList<String> getListHistory(){
        ArrayList<String> listHistory = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, "root", "");
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery("select word from history");
            while(rs.next()){
                listHistory.add(rs.getString(1));
            }
        }
        catch (Exception e){
            System.out.println("Exception in read history!");
            isConnected = false;
        }
        return listHistory;
    }

    public static void add(String word){
        if(word.length() > 1 && isConnected){
            word = InitData.convertS(word);
            String addquery = "INSERT INTO `history` (`word`) VALUES ('" + word + "');";
            try {
                statement.executeUpdate(addquery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteAll(){
        try {
            if(isConnected)
                statement.executeUpdate("TRUNCATE history");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
