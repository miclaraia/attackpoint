package com.michael.attackpoint.log.addentry.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.michael.attackpoint.log.addentry.pickers.ManagerContract;
import com.michael.attackpoint.log.data.LogRepositories;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loglist.LogPresenter;

import java.util.ArrayList;

/**
 * Created by michael on 4/13/16.
 */
public class TrainingFragment extends Fragment implements TrainingContract.View,
        ManagerContract.Activity {

    private TrainingContract.Presenter mPresenter;
    private SubV

    public static TrainingFragment newInstance () {
        //Bundle arguments = new Bundle();
        //arguments.putString(USER_ID, user_id);
        TrainingFragment fragment = new TrainingFragment();
        //fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mAdapter = new LogAdapter(new ArrayList<LogInfo>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.loadLog(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        mPresenter = new TrainingPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_log, viewGroup, false);

        return root;
    }
}
