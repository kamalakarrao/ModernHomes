package com.hksapps.kamal.modernhomes.models;

public class Switch {
    String name;
    String status;


    public Switch(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Switch(String name, String status) {
        this.name = name;
        this.status = status;
    }



}
