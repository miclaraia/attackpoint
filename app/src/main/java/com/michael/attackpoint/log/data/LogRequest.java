package com.michael.attackpoint.log.data;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.attackpoint.log.loginfo.LogClimb;
import com.michael.attackpoint.log.loginfo.LogColor;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogIntensity;
import com.michael.attackpoint.log.loginfo.Note;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by michael on 8/16/15.
 */
public class LogRequest extends com.android.volley.Request<List<LogInfo>> {
    private static final String DEBUG_TAG = "attackpoint.Request";
    private static final String BASE_URL = "http://www.attackpoint.org/log.jsp/user_";

    private final Response.Listener<List<LogInfo>> mListener;

    public LogRequest(int userID, Response.Listener<List<LogInfo>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, BASE_URL + userID, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<List<LogInfo>> parseNetworkResponse(NetworkResponse response) {
        List<LogInfo> logList;
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogBuilder builder = new LogBuilder();
            logList = builder.getLog(Jsoup.parse(data));

            return Response.success(logList, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }

    }

    @Override
    protected void deliverResponse(List<LogInfo> response) {
        mListener.onResponse(response);
    }

    private static String buildURL(String id) {
        String[] pieces = {BASE_URL, id};
        return TextUtils.join("/", pieces);
    }

    //++++++++++++++++++++   JSOUP   ++++++++++++++++++++


}

