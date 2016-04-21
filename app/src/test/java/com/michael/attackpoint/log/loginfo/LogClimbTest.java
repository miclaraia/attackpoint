package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by michael on 3/21/16.
 */
public class LogClimbTest {
    private static final LogClimb.Climb EMPTY = new LogClimb.Climb();
    private static final LogClimb.Climb NONEMPTY = new LogClimb.Climb(100);
    private static final String PARSE_STRING = "123m";
    private static final LogClimb.Climb PARSE = new LogClimb.Climb(123, "m");

    private LogClimb mLogClimb;

    @Before
    public void setUp() {
        mLogClimb = new LogClimb();
    }

    @Test
    public void set_setsItem() {
        mLogClimb.set(EMPTY);
        assertThat(mLogClimb.get(), equalTo(EMPTY));
        mLogClimb.set(NONEMPTY);
        assertThat(mLogClimb.get(), equalTo(NONEMPTY));
    }

    @Test
    public void isEmpty_true() {
        mLogClimb.set(EMPTY);
        assertTrue(mLogClimb.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogClimb.set(NONEMPTY);
        assertFalse(mLogClimb.isEmpty());
    }

    @Test
    public void toString_100m() {
        mLogClimb.set(NONEMPTY);
        assertThat(mLogClimb.toString(), equalTo("+100m"));
    }

    @Test
    public void toString_empty() {
        mLogClimb.set(EMPTY);
        assertThat(mLogClimb.toString(), equalTo(""));
    }

    @Test
    public void toJSON_containskeys() {
        JSONObject json = new JSONObject();
        mLogClimb.set(NONEMPTY);
        json = mLogClimb.toJSON(json);

        assertTrue(json.has(LogClimb.JSON_CLIMB));
        assertTrue(json.has(LogClimb.JSON_UNIT));
    }

    @Test
    public void toJSON_goodData() throws JSONException{
        JSONObject json = new JSONObject();
        mLogClimb.set(NONEMPTY);
        json = mLogClimb.toJSON(json);

        assertThat(json.getInt(LogClimb.JSON_CLIMB), equalTo(NONEMPTY.getClimb()));
        assertThat(json.getString(LogClimb.JSON_UNIT), equalTo(NONEMPTY.getUnit()));
    }

    @Test
    public void fromJSON_parses() {
        mLogClimb.set(NONEMPTY);
        JSONObject json = new JSONObject();
        json = mLogClimb.toJSON(json);

        mLogClimb.set(new LogClimb.Climb());
        mLogClimb.fromJSON(json);

        assertThat(mLogClimb.mItem.getClimb(), equalTo(NONEMPTY.getClimb()));
        assertThat(mLogClimb.mItem.getUnit(), equalTo(NONEMPTY.getUnit()));
    }

    @Test
    public void parseClimb_parses() {
        LogClimb.Climb test = LogClimb.parseClimb(PARSE_STRING);
        assertThat(test, equalTo(PARSE));
    }
}
