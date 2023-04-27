package com.example.playpalapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playpalapp.model.Contact;
import com.example.playpalapp.model.User;

import java.util.List;
import java.util.Random;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    public interface ContactsAdapterDelegate {
        void didSelectContact(int position, View view);
    }

    ContactsAdapterDelegate delegate;

    List<Contact> contactList;

    public ContactsAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int position) {
        String name = contactList.get(position).username;
        String message = contactList.get(position).text;
        String divider = "------------------------------------------------------------------------------";

        holder.setData(selectRandomColor(), name, message, divider);

        holder.itemView.setOnClickListener(v -> {
            if (delegate != null) {
                delegate.didSelectContact(position, holder.itemView);
            }
        });
    }

    private int selectRandomColor() {
        int[] COLORS = {Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GRAY, Color.LTGRAY, Color.WHITE};

        int rnd = new Random().nextInt(COLORS.length);
        return COLORS[rnd];
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView coloredLogo;
        TextView contactName, lastMessage, line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coloredLogo = itemView.findViewById(R.id.colored_logo);
            contactName = itemView.findViewById(R.id.contact_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            line = itemView.findViewById(R.id.line);
        }

        private void setData(int color, String name, String message, String divider) {
            coloredLogo.setBackgroundColor(color);
            contactName.setText(name);
            lastMessage.setText(message);
            line.setText(divider);
        }
    }
}
