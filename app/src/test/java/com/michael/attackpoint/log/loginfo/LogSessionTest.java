package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 3/21/16.
 */
public class LogSessionTest {
    private static final Integer EMPTY = 0;
    private static final Integer NONEMPTY = 16;
    private static final String NONEMPTY_STRING = "04:00 PM";
    private static final String NONEMPTY_JSON = "16:00";

    private LogSession mLogSession;

    private static Calendar getSession(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        return cal;
    }

    @Before
    public void setup() {
        mLogSession = new LogSession();
    }

    @Test
    public void getSetTest() {
        mLogSession.set(getSession(EMPTY));
        assertThat(mLogSession.get().get(Calendar.HOUR_OF_DAY), equalTo(EMPTY));

        mLogSession.set(getSession(NONEMPTY));
        assertThat(mLogSession.get().get(Calendar.HOUR_OF_DAY), equalTo(NONEMPTY));
    }

    @Test
    public void isEmpty_True() {
        mLogSession.set(getSession(EMPTY));
        assertTrue(mLogSession.isEmpty());
    }

    @Test
    public void isEmpty_False() {
        mLogSession.set(getSession(NONEMPTY));
        assertFalse(mLogSession.isEmpty());
    }

    @Test
    public void toString_Test() {
        mLogSession.set(getSession(NONEMPTY));
        assertThat(mLogSession.toString(), equalTo(NONEMPTY_STRING));
    }

    @Test
    public void toString_EmptyTest() {
        mLogSession.set(getSession(EMPTY));
        assertThat(mLogSession.toString(), equalTo(""));
    }

    @Test
    public void toFormString_Test() {
        mLogSession.set(getSession(NONEMPTY));
        assertThat(mLogSession.toFormString(), equalTo(NONEMPTY.toString()));
    }

    @Test
    public void toFormString_EmptyTest() {
        mLogSession.set(getSession(EMPTY));
        assertThat(mLogSession.toFormString(), equalTo("-1"));
    }

    @Test
    public void toJSON_containskeys() {
        mLogSession.set(getSession(NONEMPTY));
        JSONObject json = mLogSession.toJSON(new JSONObject());

        assertTrue(json.has(LogSession.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        Calendar cal = getSession(NONEMPTY);
        mLogSession.set(cal);
        JSONObject json = mLogSession.toJSON(new JSONObject());
        String session = json.getString(LogSession.JSON);

        assertThat(session, equalTo(NONEMPTY_JSON));
    }

    @Test
    public void fromJSON_setsData() {
        Calendar cal = getSession(NONEMPTY);
        mLogSession.set(cal);
        JSONObject json = mLogSession.toJSON(new JSONObject());
        mLogSession.set(getSession(EMPTY));

        mLogSession.fromJSON(json);

        Assert.assertThat(mLogSession.get().get(Calendar.HOUR_OF_DAY),
                equalTo(NONEMPTY));
    }
}