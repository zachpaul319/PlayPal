package com.example.playpalapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.playpalapp.model.Message;
import com.example.playpalapp.model.MessageModel;
import com.example.playpalapp.model.NewMessageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messages;
    int userId, contactId;
    MessagesAdapter messagesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        userId = getArguments().getInt("userId");
        //contactId = getArguments().getInt("contactId");
        contactId = 10;
        messages = (List<Message>) getArguments().getSerializable("messageList");

        recyclerView = view.findViewById(R.id.recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);

        messagesAdapter = new MessagesAdapter(messages, userId);
        recyclerView.setAdapter(messagesAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEditText.getText().toString();
                if (!text.equals("")) {
                    NewMessageRequest newMessageRequest = new NewMessageRequest(userId, contactId, text);
                    MessageModel messageModel = new MessageModel();
                    messageModel.sendMessage(newMessageRequest, new MessageModel.SendMessageResponseHandler() {
                        @Override
                        public void response(int messageId) {
                            addMessage(messageId, userId, contactId, text);
                            messageEditText.setText("");
                        }

                        @Override
                        public void error() {
                            Toaster.showToast(getContext(), "Message not sent");
                        }
                    });
                }
            }
        });
        return view;
    }

    private void addMessage(int messageId, int senderId, int recipientId, String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(new Message(messageId, senderId, recipientId, text));
                messagesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });
    }
}