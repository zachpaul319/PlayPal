package com.example.playpalapp.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playpalapp.ServiceClient;
import com.example.playpalapp.model.types.AuthRequest;
import com.example.playpalapp.model.types.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactModel {
    public interface GetContactsResponseHandler {
        void response(List<Contact> contactList);
        void error();
    }

    public interface DeleteContactResponseHandler {
        void response();
        void error();
    }

    public void getContacts(Context context, int userId, GetContactsResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/contacts/" + Integer.toString(userId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Type returnedContactList = new TypeToken<ArrayList<Contact>>() {}.getType();
                try {
                    List<Contact> contactList = gson.fromJson(response.get("data").toString(), returnedContactList);
                    handler.response(contactList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.error();
            }
        });
        ServiceClient client = ServiceClient.sharedServiceClient(context);
        client.addRequest(jsonObjectRequest);
    }

    public void deleteContact(Context context, int userId, int contactId, DeleteContactResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.DELETE, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/contacts/" + Integer.toString(userId) + "/" + Integer.toString(contactId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handler.response();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.error();
            }
        });
        ServiceClient client = ServiceClient.sharedServiceClient(context);
        client.addRequest(jsonObjectRequest);
    }
}
