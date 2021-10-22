package com.example.dictionary;

import java.util.*;

public class GameHangManAIGuess {
    private int wordLength;
    private String secretWord = "";
    private int incorrectGuess = 0;
    private HashSet<Character> previousGuesses = new HashSet<Character>();
    private boolean stop;
    private ArrayList<String> wordList;
    private final int MAX_GUESSES = 10;
    private HashSet<Character> remainingChars = new HashSet<>();
    private int status = 0; //1: ko con tu de doan, 2: doan dung, 3: sai qua nhieu

    public void initialize(int len)
    {
//        InitData.loadData();
        wordLength = len;
        wordList = InitData.getKeyEV();
        for(int i = 0; i < wordLength; i++){
            secretWord += "-";
        }
        incorrectGuess = 0;
        stop = false;

        for (char c = 'a'; c <= 'z'; c++)
            remainingChars.add(c);
    }

    public void render()
    {
        System.out.println("\nIncorrect guess = " + incorrectGuess
                + "   previous guesses = ");
        for (char c : previousGuesses)
            System.out.print(c);
        System.out.println("   secretWord = " + secretWord);
    }

    public boolean isAllDash(String s)
    {
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) != '-') return false;
        return true;
    }

    public char getVowelGuess()
    {
        char[] vowel = {'e', 'a', 'o', 'i', 'u'};
        for (char c : vowel) {
            if (remainingChars.contains(c)){
                remainingChars.remove(c);
                return c;
            }
        }
        return 0;
    }

    public boolean isSuitableWord(String word)
    {
        if (word.length() != wordLength) return false;
        for (int i = 0; i < word.length(); i++) {
            if (secretWord.charAt(i) != '-') {
                if (word.charAt(i) != secretWord.charAt(i)) return false;
            }
            else if (!remainingChars.contains(word.charAt(i)))
                return false;
        }
        return true;
    }

    public ArrayList<String> getSuitableWords()
    {
        ArrayList<String> result = new ArrayList<>();
        for (String word : wordList)
            if (isSuitableWord(word))
                result.add(word);
        return result;
    }

    public HashMap<Character, Integer> getOccurenceCount(ArrayList<String> wordList)
    {
        HashMap<Character, Integer> count = new HashMap<>();
        for (char c: remainingChars) count.put(c, 0);
        for (String word : wordList) {
            for (int i = 0; i < word.length(); i++)
                if (count.containsKey(word.charAt(i)))
                    count.replace(word.charAt(i), count.get(word.charAt(i)) + 1);
        }
        return count;
    }

    public char getMaxOccurenceChar(HashMap<Character, Integer> count)
    {
        char best = 0;
        int best_count = 0;
        for (Map.Entry<Character, Integer> entry : count.entrySet())
            if (entry.getValue() > best_count) {
                best = entry.getKey();
                best_count = entry.getValue();
            }
        remainingChars.remove(best);
        return best;
    }

    public char getNextGuess()
    {
        if (remainingChars.size() == 0){
            status = 1;
        }

        if (isAllDash(secretWord))
            return getVowelGuess();

        ArrayList<String> filteredWordList = getSuitableWords();
        HashMap<Character, Integer> occurenceCount = getOccurenceCount(filteredWordList);
        return getMaxOccurenceChar(occurenceCount);
    }

    public void updateSecretWord(String mask)
    {
        for (int i = 0; i < secretWord.length(); i++)
            if (mask.charAt(i) != '-')
                secretWord = secretWord.substring(0, i) + mask.charAt(i) + secretWord.substring(i + 1);
    }

    public boolean isAllNotDash(String s)
    {
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '-') return false;
        return true;
    }

    public void update(char guess, String mask)
    {
//        if (!isGoodMask(guess, mask))
//            System.out.println("mistake entering answer");

        previousGuesses.add(guess);
        if (isAllDash(mask)) {
            incorrectGuess ++;
            if (incorrectGuess == MAX_GUESSES) {
                status = 3;
                stop = true;
            }
        } else {
            updateSecretWord(mask);
            if (isAllNotDash(secretWord)) {
                stop = true;
                status = 2;
            }
        }
    }

//    public void main(String[] args) {
//        render();
//
//        do {
//            char guess = getNextGuess();
//            if (guess == 0) {
//                System.out.println("I give up, hang me");
//            }
//
//            do {
//                try {
//                    String mask = getUserAnswer(guess);
//                    update(guess, mask);
//                    break;
//                } catch (Exception e) {
//                    System.out.println("Invalid mask, try again");
//                }
//            } while (true);
//            render();
//        } while (!stop);
//    }

    GameHangManAIGuess(int len){
        initialize(len);
    }

    public String getSecretWord(){
        return secretWord;
    }

    public int getIncorrectGuess(){
        return MAX_GUESSES - incorrectGuess;
    }

    public String getPreviousGuesses(){
        StringBuilder res = new StringBuilder("Previous guesses : e");
        for(char s : previousGuesses){
            res.append(", " + s);
        }
        return res.toString();
    }

    public int getStatus(){
        return status;
    }
}

