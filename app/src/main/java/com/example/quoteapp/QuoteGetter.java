package com.example.quoteapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class QuoteGetter {
    private String url = "https://quotes-app-backend.herokuapp.com/getrandomquote";
    private RequestQueue queue;
    private static QuoteGetter instance;
    private static Context ctx;

    private QuoteGetter(Context context) {
        ctx = context;
        queue = getRequestQueue();

    }

    public static synchronized QuoteGetter getInstance(Context context) {
        if (instance == null) {
            instance = new QuoteGetter(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return queue;
    }

//    public Map<String,String> getRandomQuote(){
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        String jsonString = response ; //assign your JSON String here
//                        JSONObject obj = null;
//                        String quote = "";
//                        String author = "";
//                        try {
//                            obj = new JSONObject(jsonString);
//                            quote = obj.getString("Quote");
//                            author = obj.getString("Author");
//                        } catch (JSONException e) {
//                            Log.i("Printing stack trace","Printing stack trace");
//                            e.printStackTrace();
//                            quote = "That didn't work!";
//                            author = "Error!";
//                        }
//                        quoteTv.setText(quote);
//                        authorTv.setText(author);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("FAIL","BAD");
//                quoteTv.setText("That didn't work!");
//                authorTv.setText("Error!");
//            }
//        });
//
//        stringRequest.setTag(TAG);
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
}
