package com.example.playpalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playpalapp.model.types.Note;
import com.example.playpalapp.model.NoteModel;
import com.example.playpalapp.tools.Toaster;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment implements NotesAdapter.NotesAdapterDelegate {
    RecyclerView recyclerView;
    List<Note> notes;
    int userId;
    NotesAdapter notesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        userId = getArguments().getInt("userId");
        notes = (List<Note>) getArguments().getSerializable("notesList");

        recyclerView = view.findViewById(R.id.notes_recycler_view);
        notesAdapter = new NotesAdapter(notes);
        notesAdapter.delegate = this;
        recyclerView.setAdapter(notesAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        view.findViewById(R.id.addNewNoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                bundle.putSerializable("notesList", (Serializable) notes);
                Navigation.findNavController(view).navigate(R.id.action_notesFragment_to_newNoteFragment, bundle);
            }
        });

        return view;
    }

    @Override
    public void didSelectNote(int position, View view) {
        Note note = notes.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        bundle.putInt("position", position);
        bundle.putParcelable("note", note);
        bundle.putSerializable("notesList", (Serializable) notes);
        Navigation.findNavController(view).navigate(R.id.action_notesFragment_to_updateNoteFragment, bundle);
    }

    @Override
    public void didHoldNote(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int noteId = notes.get(position).noteId;

                NoteModel noteModel = new NoteModel();
                noteModel.deleteNote(getContext(), noteId, new NoteModel.DeleteNoteResponseHandler() {
                    @Override
                    public void response() {
                        removeNote(position);
                        Toaster.showToast(getContext(), "Note deleted");
                    }

                    @Override
                    public void error() {
                        Toaster.showToast(getContext(), "Couldn't delete message");
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void removeNote(int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notes.remove(position);
                notesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(notesAdapter.getItemCount());
            }
        });
    }
}