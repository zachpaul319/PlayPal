package com.example.playpalapp.model.types;

import android.util.Base64;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AuthRequest extends JsonObjectRequest {
    public static String username = "";
    public static String password = "";

    public AuthRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String credential = String.format("%s:%s", username, password);
        String encoded = Base64.encodeToString(credential.getBytes(), 0);

        headers.put("Authorization", "Basic " + encoded);
        return headers;
    }
}
