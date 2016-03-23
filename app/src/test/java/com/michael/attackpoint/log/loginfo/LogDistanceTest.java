package com.michael.attackpoint.log.loginfo;

import com.michael.attackpoint.log.loginfo.LogDistance.Distance;

import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigestSpi;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 3/21/16.
 */
public class LogDistanceTest {
    private static final Double EMPTY = 0.0;
    private static final Double NONEMPTY = 1.665;
    private static final String NONEMPTY_STRING = "1.67 km";

    private LogDistance mLogDistance;

    private static final Distance getDistance(Double distance) {
        Distance d = new Distance();
        d.setDistance(distance);
        return d;
    }

    @Before
    public void setup() {
        mLogDistance = new LogDistance();
    }

    @Test
    public void set_getSetItem() {
        Distance d = getDistance(EMPTY);
        mLogDistance.set(d);
        assertThat(mLogDistance.get(), equalTo(d));

        d = getDistance(NONEMPTY);
        mLogDistance.set(d);
        assertThat(mLogDistance.get(), equalTo(d));
    }

    @Test
    public void isEmpty_true() {
        mLogDistance.set(getDistance(EMPTY));
        assertTrue(mLogDistance.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        mLogDistance.set(getDistance(NONEMPTY));
        assertFalse(mLogDistance.isEmpty());
    }

    @Test
    public void toString_returnsString() {
        mLogDistance.set(getDistance(NONEMPTY));
        assertThat(mLogDistance.toString(), equalTo(NONEMPTY_STRING));
    }
}