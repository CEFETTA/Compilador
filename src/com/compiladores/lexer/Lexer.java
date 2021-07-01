package com.compiladores.lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    // contador de linhas
    public static int line = 1;
    // caracter atual do arquivo fonte
    private char ch = ' ';
    private FileReader file;

    // Tabela de Símbolos
    public Hashtable words = new Hashtable();

    // Método que insere palavras reservados na TS
    void reserve(Word w){
        words.put(w.lexeme, w);
    } // lexema é a palavra chave para a entrada na TS

    // Método construtor
    public Lexer(String fileName) throws FileNotFoundException {
        try{
            this.file = new FileReader(fileName);
        } catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado.");
            throw e;
        }

        // insere as palavras reservadas na TS
        reserve(new Word("class", Tag.CLASS));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("init", Tag.INIT));
        reserve(new Word("stop", Tag.STOP));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("write", Tag.WRITE));
    }

    // lê o próximo caracter
    private void readch() throws IOException {
        ch = (char) file.read();
    }

    // lê o próximo caracter e verifica se é igual a c
    private boolean readch(char c) throws IOException {
        readch();
        if(ch != c) return false;
        ch = ' ';
        return true;
    }

    // retorna o próximo Token da Linguagem
    public Token scan() throws Exception {
        // Desconsidera delimitadores e comentários na entrada
        for(;; readch()){
            if(ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') continue;
            else if(ch == '/'){
                // comentário de uma linha
                if(readch('/')){
                    while (ch != '\n' && ch != 65535){
                        readch();
                    }
                }
                // comentário de múltiplas linhas
                else if(ch == '*'){
                    char ant = ' ';
                    while (!(ant == '*' && ch == '/')){
                        // final do arquivo
                        if(ch == 65535){
                            throw new Exception("Error: Comentário não finalizado!");
                        }
                        if(ch == '\n') line++;
                        ant = ch;
                        readch();
                    }
                    if(ch == 65535) return Word.eof;
                }
                // é apenas divisão
                else return Word.div;
                continue;
            }
            else if(ch == '\n') line++; // conta linha
            else break;
        }

        // Operadores
        switch (ch){
            case '&':
                if(readch('&')) return Word.and;
                else return new Token('&');
            case '>':
                if(readch('=')) return Word.ge;
                else return Word.gt;
            case '<':
                if(readch('=')) return Word.le;
                else return Word.lt;
            case '!':
                if(readch('=')) return Word.dif;
                else return Word.not;
            case '=':
                if(readch('=')) return Word.eq;
                else return Word.ass;
            case '+': readch(); return Word.add;
            case '-': readch(); return Word.sub;
            case '|':
                if(readch('|')) return Word.or;
                else return new Token('|');
            case '*': readch(); return Word.mul;
            case ';': readch(); return Word.semi;
            case ',': readch(); return Word.comm;
            case '(': readch(); return Word.parOp;
            case ')': readch(); return Word.parCl;
            case '{': readch(); return Word.keyOp;
            case '}': readch(); return Word.keyCl;
            case 65535: return Word.eof;
        }

        // Números
        if(Character.isDigit(ch)){
            if(ch == '0'){
                if(readch('.')){
                    readch();
                    if(!Character.isDigit(ch)){
                        throw new Exception("Error: Contante 0. mal formatada!");
                    }
                    double real = 0.0;
                    int exp = 1;
                    do{
                        real = real + Character.digit(ch, 10)/Math.pow(10, exp);
                        exp++;
                        readch();
                    }while(Character.isDigit(ch));

                    return new Real(real);
                }
                return new Inteiro(Integer.parseInt("0"));
            }

            int value = 0;
            do{
                value = 10 * value + Character.digit(ch, 10);
                readch();
            }while (Character.isDigit(ch));

            // inteiro
            if(ch != '.') return new Inteiro(value);

            //real
            readch();
            if(!Character.isDigit(ch)){
                throw new Exception("Error: Real "+value+". mal formatado!");
            }
            double real = (double) value;
            int exp = 1;
            do{
                real = real + Character.digit(ch, 10)/Math.pow(10, exp);
                exp++;
                readch();
            }while (Character.isDigit(ch));

            return new Real(real);
        }

        //literal
        if(ch == '"'){
            StringBuffer sb = new StringBuffer();
            do{
                // fim do arquivo
                if(ch == 65535)
                    throw new Exception("Error: Literal não fechado!");
                if(ch != '\n' && ch != '\'' && ch != '"'){
                    sb.append(ch);
                }
                if(ch == '\n') line++;
                readch();
            }while(ch != '"');
            String s = sb.toString();
            readch();
            return new Literal(s);
        }

        // Identificadores
        if(Character.isLetter(ch)){

            StringBuffer sb = new StringBuffer();
            do{
                sb.append(ch);
                readch();
            }while(Character.isLetterOrDigit(ch) || ch == '_');

            String s = sb.toString();
            Word w = (Word) words.get(s);
            if(w != null) return w; // palavra já existe
            w = new Word(s, Tag.ID);
            words.put(s, w); // insere na TS
            return w;
        }

        // Caracteres não especificados
        Token t = new Token(ch);
        readch();
        return t;
    }
}
