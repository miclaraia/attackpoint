package com.michael.attackpoint.training.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 2/24/16.
 */
public class AddTrainingRequest extends Request<LogInfo> {
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
    private static final String FIELD_SESSION = "sessionstarthour";
    private static final String FIELD_ACTIVITY = "activitytypeid";
    private static final String FIELD_ACTIVITY_NEW = "newactivitytype";
    private static final String FIELD_ACTIVITY_SUBTYPE = "activitymodifiers";
    private static final String FIELD_DURATION = "sessionlength";
    private static final String FIELD_DISTANCE = "distance";
    private static final String FIELD_UNITS = "distanceunits";
    private static final String FIELD_CLIMB = "climb";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_INTENSITY = "intensity";

    protected LogInfo mLogInfo;
    private Response.Listener mListener;
    private Singleton mSingleton;

    public AddTrainingRequest(LogInfo li,
                        Response.Listener<LogInfo> listener,
                        Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, errorListener);
        mListener = listener;
        mSingleton = Singleton.getInstance();

        mLogInfo = li;
    }

    public AddTrainingRequest(LogInfo li, String url,
                              Response.Listener<LogInfo> listener,
                              Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        mListener = listener;
        mSingleton = Singleton.getInstance();

        mLogInfo = li;
    }

    public Map<String, String> createParams(LogInfo li) {
        LogInfo.Strings strings = li.strings();
        Map<String, String> params = new HashMap<>();

        // Date
        Calendar d = (Calendar) li.get(LogInfo.KEY_DATE).get();
        params.put(FIELD_YEAR, "" + d.get(Calendar.YEAR));
        params.put(FIELD_MONTH, "" + (new SimpleDateFormat("MM").format(d.getTime())));
        params.put(FIELD_DAY, "" + (new SimpleDateFormat("dd").format(d.getTime())));

        // Session
        LogSession session = (LogSession) li.get(LogInfo.KEY_SESSION);
        params.put(FIELD_SESSION, session.toFormString());

        // Activity type
        Integer activity = ((LogInfoActivity) li.get(LogInfo.KEY_ACTIVITY)).getID();
        params.put(FIELD_ACTIVITY, activity.toString());

        // Duration
        String duration = li.get(LogInfo.KEY_DURATION).toFormString();
        params.put(FIELD_DURATION, duration);

        // Distance
        LogDistance ld = (LogDistance) li.get(LogInfo.KEY_DISTANCE);
        if (!ld.isEmpty()) {
            params.put(FIELD_DISTANCE, ld.get().getDistance().toString());
            params.put(FIELD_UNITS, ld.get().getUnit().toString());
        }

        // Description
        if (!li.get(LogInfo.KEY_DESCRIPTION).isEmpty())
            params.put(FIELD_DESCRIPTION, strings.description);

        // Intensity
        params.put(FIELD_INTENSITY, strings.intensity);

        params.put("workouttypeid", "1");
        params.put("isplan", "0");
        //params.put("sessionstarthour", "-1");
        params.put("shoes", "null");

        return params;
    }

    @Override
    public Map<String, String> getParams() {
        return createParams(mLogInfo);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<String, String>();
        params.put("Content-Type","application/x-www-form-urlencoded");
        return params;
    }

    @Override
    protected Response<LogInfo> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d(DEBUG_TAG, networkResponse.toString());
        return Response.success(mLogInfo, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(LogInfo success) {
        mListener.onResponse(success);
    }
}
