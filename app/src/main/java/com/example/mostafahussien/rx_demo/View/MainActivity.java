package com.example.mostafahussien.rx_demo.View;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafahussien.rx_demo.Model.Note;
import com.example.mostafahussien.rx_demo.NoteAdapter;
import com.example.mostafahussien.rx_demo.Presenter.MainPresenter;
import com.example.mostafahussien.rx_demo.Presenter.MainPresenterImp;
import com.example.mostafahussien.rx_demo.R;
import com.example.mostafahussien.rx_demo.Utilities.DividerItemDecoration;
import com.example.mostafahussien.rx_demo.Utilities.RecyclerClickListener;

import java.util.ArrayList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainView{
    private NoteAdapter mAdapter;
    private List<Note> notesList;
    private MainPresenter presenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_empty_notes_view)
    TextView noNotesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });
        presenter=new MainPresenterImp(this);
        presenter.onCreate();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.addOnItemTouchListener(new RecyclerClickListener(this,
                recyclerView, new RecyclerClickListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

    }
    @Override
    public void initAdapter() {
        notesList = new ArrayList<>();
        mAdapter=new NoteAdapter(this,notesList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void deleteNote(int position) {
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);
        Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateNote(int position, Note note) {
        notesList.set(position, note);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void createNote(Note note) {
        notesList.add(0,note);
        mAdapter.notifyItemInserted(0);
    }

    @Override
    public void createFailed() {
        Toast.makeText(getApplicationContext(), "Enter note text", Toast.LENGTH_LONG).show();
    }

    @Override
    public void checkEmptyList() {
        if (notesList.size() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListAdapter(List<Note> notes) {
        notesList.clear();
        notesList.addAll(notes);
        mAdapter.notifyDataSetChanged();
    }


    private void showActionsDialog(int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    presenter.deleteNote(notesList.get(position).getId(), position);
                }
            }
        });
        builder.show();
    }



    public void showNoteDialog(final boolean shouldUpdate, final Note note, final int position){
        LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
        View view=inflater.inflate(R.layout.note_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final EditText inputNote=view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate? getString(R.string.lbl_new_note_title):"Edit Note");
        if(shouldUpdate&&note==null){
            inputNote.setText(note.getNote());
        }
        builder.setCancelable(false).setPositiveButton(shouldUpdate?"Update note":"Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {

            }
        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // shouldUpdate note by it's id
                    presenter.updateNote(note.getId(), inputNote.getText().toString(), position, note);
                } else {
                    // create new note
                    presenter.createNote(inputNote.getText().toString());
                }
            }
        });
    }

}