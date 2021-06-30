package com.compiladores;

import com.compiladores.lexer.Lexer;
import com.compiladores.lexer.Tag;
import com.compiladores.lexer.Token;
import java.io.IOException;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws IOException {
        Token tok;
        try {
            System.out.println(args[0]);
            Lexer analisadorLexico = new Lexer(args[0]);

            // exibe os lexemas
            System.out.println("--------- Lexemas ---------");
            do{
                tok = analisadorLexico.scan();
                System.out.println("Lexema: "+tok.toString()+" - Tag: "+tok.tag);
            }while(tok.tag != Tag.FINAL_FILE);

            // Tabela de Símbolos
            System.out.println("--------- TS ---------");
            Enumeration keys = analisadorLexico.words.keys();

            while (keys.hasMoreElements()){
                String key = (String) keys.nextElement();
                System.out.println("Lexema: "+key+" - Tag: "+(((Token)analisadorLexico.words.get(key)).tag));
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
