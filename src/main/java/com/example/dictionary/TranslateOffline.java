package com.example.dictionary;

import com.example.GoogleAPI.Language;

import java.util.*;

public class TranslateOffline {
    private static HashMap<String, String> EVdata = InitData.getHashMapEV();
    private static HashMap<String, String> VEdata = InitData.getHashMapVE();
    private static ArrayList<String> EVlistKey = InitData.getKeyEV();
    private static ArrayList<String> VElistKey = InitData.getKeyVE();

    public static String textTranslate(String langFrom, String str) {
        str = str.toLowerCase().trim();
        if(langFrom.equals(Language.ENGLISH) || langFrom.equals("ENGLISH")) return EVdata.get(str);
        else if(langFrom.equals(Language.VIETNAMESE) || langFrom.equals("VIETNAMESE")) return VEdata.get(str);

        return "Invalid";
    }

    public static Set<String> listWordSuggestE(String word, String langFrom){
        word = word.toLowerCase().trim();
        Set<String> result = new HashSet<>();
        if(langFrom.equals(Language.ENGLISH) || langFrom.equals("ENGLISH")) {
            for (String s : EVlistKey) {
                if (s.startsWith(word)) {
                    result.add(s);
                }
            }
        }
        else {
            for (String s : VElistKey) {
                if (s.startsWith(word)) {
                    result.add(s);
                }
            }
        }
        return result;
    }
}
