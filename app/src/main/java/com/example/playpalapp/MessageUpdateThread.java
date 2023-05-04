package com.example.playpalapp;

import static com.example.playpalapp.ContactsFragment.contacts;
import static com.example.playpalapp.ContactsFragment.contactsAdapter;
import static com.example.playpalapp.MessagesFragment.messages;
import static com.example.playpalapp.MessagesFragment.messagesAdapter;
import static com.example.playpalapp.MessagesFragment.recyclerView;

import android.app.Activity;
import android.content.Context;

import com.example.playpalapp.model.types.Contact;
import com.example.playpalapp.model.types.Message;
import com.example.playpalapp.model.MessageModel;
import com.example.playpalapp.tools.Toaster;

public class MessageUpdateThread extends Thread implements Runnable {
    private Context context;
    private Activity activity;
    private int userId, contactId, contactPosition;
    private String lastText, lastMessageTimestamp;

    private static final long INTERVAL = 4000;
    private boolean running = true;

    public MessageUpdateThread(Context context, Activity activity, int userId, int contactId, int contactPosition, String lastText, String lastMessageTimestamp) {
        this.context = context;
        this.activity = activity;

        this.userId = userId;
        this.contactId = contactId;
        this.contactPosition = contactPosition;
        this.lastText = lastText;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    @Override
    public void run() {
        while (running) {
            try {
                MessageModel messageModel = new MessageModel();
                messageModel.getLatestMessage(context, userId, contactId, new MessageModel.GetLatestMessageResponseHandler() {
                    @Override
                    public void response(Message message) {
                        if (contactId == message.senderId && ((!message.text.equals(lastText)) || (!message.timestamp.equals(lastMessageTimestamp)))) {
                            lastText = message.text;
                            lastMessageTimestamp = message.timestamp;

                            addMessage(activity, message.messageId, message.senderId, message.recipientId, message.text);
                            editContact(message.text, contactPosition);

                            Toaster.showToast(context, "New message");
                        }
                    }

                    @Override
                    public void error() {
                        Toaster.showToast(context, "An error occurred");
                    }
                });

                Thread.sleep(INTERVAL);
            } catch (Exception e) {
                running = false;
                e.printStackTrace();
            }
        }
    }

    private void addMessage(Activity activity, int messageId, int senderId, int recipientId, String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(new Message(messageId, senderId, recipientId, text, null));
                messagesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });
    }

    private void editContact(String lastMessage, int contactPosition) {
        Contact editedContact = contacts.get(contactPosition);
        contacts.remove(contactPosition);

        editedContact.text = lastMessage;
        contacts.add(0, editedContact);
        contactsAdapter.notifyDataSetChanged();
    }
}
