package com.michael.attackpoint;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.michael.attackpoint.dialogs.TrainingDatePicker;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        view.findViewById(R.id.training_date).setOnClickListener(trainingListener);

        return view;
    }

    private View.OnClickListener trainingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.training_date:
                    DialogFragment newFragment = new TrainingDatePicker();
                    newFragment.show(getFragmentManager(), "timePicker");
            }
        }
    };
}
