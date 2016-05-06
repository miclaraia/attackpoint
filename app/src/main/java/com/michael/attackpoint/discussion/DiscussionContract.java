package com.michael.attackpoint.discussion;

import java.util.List;

/**
 * Created by michael on 4/26/16.
 */
public interface DiscussionContract {

    interface View {
        void setProgressIndicator(boolean state);

        void showSnackbar(String message);

        void showDiscussion(Discussion discussion);

        void showNewComment();
    }

    interface Presenter {
        void loadDiscussion();

        void newComment();
    }
}
