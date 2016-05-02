package com.michael.attackpoint.discussion.data;

import com.android.volley.RequestQueue;
import com.michael.attackpoint.discussion.Discussion;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;

/**
 * Created by michael on 5/2/16.
 */
public class LogRepositoryImplTest {

    @Mock
    private Discussion mDiscussion;

    @Mock
    private RequestQueue mRequestQueue;

    private DiscussionRepositoryImpl mRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mRepository = new DiscussionRepositoryImpl(mRequestQueue);
    }

    @Test
    public void inLocalCache_true() {
        mRepository.mDiscussions.put(1, mDiscussion);
        assertTrue(mRepository.inLocalCache(1));
    }

    @Test
    public void inLocalCache_false() {
        mRepository.inLocalCache(1);
    }
}
