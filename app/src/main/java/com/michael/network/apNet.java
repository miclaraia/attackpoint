package com.michael.network;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.michael.attackpoint.MyAdapter;
import com.michael.attackpoint.Singleton;
import com.michael.objects.Distance;
import com.michael.objects.LogInfo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by michael on 8/5/15.
 */
public class apNet {
    private static final String DEBUG_TAG = "attackpoint.apNet";
    private static final int LOG = 0;

    Singleton singleton = Singleton.getInstance();
    private static final String loginURL = "http://www.attackpoint.org/dologin.jsp";
    //private static Map<String, String> cookies;
    private static CookieManager cookies;

    //String url = "http://www.attackpoint.org/log.jsp/user_11778";
    String url = "http://www.attackpoint.org/viewlog.jsp/user_11778/period-7/enddate-2015-08-02";
    int type = LOG;

    //TODO
    // login to attackpoint, setting the session cookie
    /*public void login(String user, String pass) {
        try {
            Connection.Response res = Jsoup
                    .connect(loginURL)
                    .data("username", user, "password", pass)
                    .method(Connection.Method.POST)
                    .execute();
            Map<String, String> cookies = res.cookies();
            android.util.apLog.d(DEBUG_TAG, cookies.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO handle error
        }
    }

    // GET connection to attackpoint, includes session cookie
    public Document connect(String url, Map<String, String> cookies) throws IOException {
        Document doc = Jsoup.connect(url).cookies(cookies).get();
        return doc;
    }

    // GET connection to attackpoint, no session cookie
    public Document connect(String url) throws IOException {
            Document doc = Jsoup.connect(url).get();
            return doc;
    }*/

    /*public String connect(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Result handling
                        System.out.println(response.substring(0,100));

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                        error.printStackTrace();
            }
        });

// Add the request to the queue
        singleton.add(stringRequest);
    }*/

    private InputStream getHTTP(URL url) throws IOException{
        InputStream is = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            android.util.Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            return is;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    //Converts InputStream to String
    private String InputStreamtoString(InputStream is) throws IOException {
        //Initiates buffer and StringBuilder
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        //Appends to StringBuilder line-by-line
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append((line + "\n"));
        }

        String s = sb.toString();
        android.util.Log.d(DEBUG_TAG, "html response: " + s);
        return s;
    }

    // generate url needed for transaction
    public void genURL() {

    }
}
