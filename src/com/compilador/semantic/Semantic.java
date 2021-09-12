package com.compilador.semantic;

import com.compilador.lexer.Lexer;
import com.compilador.lexer.Tag;
import com.compilador.models.Const;
import com.compilador.models.Id;
import com.compilador.models.Memory;
import com.compilador.models.Type;

public class Semantic {
    private Lexer lex;
    private Memory memory;

    public Semantic(Lexer p){
        this.lex = p;
        this.memory = new Memory();
    }

    public void error(String rule) {
        System.out.println("Erro Semântico (linha "+ this.lex.line +"): " + rule + "\ndetalhes: " + this.lex.lineDetails);
        System.exit(1);
    }

    public void addId(Id identifier) throws Exception {
        if(!memory.hasId(identifier.name)){
            memory.addItem(identifier);
        }
        else {
            this.error("Identificador '"+identifier.name+"' já foi declarado");
        }
    }

    public Const getValue(String id){
        if(memory.hasId(id)){
            return memory.getValue(id);
        }
        this.error("Identificador '"+id+"' não foi declarado");
        return null;
    }

    public Type getIdentifierType(String id) {
        Const value = this.getValue(id);
        return value.type;
    }

    public void checkIdentifierCompatibility(String id, Type expectedType) {
        Type idType = this.getIdentifierType(id);
        this.checkIsTypeCompatible(expectedType, idType);
    }

    public void checkIsTypeCompatible(Type assignedType, Type expressionType) {
        if (
                assignedType == Type.ANY
                || expressionType == Type.ANY
                || assignedType == expressionType
        ) {
            return;
        }
        if (assignedType == Type.FLOAT && expressionType == Type.INT) {
            return;
        }
        this.error("O tipo utilizado na operação " + expressionType + " não é compatível com o tipo da atribuição " + assignedType);
    }
}
