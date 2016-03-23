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
import static org.junit.Assert.fail;

/**
 * Created by michael on 3/21/16.
 */
public class LogIntensityTest {
    private static final Integer EMPTY = 0;
    private static final Integer NONEMPTY = 3;

    private LogIntensity mLogIntensity;

    @Before
    public void setup() {
        mLogIntensity = new LogIntensity();
    }

    @Test
    public void setup_getSetItem() {
        mLogIntensity.set(EMPTY);
        assertThat(mLogIntensity.get(), equalTo(EMPTY));
        mLogIntensity.set(NONEMPTY);
        assertThat(mLogIntensity.get(), equalTo(NONEMPTY));
    }

    @Test
    public void isEmpty_true() {
        mLogIntensity.set(EMPTY);
        assertTrue(mLogIntensity.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogIntensity.set(NONEMPTY);
        assertFalse(mLogIntensity.isEmpty());
    }

    @Test
    public void toString_returnString() {
        mLogIntensity.set(NONEMPTY);
        assertThat(mLogIntensity.toString(), equalTo(NONEMPTY.toString()));
    }

    @Test
    public void toJSON_containskeys() {
        mLogIntensity.set(NONEMPTY);
        JSONObject json = mLogIntensity.toJSON(new JSONObject());

        assertTrue(json.has(LogIntensity.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        mLogIntensity.set(NONEMPTY);
        JSONObject json = mLogIntensity.toJSON(new JSONObject());
        Integer intensity = json.getInt(LogIntensity.JSON);

        assertThat(intensity, equalTo(NONEMPTY));
    }

    @Test
    public void fromJSON_setsData() {
        mLogIntensity.set(NONEMPTY);
        JSONObject json = mLogIntensity.toJSON(new JSONObject());
        mLogIntensity.set(EMPTY);

        mLogIntensity.fromJSON(json);

        Assert.assertThat(mLogIntensity.get(), equalTo(NONEMPTY));
    }
}