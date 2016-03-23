package com.michael.attackpoint.log.loginfo;

import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.util.AndroidFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 3/21/16.
 */
public class LogInfoActivityTest {
    private static final String EMPTY = "";
    private static final String NONEMPTY = "Running";

    private LogInfoActivity mLogInfoActivity;

    @Mock
    private AndroidFactory mAndroidFactory;

    @Mock
    private ActivityTable mActivityTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        mLogInfoActivity = new LogInfoActivity();
    }

    @Test
    public void setItem_getSetItem() {
        mLogInfoActivity.set(NONEMPTY);
        assertThat(mLogInfoActivity.get(), equalTo(NONEMPTY));
        mLogInfoActivity.set(EMPTY);
        assertThat(mLogInfoActivity.get(), equalTo(EMPTY));
    }

    @Test
    public void isEmpty_true() {
        mLogInfoActivity.set(EMPTY);
        assertTrue(mLogInfoActivity.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogInfoActivity.set(NONEMPTY);
        assertFalse(mLogInfoActivity.isEmpty());
    }

    @Test
    public void toString_returnsString() {
        when(mActivityTable.getFirst()).thenReturn("");

        mLogInfoActivity.set(NONEMPTY);
        assertThat(mLogInfoActivity.toString(), equalTo(NONEMPTY));

        mLogInfoActivity.set(EMPTY);
        assertThat(mLogInfoActivity.toString(), equalTo(""));
        verify(mActivityTable).getFirst();
    }

    @Test
    public void toJSON_containskeys() {
        mLogInfoActivity.set(NONEMPTY);
        JSONObject json = mLogInfoActivity.toJSON(new JSONObject());

        assertTrue(json.has(LogInfoActivity.JSON));
    }

    @Test
    public void toJSON_goodData() throws JSONException {
        mLogInfoActivity.set(NONEMPTY);
        JSONObject json = mLogInfoActivity.toJSON(new JSONObject());
        String s = json.getString(LogInfoActivity.JSON);

        assertThat(s, equalTo(NONEMPTY));
    }

    @Test
    public void fromJSON_setsData() {
        mLogInfoActivity.set(NONEMPTY);
        JSONObject json = mLogInfoActivity.toJSON(new JSONObject());

        mLogInfoActivity.set(EMPTY);

        mLogInfoActivity.fromJSON(json);
        assertThat(mLogInfoActivity.get(), equalTo(NONEMPTY));
    }


}