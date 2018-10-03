package com.hksapps.kamal.modernhomes.models;

public class Room {
    String room_id;
    String room_name;
    String room_img;



    public Room(String room_id, String room_name, String img_url) {
        this.room_id = room_id;
        this.room_name = room_name;
        this.room_img = img_url;
    }

    public Room(String room_id) {
        this.room_id = room_id;
    }

    public Room(){

    }



    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_img() {
        return room_img;
    }

    public void setRoom_img(String room_img) {
        this.room_img = room_img;
    }
}
