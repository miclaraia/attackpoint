package com.michael.attackpoint.training.activity;

import android.content.Intent;

import com.android.volley.Request;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.training.details.ViewHolder;

/**
 * Created by michael on 3/11/16.
 */
public class UpdateTrainingActivity extends TrainingActivity {
    public static final String INTENT_JSON = "json";

    @Override
    protected LogInfo initLogInfo() {
        Intent intent = getIntent();
        String json = intent.getStringExtra(INTENT_JSON);
        return new LogInfo();
    }

    @Override
    protected Managers initManagers(ViewHolder vh) {
        return new Managers(this, vh, mLogInfo);
    }

    @Override
    protected Request performRequest(LogInfo li) {
        return null;
    }
}
