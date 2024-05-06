package com.example.demosystemfront.Entities;


public class AccType {

    int id;

    String name;


    public AccType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AccType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
