package com.michael.attackpoint.log.loginfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 3/21/16.
 */
public class LogInfoTest {
    
    @Mock
    LogInfo mLogInfoMock;

    @Mock
    LogInfoItem mLogInfoItemMock;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void genStrings_callsAllKeys() {
        when(mLogInfoMock.get(anyString())).thenReturn(mLogInfoItemMock);
        when(mLogInfoItemMock.toString()).thenReturn("");
        when(mLogInfoItemMock.get()).thenReturn(0);

        new LogInfo.Strings(mLogInfoMock);
        verify(mLogInfoMock).get(LogInfo.KEY_ACTIVITY);
        verify(mLogInfoMock).get(LogInfo.KEY_CLIMB);
        verify(mLogInfoMock).get(LogInfo.KEY_COLOR);
        verify(mLogInfoMock).get(LogInfo.KEY_DATE);
        verify(mLogInfoMock).get(LogInfo.KEY_DESCRIPTION);
        verify(mLogInfoMock).get(LogInfo.KEY_DISTANCE);
        verify(mLogInfoMock).get(LogInfo.KEY_DURATION);
        verify(mLogInfoMock).get(LogInfo.KEY_INTENSITY);
        verify(mLogInfoMock).get(LogInfo.KEY_PACE);
    }
}