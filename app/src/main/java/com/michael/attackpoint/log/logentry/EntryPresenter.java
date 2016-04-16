package com.michael.attackpoint.log.logentry;

import com.michael.attackpoint.log.data.LogRepository;

/**
 * Created by michael on 4/16/16.
 */
public class EntryPresenter implements EntryContract.Presenter {

    private LogRepository mLogRepository;
    private EntryContract.View mView;
    private String mLogId;
    private int mUser;

    public EntryPresenter(LogRepository logRepository, EntryContract.View view, int userID, String logID) {
        mLogRepository = logRepository;
        mView = view;
        mUser = userID;
        mLogId = logID;
    }

    @Override
    public void loadEntry() {

    }
}
