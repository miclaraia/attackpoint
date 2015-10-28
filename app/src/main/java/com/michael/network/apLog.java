package com.michael.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.michael.attackpoint.adapters.LogAdapter;
import com.michael.objects.Distance;
import com.michael.objects.LogInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by michael on 8/16/15.
 */
public class apLog extends apNet {
    private static final String DEBUG_TAG = "attackpoint.apLog";

    public Document document;
    private ArrayList<LogInfo> logInfoList;
    private LogAdapter recycler;

    public apLog(LogAdapter recycler) {
        this.recycler = recycler;
    }

    //gets activities from document and sets them to class list variable
    public void getLog() {
        StringRequest apRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<LogInfo> li = getActivities(Jsoup.parse(response));
                        logInfoList = li;
                        System.out.println(response.substring(0,100));
                        recycler.updateList(li);

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
        singleton.add(apRequest);
    }

    //gets meta data and text from an activity in the html
    //returns LogInfo object
    public LogInfo getActivity(Element activity) {
        Element meta = activity.getElementsByTag("p").first();

        String type = meta.getElementsByTag("b").first().text();
        String time = meta.getElementsByAttributeValue("xclass", "i0").first().text();
        String intensity = meta.getElementsByAttributeValueStarting("title", "intensity").first().text();
        Distance distance = getMetaDistance(meta);

        String text = activity.select(".descrow:not(.privatenote)").first().html();
        String color = getActivityColor(activity);


        LogInfo details = new LogInfo(type);
        details.time.set(time);
        details.intensity.set(intensity);
        details.distance.set(distance);
        if (distance != null) {
            details.pace.set(details.time.get(), distance);
        }
        details.setText(text);
        details.color.set(color);

        return details;
    }

    //splits document into activity entries and strips of confounding elements
    //returns list of LogInfo objects
    public ArrayList<LogInfo> getActivities(Document soup) {
        ArrayList<LogInfo> li = new ArrayList<LogInfo>();
        Elements activities = soup.getElementsByAttributeValue("class","tlactivity");
        for (Element activity : activities) {
            if (activity.select(".descrowtype1").size() > 0) continue;
            li.add(getActivity(activity));
        }
        return li;
    }

    //gets the activity distance. returns null if nonexistant
    public Distance getMetaDistance(Element meta) {
        String metaString = meta.toString();
        String[] split = metaString.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("km")) {
                Distance distance = new Distance(split[i - 1], split[i]);
                return distance;
            }
        }
        return null;
    }

    //gets the color of the activity
    public String getActivityColor(Element activity) {
        Element actcolor = activity.getElementsByAttributeValue("class", "actcolor").first();
        String color = actcolor.attr("style");
        color = color.split(":")[1];
        return color;
    }
}

