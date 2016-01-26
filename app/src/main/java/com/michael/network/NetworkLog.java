package com.michael.network;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.adapters.LogAdapter;
import com.michael.attackpoint.loginfo.Distance;
import com.michael.attackpoint.loginfo.LogInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/16/15.
 */
public class NetworkLog extends Request<List<LogInfo>> {
    private static final String DEBUG_TAG = "attackpoint.NetworkLog";
    private static final String BASE_URL = "http://www.attackpoint.org/log.jsp/user_";


    private static final String TYPE_LOG = "viewlog.jsp";

    public Document document;
    private ArrayList<LogInfo> logInfoList;
    private LogAdapter recycler;
    private Singleton singleton;
    private static CookieManager cookies;

    private final Response.Listener<List<LogInfo>> mListener;

    public NetworkLog(int userID, Response.Listener<List<LogInfo>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, BASE_URL + userID, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<List<LogInfo>> parseNetworkResponse(NetworkResponse response) {
        List<LogInfo> activities;
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            activities = getActivities(Jsoup.parse(data));
        } catch (Exception e) {
            e.printStackTrace();
            activities = new ArrayList();
        }
        return Response.success(activities, HttpHeaderParser.parseCacheHeaders(response));

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

    //gets meta data and text from an activity in the html
    //returns LogInfo object
    public LogInfo getActivity(Element activity) {
        String type = activity.getElementsByTag("b").first().text();

        if (type.equals("Event:")) {
            return new LogInfo();
        } else {
            Element meta = activity.getElementsByTag("p").first();

            String text = activity.select(".descrow:not(.privatenote)").first().html();
            // TODO fix this
            if (type.equals("Note")) {
                LogInfo details = new LogInfo();
                details.setType(type);
                details.setText(text);
                return details;
            } else {
                String time = meta.getElementsByAttributeValue("xclass", "i0").first().text();
                String intensity = meta.getElementsByAttributeValueStarting("title", "intensity").first().text();
                Distance distance = getMetaDistance(meta);

                String color = getActivityColor(activity);

                LogInfo details = new LogInfo();
                details.setType(type);
                details.setTime(time);;
                details.setIntensity(intensity);

                if (!distance.isEmpty()) {
                    details.setDistance(distance);
                    details.setPace();
                }

                details.setText(text);
                details.setColor(color);

                return details;
            }
        }
    }

    //splits document into activity entries and strips of confounding elements
    //returns list of LogInfo objects
    public ArrayList<LogInfo> getActivities(Document soup) {
        ArrayList<LogInfo> li = new ArrayList<LogInfo>();
        Elements days = soup.getElementsByAttributeValue("class", "tlday");

        for (Element day : days) {
            String date = day.getElementsByTag("h3").first().text();

            Elements activities = day.getElementsByAttributeValue("class", "tlactivity");
            for (Element activity : activities) {
                if (activity.select(".descrowtype1").size() > 0) continue;
                LogInfo info = getActivity(activity);
                info.setDate(date);
                li.add(info);
            }
        }

        return li;
    }

    //gets the activity distance. returns null if nonexistant
    public Distance getMetaDistance(Element meta) {
        String metaString = meta.toString();
        String[] split = metaString.split(" ");

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("km") || split[i].equals("mi")) {
                char dArray[] = split[i-1].toCharArray();
                String d = "";

                for (int j = 0; j < dArray.length; j++) {
                    char c = dArray[j];
                    if (c == '.' || c >= '0' && c <= '9') {
                        Log.d(DEBUG_TAG, ""+c);
                        d += c;
                    }
                }

                Distance distance = new Distance(d, split[i]);
                return distance;
            }
        }
        return new Distance();
    }

    //gets the color of the activity
    public String getActivityColor(Element activity) {
        Element actcolor = activity.getElementsByAttributeValue("class", "actcolor").first();
        String color = actcolor.attr("style");
        color = color.split(":")[1];
        return color;
    }
}

