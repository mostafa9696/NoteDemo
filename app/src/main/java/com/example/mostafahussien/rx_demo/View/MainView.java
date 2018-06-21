package com.example.mostafahussien.rx_demo.View;

import com.example.mostafahussien.rx_demo.Model.Note;

import java.util.List;

/**
 * Created by Mostafa Hussien on 21/06/2018.
 */

public interface MainView {
    void setListAdapter(List<Note> notes);
    void initAdapter();
    void deleteNote(int position);
    void updateNote(int position, Note note);
    void createNote(Note note);
    void createFailed();
    void checkEmptyList();

}
