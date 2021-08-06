package com.compilador.lexer;

public class Literal extends Token{
    public final String value;

    public Literal(String v){
        super(Tag.LIT_CONST);
        this.value = v;
    }

    public String toString(){
        return ""+this.value;
    }
}
