package com.michael.attackpoint.log.logentry;

import com.michael.attackpoint.log.data.LogRepository;
import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 4/16/16.
 */
public class EntryPresenter implements EntryContract.Presenter {

    private LogRepository mLogRepository;
    private EntryContract.View mView;
    private int mLogId;
    private int mUser;

    public EntryPresenter(LogRepository logRepository, EntryContract.View view, int userID, int logID) {
        mLogRepository = logRepository;
        mView = view;
        mUser = userID;
        mLogId = logID;
    }

    @Override
    public void loadEntry() {
        mLogRepository.getLogEntry(mUser, mLogId, new LogRepository.LoadLogEntryCallback() {
            @Override
            public void onLoaded(LogInfo logInfo) {
                mView.ShowEntry(logInfo);
            }
        });
    }

    @Override
    public void showComment(int id) {
        mView.showComment(id);
    }
}
