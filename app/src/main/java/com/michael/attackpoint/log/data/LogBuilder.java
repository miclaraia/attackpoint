package com.michael.attackpoint.log.data;

import android.util.Log;

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
 * Created by michael on 4/15/16.
 */
public class LogBuilder {

    //splits document into activity entries and strips of confounding elements
    //returns list of LogInfo objects
    public List<LogInfo> getLog(Document document) {
        ArrayList<LogInfo> logList = new ArrayList<>();

        // split log into days
        Elements days = document.getElementsByAttributeValue("class", "tlday");
        for (Element day : days) {

            // split day into sessions (hours)
            Elements sessionGroups = day.select(".tlssh");
            for (Element session : sessionGroups) {

                // split session into individual entries
                Elements logEntries = session.getElementsByAttributeValue("class", "tlactivity");
                for (Element logEntry : logEntries) {

                    //checks if item is a comment
                    if (logEntry.select(".descrowtype1").size() > 0){
                        /*LogInfo last = liList.get(liList.size() - 1);

                        //adds comment to previous activity
                        Element c = activity.select(".descrowtype1").first();
                        Element a = c.getElementsByTag("a").first();
                        String id = a.attr("href").split("_")[1];
                        String title = a.text().substring(4);
                        last.addComment(title, id);*/
                    } else {
                        // not a comment
                        LogInfo info = getLogEntry(logEntry, day, session);
                        if (info != null) {
                            logList.add(info);
                        }
                    }
                }
            }
        }

        return logList;
    }

    //gets meta data and text from an activity in the html
    public LogInfo getLogEntry(Element logEntry, Element day, Element session) {
        try {
            String type = getActivity(logEntry);

            //ignores event entries
            if (type.equals("Event:")) {
                return null;
            } else {
                Element meta = logEntry.getElementsByTag("p").first();

                // special case when entry is a note
                if (type.equals("Note")) {
                    LogInfo note = new Note(type);
                    note = getDescription(logEntry, note);
                    return note;
                } else {

                    LogInfo logInfo = new LogInfo();
                    logInfo = getActivity(logEntry, logInfo);
                    logInfo = getClimb(meta, logInfo);
                    logInfo = getColor(logEntry, logInfo);
                    logInfo = getDate(day, session, logInfo);
                    logInfo = getDescription(logEntry, logInfo);
                    logInfo = getDistance(meta, logInfo);
                    logInfo = getDuration(meta, logInfo);
                    logInfo = getIntensity(meta, logInfo);

                    logInfo.setPace();
                    // TODO set ID from edit link

                    return logInfo;
                }
            }
        } catch (NullPointerException e) {
            // TODO usually caused by splits
            e.printStackTrace();
            return null;
        }
    }

    // ++++ Activity ++++
    protected LogInfo getActivity(Element element, LogInfo logInfo) {
        LogInfoActivity item = getActivity(element, new LogInfoActivity());
        logInfo.set(LogInfo.KEY_ACTIVITY, item);
        return logInfo;
    }

    public LogInfoActivity getActivity(Element logEntry, LogInfoActivity logActivity) throws NullPointerException {
        String type = logEntry.getElementsByTag("b").first().text();
        logActivity.set(type);

        return logActivity;
    }

    public String getActivity(Element logEntry) throws NullPointerException {
        String type = logEntry.getElementsByTag("b").first().text();
        return type;
    }

    // ++++ Climb ++++
    protected LogInfo getClimb(Element element, LogInfo logInfo) {
        LogClimb item = getClimb(element, new LogClimb());
        logInfo.set(LogInfo.KEY_CLIMB, item);
        return logInfo;
    }

    public LogClimb getClimb(Element meta, LogClimb logClimb) {
        Elements span = meta.select("span[title*=\"climb\"");
        if (span.size() > 0) {
            String climbString = span.first().text();

            char[] cArray = climbString.toCharArray();
            int l = cArray.length;
            for (int i = l - 1; i > 1; i--) {
                char c = cArray[i];
                if (c >= '0' && c <= '9') {
                    int climb = Integer.parseInt(climbString.substring(1, i + 1));
                    String unit = climbString.substring(i + 1, l);
                    logClimb.set(new LogClimb.Climb(climb, unit));

                    Log.v("CLIMB", logClimb.toString());
                    return logClimb;
                }
            }
        }
        return logClimb;
    }

    // ++++ Color ++++
    protected LogInfo getColor(Element element, LogInfo logInfo) {
        LogColor item = getColor(element, new LogColor());
        logInfo.set(LogInfo.KEY_COLOR, item);
        return logInfo;
    }

    public LogColor getColor(Element logEntry, LogColor logColor) {
        Element actcolor = logEntry.getElementsByAttributeValue("class", "actcolor").first();
        String c = actcolor.attr("style");
        c = c.split(":")[1];

        int color = LogColor.parseColor(c);
        logColor.set(color);

        return logColor;
    }

