package com.example.quoteapp;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.*;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {
    RequestQueue queue;
    StringRequest stringRequest;
    String TAG = "getQuote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView dateTv = (TextView) findViewById(R.id.dateTv);
        TextView quoteTv = (TextView) findViewById(R.id.quoteTv);
        TextView authorTv = (TextView) findViewById(R.id.authorTv);

        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate = dateFormat.format(currentDate);

        dateTv.setText("Hello! Today is\n" + strDate);

        queue = Volley.newRequestQueue(this);
        String url ="https://quotes-app-backend.herokuapp.com/getrandomquote";
        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String jsonString = response ; //assign your JSON String here
                        JSONObject obj = null;
                        String quote = "";
                        String author = "";
                        try {
                            obj = new JSONObject(jsonString);
                             quote = obj.getString("Quote");
                             author = obj.getString("Author");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            quote = "That didn't work!";
                            author = "Error!";
                        }
                        quoteTv.setText(quote);
                        authorTv.setText(author);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                quoteTv.setText("That didn't work!");
                authorTv.setText("Error!");
            }
        });

        stringRequest.setTag(TAG);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}