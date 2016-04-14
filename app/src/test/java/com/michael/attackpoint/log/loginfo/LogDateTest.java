package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;

/**
 * Created by michael on 3/21/16.
 */
public class LogDateTest {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMdd-HH");
    private static final String DATE = "20160301-00";
    private static final String LOG_DATE_STRING = "enddate-2016-03-20";
    private static final String LOG_DATE = "20160320-00";

    private LogDate mLogDate;

    public static Calendar getCal(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(FORMATTER.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
        return cal;
    }

    @Before
    public void setUp() {
        mLogDate = new LogDate();
    }

    @Test
    public void isEmpty_false() {
        assertFalse(mLogDate.isEmpty());
    }

    @Test
    public void toString_nonemptySession() {
        Calendar cal = getCal("20160301-15");
        mLogDate.set(cal);

        String date = mLogDate.toString();
        assertThat(date, equalTo("Tue Mar 1 - 3 PM"));
    }

    @Test
    public void toString_emptySession() {
        Calendar cal = getCal("20160301-00");
        mLogDate.set(cal);

        String date = mLogDate.toString();
        assertThat(date, equalTo("Tue Mar 1"));
    }

    @Test
    public void toJSON_containskeys() {
        JSONObject json = new JSONObject();
        mLogDate.set(getCal(DATE));
        json = mLogDate.toJSON(json);

        assertTrue(json.has(LogDate.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat(LogDate.JSON_FORMAT);
        Calendar cal = getCal(DATE);

        mLogDate.set(cal);
        JSONObject json = mLogDate.toJSON(new JSONObject());
        String date = json.getString(LogDate.JSON);

        assertThat(date, equalTo(sdf.format(cal.getTime())));
    }

    @Test
    public void fromJSON_setsDate() {
        mLogDate.set(getCal(DATE));
        JSONObject json = mLogDate.toJSON(new JSONObject());
        String compare = mLogDate.toString();

        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        mLogDate.set(cal);

        mLogDate.fromJSON(json);

        assertThat(mLogDate.toString(), equalTo(compare));
    }

    @Test
    public void parseLogDate_parsesLogString() throws ParseException {
        String test = FORMATTER.format(LogDate.parseLogDate("enddate-2016-03-20").getTime());
        assertThat(test, equalTo("20160320-00"));
    }

    @Test
    public void parseLogSession_parsesLogString() throws ParseException {
        assertThat(LogDate.parseLogSession("7 AM"), equalTo(7));
        assertThat(LogDate.parseLogSession("7 PM"), equalTo(19));
    }

    @Test
    public void setSession_preservesDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = getCal("20160301-00");
        mLogDate.setDate(cal);

        String date_before = sdf.format(mLogDate.get().getTime());

        mLogDate.setSession(7);
        String date_after = sdf.format(mLogDate.get().getTime());

        assertThat(date_before, equalTo(date_after));
    }

    @Test
    public void setDate_preservesDate() throws ParseException {
        mLogDate.set(getCal("20160301-05"));
        mLogDate.setDate(getCal("20160301-00"));

        assertThat(mLogDate.getSession(), equalTo("5 AM"));
    }

    @Test
    public void getSession_formatsSession() {
        mLogDate.setSession(7);
        assertThat(mLogDate.getSession(), equalTo("7 AM"));

        mLogDate.setSession(19);
        assertThat(mLogDate.getSession(), equalTo("7 PM"));
    }

    @Test
    public void getSession_formTest() {
        mLogDate.setSession(7);
        String test = mLogDate.getSession_form();
        assertThat(test, equalTo("7"));
    }

    @Test
    public void getSession_emptyFormTest() {
        mLogDate.setSession(0);
        String test = mLogDate.getSession_form();
        assertThat(test, equalTo("-1"));
    }

    @Test
    public void getDate_formatsDate() {
        mLogDate.setDate(getCal("20160301-00"));
        assertThat(mLogDate.getDate(), equalTo("Tue Mar 1"));

        mLogDate.setDate(getCal("20160410-05"));
        assertThat(mLogDate.getDate(), equalTo("Sun Apr 10"));
    }

    @Test
    public void isEmptySession_true() {
        mLogDate.setSession(0);
        assertTrue(mLogDate.isEmptySession());
    }

    @Test
    public void isEmptySession_false() {
        mLogDate.setSession(5);
        assertFalse(mLogDate.isEmptySession());
    }
}