package com.michael.attackpoint.discussion;

/**
 * Created by michael on 4/26/16.
 */
public class DiscussionPresenter implements DiscussionContract.Presenter {

    private DiscussionContract.View mView;
    private int mDiscussionId;
    private Discussion mDiscussion;

    public DiscussionPresenter(DiscussionContract.View view, int user) {
        mView = view;
        //mUser
    }

    @Override
    public void loadDiscussion() {

    }

    @Override
    public void newComment() {

    }
}
