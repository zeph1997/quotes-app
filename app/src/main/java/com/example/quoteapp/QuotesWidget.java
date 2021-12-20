package com.example.quoteapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class QuotesWidget extends AppWidgetProvider {
    private static RequestQueue queue;
    private static StringRequest stringRequest;
    private static String TAG = "getQuote";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate = dateFormat.format(currentDate);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quotes_widget);

        views.setTextViewText(R.id.widget_dateTv, strDate);

        queue = Volley.newRequestQueue(context.getApplicationContext());
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
                        Log.i("CHECKER",quote);
                        Log.i("CHECKER",author);
                        views.setTextViewText(R.id.widget_quoteTv, quote);
                        views.setTextViewText(R.id.widget_authorTv, author);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("FAIL","BAD");
                views.setTextViewText(R.id.widget_quoteTv, "That didn't work!");
                views.setTextViewText(R.id.widget_authorTv, "Error!");
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });

        stringRequest.setTag(TAG);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}