package com.compiladores.lexer;

public class Num extends Token{
    public final int value;

    public Num(int v) {
        super(Tag.INT_CONST);
        this.value = v;
    }

    public String toString(){
        return "" + this.value;
    }
}
