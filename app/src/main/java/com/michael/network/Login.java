package com.michael.network;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.Singleton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 8/18/15.
 */
public class Login extends AsyncTask<String, Void, String> {
    private static final String DEBUG_TAG = "attackpoint.Login";
    private Singleton singleton = Singleton.getInstance();

    /*public void login() {
        String url = "http://www.attackpoint.org/dologin.jsp";
        AttackpointRequest apRequest = new AttackpointRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(DEBUG_TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username","miclaraia");
                params.put("password","opahans3");
                //params.put("returl", "returl:http%3A%2F%2Fwww.attackpoint.org%2F");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                Log.d(DEBUG_TAG, params.toString());
                return params;
            }

            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                Map headers = response.headers;
                String cookie = headers.get("Set-Cookie").toString();
                Singleton.getInstance().getPreferences().saveCookie(cookie);
                return super.parseNetworkResponse(response);
            }
        };

        // Add the request to the queue
        singleton.add(apRequest);
    }*/

    @Override
    protected String doInBackground(String... params) {
        InputStream is = connect();
        String s = null;
        try {
            s = InputStreamtoString(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return s;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // todo write cookie to preferences
        Log.d(DEBUG_TAG,"onPostExecute");
        Log.d(DEBUG_TAG,"" + result);
    }

    private InputStream connect() {
        try {
            URL url = new URL("http://www.attackpoint.org/dologin.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);

            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("username", "miclaraia"));
            form.add(new BasicNameValuePair("password", "123456"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(form));
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            Map<String, List<String>> headers = conn.getHeaderFields();

            //TODO REMOVE TEMP
            singleton.getCookie().setCookie(headers);
            //TODO REMOVE

            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String findCookie(Map<String, List<String>> headers) {
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                //todo remove temp string
                String temp = cookie.split("=")[0];
                if (cookie.split("=")[0] == "login") return cookie;
            }
        }
        return null;
    }

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

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
