package com.michael.attackpoint.discussion;

import com.android.volley.NoConnectionError;
import com.michael.attackpoint.discussion.data.DiscussionRepository;
import com.michael.attackpoint.discussion.data.DiscussionRepositoryImpl;

/**
 * Created by michael on 4/26/16.
 */
public class DiscussionPresenter implements DiscussionContract.Presenter {

    private DiscussionContract.View mView;
    private int mDiscussionId;
    private Discussion mDiscussion;
    private DiscussionRepository mRepository;

    public DiscussionPresenter(DiscussionRepository repository, DiscussionContract.View view, int discussionId) {
        mRepository = repository;
        mView = view;
        mDiscussionId = discussionId;
    }

    @Override
    public void loadDiscussion() {
        mView.setProgressIndicator(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mRepository.getDiscussion(mDiscussionId, new DiscussionRepository.LoadCallback() {
                    @Override
                    public void onLoaded(Discussion discussion) {
                        mView.showDiscussion(discussion);
                        mView.setProgressIndicator(false);
                    }

                    @Override
                    public void onError(Exception e) {
                        if (e instanceof NoConnectionError) {
                            // No network connection
                            mView.showSnackbar("No Network");
                            mView.setProgressIndicator(false);
                        } else {
                            // somethine went wrong getting user log
                            mView.showSnackbar("Something went wrong :(");
                            mView.setProgressIndicator(false);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void newComment() {
        mView.showNewComment();
    }
}
