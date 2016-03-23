package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 3/21/16.
 */
public class LogPaceTest {

    private LogPace mLogPace;

    @Before
    public void setup() {
        mLogPace = new LogPace();
    }
}