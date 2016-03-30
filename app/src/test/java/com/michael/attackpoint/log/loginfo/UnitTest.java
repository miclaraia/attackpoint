package com.michael.attackpoint.log.loginfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 3/21/16.
 */
public class UnitTest {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("mmss");
    private static final String NAME = "Test";
    private static final String NICKNAME = "test";
    private static final Double MULTIPLIER = 10.0;
    private Unit mUnit;

    @Before
    public void setup() {
        mUnit = new Unit(NAME, NICKNAME, MULTIPLIER);
    }

    @Test
    public void convert_distanceTest() {
        assertThat(mUnit.convert(1.0), equalTo(10.0));
    }

    @Test
    public void convert_paceTest() {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,10,0);
        cal = mUnit.convert(cal);

        Calendar compare = Calendar.getInstance();
        compare.set(0,0,0,0,1,0);

        assertThat(FORMAT.format(cal.getTime()), equalTo(FORMAT.format(compare.getTime())));
    }

    @Test
    public void standard_distanceTest() {
        assertThat(mUnit.standard(10.0), equalTo(1.0));
    }

    @Test
    public void standard_paceTest() {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,1,0);
        cal = mUnit.standard(cal);

        Calendar compare = Calendar.getInstance();
        compare.set(0,0,0,0,10,0);

        assertThat(FORMAT.format(cal.getTime()), equalTo(FORMAT.format(compare.getTime())));
    }

    @Test
    public void toString_test() {
        assertThat(mUnit.toString(), equalTo(NAME));
    }

    @Test
    public void toNickname_test() {
        assertThat(mUnit.toNickname(), equalTo(NICKNAME));
    }

    @Test
    public void equals_trueTest() {
        Unit test = new Unit(NAME, NICKNAME, MULTIPLIER);
        assertTrue(mUnit.equals(test));
    }

    @Test
    public void equals_falseTest() {
        assertFalse(mUnit.equals(new Unit("", NICKNAME, MULTIPLIER)));
        assertFalse(mUnit.equals(new Unit(NAME, "", MULTIPLIER)));
        assertFalse(mUnit.equals(new Unit(NAME, NICKNAME, 0)));
    }
}