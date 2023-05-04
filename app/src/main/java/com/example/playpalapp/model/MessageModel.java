package com.example.playpalapp.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playpalapp.ServiceClient;
import com.example.playpalapp.model.types.AuthRequest;
import com.example.playpalapp.model.types.Message;
import com.example.playpalapp.model.types.NewMessageRequest;
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

    public interface GetLatestMessageResponseHandler {
        void response(Message message);
        void error();
    }

    public interface SendMessageResponseHandler {
        void response(int messageId);
        void error();
    }

    public interface DeleteMessageResponseHandler {
        void response();
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

    public void getLatestMessage(Context context, int userId, int contactId, GetLatestMessageResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/messages/" + Integer.toString(userId) + "/" + Integer.toString(contactId) + "/-1", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    Message message = gson.fromJson(response.get("data").toString(), Message.class);
                    handler.response(message);
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

    public void sendMessage(Context context, NewMessageRequest newMessageRequest, SendMessageResponseHandler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(newMessageRequest);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.POST, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/messages/", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Type returnedMessageId = new TypeToken<Integer>() {}.getType();
                try {
                    int messageId = gson.fromJson(response.get("data").toString(), returnedMessageId);
                    handler.response(messageId);
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

    public void deleteMessage(Context context, int messageId, DeleteMessageResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.DELETE, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/messages/" + Integer.toString(messageId), null, new Response.Listener<JSONObject>() {
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
