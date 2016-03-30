package com.michael.attackpoint.log.loglist;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.michael.attackpoint.log.data.LogRepository;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.EspressoIdlingResource;

import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public class LogPresenter implements LogContract.Presenter {
    private static final String DEBUG_TAG = "logpresenter";

    private LogRepository mLogRepository;
    private LogContract.View mLogView;
    private int mUser;

    public LogPresenter(LogRepository repository, LogContract.View view, int user) {
        mLogRepository = repository;
        mLogView = view;
        mUser = user;
    }

    @Override
    public void loadLog(boolean forceUpdate) {
        mLogView.setProgressIndicator(true);
        if (forceUpdate) {
            mLogRepository.refreshData(mUser, new LogRepository.RefreshCallback() {
                @Override
                public void done() {
                    loadLog(false);
                }
            });
        } else {
            // The network request might be handled in a different thread so make sure Espresso knows
            // that the app is busy until the response is handled.
            EspressoIdlingResource.increment(); // App is busy until further notice
            mLogRepository.getLog(mUser, new LogRepository.LoadLogCallback() {
                @Override
                public void onLoaded(List<LogInfo> logList) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                    mLogView.showLog(logList);
                    mLogView.setProgressIndicator(false);
                }
            });
        }
    }

    @Override
    public void addNewEntry() {
        mLogView.showAddEntry();
    }

    @Override
    public void openEntryDetails(@NonNull LogInfo requestedEntry) {
        String id = Integer.valueOf(requestedEntry.getID()).toString();
        mLogView.showEntryDetail(id);
    }
}
