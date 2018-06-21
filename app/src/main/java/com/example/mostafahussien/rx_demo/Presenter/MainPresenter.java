package com.example.mostafahussien.rx_demo.Presenter;

import com.example.mostafahussien.rx_demo.Interactor.MainInteractor;
import com.example.mostafahussien.rx_demo.Model.Note;

/**
 * Created by Mostafa Hussien on 21/06/2018.
 */

public interface MainPresenter {
    void onCreate();

    void deleteNote(int id, int position);

    void updateNote(int id, String s, int position, Note note);

    void createNote(String s);
}
