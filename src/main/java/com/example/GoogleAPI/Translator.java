package com.example.GoogleAPI;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.example.GoogleAPI.parsing.Parse;
import com.example.GoogleAPI.parsing.ParseTextDetect;
import com.example.GoogleAPI.parsing.ParseTextTranslate;
import com.example.GoogleAPI.text.Text;
import com.example.GoogleAPI.text.TextTranslate;


public class Translator {
    private static Translator translator;

    private Translator() {
    }

    public static synchronized Translator getInstance() {
        if (translator == null) {
            translator = new Translator();
        }

        return translator;
    }

    public void translate(TextTranslate textTranslate) {
        Parse parse = new ParseTextTranslate(textTranslate);
        parse.parse();
    }

    public String translate(String text, String languageInput, String languageOutput) {
        Text input = new Text(text, languageInput);
        TextTranslate textTranslate = new TextTranslate(input, languageOutput);
        Parse parse = new ParseTextTranslate(textTranslate);
        parse.parse();
        return textTranslate.getOutput().getText();
    }

    public String detect(String text) {
        Text input = new Text(text);
        Parse parse = new ParseTextDetect(input);
        parse.parse();
        return input.getLanguage();
    }
}
