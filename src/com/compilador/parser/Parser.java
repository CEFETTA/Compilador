package com.compilador.parser;

import com.compilador.lexer.Lexer;
import com.compilador.lexer.Tag;
import com.compilador.lexer.Token;
import com.compilador.lexer.Word;
import com.compilador.models.Id;
import com.compilador.models.Type;
import com.compilador.semantic.Semantic;

public class Parser {
    // analisador léxico
    private Lexer lex;
    // token atual
    private Token tok;
    // analisador Semântico
    private Semantic sem;

    // método do construtor
    public Parser(Lexer l) throws Exception {
        this.lex = l;
        this.sem = new Semantic(lex);
        this.S();
    }

    // avança na análise
    Token advance() throws Exception {
        Token previous = this.tok;
        this.tok = lex.scan();
        return previous;
    }

    // verifica a tag
    Token eat(int t, String rule) throws Exception{
        if(tok.tag == t) return this.advance();
        else error(rule);
        return null;
    }

    // exibe o erro
    void error(String rule) throws Exception {
        throw new Exception("Error: Regra: "+rule+" na linha: "+this.lex.line + "\ndetalhes: " + this.lex.lineDetails);
    }

    // primeiro símbolo: S
    void S() throws Exception {
        String rule = "S";
        // pega o primeiro token
        this.advance();

        // S -> program $
        if(this.tok.tag == Tag.CLASS){
            this.program(); eat(Tag.EOF, rule);
        }
        // caso contrário, há erro.
        else{
            this.error(rule);
        }
    }

    // Símbolo: program
    void program() throws Exception {
        String rule = "program";
        // program 	-> class identifier program-prime
        if(this.tok.tag == Tag.CLASS){
            this.eat(Tag.CLASS, rule);
            this.eat(Tag.ID, rule);
            this.programPrime();
        }
        // erro
        else {
            this.error(rule);
        }
    }

    // Símbolo program-prime
    void programPrime() throws Exception {
        String rule = "program-prime";
        switch (this.tok.tag) {
            //program-prime	-> decl-list body
            case Tag.INT:
            case Tag.STRING:
            case Tag.FLOAT:
                this.declList();
                this.body();
                break;
            // program-prime ->  body
            case Tag.INIT:
                this.body(); break;
            default: this.error(rule);
        }
    }

