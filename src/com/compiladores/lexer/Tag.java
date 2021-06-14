package com.compiladores.lexer;

/*
* Classe que define as constantes para os tokens
* */

public class Tag {
    public final static int
            // Palavras reservadas
            CLASS   =   256,
            INT     =   257,
            STRING  =   258,
            FLOAT   =   259,
            INIT    =   260,
            STOP    =   261,
            IF      =   262,
            ELSE    =   263,
            DO      =   264,
            WHILE   =   265,
            READ    =   266,
            WRITE   =   267,

            // Operadores e pontuação
            MUL     =   268, // '*'
            DIV     =   269, // '/'
            AND     =   270, // '&&'
            OR      =   271, // '||'
            ADD     =   272, // '+'
            SUB     =   273, // '-'
            GT      =   274, // '>'
            GTE     =   275, // '>='
            LT      =   276, // '<'
            LTE     =   277, // '<='
            DIF     =   278, // '!='
            EQ      =   279, // '=='
            SEMCOL  =   280, // ';'
            COMM    =   281, // ','
            ASS     =   282, // '='
            PAR_OP  =   283, // '('
            PAR_CLO =   284, // ')'
            KEY_OP  =   285, // '{'
            KEY_CLO =   286, // '}'

            // Outros Tokens
            ID          =   287, // nome
            INT_CONST   =   288, // 123
            LIT_CONST   =   289, // "aaa"
            REAL_CONST  =   290, // 12.4

            // delimitadores
            SING_COM    =   291, // '//' comentário de uma linha
            MUL_COM_OP  =   292, // '/*' início de multiplo comentário
            MUL_COM_CLO =   293, // '*/' fim de multiplo comentário
            TRUE        =   294, // 'true'
            FALSE       =   295; // 'false'
}
