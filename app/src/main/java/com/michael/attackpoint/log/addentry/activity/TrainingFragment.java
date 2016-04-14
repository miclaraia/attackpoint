package com.michael.attackpoint.log.addentry.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.michael.attackpoint.R;
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
    private static final String DEBUG_TAG = "trainingfragment";

    private TrainingContract.Presenter mPresenter;
    private ViewHolder mViewHolder;

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
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
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

        View root = inflater.inflate(R.layout.fragment_training, viewGroup, false);
        mViewHolder = new ViewHolder(root);

        final ViewHolder vh = mViewHolder;
        View workout = vh.workout.parent;
        workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.findViewById(R.id.item).performClick();
            }
        });
        Spinner workoutSpinner = (Spinner) vh.workout.item;
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.training_workout, android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutAdapter);

        // initialize submit button
        vh.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "submit button clicked, submitting training");
                mPresenter.onSubmit();
            }
        });
        return root;
    }

    @Override
    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    @Override
    public void showNoNetworkError() {
        // TODO
    }

    @Override
    public void showInvalidEntryError() {
        // TODO
    }

    @Override
    public ManagerContract.Activity getManagerActivity() {
        return this;
    }
}
