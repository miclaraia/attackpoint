package com.michael.network;

import android.text.format.Time;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.CalendarTime;
import com.michael.attackpoint.log.loginfo.Date;
import com.michael.attackpoint.log.loginfo.Duration;
import com.michael.attackpoint.log.loginfo.LogInfo;

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
        mParams.put(FIELD_MONTH, "" + d.get(Calendar.MONTH));
        mParams.put(FIELD_DAY, "" + d.get(Calendar.DAY_OF_MONTH));

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
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }


    @Override
    protected Response<Boolean> parseNetworkResponse(NetworkResponse networkResponse) {
        return null;
    }

    @Override
    protected void deliverResponse(Boolean success) {
        mListener.onResponse(success);
    }
}
