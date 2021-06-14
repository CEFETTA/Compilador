package com.compiladores.lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public static int line = 1;
    private char ch = ' ';
    private FileReader file;

    private Hashtable words = new Hashtable();

    void reserve(Word w){
        words.put(w.lexeme, w);
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try{
            this.file = new FileReader(fileName);
        } catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado.");
            throw e;
        }

        reserve(new Word("if", Tag.IF));
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if(ch != c) return false;
        ch = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for(;; readch()){
            if(ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') continue;
            else if(ch == '\n') line++;
            else break;
        }

        // Operadores
        switch (ch){
            case '&':
                if(readch('&')) return Word.and;
                else return new Token('&');
        }

        // Números
        if(Character.isDigit(ch)){
            int value = 0;
            do{
                value = 10 * value + Character.digit(ch, 10);
                readch();
            }while(Character.isDigit(ch));
            return new Num(value);
        }

        // Identificadores
        if(Character.isLetter(ch)){
            StringBuffer sb = new StringBuffer();
            do{
                sb.append(ch);
                readch();
            }while(Character.isLetterOrDigit(ch));

            String s = sb.toString();
            Word w = (Word) words.get(s);
            if(w != null) return w; // palavra já existe
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }

        // Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        return t;
    }
}
