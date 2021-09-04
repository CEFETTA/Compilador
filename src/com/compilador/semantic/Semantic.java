package com.compilador.semantic;

import com.compilador.lexer.Lexer;
import com.compilador.lexer.Tag;
import com.compilador.models.Const;
import com.compilador.models.Id;
import com.compilador.models.Memory;

public class Semantic {
    private Lexer lex;
    private Memory memory;

    public Semantic(Lexer p){
        this.lex = p;
        this.memory = new Memory();
    }

    public void addId(Id identifier) throws Exception {
        if(!memory.hasId(identifier.name)){
            memory.addItem(identifier);
        }
        else {
            System.out.println("Erro Semântico: Identificador '"+identifier.name+"' já foi declarado");
            System.exit(1);
        }
    }

    public Const getValue(String id){
        if(memory.hasId(id)){
            return memory.getValue(id);
        }
        System.out.println("Erro Semântico: Identificador '"+id+"' não foi declarado");
        System.exit(1);
        return null;
    }

    // !
    public Const opNot(Const c){
        if(c.typeTag == Tag.INT){
            Integer value = c.value == Integer.valueOf(0) ? 1 : 0;
            return new Const(value, c.typeTag);
        }

        System.out.println("Erro Semântico: Só é possível utilizar o operador de negação em expressões inteiras (0=false; 1=true)!");
        System.exit(1);
        return null;
    }

    // -
    public Const opMinus(Const c){
        if(c.typeTag == Tag.INT){
            return new Const(-((int) c.value), c.typeTag);
        }
        if(c.typeTag == Tag.FLOAT){
            return new Const(-((float) c.value), c.typeTag);
        }
        System.out.println("Erro Semântico: Só é possível utilizar o operador de menos em inteiros ou reais!");
        System.exit(1);
        return null;
    }

    // mulop
    public Const opMulop(Const factor, Const mulfactor, int t){
        if(factor.typeTag == mulfactor.typeTag){
            if(!isNumber(factor)){
                System.out.println("Erro Semântico: Operador válido apenas para números!");
                System.exit(1);
            }

            return new Const(factor.value, factor.typeTag);
        }

        System.out.println("Erro Semântico: não foi possível executar a operação "+t+" com ' "+factor.typeTag+" and "+mulfactor.typeTag+"'!");
        System.exit(1);
        return null;
    }

    public Const opSimpleExpr(Const term, Const addTerm, int t){
        if(term.typeTag == addTerm.typeTag){
            if(term.typeTag == Tag.STRING && t != Tag.ADD){
                System.out.println("Erro Semântico: string não é compatível com o operador!");
                System.exit(1);
            }
            if(!isNumber(term) && t != Tag.SUB){
                System.out.println("Erro Semântico: Operador válido apenas para números!");
                System.exit(1);
            }

            return new Const(term.value, term.typeTag);
        }

        System.out.println("Erro Semântico: não foi possível executar a operação "+t+" com ' "+addTerm.typeTag+" and "+term.typeTag+"'!");
        System.exit(1);
        return null;
    }

    public Const opRelop(Const expression, Const operationExp, int t){
        if(
                expression.typeTag == operationExp.typeTag
                || expression.typeTag == Tag.FLOAT && isNumber(operationExp)
        ){
            return new Const(expression.value, expression.typeTag);
        }
        System.out.println("Erro Semântico: não foi possível executar a operação "+t+" com ' "+expression.typeTag+" and "+operationExp.typeTag+"', são tipos diferentes!");
        System.exit(1);
        return null;
    }

    private static boolean isNumber(Const c){
        return  c.typeTag == Tag.FLOAT || c.typeTag == Tag.INT;
    }
}
