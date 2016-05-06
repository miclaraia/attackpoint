package com.michael.attackpoint.discussion.data;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.michael.attackpoint.discussion.Discussion;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by michael on 5/2/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DiscussionRequest.class)
public class DiscussionRepositoryImplTest {

    @Mock
    private Discussion mDiscussion;

    @Mock
    private RequestQueue mRequestQueue;

    @Mock
    private DiscussionRepository.LoadCallback mFakeCallback;

    @Mock
    private DiscussionRepository.RefreshCallback mRefreshCallback;

    @Mock
    private DiscussionRequest mDiscussionRequest;

    @Captor
    private ArgumentCaptor<Discussion> mDiscussionCaptor;

    private DiscussionRepositoryImpl mRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mRepository = new DiscussionRepositoryImpl(mRequestQueue);
        mRepository.mRequestQueue = mRequestQueue;
    }

    @Test
    public void inLocalCache_true() {
        mRepository.mDiscussions.put(1, mDiscussion);
        assertTrue(mRepository.inLocalCache(1));
    }

    @Test
    public void inLocalCache_false() {
        assertFalse(mRepository.inLocalCache(1));
    }

    @Test
    public void getDiscussion_forceRefreshTest() {
        // check that repository refreshes
        // even if cache contains discussion with id
        mockDiscussionRequest();
        mRepository.mDiscussions.put(100, mDiscussion);
        mRepository.getDiscussionWorker(true, 100, mFakeCallback);

        verify(mRequestQueue).add(any(DiscussionRequest.class));
    }

    @Test
    public void getDiscussion_fromMemoryTest() {
        // returns discussion from cache
        // no refresh
        mRepository.mDiscussions.put(101, mDiscussion);
        mRepository.getDiscussionWorker(false, 101, mFakeCallback);

        verify(mRequestQueue, never()).add(any(Request.class));
        verify(mFakeCallback).onLoaded(mDiscussionCaptor.capture());

        assertThat(mDiscussionCaptor.getValue(), equalTo(mDiscussion));
    }

    @Test
    public void getDiscussion_notInMemoryTest() {
        // cache not in memory, performs refresh
        mockDiscussionRequest();
        mRepository.mDiscussions.clear();
        mRepository.getDiscussionWorker(false, 102, mFakeCallback);

        verify(mRequestQueue).add(any(DiscussionRequest.class));
    }

    @Test
    public void refreshDiscussion_makesRequest() {
        mockDiscussionRequest();
        mRepository.refreshDiscussion(103, mRefreshCallback);

        verify(mRequestQueue).add(any(DiscussionRequest.class));
    }

    public void mockDiscussionRequest() {
        PowerMockito.mockStatic(DiscussionRequest.class);
        Mockito.when(DiscussionRequest.newInstance(anyInt(),
                any(Response.Listener.class),
                any(Response.ErrorListener.class))).thenReturn(mDiscussionRequest);
    }
}
