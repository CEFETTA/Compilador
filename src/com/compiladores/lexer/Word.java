package com.compiladores.lexer;

public class Word extends Token{
    public String lexeme = "";

    public static final Word mul = new Word("*", Tag.MUL);
    public static final Word div = new Word("/", Tag.DIV);
    public static final Word and = new Word("&&", Tag.AND);
    public static final Word or = new Word("or", Tag.OR);
    public static final Word ge = new Word(">=", Tag.GTE);
    public static final Word gt = new Word(">", Tag.GT);
    public static final Word le = new Word("<=", Tag.LTE);
    public static final Word lt = new Word("<", Tag.LT);
    public static final Word dif = new Word("!=", Tag.DIF);
    public static final Word not = new Word("!", Tag.NOT);
    public static final Word eq = new Word("==", Tag.EQ);
    public static final Word ass = new Word("=", Tag.ASS);
    public static final Word add = new Word("=", Tag.ADD);
    public static final Word sub = new Word("=", Tag.SUB);
    public static final Word semi = new Word(";", Tag.SEMCOL);
    public static final Word comm = new Word(",", Tag.COMM);
    public static final Word parOp = new Word("(", Tag.PAR_OP);
    public static final Word parCl = new Word(")", Tag.PAR_CLO);
    public static final Word keyOp = new Word("{", Tag.KEY_OP);
    public static final Word keyCl = new Word("}", Tag.KEY_CLO);
    public static final Word eof = new Word("", Tag.FINAL_FILE);


    public Word(String s, int tag){
        super(tag);
        this.lexeme = s;
    }

    public String toString(){
        return this.lexeme;
    }
}
