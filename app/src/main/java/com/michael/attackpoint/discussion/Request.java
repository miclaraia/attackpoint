package com.michael.attackpoint.discussion;

import android.text.Html;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by michael on 1/27/16.
 */
public class Request extends com.android.volley.Request<Discussion> {
    private static final String DEBUG_TAG = "discussion.request";
    private static final String BASE_URL = "http://www.attackpoint.org/discussionthread.jsp/message_";

    private final Response.Listener<Discussion> mListener;
    private int mId;

    public Request(int id, Response.Listener<Discussion> listener,
                       Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(id), errorListener);
        mListener = listener;
        mId = id;
    }

    @Override
    protected Response<Discussion> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Discussion discussion = getDiscussion(Jsoup.parse(data));
            
            return Response.success(discussion, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(Discussion discussion) {
        mListener.onResponse(discussion);
    }

    private static String buildUrl(int id) {
        return BASE_URL + id;
    }

    //++++++++++++++++++++   JSOUP   ++++++++++++++++++++

    private Discussion getDiscussion(Element html) {
        //title of discussion
        String title = html.getElementsByTag("h1").first().text();

        //category of discussion
        Element main = html.getElementById("contents");
        String category = getCategory(main);

        Discussion discussion = new Discussion(mId, title, category);

        // gets comments and adds them to the discussion
        Elements comments = main.select("#messages div.discussion_post");
        for (Element item : comments) {
            Comment comment = getComment(item);
            discussion.addComment(comment);
        }

        return discussion;
    }

    /**
     * tries to find category that this discussion is in
     * @param main
     * @return
     */
    private String getCategory(Element main) {
        Elements text = main.getElementsByTag("p");
        for (Element p : text) {
            String s = p.text();
            if (s.startsWith("in: ") && s.endsWith(";")) {
                // returns string between leading 'in: ' and trailing ';'
                return s.substring(4, s.length() - 2);
            }
        }

        return "";
    }

    private Comment getComment(Element c) {
        //gets id of comment
        String id_string = c.id().split("message")[1];
        int id = Integer.parseInt(id_string);

        // gets body text of comment
        Element t = c.select("discussion_post_body").first();
        String text = t.html();
        text = Html.fromHtml(text).toString();

        // gets username and user id from comment link to user
        Element u = c.select(".discussion_post_name a").first();
        String username = u.text();
        String uid = u.attr("href").split("_")[1];
        int user = Integer.parseInt(uid);

        // gets gmt timestamp from 'datetime' attribute
        String timestamp = c.select("span.discussion_post_time").first().attr("datetime");

        Comment comment = new Comment(text, mId, user, username, timestamp);
        return comment;
    }
}
