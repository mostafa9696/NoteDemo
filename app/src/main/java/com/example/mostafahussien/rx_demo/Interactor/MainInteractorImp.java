package com.example.mostafahussien.rx_demo.Interactor;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.mostafahussien.rx_demo.Model.Note;
import com.example.mostafahussien.rx_demo.Model.User;
import com.example.mostafahussien.rx_demo.Network.ApiClient;
import com.example.mostafahussien.rx_demo.Network.ApiService;
import com.example.mostafahussien.rx_demo.Utilities.PrefsUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mostafa Hussien on 21/06/2018.
 */

public class MainInteractorImp implements MainInteractor {
    private CompositeDisposable disposable;
    private ApiService apiService;
    private Context context;

    public MainInteractorImp(Context context) {
        disposable = new CompositeDisposable();
        apiService = ApiClient.getClient(context).create(ApiService.class);
        this.context = context;
    }


    @Override
    public void loginUser() {
        String uniqueID = UUID.randomUUID().toString();
        disposable.add(
                apiService.register(uniqueID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                PrefsUtils.storeApiKey(context, user.getApiKey());
                                Toast.makeText(context,
                                        "Device is registered successfully! ApiKey: " + PrefsUtils.getApiKey(context),
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void fetchNotes(OnLoginListener listener) {
        disposable.add(
                apiService.fetchAllNotes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new io.reactivex.functions.Function<List<Note>, List<Note>>() {
                            @Override
                            public List<Note> apply(List<Note> notes) throws Exception {
                                Collections.sort(notes, new Comparator<Note>() {
                                    @Override
                                    public int compare(Note n1, Note n2) {
                                        return n2.getId() - n1.getId();
                                    }
                                });
                                return notes;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<List<Note>>() {
                            @Override
                            public void onSuccess(List<Note> notes) {
                                listener.onFinishedFetchNotes(notes);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })

        );
    }
    @Override
    public void deleteSelectedNote(int id, int position, OnLoginListener listener) {
        disposable.add(
                apiService.deleteNote(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                listener.onFinishedDeleteNote(position);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        })
        );
    }

    @Override
    public void updateSelectedNote(int id, String s, int position, Note note, OnLoginListener listener) {
        disposable.add(
                apiService.updateNote(id, s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                note.setNote(s);
                                listener.onFinishedUpdateNote(position, note);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        }));
    }

    @Override
    public void createNewNote(String s, OnLoginListener listener) {
        disposable.add(
                apiService.createNote(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Note>() {

                            @Override
                            public void onSuccess(Note note) {
                                if (TextUtils.isEmpty(note.getNote())){
                                    listener.onFailureCreateNote();
                                    return;
                                }
                                listener.onFinishedCreateNote(note);

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }));
    }
}
