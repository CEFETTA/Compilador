package com.compiladores.lexer;

public class Real extends Token{
    public final float value;

    public Real(float v){
        super(Tag.REAL_CONST);
        this.value = v;
    }

    public String toString(){
        return "" + this.value;
    }
}