    // ++++ Date ++++
    public LogInfo getDate(Element day, Element session, LogInfo logInfo) {
        LogDate item = getDate(day, new LogDate());
        item = getSession(session, item);
        logInfo.set(LogInfo.KEY_DATE, item);
        return logInfo;
    }

    public LogDate getDate(Element day, LogDate logDate) {
        Element d = day.getElementsByTag("a").first();
        String[] permalink = d.attr("href").split("/");
        String dateString = permalink[permalink.length - 1];

        try {
            Calendar cal = LogDate.parseLogDate(dateString);
            logDate.setDate(cal);
            return logDate;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public LogDate getSession(Element session, LogDate logDate) {
        Element sessionStart = session.select(".sessionstart").first();
        try {
            int hour = LogDate.parseLogSession(sessionStart.text());
            logDate.setSession(hour);
        } catch (ParseException e) {
            e.printStackTrace();
            logDate.setSession(0);
        } catch (NullPointerException e) {
            logDate.setSession(0);
        }

        return logDate;
    }

    // ++++ Description ++++
    protected LogInfo getDescription(Element element, LogInfo logInfo) {
        LogDescription item = getDescription(element, new LogDescription());
        logInfo.set(LogInfo.KEY_DESCRIPTION, item);
        return logInfo;
    }

    public LogDescription getDescription(Element logEntry, LogDescription logDescription) {
        Element element = logEntry.select(".descrow:not(.privatenote)").first();
        element = stripWhitespace(element);

        String text = element.html();

        // checks for empty description
        if (!text.equals("")) {
            logDescription.set(text);
        }
        return logDescription;
    }

    // ++++ Distance ++++
    protected LogInfo getDistance(Element element, LogInfo logInfo) {
        LogDistance item = getDistance(element, new LogDistance());
        logInfo.set(LogInfo.KEY_DISTANCE, item);
        return logInfo;
    }

    protected LogDistance getDistance(Element meta, LogDistance logDistance) {
        String metaString = meta.toString();
        String[] split = metaString.split(" ");

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("km") || split[i].equals("mi")) {
                char dArray[] = split[i-1].toCharArray();
                String d = "";

                for (int j = 0; j < dArray.length; j++) {
                    char c = dArray[j];
                    if (c == '.' || c >= '0' && c <= '9') {
                        d += c;
                    }
                }
                if (!d.equals("")) {
                    float distance = Float.parseFloat(d);
                    logDistance.set(new LogDistance.Distance(distance, split[i]));
                }
                break;
            }
        }
        return logDistance;
    }

    // ++++ Duration ++++
    protected LogInfo getDuration(Element element, LogInfo logInfo) {
        LogDuration item = getDuration(element, new LogDuration());
        logInfo.set(LogInfo.KEY_DURATION, item);
        return logInfo;
    }

    public LogDuration getDuration(Element meta, LogDuration logDuration) {
        String s = meta.getElementsByAttributeValue("xclass", "i0").first().text();
        try {
            Calendar cal = LogDuration.parseLog(s);
            logDuration.set(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return logDuration;
    }

    // ++++ Intensity ++++
    protected LogInfo getIntensity(Element element, LogInfo logInfo) {
        LogIntensity item = getIntensity(element, new LogIntensity());
        logInfo.set(LogInfo.KEY_INTENSITY, item);
        return logInfo;
    }

    public LogIntensity getIntensity(Element meta, LogIntensity logIntensity) {
        String i = meta.getElementsByAttributeValueStarting("title", "intensity").first().text();
        if (!i.equals("")) {
            int intensity = Integer.parseInt(i);
            logIntensity.set(intensity);
        }
        return logIntensity;
    }

    // ++++ utils ++++
    public static Element stripWhitespace(Element element) {
        List<Node> nodes = element.childNodes();
        List<Element> remove = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Log.v("whitespace", node.toString());
            if (node instanceof TextNode) {
                String text = ((TextNode) node).text();
                if (text.equals("") || text.equals("\r") || text.equals(" ")) continue;
                else break;
            } else if (node instanceof Element) {
                Element e = (Element) node;
                if (e.tagName().equals("br")) remove.add(e);
                else break;
            } else break;
        }

        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            Log.v("whitespace", node.toString());
            if (node instanceof TextNode) {
                String text = ((TextNode) node).text();
                if (text.equals("") || text.equals("\r") || text.equals(" ")) continue;
                else break;
            } else if (node instanceof Element) {
                Element e = (Element) node;
                if (e.tagName().equals("br")) remove.add(e);
                else break;
            } else break;
        }

        for (Element r : remove) {
            r.remove();
        }

        return element;
    }
}
