package com.michael.attackpoint.log.loginfo;

import com.google.android.gms.maps.internal.MapLifecycleDelegate;

import org.hamcrest.MatcherAssert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by michael on 3/21/16.
 */
public class LogDurationTest {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HHmmss");
    private static final String EMPTY = "000000";
    private static final String NONEMPTY = "043546";
    private static final String NONEMPTY_STRING = "4:35:46";
    private static final String NONEMPTY_JSON = "04:35:46";
    private static final String NONEMPTY_2 = "000001";
    private static final String NONEMPTY_2_STRING = "1";
    private static final String NONEMPTY_3 = "001000";
    private static final String NONEMPTY_3_STRING = "10:00";

    private LogDuration mLogDuration;

    private static final Calendar getCal(String time) {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        try {
            cal.setTime(FORMAT.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
        return cal;
    }

    @Before
    public void setup() {
        mLogDuration = new LogDuration();
    }

    @Test
    public void set_getSetItem() {
        mLogDuration.set(getCal(NONEMPTY));
        assertThat(FORMAT.format(mLogDuration.get().getTime()), equalTo(NONEMPTY));

        mLogDuration.set(getCal(EMPTY));
        assertThat(FORMAT.format(mLogDuration.get().getTime()), equalTo(EMPTY));
    }

    @Test
    public void isEmpty_true() {
        mLogDuration.set(getCal(EMPTY));
        assertTrue(mLogDuration.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogDuration.set(getCal(NONEMPTY));
        assertFalse(mLogDuration.isEmpty());

        mLogDuration.set(getCal(NONEMPTY_2));
        assertFalse(mLogDuration.isEmpty());

        mLogDuration.set(getCal(NONEMPTY_3));
        assertFalse(mLogDuration.isEmpty());
    }

    @Test
    public void toString_returnsString() {
        mLogDuration.set(getCal(NONEMPTY));
        assertThat(mLogDuration.toString(), equalTo(NONEMPTY_STRING));

        mLogDuration.set(getCal(NONEMPTY_2));
        assertThat(mLogDuration.toString(), equalTo(NONEMPTY_2_STRING));

        mLogDuration.set(getCal(NONEMPTY_3));
        assertThat(mLogDuration.toString(), equalTo(NONEMPTY_3_STRING));
    }

    @Test
    public void toFormString() {
        mLogDuration.set(getCal(NONEMPTY));
        assertThat(mLogDuration.toFormString(), equalTo(NONEMPTY));

        mLogDuration.set(getCal(NONEMPTY_2));
        assertThat(mLogDuration.toFormString(), equalTo(NONEMPTY_2));

        mLogDuration.set(getCal(NONEMPTY_3));
        assertThat(mLogDuration.toFormString(), equalTo(NONEMPTY_3));
    }

    @Test
    public void toJSON_containskeys() {
        mLogDuration.set(getCal(NONEMPTY));
        JSONObject json = mLogDuration.toJSON(new JSONObject());

        assertTrue(json.has(LogDuration.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        mLogDuration.set(getCal(NONEMPTY));
        JSONObject json = mLogDuration.toJSON(new JSONObject());
        String duration = json.getString(LogDuration.JSON);

        assertThat(duration, equalTo(NONEMPTY_JSON));
    }

    @Test
    public void fromJSON_setsData() {
        Calendar cal = getCal(NONEMPTY);
        mLogDuration.set(cal);
        JSONObject json = mLogDuration.toJSON(new JSONObject());
        mLogDuration.set(getCal(EMPTY));

        mLogDuration.fromJSON(json);

        Assert.assertThat(mLogDuration.get(), equalTo(cal));
    }

    @Test
    public void parseLog_parsesLogString() throws ParseException {
        String test;

        test = FORMAT.format(LogDuration.parseLog(NONEMPTY_STRING).getTime());
        assertThat(test, equalTo(NONEMPTY));

        test = FORMAT.format(LogDuration.parseLog(NONEMPTY_2_STRING).getTime());
        assertThat(test, equalTo(NONEMPTY_2));

        test = FORMAT.format(LogDuration.parseLog(NONEMPTY_3_STRING).getTime());
        assertThat(test, equalTo(NONEMPTY_3));
    }
}