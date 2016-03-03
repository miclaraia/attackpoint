package com.michael.attackpoint.training;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 2/26/16.
 */
public class TrainingTypeRequest extends Request<Map<String, Integer>> {
    private static final String URL = "http://www.attackpoint.org/newtraining.jsp";

    private Response.Listener<Map<String, Integer>> mListener;

    public TrainingTypeRequest(int userID, Response.Listener<Map<String, Integer>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<Map<String, Integer>> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String responseString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            Map<String, Integer> response = getMap(Jsoup.parse(responseString));
            
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(Map<String, Integer> response) {
        mListener.onResponse(response);
    }

    //++++++++++++++++++++   JSOUP   ++++++++++++++++++++
    private Map<String, Integer> getMap(Document soup) {
        Element menu = soup.getElementById("activitytypeid");
        Elements options = menu.getElementsByTag("option");
        Map<String, Integer> map = new HashMap<>();
        for (Element option : options) {
            String key = option.text();
            Integer value = Integer.parseInt(option.attr("value"));
            map.put(key, value);
        }

        return map;
    }
}
