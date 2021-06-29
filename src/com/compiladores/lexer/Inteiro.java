package com.compiladores.lexer;

public class Inteiro extends Token{
    public final int value;

    public Inteiro(int v) {
        super(Tag.INT_CONST);
        this.value = v;
    }

    public String toString(){
        return "" + this.value;
    }
}
