package com.compilador.models;

public class MemoryItem {
    public Id identifier;
    public Const value;

    public MemoryItem(Id identifier){
        this.identifier = identifier;
        this.value = new Const(0, identifier.type);
    }
}
