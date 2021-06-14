package com.compiladores.lexer;

public class Word extends Token{
    public String lexeme = "";

    public static final Word
            mul = new Word("*", Tag.MUL),
            div = new Word("/", Tag.DIV),
            and = new Word("&&", Tag.AND),
            or = new Word("or", Tag.OR);

    public Word(String s, int tag){
        super(tag);
        this.lexeme = s;
    }

    public String toString(){
        return this.lexeme;
    }
}
