package com.example.playpalapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.playpalapp.model.NewNoteRequest;
import com.example.playpalapp.model.Note;
import com.example.playpalapp.model.NoteModel;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewNoteFragment extends Fragment {
    int userId;
    String title, content;
    List<Note> notes;
    EditText titleInputField, descriptionInputField;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewNoteFragment newInstance(String param1, String param2) {
        NewNoteFragment fragment = new NewNoteFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        userId = getArguments().getInt("userId");
        notes = (List<Note>) getArguments().getSerializable("notesList");

        titleInputField = view.findViewById(R.id.titleInputField);
        descriptionInputField = view.findViewById(R.id.descriptionInputField);



        view.findViewById(R.id.saveNoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleInputField.getText().toString();
                content = descriptionInputField.getText().toString();
                NewNoteRequest newNoteRequest = new NewNoteRequest(userId, title, content);

                NoteModel noteModel = new NoteModel();
                noteModel.addNewNote(newNoteRequest, new NoteModel.AddNewNoteResponseHandler() {
                    @Override
                    public void response(int noteId) {
                        notes.add(0, new Note(noteId, userId, title, content));
                        Bundle bundle = new Bundle();
                        bundle.putInt("userId", userId);
                        bundle.putSerializable("notesList", (Serializable) notes);
                        Navigation.findNavController(view).navigate(R.id.action_newNoteFragment_to_notesFragment, bundle);
                    }

                    @Override
                    public void error() {
                        Toaster.showToast(getContext(), "Could not add new note");
                    }
                });
            }
        });

        return view;
    }
}