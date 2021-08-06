package com.compilador;

import com.compilador.lexer.Lexer;
import com.compilador.parser.Parser;

public class Main {

    public static void main(String[] args) {
        try {
            Lexer analisadorLexico = new Lexer(args[0]);
            Parser analisadorSintatico = new Parser(analisadorLexico);

            System.out.println("Programa sintaticamente correto!");
        }catch (Exception e){
            System.out.println("Programa com erro sintático!");
            System.out.println(e.toString());
        }
    }
}
