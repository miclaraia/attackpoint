package com.michael.attackpoint.log;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
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
        try {
            LogInfoActivity type = getType(activity);
            LogDescription text = getDescription(activity);

            //ignores events
            if (type.get().equals("Event:")) {
                return null;
            } else {
                Element meta = activity.getElementsByTag("p").first();

                // special case when entry is a note
                if (type.get().equals("Note")) {
                    LogInfo details = new Note(type.get(), text.get());
                    return details;
                } else {

                    LogInfo details = new LogInfo();
                    details.set(LogInfo.KEY_DURATION, getDuration(meta));
                    details.set(LogInfo.KEY_DISTANCE, getDistance(meta));
                    details.set(LogInfo.KEY_INTENSITY, getIntensity(meta));
                    details.set(LogInfo.KEY_COLOR, getColor(activity));
                    details.set(LogInfo.KEY_CLIMB, getClimb(meta));
                    details.set(LogInfo.KEY_ACTIVITY, type);
                    details.set(LogInfo.KEY_DESCRIPTION, text);

                    details.setPace();

                    return details;
                }
            }
        } catch (NullPointerException e) {
            // TODO usually caused by splits
            e.printStackTrace();
            Log.d(DEBUG_TAG, activity.toString().replace("\r","").replace("\n",""));
            return null;
        }
    }

    //splits document into activity entries and strips of confounding elements
    //returns list of LogInfo objects
    public ArrayList<LogInfo> getActivities(Document soup) {
        ArrayList<LogInfo> liList = new ArrayList<LogInfo>();
        Elements days = soup.getElementsByAttributeValue("class", "tlday");

        for (Element day : days) {
            LogDate date = getDate(day);

            Elements activities = day.getElementsByAttributeValue("class", "tlactivity");
            for (Element activity : activities) {
                //checks if this row is a comment
                if (activity.select(".descrowtype1").size() > 0){
                    LogInfo last = liList.get(liList.size() - 1);

                    //adds comment to previous activity
                    /*Element c = activity.select(".descrowtype1").first();
                    Element a = c.getElementsByTag("a").first();
                    String id = a.attr("href").split("_")[1];
                    String title = a.text().substring(4);
                    last.addComment(title, id);*/
                } else {
                    LogInfo info = getActivity(activity);
                    if (info != null) {
                        info.set(LogInfo.KEY_DATE, date);

                        liList.add(info);
                    }
                }
            }
        }

        return liList;
    }

    //gets the activity distance. returns null if nonexistant
    private LogDistance getDistance(Element meta) {
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

                LogDistance ld = new LogDistance();
                if (!d.equals("")) {
                    float distance = Float.parseFloat(d);
                    ld.set(new LogDistance.Distance(distance, split[i]));
                }

                return ld;
            }
        }
        return new LogDistance();
    }

    public LogDuration getDuration(Element meta) {
        String s = meta.getElementsByAttributeValue("xclass", "i0").first().text();
        LogDuration ld = new LogDuration();
        try {
            Calendar cal = LogDuration.parseLog(s);
            ld.set(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ld;
    }

    public LogIntensity getIntensity(Element meta) {
        String i = meta.getElementsByAttributeValueStarting("title", "intensity").first().text();
        LogIntensity li = new LogIntensity();
        if (!i.equals("")) {
            int intensity = Integer.parseInt(i);
            li.set(intensity);
        }
        return li;
    }

    public LogInfoActivity getType(Element activity) throws NullPointerException {
        String type = activity.getElementsByTag("b").first().text();

        LogInfoActivity la = new LogInfoActivity();
        la.set(type);

        return la;
    }

    /**
     * Gets climb from meta data if it exists
     * @param meta
     * @return
     */
    public LogClimb getClimb(Element meta) {
        Elements span = meta.select("span[title*=\"climb\"");
        LogClimb lc = new LogClimb();
        if (span.size() > 0) {
            String climbString = span.first().text();

            char[] cArray = climbString.toCharArray();
            int l = cArray.length;
            for (int i = l - 1; i > 1; i--) {
                char c = cArray[i];
                if (c >= '0' && c <= '9') {
                    int climb = Integer.parseInt(climbString.substring(1, i + 1));
                    String unit = climbString.substring(i + 1, l);
                    lc.set(new LogClimb.Climb(climb, unit));

                    Log.d("CLIMB", lc.toString());
                    break;
                }
            }
        }
        return lc;
    }

    public LogDescription getDescription(Element activity) {
        Element element = activity.select(".descrow:not(.privatenote)").first();
        element = stripWhitespace(element);

        String text = element.html();
        LogDescription ld = new LogDescription();

        // checks for empty description
        if (!text.equals("")) {
            ld.set(text);
        }
        return ld;
    }

    public LogDate getDate(Element day) {
        Element d = day.getElementsByTag("a").first();
        String[] permalink = d.attr("href").split("/");
        String dateString = permalink[permalink.length - 1];

        LogDate ld = new LogDate();
        try {
            Calendar cal = LogDate.parseLog(dateString);
            ld.set(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return ld;
        }
    }

    //gets the color of the activity
    public LogColor getColor(Element activity) {
        Element actcolor = activity.getElementsByAttributeValue("class", "actcolor").first();
        String c = actcolor.attr("style");
        c = c.split(":")[1];

        int color = LogColor.parseColor(c);
        LogColor lc = new LogColor();
        lc.set(color);

        return lc;
    }

    public static Element stripWhitespace(Element element) {
        List<Node> nodes = element.childNodes();
        List<Element> remove = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Log.d("whitespace", node.toString());
            if (node instanceof TextNode) {
                String text = ((TextNode) node).text();
                if (text.equals("") || text.equals("\r") || text.equals(" ")) continue;
                else break;
            } else if (node instanceof Element) {
                Element e = (Element) node;
                if (e.tagName().equals("br")) remove.add(e);
            } else break;
        }

        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            Log.d("whitespace", node.toString());
            if (node instanceof TextNode) {
                String text = ((TextNode) node).text();
                if (text.equals("") || text.equals("\r") || text.equals(" ")) continue;
                else break;
            } else if (node instanceof Element) {
                Element e = (Element) node;
                if (e.tagName().equals("br")) remove.add(e);
            } else break;
        }

        for (Element r : remove) {
            r.remove();
        }

        return element;
    }
}

