package com.michael.attackpoint.log.addentry.activity;

import com.michael.attackpoint.log.addentry.pickers.Managers;
import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 4/13/16.
 */
public class TrainingPresenter implements TrainingContract.Presenter {

    private TrainingContract.View mView;
    protected Managers mManagers;
    protected LogInfo mLogInfo;

    public TrainingPresenter(TrainingContract.View view) {
        mView = view;

        mManagers = new Managers(mView.getManagerActivity());
        mLogInfo = new LogInfo();
    }



    @Override
    public void onSubmit() {
        // TODO submits loginfo to attackpoint
    }
}
