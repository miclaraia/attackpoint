package com.michael.attackpoint;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.michael.attackpoint.dialogs.NumberPickerDialog;
import com.michael.attackpoint.dialogs.TrainingDatePicker;
import com.michael.attackpoint.dialogs.TrainingDurationPicker;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Singleton.getInstance().setActivity(this);

        findViewById(R.id.training_date).setOnClickListener(trainingListener);

        // TODO create single custom adapter for all spinners and load from attackpoint
        // initialize activity type spinner
        RelativeLayout activity = (RelativeLayout) findViewById(R.id.training_activity);
        activity.setOnClickListener(new RelativeClickListener());
        Spinner activitySpinner = (Spinner) activity.findViewById(R.id.item);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_activities, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        // initialize workout type spinner (long, interval, hills, etc)
        RelativeLayout workout = (RelativeLayout) findViewById(R.id.training_workout);
        workout.setOnClickListener(new RelativeClickListener());
        Spinner workoutSpinner = (Spinner) workout.findViewById(R.id.item);
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_workout, android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutAdapter);

        // initialize intensity number picker
        findViewById(R.id.training_intensity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new NumberPickerDialog();
                dialog.show(getFragmentManager(), "numberpicker");
            }
        });

        // initialize duration number picker
        View duration = findViewById(R.id.training_duration);
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView time = (TextView) v.findViewById(R.id.item);
                String timeString = time.getText().toString();

                TrainingDurationPicker dialog = new TrainingDurationPicker();
                Bundle bundle = new Bundle();
                bundle.putString("time_string", timeString);
                dialog.setArguments(bundle);
                dialog.setResultView(time);
                dialog.show(getFragmentManager(), "durationpicker");
            }
        });

        // initialize distance data entry
        View distance = findViewById(R.id.training_distance);
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = v.findViewById(R.id.item);
                view.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(view, 0);
            }
        });

        // initialize description data entry
        View description = findViewById(R.id.training_description);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = v.findViewById(R.id.item);
                view.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(view, 0);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setActivity(this);
    }

    private View.OnClickListener trainingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.training_date:
                    DialogFragment newFragment = new TrainingDatePicker();
                    newFragment.show(getFragmentManager(), "timePicker");
                    break;
                case R.id.training_duration:
                    TextView time = (TextView) v.findViewById(R.id.item);
                    String timeString = time.getText().toString();

                    DialogFragment dialog = new TrainingDurationPicker();
                    Bundle bundle = new Bundle();
                    bundle.putString("time_string", timeString);
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "durationpicker");

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
