package com.example.playpalapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.playpalapp.model.Note;
import com.example.playpalapp.model.NoteModel;
import com.example.playpalapp.model.UpdateNoteRequest;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateNoteFragment extends Fragment {
    int userId, position;
    String title, content;
    Note note;
    List<Note> notes;
    EditText updateTitleInputField, updateDescriptionInputField;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateNoteFragment newInstance(String param1, String param2) {
        UpdateNoteFragment fragment = new UpdateNoteFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_note, container, false);
        userId = getArguments().getInt("userId");
        position = getArguments().getInt("position");
        note = getArguments().getParcelable("note");
        notes = (List<Note>) getArguments().getSerializable("notesList");

        updateTitleInputField = view.findViewById(R.id.updateTitleInputField);
        updateDescriptionInputField = view.findViewById(R.id.updateDescriptionInputField);

        updateTitleInputField.setText(note.title);
        updateDescriptionInputField.setText(note.content);

        view.findViewById(R.id.updateNoteSaveChangesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = updateTitleInputField.getText().toString();
                content = updateDescriptionInputField.getText().toString();

                int noteId = note.noteId;
                UpdateNoteRequest updateNoteRequest = new UpdateNoteRequest(title, content);

                NoteModel noteModel = new NoteModel();
                noteModel.updateNote(getContext(), noteId, updateNoteRequest, new NoteModel.UpdateNoteResponseHandler() {
                    @Override
                    public void response() {
                        Note updatedNote = notes.get(position);
                        notes.remove(position);

                        updatedNote.title = title;
                        updatedNote.content = content;
                        notes.add(0, updatedNote);

                        Bundle bundle = new Bundle();
                        bundle.putInt("userId", userId);
                        bundle.putSerializable("notesList", (Serializable) notes);
                        Navigation.findNavController(view).navigate(R.id.action_updateNoteFragment_to_notesFragment, bundle);
                    }

                    @Override
                    public void error() {
                        Toaster.showToast(getContext(), "Could not update note");
                    }
                });
            }
        });

        return view;
    }
}