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

public class NoteModel {
    public interface GetNotesResponseHandler {
        void response(List<Note> notesList);
        void error();
    }

    public interface AddNewNoteResponseHandler {
        void response(int noteId);
        void error();
    }

    public interface UpdateNoteResponseHandler {
        void response();
        void error();
    }

    public interface DeleteNoteResponseHandler {
        void response();
        void error();
    }

    public void getNotes(Context context, int userId, GetNotesResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/notes/" + Integer.toString(userId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Type returnedNoteList = new TypeToken<ArrayList<Note>>() {}.getType();
                try {
                    List<Note> notesList = gson.fromJson(response.get("data").toString(), returnedNoteList);
                    handler.response(notesList);
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

    public void addNewNote(Context context, NewNoteRequest newNoteRequest, AddNewNoteResponseHandler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(newNoteRequest);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.POST, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/notes/", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Type returnedNoteId = new TypeToken<Integer>() {}.getType();
                try {
                    int noteId = gson.fromJson(response.get("data").toString(), returnedNoteId);
                    handler.response(noteId);
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

    public void updateNote(Context context, int noteId, UpdateNoteRequest updateNoteRequest, UpdateNoteResponseHandler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(updateNoteRequest);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.PUT, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/notes/" + Integer.toString(noteId), jsonObject, new Response.Listener<JSONObject>() {
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

    public void deleteNote(Context context, int noteId, DeleteNoteResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.DELETE, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/notes/" + Integer.toString(noteId), null, new Response.Listener<JSONObject>() {
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
