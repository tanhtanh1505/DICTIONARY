package com.example.dictionary;

import java.util.Random;

public class GameHangMan {
    private final int MAX_BAD_GUESSES = 7;
    private int numFails = 0;
    private String answer;
    private String gWord = "";
    private int score = 100;
    private int len = 0;

    public GameHangMan() {
    }

    public void init() {
        Random rd = new Random();
        answer = InitData.getKeyEV().get(rd.nextInt(InitData.getKeyEV().size() - 1));
        for (int i = 0; i < answer.length(); i++)
            gWord = gWord + "_ ";
        System.out.println(answer);
    }

    public void check(char c) {
        boolean ok = false;
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == c && gWord.charAt(i * 2) != c) {
                gWord = gWord.substring(0, i * 2) + c + gWord.substring(i * 2 + 1);
                score += 100;
                len++;
                ok = true;
            }
        }

        if (!ok) {
            numFails++;
        }
    }

    public String getGuessedWord() {
        return gWord;
    }

    public int getNumberHeart() {
        return MAX_BAD_GUESSES - numFails;
    }

    public boolean getStatus() {
        return numFails < MAX_BAD_GUESSES;
    }

    public int getScore() {
        return score;
    }

    public boolean getWin(){return len == answer.length();}

    public void getSuggestWord(){
        if(score >= 100) {
            for (int i = 0; i < answer.length(); i++) {
                if (answer.charAt(i) != gWord.charAt(i * 2)) {
                    score -= 200;
                    check(answer.charAt(i));
                    return;
                }
            }
        }
    }
}
