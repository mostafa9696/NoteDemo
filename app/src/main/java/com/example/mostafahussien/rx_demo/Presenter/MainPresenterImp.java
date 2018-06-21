package com.example.mostafahussien.rx_demo.Presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.mostafahussien.rx_demo.Interactor.MainInteractor;
import com.example.mostafahussien.rx_demo.Interactor.MainInteractorImp;
import com.example.mostafahussien.rx_demo.Model.Note;
import com.example.mostafahussien.rx_demo.Utilities.PrefsUtils;
import com.example.mostafahussien.rx_demo.View.MainView;

import java.util.List;

/**
 * Created by Mostafa Hussien on 21/06/2018.
 */

public class MainPresenterImp implements MainPresenter, MainInteractor.OnLoginListener {
    MainView view;
    MainInteractor interactor;
    Context context;
    public MainPresenterImp(MainView view) {
        context= (Context) view;
        this.view = view;
        this.interactor = new MainInteractorImp(context);

    }
    @Override
    public void onCreate() {
        view.initAdapter();

        String API_KEY= PrefsUtils.getApiKey(context);
        if(TextUtils.isEmpty(API_KEY)) {
            interactor.loginUser();
            view.checkEmptyList();
        } else {
            interactor.fetchNotes(this);
        }
    }

    @Override
    public void deleteNote(int id, int position) {
        interactor.deleteSelectedNote(id, position, this);
    }

    @Override
    public void updateNote(int id, String s, int position,Note note) {
        interactor.updateSelectedNote(id, s, position, note, this);
    }

    @Override
    public void createNote(String s) {
        interactor.createNewNote(s,this);

    }


    @Override
    public void onFinishedFetchNotes(List<Note> notes) {
        view.setListAdapter(notes);
        view.checkEmptyList();
    }

    @Override
    public void onFinishedDeleteNote(int position) {
        view.deleteNote(position);
        view.checkEmptyList();
    }

    @Override
    public void onFinishedUpdateNote(int position, Note note) {
        view.updateNote(position,note);
    }

    @Override
    public void onFinishedCreateNote(Note note) {
        view.createNote(note);
        view.checkEmptyList();
    }

    @Override
    public void onFailureCreateNote() {
        view.createFailed();
    }
}
