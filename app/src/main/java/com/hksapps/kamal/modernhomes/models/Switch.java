package com.hksapps.kamal.modernhomes.models;

public class Switch {
    String name;
    String status;
    String switch_img;


    public Switch(String name, String status, String switch_img) {
        this.name = name;
        this.status = status;
        this.switch_img = switch_img;
    }

    public Switch() {
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


    public String getSwitch_img() {
        return switch_img;
    }

    public void setSwitch_img(String switch_img) {
        this.switch_img = switch_img;
    }


}
