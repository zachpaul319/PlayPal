package com.example.playpalapp.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.playpalapp.ServiceClient;
import com.example.playpalapp.model.types.AuthRequest;
import com.example.playpalapp.model.types.NewUserRequest;
import com.example.playpalapp.model.types.UpdateUserProductionsRequest;
import com.example.playpalapp.model.types.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
    public interface GetUserResponseHandler {
        void response(User user);
        void error();
    }

    public interface GetAllUsersResponseHandler {
        void response(List<User> userList);
        void error();
    }

    public interface CreateUserResponseHandler {
        void response(int status);
        void error();
    }

    public interface UpdateUserProductionsResponseHandler {
        void response();
        void error();
    }

    public interface DeleteUserResponseHandler {
        void response();
        void error();
    }

    public void getUserByAuth(Context context, String username, String password, GetUserResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/", null, new Response.Listener<JSONObject>() {
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
        ServiceClient client = ServiceClient.sharedServiceClient(context);
        client.addRequest(jsonObjectRequest);
    }

    public void getUserById(Context context, int userId, GetUserResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/" + Integer.toString(userId), null, new Response.Listener<JSONObject>() {
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
        ServiceClient client = ServiceClient.sharedServiceClient(context);
        client.addRequest(jsonObjectRequest);
    }

    public void getAllUsers(Context context, GetAllUsersResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.GET, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/-1", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Type returnedUserList = new TypeToken<ArrayList<User>>() {}.getType();
                try {
                    List<User> userList = gson.fromJson(response.get("data").toString(), returnedUserList);
                    handler.response(userList);
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

    public void createUser(Context context, NewUserRequest newUserRequestObject, CreateUserResponseHandler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(newUserRequestObject);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Type responseStatus = new TypeToken<Integer>() {}.getType();
                int status = 0;
                try {
                    status = gson.fromJson(response.get("status").toString(), responseStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.response(status);
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

    public void updateUserProductions(Context context, int userId, UpdateUserProductionsRequest updateUserProductionsRequest, UpdateUserProductionsResponseHandler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(updateUserProductionsRequest);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.PUT, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/" + Integer.toString(userId), jsonObject, new Response.Listener<JSONObject>() {
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

    public void deleteUser(Context context, int userId, DeleteUserResponseHandler handler) {
        JsonObjectRequest jsonObjectRequest = new AuthRequest(Request.Method.DELETE, "https://mopsdev.bw.edu/~zpaul20/playpal/www/rest.php/users/" + Integer.toString(userId), null, new Response.Listener<JSONObject>() {
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
