package com.example.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RankHangMan {
    private static final String url = "jdbc:mysql://localhost:3307/dictiondb";

    private static Connection con;
    private static Statement statement;

    public static String getListRank(){
        String listRank = "";
        try {
            con = DriverManager.getConnection(url, "root", "");
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery("select * from rank_hang_man order by score desc");
            ArrayList<String> lName = new ArrayList<>();
            ArrayList<String> lScore = new ArrayList<>();
            while(rs.next()){
                lName.add(rs.getString(1));
                lScore.add(rs.getString(2));
            }
            listRank = creatListRankHtml(lName, lScore);
        }
        catch (Exception e){
            System.out.println("Error read file bookmark!");
        }
        return listRank;
    }

    public static void add(String name, int sc){
        String addquery = "INSERT INTO `rank_hang_man` (`name`, `score`) VALUES ('" + name + "', '" + sc + "');";
        try {
            statement.executeUpdate(addquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll(){
        try {
            statement = con.createStatement();
            statement.executeUpdate("TRUNCATE rank_hang_man");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String creatListRankHtml(ArrayList<String> lName, ArrayList<String> lScore){
        StringBuilder res = new StringBuilder();
        res.append("<html lang=\"en\">");
        res.append("<body><table border=\"1\" width = \"582\" height = \"410\"><tr height = \"38\">");
        res.append("<th ><p style=\"font-size: 20px \">Name</p></th>");
        res.append("<th ><p style=\"font-size: 20px \">Score</p></th>");
        res.append("</tr><tr><th>");
        for(String name : lName){
            res.append("<p style=\"font-size: 20px \">" + name + "</p>");
        }
        res.append("</p></th><th>");
        for(String score : lScore){
            res.append("<p style=\"font-size: 20px \">" + score + "</p>");
        }
        res.append("</th></tr></table></body></html>");

        return res.toString();
    }
}
