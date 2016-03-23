package com.michael.attackpoint.log.loginfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.michael.attackpoint.log.loginfo.LogPace.Pace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by michael on 3/23/16.
 */
public class PaceTest {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("mmss");
    private static final Unit UNIT_K = Unit.UnitManager.getUnit("km");
    private static final Unit UNIT_M = Unit.UnitManager.getUnit("mi");
    private static final String PACE_1 = "0420";
    private static final String PACE_1_M = "0656";
    private static final String PACE_2 = "0545";
    private static final String PACE_2_M = "0912";

    private Pace mPace;

    private Calendar getPace(String pace) {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        try {
            cal.setTime(FORMAT.parse(pace));
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }

        return cal;
    }

    @Before
    public void setup() {
        mPace = new Pace();
    }

    @Test
    public void setUnit_getSetTest() {
        mPace.setUnit(UNIT_K);
        assertThat(mPace.getUnit(), equalTo(UNIT_K));
        mPace.setUnit(UNIT_M);
        assertThat(mPace.getUnit(), equalTo(UNIT_M));
    }

    @Test
    public void setPace_getSetTest() {
        mPace.setUnit(UNIT_K);

        mPace.setPace(getPace(PACE_1));
        String test = FORMAT.format(mPace.getPace().getTime());
        assertThat(test, equalTo(PACE_1));

        mPace.setPace(getPace(PACE_2));
        test = FORMAT.format(mPace.getPace().getTime());
        assertThat(test, equalTo(PACE_2));
    }

    @Test
    public void setPace_convertsToStandardTest() {
        mPace.setUnit(UNIT_M);
        Calendar pace_m = getPace(PACE_1_M);
        mPace.setPace(pace_m);
        String test = FORMAT.format(mPace.getPaceStandard().getTime());
        assertThat(test, equalTo(PACE_1));
    }
}
