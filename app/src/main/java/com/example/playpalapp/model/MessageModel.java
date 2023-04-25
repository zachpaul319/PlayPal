package com.example.playpalapp.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playpalapp.ServiceClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageModel {
    public interface GetMessagesResponseHandler {
        void response(List<Message> messageList);
        void error();
    }

    public void getMessages(Context context, int userId, int contactId, GetMessagesResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/messages/" + Integer.toString(userId) + "/" + Integer.toString(contactId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Type returnedMessageList = new TypeToken<ArrayList<Message>>() {}.getType();
                try {
                    List<Message> messageList = gson.fromJson(response.get("data").toString(), returnedMessageList);
                    handler.response(messageList);
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
}
