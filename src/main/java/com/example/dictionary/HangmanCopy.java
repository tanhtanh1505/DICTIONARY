package com.example.dictionary;

import java.util.*;

public class HangmanCopy {
    private static int wordLength;
    private static final int MAX_GUESSES = 7;
    private static String secretWord;
    private static int incorrectGuess;
    private static HashSet<Character> previousGuesses = new HashSet<>();
    private static boolean stop;

    HangmanCopy(){

    }

    static void initialize(int len){
        wordLength = len;
        for(int i = 0; i < len; i++)
            secretWord += '-';
        incorrectGuess = 0;
        stop = false;
    }

    static char getNextGuess(){
        ArrayList<String> wordList = InitData.getKeyEV();
        HashSet<Character> remainingChars = getRemainingChars();
        if (remainingChars.size() == 0)
            return 0;

        if (isAllDash(secretWord))
            return getVowelGuess(remainingChars);

        ArrayList<String> filteredWordList = getSuitableWords(wordList, secretWord, remainingChars);
        HashMap<Character, Integer> occurenceCount = getOccurenceCount(remainingChars, filteredWordList);
        return getMaxOccurenceChar(remainingChars, occurenceCount);
    }

    static HashSet<Character> getRemainingChars()
    {
        HashSet<Character> remainingChars = new HashSet<>();
        for (char c = 'a'; c <= 'z'; c++)
            remainingChars.add(c);
        for (char c: previousGuesses)
            remainingChars.remove(c);
        return remainingChars;
    }

    static boolean isAllDash(String s)
    {
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) != '-') return false;
        return true;
    }

    static char getVowelGuess(HashSet<Character> remainingChars)
    {
        char vowel[] = {'e', 'a', 'o', 'i', 'u'};
        for (char c : vowel) {
            if (remainingChars.contains(c))
                return c;
        }
        return 0;
    }

    static ArrayList<String> getSuitableWords(ArrayList<String> wordList, String secretWord, HashSet<Character> remainingChars)
    {
        ArrayList<String> result = new ArrayList<>();
        for (String word : wordList)
        if (isSuitableWord(word, secretWord, remainingChars)){
            result.add(word);
        }
        return result;
    }

    static boolean isSuitableWord(String word, String secretWord, HashSet<Character> remainingChars)
    {
        if (word.length() != secretWord.length()) return false;
        for (int i = 0; i < word.length(); i++) {
        if (secretWord.charAt(i) != '-') {
            if (word.charAt(i) != secretWord.charAt(i)) return false;
        }
        else if (remainingChars.contains(word.charAt(i)))
            return false;
    }
        return true;
    }

    static HashMap<Character, Integer> getOccurenceCount(HashSet<Character> remainingChars, ArrayList<String> wordList)
    {
        HashMap<Character, Integer> count = new HashMap<>();
        for (char c: remainingChars) count.replace(c, 0);

        for (String word : wordList) {
        for (int i = 0; i < word.length(); i++)
            if (count.containsKey(word.charAt(i)))
                count.replace(word.charAt(i), count.get(word.charAt(i)));
    }
        return count;
    }

    static char getMaxOccurenceChar(HashSet<Character> remainingChars, HashMap<Character, Integer> count)
    {
        char best = 0;
        int best_count = 0;
        for (Map.Entry<Character, Integer> p : count.entrySet())
            if (p.getValue() > best_count) {
                best = p.getKey();
                best_count = p.getValue();
            }
        return best;
    }

    static void update(char guess, String mask)
    {
        if (!isGoodMask(guess, mask))
            System.out.println("mistake entering answer");

        previousGuesses.add(guess);
        if (isAllDash(mask)) {
            incorrectGuess ++;
            if (incorrectGuess == MAX_GUESSES) stop = true;
        } else {
            updateSecretWord(mask);
            if (isAllNotDash(secretWord)) stop = true;
        }
    }

    static boolean isGoodMask(char guess, String mask)
    {
        if (mask.length() != secretWord.length()) return false;
        for (int i = 0; i < secretWord.length(); i++)
        if (mask.charAt(i) != '-') {
            if (mask.charAt(i) != guess)
                return false;
            if (secretWord.charAt(i) != '-' && secretWord.charAt(i) != mask.charAt(i))
                return false;
        }
        return true;
    }

    static void updateSecretWord(String mask)
    {
        for (int i = 0; i < secretWord.length(); i++)
        if (mask.charAt(i) != '-')
            secretWord = secretWord.substring(0, i) + mask.charAt(i) + secretWord.substring(i + 1);
    }

    static boolean isAllNotDash(String s)
    {
        return !s.contains("-");
    }

    static String getUserAnswer(char guess)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("I guess " + guess + ", please enter your mask: ");
        String answer = sc.next();
        return answer;
    }


    static void render(int incorrectGuess)
    {
        System.out.println("Incorrect guess = " + incorrectGuess + "   previous guesses = ");
        for (Character c : previousGuesses)
            System.out.print(c);
        System.out.println("   secretWord = " + secretWord);
    }

    public static void main(String[] args) {
        initialize(5);
        System.out.println("Incorrect guess = " + incorrectGuess + "   previous guesses = ");
        for (Character c : previousGuesses)
            System.out.print(c);
        System.out.println("   secretWord = " + secretWord);

        do {
            char guess = getNextGuess();
            if (guess == 0) {
                System.out.println("I give up, hang me");
            }
            do {
                try {
                    String mask = getUserAnswer(guess);
                    update(guess, mask);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid mask, try again");
                }
            } while (true);
            render(incorrectGuess);
        } while (!stop);
    }
}
