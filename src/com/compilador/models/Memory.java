package com.compilador.models;

import java.util.HashMap;

public class Memory {
    public HashMap<String, MemoryItem> memory;

    public Memory() {
        this.memory = new HashMap<>();
    }

    public boolean hasId(String id){
        return memory.containsKey(id);
    }

    public void addItem(Id identifier){
        this.memory.put(identifier.name, new MemoryItem(identifier));
    }

    public Type getType(String id){
        return memory.get(id).identifier.type;
    }

    public Const getValue(String id){
        return memory.get(id).value;
    }

    public void setValue(String id, Const value){
        memory.get(id).value = value;
    }
}
