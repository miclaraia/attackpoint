package com.michael.attackpoint.discussion;

import com.michael.attackpoint.discussion.data.DiscussionRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by michael on 5/6/16.
 */
public class DiscussionPresenterTest {

    @Mock
    private DiscussionRepository mRepository;

    @Mock
    private DiscussionContract.View mView;

    @Mock
    private Discussion mDiscussion;

    @Captor
    private ArgumentCaptor<DiscussionRepository.LoadCallback> mCallbackCaptor;

    private DiscussionPresenter mPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new DiscussionPresenter(mRepository, mView, 100);
    }

    @Test
    public void loadDiscussion_test() {
        mPresenter.loadDiscussion();

        verify(mView).setProgressIndicator(true);
        verify(mRepository).getDiscussion(eq(100), mCallbackCaptor.capture());

        mCallbackCaptor.getValue().onLoaded(mDiscussion);

        verify(mView).setProgressIndicator(false);
        verify(mView).showDiscussion(mDiscussion);
    }

    @Test
    public void newComment_test() {
        mPresenter.newComment();
        verify(mView).showNewComment();
    }
}
