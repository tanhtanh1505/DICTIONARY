package com.example.dictionary;

import com.example.GoogleAPI.Audio;
import com.example.GoogleAPI.GoogleTranslate;
import com.example.GoogleAPI.Language;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;

public class TranslateByGoogle {
    public static String textTranslate(String langFrom, String langTo, String str) throws IOException {
        return GoogleTranslate.translate(langFrom, langTo, str);
    }
    public static String essayTranslate(String langFrom, String langTo, String str) throws IOException {
        langFrom = langFrom.toLowerCase().substring(0,2);
        langTo = langTo.toLowerCase().substring(0,2);
        String[] input = str.split("\n");
        StringBuilder result = new StringBuilder();
        for (String s : input) {
            result.append(textTranslate(langFrom, langTo, s)).append("\n");
        }
        return result.toString();
    }

    public static void speak(String s, String langOut) throws IOException, JavaLayerException {
        Audio audio = Audio.getInstance();
        InputStream sound = audio.getAudio(s.trim(), langOut);
        audio.play(sound);
    }

    public static void speakEssay(String s, String langOut) throws IOException, JavaLayerException {
        String[] input = s.split("\n");
        Audio audio = Audio.getInstance();
        for (String sc : input) {
            InputStream sound = audio.getAudio(sc.trim(), langOut);
            audio.play(sound);
        }
    }
}