    // Símbolo: decl-list
    void declList() throws Exception {
        String rule = "decl-list";
        switch (this.tok.tag){
            // decl-list -> decl ";" decl-list-prime
            case Tag.INT:
            case Tag.STRING:
            case Tag.FLOAT:
                this.decl();
                this.eat(Tag.SEMCOL, rule);
                this.declListPrime();
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: decl-list-prime
    void declListPrime() throws Exception {
        String rule = "decl-list-prime";
        switch (this.tok.tag) {
            // decl-list-prime -> decl ";" decl-list-prime
            case Tag.INT:
            case Tag.STRING:
            case Tag.FLOAT:
                this.decl();
                this.eat(Tag.SEMCOL, rule);
                this.declListPrime();
                break;
            // decl-list-prime -> λ
            case Tag.INIT: break;
            default: this.error(rule);
        }
    }

    // Símbolo: decl
    void decl() throws Exception {
        switch (this.tok.tag) {
            // decl	-> type ident-list
            case Tag.INT:
            case Tag.STRING:
            case Tag.FLOAT:
                Type identifierType = this.type();
                this.identList(identifierType);
                break;
            default: this.error("decl");
        }
    }

    // Símbolo: ident-list
    void identList(Type identifierType) throws Exception {
        String rule = "ident-list";
        // ident-list -> identifier ident-list-prime
        if(this.tok.tag == Tag.ID){
            this.sem.addId(new Id(((Word) this.tok).lexeme, identifierType));
            this.eat(Tag.ID, rule);
            this.identListPrime(identifierType);
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: ident-list-prime
    void identListPrime(Type identifierType) throws  Exception {
        String rule = "ident-list-prime";
        switch (this.tok.tag) {
            // ident-list-prime	-> "," identifier ident-list-prime
            case Tag.COMM:
                this.eat(Tag.COMM, rule);
                this.sem.addId(new Id(((Word) this.tok).lexeme, identifierType));
                this.eat(Tag.ID, rule);
                this.identListPrime(identifierType);
                break;
            // ident-list-prime -> λ
            case Tag.SEMCOL: break;
            default: this.error(rule);
        }
    }

    // Símbolo: type
    Type type() throws Exception {
        String rule = "type";

        switch (this.tok.tag) {
            // type	-> int
            case Tag.INT:
                this.eat(Tag.INT, rule);
                return Type.INT;
            // type	-> string
            case Tag.STRING:
                this.eat(Tag.STRING, rule);
                return Type.STRING;
            // type	-> float
            case Tag.FLOAT:
                this.eat(Tag.FLOAT, rule);
                return Type.FLOAT;
            default: this.error(rule);
        }
        return Type.ERR;
    }

    // Símbolo: body
    void body() throws Exception {
        String rule = "body";
        // body -> init stmt-list stop
        if(this.tok.tag == Tag.INIT){
            this.eat(Tag.INIT, rule);
            this.stmtList();
            this.eat(Tag.STOP, rule);
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: stmt-list
    void stmtList() throws Exception {
        String rule = "stmt-list";

        switch (this.tok.tag) {
            // stmt-list -> stmt ";" stmt-list-prime
            case Tag.ID:
            case Tag.IF:
            case Tag.DO:
            case Tag.READ:
            case Tag.WRITE:
                this.stmt();
                this.eat(Tag.SEMCOL, rule);
                this.stmtListPrime();
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: stmt-list-prime
    void stmtListPrime() throws Exception {
        String rule = "stmt-list-prime";
        switch (this.tok.tag) {
            // stmt-list-prime -> stmt ";" stmt-list-prime
            case Tag.ID:
            case Tag.IF:
            case Tag.DO:
            case Tag.READ:
            case Tag.WRITE:
                this.stmt();
                this.eat(Tag.SEMCOL, rule);
                this.stmtListPrime();
                break;
            // stmt-list-prime -> λ
            case Tag.STOP:
            case Tag.KEY_CLO:
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: stmt
    void stmt() throws Exception {
        switch (this.tok.tag) {
            // stmt	-> assign-stmt
            case Tag.ID: this.assignStmt(); break;
            // stmt	-> if-stmt
            case Tag.IF: this.ifStmt(); break;
            // stmt	-> do-stmt
            case Tag.DO: this.doStmt();  break;
            // stmt	-> read-stmt
            case Tag.READ: this.readStmt(); break;
            // stmt	-> write-stmt
            case Tag.WRITE: this.writeStmt(); break;
            default: this.error("stmt");
        }
    }

    // Símbolo: assign-stmt
    void assignStmt() throws Exception {
        String rule = "assign-stmt";
        // assign-stmt -> identifier "=" simple-expr
        if(this.tok.tag == Tag.ID){
            Type expectedType = this.sem.getIdentifierType(this.asLexeme(this.tok));
            this.eat(Tag.ID, rule);
            this.eat(Tag.ASS, rule);
            this.simpleExpr(expectedType);
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: if-stmt
    void ifStmt() throws Exception {
        String rule = "if-stmt";

        // if-stmt -> if "(" condition ")" "{" stmt-list "}" if-prime
        if(this.tok.tag == Tag.IF){
            this.eat(Tag.IF, rule);
            this.eat(Tag.PAR_OP, rule);
            this.condition();
            this.eat(Tag.PAR_CLO, rule);
            this.eat(Tag.KEY_OP, rule);
            this.stmtList();
            this.eat(Tag.KEY_CLO, rule);
            this.ifPrime();
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: if-prime
    void ifPrime() throws Exception {
        String rule = "if-prime";

        switch (this.tok.tag) {
            // if-prime	-> else “{“ stmt-list "}"
            case Tag.ELSE:
                this.eat(Tag.ELSE, rule);
                this.eat(Tag.KEY_OP, rule);
                this.stmtList();
                this.eat(Tag.KEY_CLO, rule);
                break;
            // if-prime	-> λ
            case Tag.SEMCOL: break;
            default: this.error(rule);
        }
    }

    // Símbolo: condition
    void condition() throws Exception {
        switch (this.tok.tag) {
            // condition -> expression
            case Tag.ID:
            case Tag.PAR_OP:
            case Tag.NOT:
            case Tag.SUB:
            case Tag.LIT_CONST:
            case Tag.REAL_CONST:
            case Tag.INT_CONST:
                this.expression(Type.BOOL);
                break;
            default: this.error("condition");
        }
    }

    // Símbolo: do-stmt
    void doStmt() throws Exception {
        String rule = "do-stmt";
        // do-stmt -> do "{" stmt-list "}" do-suffix
        if(this.tok.tag == Tag.DO){
            this.eat(Tag.DO, rule);
            this.eat(Tag.KEY_OP, rule);
            this.stmtList();
            this.eat(Tag.KEY_CLO, rule);
            this.doSuffix();
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: do-suffix
    void doSuffix() throws Exception {
        String rule = "do-suffix";
        // do-suffix -> while "(" condition ")"
        if(this.tok.tag == Tag.WHILE) {
            this.eat(Tag.WHILE, rule);
            this.eat(Tag.PAR_OP, rule);
            this.condition();
            this.eat(Tag.PAR_CLO, rule);
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: read-stmt
    void readStmt() throws Exception {
        String rule = "read-stmt";

        // read-stmt -> read "(" identifier ")"
        if(this.tok.tag == Tag.READ){
            this.eat(Tag.READ, rule);
            this.eat(Tag.PAR_OP, rule);
            this.eat(Tag.ID, rule);
            this.eat(Tag.PAR_CLO, rule);
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: write-stmt
    void writeStmt() throws Exception {
        String rule = "write-stmt";

        // write-stmt -> write "(" writable ")"
        if(this.tok.tag == Tag.WRITE){
            this.eat(Tag.WRITE, rule);
            this.eat(Tag.PAR_OP, rule);
            this.writable();
            this.eat(Tag.PAR_CLO, rule);
        }
        else {
            this.error(rule);
        }
    }

    // Símbolo: writable
    void writable() throws Exception {
        String rule = "writable";

        switch (this.tok.tag) {
            // writable -> simple-expr
            case Tag.ID:
            case Tag.PAR_OP:
            case Tag.NOT:
            case Tag.SUB:
            case Tag.INT_CONST:
            case Tag.LIT_CONST:
            case Tag.REAL_CONST:
                this.simpleExpr(Type.ANY);
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: expression
    void expression(Type expectedType) throws Exception {
        String rule = "expression";

        switch (this.tok.tag) {
            // expression -> simple-expr expression-prime
            case Tag.ID:
            case Tag.PAR_OP:
            case Tag.NOT:
            case Tag.SUB:
            case Tag.INT_CONST:
            case Tag.LIT_CONST:
            case Tag.REAL_CONST:
                if (expectedType == Type.BOOL) {
                    this.simpleExpr(Type.ANY);
                    this.expressionPrime(expectedType);
                } else {
                    this.simpleExpr(expectedType);
                    this.expressionPrime(expectedType);
                }
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: expression-prime
    void expressionPrime(Type expectedType) throws Exception {
        String rule = "expression-prime";

        switch (this.tok.tag) {
            // expression-prime	-> relop simple-expr
            case Tag.GT:
            case Tag.GTE:
            case Tag.LT:
            case Tag.LTE:
            case Tag.DIF:
            case Tag.EQ:
                // Relop expressions can only be used when boolean compatible
                this.sem.checkIsTypeCompatible(expectedType, Type.BOOL);
                this.relop();
                this.simpleExpr(Type.ANY);
                break;
            //expression-prime -> λ
            case Tag.PAR_CLO: break;
            default: this.error(rule);
        }
    }

    // Símbolo: simple-expr
    void simpleExpr(Type expectedType) throws Exception {
        String rule = "simple-expr";

        switch (this.tok.tag) {
            // simple-expr -> term simple-expr-prime
            case Tag.ID:
            case Tag.PAR_OP:
            case Tag.NOT:
            case Tag.SUB:
            case Tag.INT_CONST:
            case Tag.LIT_CONST:
            case Tag.REAL_CONST:
                this.term(expectedType);
                this.simpleExprPrime(expectedType);
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: simple-expr-prime
    void simpleExprPrime(Type expectedType) throws Exception {
        String rule = "simple-expr-prime";

        switch (this.tok.tag) {
            // simple-expr-prime -> addop term simple-expr-prime
            case Tag.SUB:
            case Tag.ADD:
            case Tag.OR:
                this.addop(expectedType);
                this.term(expectedType);
                this.simpleExprPrime(expectedType);
                break;
            // simple-expr-prime	-> λ
            case Tag.SEMCOL:
            case Tag.PAR_CLO:
            case Tag.GT:
            case Tag.GTE:
            case Tag.LT:
            case Tag.LTE:
            case Tag.DIF:
            case Tag.EQ:
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: term
    void term(Type expectedType) throws Exception {
        String rule = "term";

        switch (this.tok.tag) {
            // term -> factor-a term-prime
            case Tag.ID:
            case Tag.PAR_OP:
            case Tag.NOT:
            case Tag.SUB:
            case Tag.INT_CONST:
            case Tag.REAL_CONST:
            case Tag.LIT_CONST:
                this.factorA(expectedType);
                this.termPrime(expectedType);
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: term-prime
    void termPrime(Type expectedType) throws Exception {
        String rule = "term-prime";

        switch (this.tok.tag) {
            // term-prime -> mulop factor-a term-prime
            case Tag.MUL:
            case Tag.DIV:
            case Tag.AND:
                this.mulop(expectedType);
                this.factorA(expectedType);
                this.termPrime(expectedType);
                break;
            // term-prime -> λ
            case Tag.SEMCOL:
            case Tag.PAR_CLO:
            case Tag.SUB:
            case Tag.GT:
            case Tag.GTE:
            case Tag.LT:
            case Tag.LTE:
            case Tag.DIF:
            case Tag.EQ:
            case Tag.ADD:
            case Tag.OR:
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: factor-a
    void factorA(Type expectedType) throws Exception {
        String rule = "factor-a";

        switch (this.tok.tag) {
            // factor-a	-> factor
            case Tag.ID:
            case Tag.PAR_OP:
            case Tag.REAL_CONST:
            case Tag.INT_CONST:
            case Tag.LIT_CONST:
                this.factor(expectedType);
                break;
            // factor-a -> "!" factor
            case Tag.NOT:
                // Not should be used only if expected type is compatible to boolean
                this.sem.checkIsTypeCompatible(expectedType, Type.BOOL);
                this.eat(Tag.NOT, rule);
                this.factor(expectedType);
                break;
            // factor-a -> "-" factor
            case Tag.SUB:
                this.eat(Tag.SUB, rule);
                this.factor(expectedType);
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: factor
    void factor(Type expectedType) throws Exception {
        String rule = "factor";

        switch (this.tok.tag) {
            // factor -> identifier
            case Tag.ID:
                this.sem.checkIdentifierCompatibility(asLexeme(this.tok), expectedType);
                this.eat(Tag.ID, rule);
                break;
            // factor -> constant
            case Tag.INT_CONST:
                this.sem.checkIsTypeCompatible(expectedType, this.convertTokenToType(this.tok));
                this.eat(Tag.INT_CONST, rule);
                break;
            case Tag.LIT_CONST:
                this.sem.checkIsTypeCompatible(expectedType, this.convertTokenToType(this.tok));
                this.eat(Tag.LIT_CONST, rule);
                break;
            case Tag.REAL_CONST:
                this.sem.checkIsTypeCompatible(expectedType, this.convertTokenToType(this.tok));
                this.eat(Tag.REAL_CONST, rule);
                break;
            // factor -> "(" expression ")"
            case Tag.PAR_OP:
                this.eat(Tag.PAR_OP, rule);
                this.expression(expectedType);
                this.eat(Tag.PAR_CLO, rule);
                break;
            default: this.error(rule);
        }
    }

    // Símbolo: relop
    void relop() throws Exception {
        String rule = "relop";

        switch (this.tok.tag) {
            //relop -> ">"
            case Tag.GT: this.eat(Tag.GT, rule); break;
            // relop -> ">="
            case Tag.GTE: this.eat(Tag.GTE, rule); break;
            // relop -> "<"
            case Tag.LT: this.eat(Tag.LT, rule); break;
            // relop -> "<="
            case Tag.LTE: this.eat(Tag.LTE, rule); break;
            // relop -> "!="
            case Tag.DIF: this.eat(Tag.DIF, rule); break;
            // relop -> "=="
            case Tag.EQ: this.eat(Tag.EQ, rule); break;
            default: this.error(rule);
        }
    }

    // Símbolo: addop
    void addop(Type expectedType) throws Exception {
        String rule = "addop";

        switch (this.tok.tag) {
            // addop -> "+"
            case Tag.ADD: this.eat(Tag.ADD, rule); break;
            // addop -> "-"
            case Tag.SUB: this.eat(Tag.SUB, rule); break;
            // addop -> "||"
            case Tag.OR:
                this.sem.checkIsTypeCompatible(expectedType, Type.BOOL);
                this.eat(Tag.OR, rule); break;
            default: this.error(rule);
        }
    }

    // Símbolo: mulop
    void mulop(Type expectedType) throws Exception {
        String rule = "mulop";

        switch (this.tok.tag) {
            // mulop -> "*"
            case Tag.MUL: this.eat(Tag.MUL, rule); break;
            // mulop -> "/"
            case Tag.DIV:
                // div operator always casts return type to float
                this.sem.checkIsTypeCompatible(expectedType, Type.FLOAT);
                this.eat(Tag.DIV, rule); break;
            // mulop -> "&&"
            case Tag.AND:
                this.sem.checkIsTypeCompatible(expectedType, Type.BOOL);
                this.eat(Tag.AND, rule); break;
            default: this.error(rule);
        }
    }

    String asLexeme(Token tok) {
        return ((Word) tok).lexeme;
    }

    Type convertTokenToType(Token tok) {
        switch (tok.tag) {
            case Tag.INT_CONST:
                return Type.INT;
            case Tag.REAL_CONST:
                return Type.FLOAT;
            case Tag.LIT_CONST:
                return Type.STRING;
        }
        return Type.ERR;
    }
}
