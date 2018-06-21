package com.example.mostafahussien.rx_demo.Model;

/**
 * Created by Mostafa Hussien on 04/06/2018.
 */

public class Note extends BaseResponse {
    int id;                 // id is set automatically by retrofit when create new note
    String note;
    String timestamp;

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
