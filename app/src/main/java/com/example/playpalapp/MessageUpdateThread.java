package com.example.playpalapp;

import static com.example.playpalapp.MessagesFragment.messages;
import static com.example.playpalapp.MessagesFragment.messagesAdapter;
import static com.example.playpalapp.MessagesFragment.recyclerView;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.playpalapp.model.Message;
import com.example.playpalapp.model.MessageModel;

public class MessageUpdateThread extends Thread implements Runnable {
    private Context context;
    private Activity activity;
    private int userId, contactId;
    private String lastText, lastMessageTimestamp;

    private static final long INTERVAL = 7500;
    private boolean running = true;

    public MessageUpdateThread(Context context, Activity activity, int userId, int contactId, String lastText, String lastMessageTimestamp) {
        this.context = context;
        this.activity = activity;

        this.userId = userId;
        this.contactId = contactId;
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
}
