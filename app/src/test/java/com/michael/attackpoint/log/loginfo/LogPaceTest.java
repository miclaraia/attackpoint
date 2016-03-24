package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.cert.LDAPCertStoreParameters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.michael.attackpoint.log.loginfo.LogPace.Pace;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by michael on 3/21/16.
 */
public class LogPaceTest {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("mmss");
    private static final String UNIT = "kilometers";
    private static final String EMPTY = "0000";
    private static final String NONEMPTY = "0420";
    private static final String NONEMPTY_STRING = "4'20/km";
    private static final String NONEMPTY_JSON = "4'20";

    private LogPace mLogPace;

    private static Pace getPace(String pace) {
        Pace p = new Pace();
        Calendar cal = p.getPace();
        try {
            cal.setTime(FORMAT.parse(pace));
            CalendarTime ct = new CalendarTime(cal);
            p.setPace(cal);
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
        return p;
    }

    @Before
    public void setup() {
        mLogPace = new LogPace();
    }

    @Test
    public void set_getSetTest() {
        mLogPace.set(getPace(EMPTY));
        String test = FORMAT.format(mLogPace.get().getPace().getTime());
        assertThat(test, equalTo(EMPTY));

        mLogPace.set(getPace(NONEMPTY));
        test = FORMAT.format(mLogPace.get().getPace().getTime());
        assertThat(test, equalTo(NONEMPTY));
    }

    @Test
    public void isEmpty_true() {
        mLogPace.set(getPace(EMPTY));
        assertTrue(mLogPace.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogPace.set(getPace(NONEMPTY));
        assertFalse(mLogPace.isEmpty());
    }

    @Test
    public void toString_returnString() {
        mLogPace.set(getPace(NONEMPTY));
        assertThat(mLogPace.toString(), equalTo(NONEMPTY_STRING));
    }

    @Test
    public void toString_emptyTest() {
        mLogPace.set(getPace(EMPTY));
        assertThat(mLogPace.toString(), equalTo(""));
    }

    @Test
    public void toJSON_containskeys() {
        mLogPace.set(getPace(NONEMPTY));
        JSONObject json = mLogPace.toJSON(new JSONObject());

        assertTrue(json.has(LogPace.JSON_PACE));
        assertTrue(json.has(LogPace.JSON_UNIT));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        Pace p = getPace(NONEMPTY);
        mLogPace.set(p);
        JSONObject json = mLogPace.toJSON(new JSONObject());
        String pace = json.getString(LogPace.JSON_PACE);
        String unit = json.getString(LogPace.JSON_UNIT);

        assertThat(pace, equalTo(NONEMPTY_JSON));
        assertThat(unit, equalTo(UNIT));
    }

    @Test
    public void fromJSON_setsData() {
        Pace p = getPace(NONEMPTY);
        mLogPace.set(p);
        JSONObject json = mLogPace.toJSON(new JSONObject());
        mLogPace.set(getPace(EMPTY));

        mLogPace.fromJSON(json);

        Assert.assertThat(mLogPace.get(), equalTo(p));
    }

    @Test
    public void calcPace_Test() throws ParseException {
        LogDuration duration = new LogDuration();
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        cal.setTime(FORMAT.parse("4000"));
        duration.set(cal);

        Unit unit = Unit.UnitManager.getDefault();

        LogDistance distance = new LogDistance();
        LogDistance.Distance d = distance.get();
        d.setDistance(5.0);
        d.setUnit(unit);
        distance.set(d);

        Pace pace = LogPace.calcPace(duration, distance);
        String calTest = FORMAT.format(pace.getPace().getTime());

        assertThat(pace.getUnit(), equalTo(unit));
        assertThat(calTest, equalTo("0800"));
    }
}