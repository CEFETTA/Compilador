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
        if(c.type == Type.BOOL){
            return new Const(!((boolean) c.value), c.type);
        }

        System.out.println("Erro Semântico: Só é possível utilizar o operador de negação em expressões booleanas!");
        System.exit(1);
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
        System.out.println("Erro Semântico: Só é possível utilizar o operador de menos em inteiros ou reais!");
        System.exit(1);
        return null;
    }

    // mulop
    public Const opMulop(Const factor, Const mulfactor, int t){
        if(factor.type == mulfactor.type){
            if(factor.type == Type.BOOL && t != Tag.AND){
                System.out.println("Erro Semântico: booleano não é compatível com o operador!");
                System.exit(1);
            }
            if(factor.type != Type.BOOL && t == Tag.AND){
                System.out.println("Erro Semântico: and só é compatível com o booleano!");
                System.exit(1);
            }
            if(!isNumber(factor)){
                System.out.println("Erro Semântico: Operador válido apenas para números!");
                System.exit(1);
            }

            return new Const(factor.value, factor.type);
        }

        System.out.println("Erro Semântico: não foi possível executar a operação "+t+" com ' "+factor.type+" and "+mulfactor.type+"'!");
        System.exit(1);
        return null;
    }

    public Const opSimpleExpr(Const term, Const addTerm, int t){
        if(term.type == addTerm.type){
            if(term.type == Type.STRING && t != Tag.ADD){
                System.out.println("Erro Semântico: string não é compatível com o operador!");
                System.exit(1);
            }
            if(term.type == Type.BOOL && t != Tag.OR){
                System.out.println("Erro Semântico: booleano não é compatível com o operador!");
                System.exit(1);
            }
            if(term.type != Type.BOOL && t == Tag.OR){
                System.out.println("Erro Semântico: or só é compatível com o booleano!");
                System.exit(1);
            }
            if(!isNumber(term) && t != Tag.SUB){
                System.out.println("Erro Semântico: Operador válido apenas para números!");
                System.exit(1);
            }

            return new Const(term.value, term.type);
        }

        System.out.println("Erro Semântico: não foi possível executar a operação "+t+" com ' "+addTerm.type+" and "+term.type+"'!");
        System.exit(1);
        return null;
    }

    public Const opRelop(Const expression, Const operationExp, int t){
        if(expression.type == operationExp.type){
            return new Const(expression.value, expression.type);
        }
        if(expression.type == Type.FLOAT && isNumber(operationExp)){
            return new Const(expression.value, expression.type);
        }
        System.out.println("Erro Semântico: não foi possível executar a operação "+t+" com ' "+expression.type+" and "+operationExp.type+"', são tipos diferentes!");
        System.exit(1);
        return null;
    }

    private static boolean isNumber(Const c){
        return  c.type == Type.FLOAT || c.type == Type.INT;
    }
}
