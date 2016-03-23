package com.michael.attackpoint.log.loginfo;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by michael on 3/23/16.
 */
public class DistanceTest {
    private static final Unit UNIT_K = Unit.UnitManager.getUnit("km");
    private static final Unit UNIT_M = Unit.UnitManager.getUnit("mi");
    private static final Double DISTANCE_1 = 1.0;
    private static final Double DISTANCE_2 = 1.6;

    LogDistance.Distance mDistance;

    @Before
    public void setup() {
        mDistance = new LogDistance.Distance();
    }

    @Test
    public void setUnit_getSetTest() {
        mDistance.setUnit(UNIT_K);
        assertThat(mDistance.getUnit(), equalTo(UNIT_K));
        mDistance.setUnit(UNIT_M);
        assertThat(mDistance.getUnit(), equalTo(UNIT_M));
    }

    @Test
    public void setDistance_getSetTest() {
        mDistance.setUnit(UNIT_K);

        mDistance.setDistance(DISTANCE_1);
        assertThat(mDistance.getDistance(), equalTo(DISTANCE_1));
        mDistance.setDistance(DISTANCE_2);
        assertThat(mDistance.getDistance(), equalTo(DISTANCE_2));
    }

    @Test
    public void setDistance_convertsToStandard() {
        mDistance.setUnit(UNIT_M);
        mDistance.setDistance(DISTANCE_1);
        assertThat(mDistance.getDistanceStandard(), equalTo(DISTANCE_2));
    }

    @Test
    public void getDistanceStandard_convertsUnit() {
        mDistance.setUnit(UNIT_M);
        mDistance.setDistance(DISTANCE_1);

        assertThat(mDistance.getDistanceStandard(), equalTo(DISTANCE_2));
    }
}
