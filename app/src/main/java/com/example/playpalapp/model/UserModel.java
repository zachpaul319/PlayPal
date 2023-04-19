package com.example.playpalapp.model;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playpalapp.ServiceClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    public interface GetUserResponseHandler {
        void response(User user);
        void error();
    }

    public void getUser(String username, String password, GetUserResponseHandler handler) {
        JsonObjectRequest request = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    User user = gson.fromJson(response.get("data").toString(), User.class);
                    handler.response(user);
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
        AuthRequest.username = username;
        AuthRequest.password = password;
        ServiceClient client = ServiceClient.sharedServiceClient(null);
        client.addRequest(request);

    }
}
