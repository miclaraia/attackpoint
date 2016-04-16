package com.michael.attackpoint.log.data;

import android.graphics.Color;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.loginfo.LogClimb;
import com.michael.attackpoint.log.loginfo.LogColor;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogInfoItem;
import com.michael.attackpoint.log.loginfo.LogIntensity;
import com.michael.attackpoint.log.loginfo.Unit;
import com.michael.attackpoint.util.AndroidFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareEverythingForTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 4/15/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Color.class})
public class LogRequestTest {
    private static String REF = "http://attackpoint.org/log.jsp/user_11778";

    @Mock
    private AndroidFactory mAndroidFactory;

    @Mock
    private ActivityTable mActivityTable;

    @Mock
    private LogInfo mFakeLogInfo;

    private Document mDocument;

    private Element mMeta;

    private Element mLogEntry;

    private Element mDay;

    private Element mWhitespace;

    private LogBuilder mLogBuilder;


    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(Log.class);
        Mockito.when(Log.d(anyString(), anyString())).thenReturn(0);
        Mockito.when(Log.e(anyString(), anyString())).thenReturn(0);
        Mockito.when(Log.i(anyString(), anyString())).thenReturn(0);
        Mockito.when(Log.v(anyString(), anyString())).thenReturn(0);


        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        mDocument = getDocument(this, "sample-log.html");
        mMeta = getDocument(this, "sample-meta.html");
        mLogEntry = getDocument(this, "sample-entry.html");
        mDay = getDocument(this, "sample-day.html");
        mWhitespace = getDocument(this, "sample-whitespace.html");

        mLogBuilder = new LogBuilder();
    }

    @Test
    public void getLog_sizeTest() {
        PowerMockito.mockStatic(Color.class);
        Mockito.when(Color.parseColor(anyString())).thenReturn(0);

        List<LogInfo> list = mLogBuilder.getLog(mDocument);
        assertThat(list.size(), equalTo(10));
    }

    @Test
    public void getActivity_test() {
        LogInfoActivity logInfoActivity = new LogInfoActivity();
        logInfoActivity = mLogBuilder.getActivity(mLogEntry, logInfoActivity);

        assertThat(logInfoActivity.get(), equalTo("Running"));
    }

    @Test
    public void getActivity_LogInfoKeyTest() {
        mLogBuilder.getActivity(mLogEntry, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_ACTIVITY), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getClimb_test() {
        LogClimb logClimb = new LogClimb();
        logClimb = mLogBuilder.getClimb(mMeta, logClimb);

        assertThat(logClimb.toString(), equalTo("+414m"));
    }

    @Test
    public void getClimb_LogInfoKeyTest() {
        mLogBuilder.getClimb(mMeta, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_CLIMB), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getColor_test() {
        PowerMockito.mockStatic(Color.class);
        Mockito.when(Color.parseColor(anyString())).thenReturn(0);

        mLogBuilder.getColor(mLogEntry, new LogColor());

        // TODO, maybe through powermock?
        PowerMockito.verifyStatic();
        Color.parseColor("#1100ff");
    }

    @Test
    public void getColor_LogInfoKeyTest() {
        PowerMockito.mockStatic(Color.class);
        Mockito.when(Color.parseColor(anyString())).thenReturn(0);

        mLogBuilder.getColor(mLogEntry, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_COLOR), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getDate_test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        LogDate logDate = new LogDate();
        logDate = mLogBuilder.getDate(mDay, logDate);

        String test = sdf.format(logDate.get().getTime());
        assertThat(test, equalTo("20160412"));
    }

    @Test
    public void getDate_LogInfoKeyTest() {
        Element session = mDay.select(".tlssh").first();
        mLogBuilder.getDate(mDay, session, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_DATE), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getSession_test() {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        LogDate logDate = new LogDate();
        logDate.set(cal);

        Element session = mDay.select(".tlssh").first();
        logDate = mLogBuilder.getSession(session, logDate);

        int hour = logDate.get().get(Calendar.HOUR_OF_DAY);
        assertThat(hour, equalTo(7));
    }

    @Test
    public void getSession_nosessionTest() throws IOException {
        Element day = getDocument(this, "sample-day-nosession.html");
        Element session = day.select(".tlssh").first();

        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        LogDate logDate = new LogDate();
        logDate.set(cal);

        logDate = mLogBuilder.getSession(session, logDate);

        assertTrue(logDate.isEmptySession());
        assertThat(logDate.getSession(), equalTo(""));
    }

    @Test
    public void getDescription_test() {
        LogDescription logDescription = new LogDescription();
        logDescription = mLogBuilder.getDescription(mLogEntry, logDescription);

        assertThat(logDescription.get(), equalTo("The usual"));
    }

    @Test
    public void getDescription_LogInfoKeyTest() {
        mLogBuilder.getDescription(mLogEntry, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_DESCRIPTION), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getDistance_test() {
        LogDistance logDistance = new LogDistance();
        logDistance = mLogBuilder.getDistance(mMeta, logDistance);

        assertThat(logDistance.toString(), equalTo("10.82 km"));
    }

    @Test
    public void getDistance_LogInfoKeyTest() {
        mLogBuilder.getDistance(mMeta, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_DISTANCE), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getDuration_test() {
        LogDuration logDuration = new LogDuration();
        logDuration = mLogBuilder.getDuration(mMeta, logDuration);

        assertThat(logDuration.toString(), equalTo("52:12"));
    }

    @Test
    public void getDuration_LogInfoKeyTest() {
        mLogBuilder.getDuration(mMeta, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_DURATION), Matchers.<LogInfoItem>any());
    }

    @Test
    public void getIntensity_test() {
        LogIntensity logIntensity = new LogIntensity();
        logIntensity = mLogBuilder.getIntensity(mMeta, logIntensity);

        assertThat(logIntensity.get(), equalTo(5));
    }

    @Test
    public void getIntensity_LogInfoKeyTest() {
        mLogBuilder.getIntensity(mMeta, mFakeLogInfo);
        verify(mFakeLogInfo).set(eq(LogInfo.KEY_INTENSITY), Matchers.<LogInfoItem>any());
    }

    @Test
    public void stripWhitespace_test() {
        Element whitespace = mWhitespace.select("div").first();
        Element stripped = LogBuilder.stripWhitespace(whitespace);

        Element compare = Jsoup.parse("<div><div>div1</div><br><div>div2</div></div>");
        compare = compare.select("div").first();
        assertThat(stripped, equalTo(compare));
    }

    private static Document getDocument(Object obj, String fileName) throws IOException {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        File file = new File(resource.getPath());

        return Jsoup.parse(file, null, REF);
    }
}
