package com.michael.attackpoint.training;

import android.text.format.Time;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.CalendarTime;
import com.michael.attackpoint.log.loginfo.Date;
import com.michael.attackpoint.log.loginfo.Duration;
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 2/24/16.
 */
public class AddTrainingRequest extends Request<Boolean> {
    private static final String DEBUG_TAG = "ap.trainingrequest";
    private static final String URL = "http://www.attackpoint.org/addtraining.jsp";
    private static final String FIELD_MAP_NAME = "NAME";
    private static final Map<String, String> FIELD_TYPE;
    static {
        Map<String, String> map = new HashMap<>();
        map.put(FIELD_MAP_NAME, "workouttypeid");
        map.put("\"Training\"", "1");
        map.put("Race", "2");
        map.put("Long", "3");
        map.put("Intervals", "4");
        map.put("Hills", "5");
        map.put("Tempo", "6");
        map.put("Warm up/down", "7");
        map.put("[None]", "null");

        FIELD_TYPE = Collections.unmodifiableMap(map);

    }
    private static final String FIELD_MONTH = "session-month";
    private static final String FIELD_DAY = "session-day";
    private static final String FIELD_YEAR = "session-year";
    // TODO attackpoint uses id# instead of the name
    private static final String FIELD_ACTIVITY = "activitytypeid";
    private static final String FIELD_ACTIVITY_NEW = "newactivitytype";
    private static final String FIELD_ACTIVITY_SUBTYPE = "activitymodifiers";
    private static final String FIELD_DURATION = "sessionlength";
    private static final String FIELD_DISTANCE = "distance";
    private static final String FIELD_UNITS = "distanceunits";
    private static final String FIELD_CLIMB = "climb";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_INTENSITY = "intensity";

    private Map<String, String> mParams;
    private Response.Listener mListener;
    private Singleton mSingleton;

    public AddTrainingRequest(LogInfo training,
                        Response.Listener<Boolean> listener,
                        Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, errorListener);
        mListener = listener;
        mSingleton = Singleton.getInstance();

        LogInfo.Strings strings = training.strings();
        mParams = new HashMap<String, String>();

        // Date
        Calendar d = training.getDate().getDate();
        mParams.put(FIELD_YEAR, "" + d.get(Calendar.YEAR));
        mParams.put(FIELD_MONTH, "" + (new SimpleDateFormat("MM").format(d.getTime())));
        mParams.put(FIELD_DAY, "" + (new SimpleDateFormat("dd").format(d.getTime())));

        // Activity type
        mParams.put(FIELD_ACTIVITY, "" + 76863);

        // Duration
        String duration = training.getDuration().toFormString();
        mParams.put(FIELD_DURATION, duration);

        // Distance
        String distance = "" + training.getDistance().distance;
        mParams.put(FIELD_DISTANCE, distance);

        // Units
        mParams.put(FIELD_UNITS, "kilometers");

        // Description
        mParams.put(FIELD_DESCRIPTION, strings.text);

        // Intensity
        mParams.put(FIELD_INTENSITY, strings.intensity);

        mParams.put("session_length", "500");
        mParams.put("workouttypeid", "1");
        mParams.put("isplan", "0");
        mParams.put("sessionstarthour", "-1");
        mParams.put("shoes", "null");


    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<String, String>();
        params.put("Content-Type","application/x-www-form-urlencoded");
        return params;
    }


    @Override
    protected Response<Boolean> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d(DEBUG_TAG, networkResponse.toString());
        return Response.success(true, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(Boolean success) {
        mListener.onResponse(success);
    }
}