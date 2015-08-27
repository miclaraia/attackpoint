package com.michael.attackpoint;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.michael.attackpoint.dialogs.NumberPickerDialog;
import com.michael.attackpoint.dialogs.TrainingDatePicker;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        findViewById(R.id.training_parent).

        findViewById(R.id.training_date).setOnClickListener(trainingListener);

        // TODO create single custom adapter for all spinners and load from attackpoint
        RelativeLayout activity = (RelativeLayout) findViewById(R.id.training_activity);
        activity.setOnClickListener(new RelativeClickListener());
        Spinner activitySpinner = (Spinner) activity.findViewById(R.id.item);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_activities, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        RelativeLayout workout = (RelativeLayout) findViewById(R.id.training_workout);
        workout.setOnClickListener(new RelativeClickListener());
        Spinner workoutSpinner = (Spinner) workout.findViewById(R.id.item);
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_workout, android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutAdapter);

        //// TODO: 8/27/15
        findViewById(R.id.training_intensity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment intensity = new NumberPickerDialog();
                intensity.show(getFragmentManager(), "numberpicker");
            }
        });
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

    private class RelativeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            view.findViewById(R.id.item).performClick();
        }
    }
}
