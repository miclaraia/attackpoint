package com.michael.attackpoint.log.data;

import android.util.ArrayMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loglist.LogFragment;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/17/16.
 */
public class LogCacheApiImpl  {
    private LogDatabase mLogDatabase;
    private RequestQueue mRequestQueue;

    public LogCacheApiImpl() {
        mLogDatabase = new LogDatabase();
        mRequestQueue = Volley.newRequestQueue(Singleton.getInstance().getContext());
    }


}
