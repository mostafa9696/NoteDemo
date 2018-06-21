package com.example.mostafahussien.rx_demo.Interactor;

import android.content.Context;

import com.example.mostafahussien.rx_demo.Model.Note;

import java.util.List;

/**
 * Created by Mostafa Hussien on 21/06/2018.
 */

public interface MainInteractor {



    interface OnLoginListener{
        void onFinishedFetchNotes(List<Note> notes);
        void onFinishedDeleteNote(int position);
        void onFinishedUpdateNote(int position, Note note);
        void onFinishedCreateNote(Note note);
        void onFailureCreateNote();
    }

    void loginUser();
    void fetchNotes(OnLoginListener listener);
    void deleteSelectedNote(int id, int position, OnLoginListener listener);
    void updateSelectedNote(int id, String s, int position, Note note, OnLoginListener listener);
    void createNewNote(String s, OnLoginListener listener);

}
