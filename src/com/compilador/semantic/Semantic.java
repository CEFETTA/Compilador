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
        System.out.println("Erro Semântico: " + rule);
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

    // !
    public Const opNot(Const c){
        if(c.type == Type.INT){
            Integer value = c.value == Integer.valueOf(0) ? 1 : 0;
            return new Const(value, c.type);
        }

        this.error("Só é possível utilizar o operador de negação em expressões inteiras (0=false; 1=true)!");
        return null;
    }

    // -
    public Const opMinus(Const c){
        if(c.type == Type.INT){
            return new Const(-((int) c.value), c.type);
        }
        if(c.type == Type.FLOAT){
            return new Const(-((float) c.value), c.type);
        }
        this.error("Só é possível utilizar o operador de menos em inteiros ou reais!");
        return null;
    }

    // mulop
    public Const opMulop(Const factor, Const mulfactor, int t){
        if(factor.type == mulfactor.type){
            if(!isNumber(factor)){
                this.error("Operador válido apenas para números!");
            }

            return new Const(factor.value, factor.type);
        }

        this.error("não foi possível executar a operação "+t+" com ' "+factor.type +" and "+mulfactor.type +"'!");
        return null;
    }

    public Const opSimpleExpr(Const term, Const addTerm, int t){
        if(term.type == addTerm.type){
            if(term.type == Type.STRING && t != Tag.ADD){
                this.error("string não é compatível com o operador!");
            }
            if(!isNumber(term) && t != Tag.SUB){
                this.error("Operador válido apenas para números!");
            }

            return new Const(term.value, term.type);
        }

        this.error("não foi possível executar a operação "+t+" com ' "+addTerm.type +" and "+term.type +"'!");
        return null;
    }

    public Const opRelop(Const expression, Const operationExp, int t){
        if(
                expression.type == operationExp.type
                || expression.type == Type.FLOAT && isNumber(operationExp)
        ){
            return new Const(expression.value, expression.type);
        }
        this.error("não foi possível executar a operação \"+t+\" com ' \"+expression.type +\" and \"+operationExp.type +\"', são tipos diferentes!\"");
        return null;
    }

    public void checkIsTypeCompatible(Type assignedType, Type expressionType) {
        // TODO: Check if type can be converted
    }

    private static boolean isNumber(Const c){
        return  c.type == Type.FLOAT || c.type == Type.INT;
    }
}
