package com.compilador.lexer;

public class Real extends Token{
    public final double value;

    public Real(double v){
        super(Tag.REAL_CONST);
        this.value = v;
    }

    public String toString(){
        return "" + this.value;
    }
}
