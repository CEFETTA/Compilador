package com.compiladores;

import com.compiladores.lexer.Lexer;
import com.compiladores.lexer.Tag;
import com.compiladores.lexer.Token;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Token tok;
        try {
            System.out.println(args[0]);
            Lexer analisadorLexico = new Lexer(args[0]);

            do{
                tok = analisadorLexico.scan();
                System.out.println(tok.toString());
            }while(tok.tag != Tag.FINAL_FILE);

        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
