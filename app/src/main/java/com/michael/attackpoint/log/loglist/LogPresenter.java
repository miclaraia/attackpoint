package com.michael.attackpoint.log.loglist;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.michael.attackpoint.account.Login;
import com.michael.attackpoint.log.data.LogRepository;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.EspressoIdlingResource;

import java.util.ArrayList;
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
        mLogView.showLog(new ArrayList<LogInfo>(0));

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice
        mLogRepository.getLog(forceUpdate, mUser, new LogRepository.LoadLogCallback() {
            @Override
            public void onLoaded(List<LogInfo> logList) {
                EspressoIdlingResource.decrement(); // Set app as idle.
                mLogView.showLog(logList);
                mLogView.setProgressIndicator(false);
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof NoConnectionError) {
                    // No network connection
                    mLogView.showSnackbar("No Network");
                    mLogView.setProgressIndicator(false);
                } else {
                    // somethine went wrong getting user log
                    mLogView.showSnackbar("Something went wrong :(");
                    mLogView.setProgressIndicator(false);
                }
            }
        });
    }

    @Override
    public void addNewEntry() {
        mLogView.showAddEntry();
    }

    @Override
    public void openEntryDetails(@NonNull LogInfo requestedEntry) {
        int logid = requestedEntry.getID();
        int userid = Login.getInstance().getUserId();
        mLogView.showEntryDetail(userid, logid);
    }
}
