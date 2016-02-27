package com.michael.attackpoint.log;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.attackpoint.log.loginfo.LogClimb;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.Note;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/16/15.
 */
public class Request extends com.android.volley.Request<List<LogInfo>> {
    private static final String DEBUG_TAG = "attackpoint.Request";
    private static final String BASE_URL = "http://www.attackpoint.org/log.jsp/user_";

    private final Response.Listener<List<LogInfo>> mListener;

    public Request(int userID, Response.Listener<List<LogInfo>> listener, Response.ErrorListener errorListener) {
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

        //ignores events
        if (type.equals("Event:")) {
            return new LogInfo();
        } else {
            Element meta = activity.getElementsByTag("p").first();

            String text = activity.select(".descrow:not(.privatenote)").first().html();
            // special case when entry is a note
            if (type.equals("Note")) {
                LogInfo details = new Note(type, text);
                return details;
            } else {
                String duration = meta.getElementsByAttributeValue("xclass", "i0").first().text();
                String intensity = meta.getElementsByAttributeValueStarting("title", "intensity").first().text();
                LogDistance distance = getMetaDistance(meta);

                String color = getActivityColor(activity);

                LogClimb climb = getMetaClimb(meta);

                LogInfo details = new LogInfo();
                details.setType(type);
                details.setDuration(duration);;
                details.setIntensity(intensity);
                details.setClimb(climb);

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
            Element d = day.getElementsByTag("a").first();
            String[] permalink = d.attr("href").split("/");
            String date = permalink[permalink.length - 1];

            Elements activities = day.getElementsByAttributeValue("class", "tlactivity");
            for (Element activity : activities) {
                //checks if this row is a comment
                if (activity.select(".descrowtype1").size() > 0){
                    LogInfo last = li.get(li.size() - 1);
                    Element c = activity.select(".descrowtype1").first();

                    Element a = c.getElementsByTag("a").first();
                    String id = a.attr("href").split("_")[1];
                    String title = a.text().substring(4);

                    //adds comment to previous activity
                    last.addComment(title, id);
                } else {
                    LogInfo info = getActivity(activity);
                    info.setDate(date);
                    li.add(info);
                }
            }
        }

        return li;
    }

    //gets the activity distance. returns null if nonexistant
    public LogDistance getMetaDistance(Element meta) {
        String metaString = meta.toString();
        String[] split = metaString.split(" ");

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("km") || split[i].equals("mi")) {
                char dArray[] = split[i-1].toCharArray();
                String d = "";

                for (int j = 0; j < dArray.length; j++) {
                    char c = dArray[j];
                    if (c == '.' || c >= '0' && c <= '9') {
                        //Log.d(DEBUG_TAG, ""+c);
                        d += c;
                    }
                }

                LogDistance distance = new LogDistance(d, split[i]);
                return distance;
            }
        }
        return new LogDistance();
    }

    /**
     * Gets climb from meta data if it exists
     * @param meta
     * @return
     */
    public LogClimb getMetaClimb(Element meta) {
        Elements span = meta.select("span[title*=\"climb\"");
        if (span.size() > 0) {
            LogClimb climb = new LogClimb(span.first().text());
            Log.d("CLIMB", climb.toString());
            return climb;
        }
        return new LogClimb();
    }

    //gets the color of the activity
    public String getActivityColor(Element activity) {
        Element actcolor = activity.getElementsByAttributeValue("class", "actcolor").first();
        String color = actcolor.attr("style");
        color = color.split(":")[1];
        return color;
    }
}

