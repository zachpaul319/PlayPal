package com.example.playpalapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playpalapp.model.Contact;
import com.example.playpalapp.model.Message;
import com.example.playpalapp.model.MessageModel;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements ContactsAdapter.ContactsAdapterDelegate {
    RecyclerView recyclerView;
    List<Contact> contacts;
    int userId;
    ContactsAdapter contactsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        userId = getArguments().getInt("userId");
        contacts = (List<Contact>) getArguments().getSerializable("contactList");

        recyclerView = view.findViewById(R.id.contacts_recycler_view);
        contactsAdapter = new ContactsAdapter(contacts);
        contactsAdapter.delegate = this;
        recyclerView.setAdapter(contactsAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void didSelectContact(int position, View view) {
        int contactId = contacts.get(position).userId;
        String contactName = contacts.get(position).username;

        MessageModel messageModel = new MessageModel();
        messageModel.getMessages(getContext(), userId, contactId, new MessageModel.GetMessagesResponseHandler() {
            @Override
            public void response(List<Message> messageList) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                bundle.putInt("contactId", contactId);
                bundle.putString("contactName", contactName);
                bundle.putSerializable("messageList", (Serializable) messageList);
                Navigation.findNavController(view).navigate(R.id.action_contactsFragment_to_messagesFragment, bundle);
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    public void didHoldContact(int position) {
        int x = 5;
    }
}