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
public class Login {
    private static final String DEBUG_TAG = "attackpoint.Login";
    //private Singleton singleton = Singleton.getInstance();
    private AuthCookie cookie;
    private Async async;

    public Login() {
        cookie = new AuthCookie();
        async = new Async();
    }

    public void login() {
        Log.d(DEBUG_TAG, "Logging in");
        if (!cookie.state()) {
            if (!async.state) {
                Log.d(DEBUG_TAG, "no previous login found, spawning new asynd");
                new Async().execute();
            } else Log.d(DEBUG_TAG, "no previous login found but async already running \n will wait");
        } else Log.d(DEBUG_TAG, "previous login found, must logout first");
    }

    public void logout() {
        Log.d(DEBUG_TAG, "logging out; clearing cookie");
        cookie.expire();
    }

    public boolean isLoggedIn() {
        return cookie.state();
    }






    private class Async extends AsyncTask<String, Void, Map<String, List<String>>> {
        private static final String AP_URL = "http://www.attackpoint.org/dologin.jsp";
        public boolean state;

        @Override
        protected Map<String, List<String>> doInBackground(String... params) {
            state = true;
            return connect();

        /*InputStream is = connect();
        String s = null;
        try {
            s = InputStreamtoString(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return s;
        }*/
        }

        @Override
        protected void onPostExecute(Map<String, List<String>> result) {
            state = false;
            // todo write cookie to preferences
            Log.d(DEBUG_TAG,"onPostExecute");
            Log.d(DEBUG_TAG,"" + result);

            cookie.setCookie(result);
        }

        //logs in to attackpoint and returns response header
        private Map<String, List<String>> connect() {
            try {
                //creates connection object and points to attackpoint URL
                URL url = new URL(AP_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);

                //login form data
                //TODO get login info from user input
                List<NameValuePair> form = new ArrayList<NameValuePair>();
                form.add(new BasicNameValuePair("username", "miclaraia"));
                form.add(new BasicNameValuePair("password", "123456"));

                //writes form data to connection
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                //close writer and outputstream
                writer.write(getQuery(form));
                writer.flush();
                writer.close();
                os.close();

                //connect
                conn.connect();
                Map<String, List<String>> headers = conn.getHeaderFields();
                return headers;

                //TODO REMOVE TEMP
                //singleton.getCookie().setCookie(headers);
                //TODO REMOVE

                //return conn.getInputStream();
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "Exception in Login.doInBackground");
                e.printStackTrace();
                return null;
            }
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

        /** takes form input data and encodes it for the request header
         * @param params items to be written as form data
         * @return form data to be put in header
         * @throws UnsupportedEncodingException
         */
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

}
