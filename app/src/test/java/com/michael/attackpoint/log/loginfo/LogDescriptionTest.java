package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 3/21/16.
 */
public class LogDescriptionTest {
    private static final String EMPTY = "";
    private static final String NONEMPTY = "not empty string";
    private static final String LONG = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tortor est, venenatis non lorem eu, mattis rhoncus ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras gravida iaculis sagittis. Nunc hendrerit tempor est, a";
    private static final String SNIPPET = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tortor est, venenatis non lorem eu, mattis rhoncus ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia";
    private LogDescription mLogDescription;

    @Before
    public void setup() {
        mLogDescription = new LogDescription();
    }

    @Test
    public void set_setsItem() {
        mLogDescription.set(EMPTY);
        assertThat(mLogDescription.get(), equalTo(EMPTY));
        mLogDescription.set(NONEMPTY);
        assertThat(mLogDescription.get(), equalTo(NONEMPTY));
    }

    @Test
    public void isEmpty_true() {
        mLogDescription.set(EMPTY);
        assertTrue(mLogDescription.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogDescription.set(NONEMPTY);
        assertFalse(mLogDescription.isEmpty());
    }

    @Test
    public void toString_returnsString() {
        mLogDescription.set(NONEMPTY);
        assertThat(mLogDescription.toString(), equalTo(NONEMPTY));
    }

    @Test
    public void toSnippet_goodSnippet() {
        mLogDescription.set(LONG);
        assertThat(mLogDescription.toSnippet(), equalTo(SNIPPET));
    }

    @Test
    public void toSnippet_shortened() {
        mLogDescription.set(LONG);
        assertThat(mLogDescription.toSnippet().length(),
                equalTo(LogDescription.MAX_SNIPPET));
    }

    @Test
    public void toJSON_containskeys() {
        JSONObject json = new JSONObject();
        mLogDescription.set(NONEMPTY);
        json = mLogDescription.toJSON(json);

        assertTrue(json.has(LogDescription.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        mLogDescription.set(NONEMPTY);
        JSONObject json = mLogDescription.toJSON(new JSONObject());
        String description = json.getString(LogDescription.JSON);

        assertThat(description, equalTo(NONEMPTY));
    }

    @Test
    public void fromJSON_setsDate() {
        mLogDescription.set(NONEMPTY);
        JSONObject json = mLogDescription.toJSON(new JSONObject());
        mLogDescription.set(EMPTY);

        mLogDescription.fromJSON(json);

        assertThat(mLogDescription.toSnippet(), equalTo(NONEMPTY));
    }
}