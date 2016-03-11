package com.michael.attackpoint.training.activity;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.training.request.AddTrainingRequest;
import com.michael.attackpoint.training.details.ViewHolder;

/**
 * Created by michael on 3/11/16.
 */
public class AddTrainingActivity extends TrainingActivity {
    private static final String DEBUG_TAG = "add_training";


    @Override
    protected LogInfo initLogInfo() {
        return new LogInfo();
    }

    @Override
    protected Managers initManagers(ViewHolder vh) {
        return new Managers(this, vh);
    }

    @Override
    protected Request performRequest(LogInfo li) {
        Request request = new AddTrainingRequest(li, new Response.Listener<LogInfo>() {
            @Override
            public void onResponse(LogInfo response) {
                Log.d(DEBUG_TAG, response.toString());
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
        Log.d(DEBUG_TAG, "finished creating training request");
        return request;
    }
}
