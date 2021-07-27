package com.compiladores;

import com.compiladores.lexer.Lexer;
import com.compiladores.lexer.Tag;
import com.compiladores.lexer.Token;
import com.compiladores.parser.Parser;

import java.util.Enumeration;

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
