package com.example.quoteapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class QuotesWidget extends AppWidgetProvider {
    private static RequestQueue queue;
//    private static StringRequest stringRequest;
    private static String TAG = "getQuote";
    private static final String ACTION_SCHEDULED_UPDATE = "com.example.quoteapp.SCHEDULED_UPDATE";

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
                            Log.i("Printing stack trace","Printing stack trace");
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
                Log.i("ISSUE BAD",error.toString());

                views.setTextViewText(R.id.widget_quoteTv, "That didn't work!");
                views.setTextViewText(R.id.widget_authorTv, "Error!");
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        super.onEnabled(context);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SCHEDULED_UPDATE)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, QuotesWidget.class));
            onUpdate(context, manager, ids);
        }

        super.onReceive(context, intent);
    }
    private static void _scheduleNextUpdate(Context context) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Substitute AppWidget for whatever you named your AppWidgetProvider subclass
        Intent intent = new Intent(context, QuotesWidget.class);
        intent.setAction(ACTION_SCHEDULED_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Get a calendar instance for midnight tomorrow.
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        // Schedule one second after midnight, to be sure we are in the right day next time this
        // method is called.  Otherwise, we risk calling onUpdate multiple times within a few
        // milliseconds
        midnight.set(Calendar.SECOND, 1);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DAY_OF_YEAR, 1);

        // For API 19 and later, set may fire the intent a little later to save battery,
        // setExact ensures the intent goes off exactly at midnight.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
        }
    }
}