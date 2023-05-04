package com.example.playpalapp;

import static com.example.playpalapp.ContactsFragment.contacts;
import static com.example.playpalapp.ContactsFragment.contactsAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.playpalapp.model.types.Contact;
import com.example.playpalapp.model.types.Message;
import com.example.playpalapp.model.MessageModel;
import com.example.playpalapp.model.types.NewMessageRequest;
import com.example.playpalapp.tools.Toaster;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements MessagesAdapter.MessagesAdapterDelegate{
    static RecyclerView recyclerView;
    static MessagesAdapter messagesAdapter;
    static List<Message> messages;

    MessageUpdateThread messageUpdateThread;
    EditText messageEditText;
    TextView nameMessageTop;
    ImageButton sendButton;
    int userId, contactId, contactPosition;
    String contactName, lastText, lastMessageTimeStamp;;
    boolean isFirstMessage = false;

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
    public void onStop() {
        super.onStop();
        messageUpdateThread.interrupt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        userId = getArguments().getInt("userId");
        contactId = getArguments().getInt("contactId");
        contactName = getArguments().getString("contactName");
        messages = (List<Message>) getArguments().getSerializable("messageList");

        if (messages.size() > 0) {
            lastText = messages.get(messages.size() - 1).text;
            lastMessageTimeStamp = messages.get(messages.size() - 1).timestamp;
        } else {
            lastText = "";
            lastMessageTimeStamp = "";
        }

        if (getArguments().containsKey("isFirstMessage")) {
            isFirstMessage = true;
        }

        if (getArguments().containsKey("contactPosition")) {
            contactPosition = getArguments().getInt("contactPosition");
        } else {
            contactPosition = 0;
        }

        recyclerView = view.findViewById(R.id.messages_recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);

        messageUpdateThread = new MessageUpdateThread(getContext(), getActivity(), userId, contactId, contactPosition, lastText, lastMessageTimeStamp);
        messageUpdateThread.start();

        messagesAdapter = new MessagesAdapter(messages, userId);
        messagesAdapter.delegate = this;
        recyclerView.setAdapter(messagesAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        nameMessageTop = view.findViewById(R.id.name_messages_top);
        nameMessageTop.setText(contactName);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEditText.getText().toString();
                if (!text.equals("")) {
                    NewMessageRequest newMessageRequest = new NewMessageRequest(userId, contactId, text);
                    MessageModel messageModel = new MessageModel();
                    messageModel.sendMessage(getContext(), newMessageRequest, new MessageModel.SendMessageResponseHandler() {
                        @Override
                        public void response(int messageId) {
                            addMessage(messageId, userId, contactId, text);

                            if (isFirstMessage) {
                                addContact(contactId, contactName, text);

                                isFirstMessage = false;
                            }

                            editContact(text, contactPosition, 0);

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

    @Override
    public void didSelectMessage(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Message");
        builder.setMessage("Are you sure you want to delete this message?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int messageId = messages.get(position).messageId;

                MessageModel messageModel = new MessageModel();
                messageModel.deleteMessage(getContext(), messageId, new MessageModel.DeleteMessageResponseHandler() {
                    @Override
                    public void response() {
                        removeMessage(position);
                        editContact(messages.get(messages.size()-1).text, contactPosition, contactPosition);
                        Toaster.showToast(getContext(), "Message deleted");
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

    private void addMessage(int messageId, int senderId, int recipientId, String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(new Message(messageId, senderId, recipientId, text, null));
                messagesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });
    }

    private void removeMessage(int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.remove(position);
                messagesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });
    }

    private void addContact(int contactId, String contactName, String text) {
        contacts.add(0, new Contact(contactId, contactName, text));
        contactsAdapter.notifyDataSetChanged();
    }

    private void editContact(String lastMessage, int currentPosition, int newPosition) {
        Contact editedContact = contacts.get(currentPosition);
        contacts.remove(currentPosition);

        editedContact.text = lastMessage;
        contacts.add(newPosition, editedContact);
        contactsAdapter.notifyDataSetChanged();
    }
}