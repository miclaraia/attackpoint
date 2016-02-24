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
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Singleton.getInstance().setActivity(this);

        final ViewHolder vh = new ViewHolder(findViewById(R.id.training_parent));

        vh.date.parent.setOnClickListener(trainingListener);

        // TODO create single custom adapter for all spinners and load from attackpoint
        // initialize activity type spinner
        View activity = vh.activity.parent;
        activity.setOnClickListener(new RelativeClickListener());
        Spinner activitySpinner = (Spinner) vh.activity.item;
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_activities, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        // initialize workout type spinner (long, interval, hills, etc)
        View workout = vh.workout.parent;
        workout.setOnClickListener(new RelativeClickListener());
        Spinner workoutSpinner = (Spinner) vh.workout.item;
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_workout, android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutAdapter);

        // initialize intensity number picker
        vh.intensity.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new NumberPickerDialog();
                dialog.show(getFragmentManager(), "numberpicker");
            }
        });

        // initialize duration number picker
        View duration = vh.duration.parent;
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView time = (TextView) vh.duration.item;
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
        View distance = vh.distance.parent;
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = vh.distance.item;
                view.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(view, 0);
            }
        });

        // initialize description data entry
        View description = vh.description.parent;
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = vh.description.item;
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

    private void submitTraining() {
        ViewHolder vh = new ViewHolder(findViewById(R.id.training_parent));
        LogInfo li = new LogInfo();

        Calendar cal = (Calendar) vh.date.item.getTag();
        li.setDate(cal);

    }

    private class RelativeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            view.findViewById(R.id.item).performClick();
        }
    }

    private class ViewHolder {
        private SubViewHolder date;
        private SubViewHolder activity;
        private SubViewHolder workout;
        private SubViewHolder intensity;
        private SubViewHolder duration;
        private SubViewHolder distance;
        private SubViewHolder description;

        private ViewHolder(View v) {
            date = new SubViewHolder(v, R.id.training_date);
            activity = new SubViewHolder(v, R.id.training_activity);
            workout = new SubViewHolder(v, R.id.training_workout);
            intensity = new SubViewHolder(v, R.id.training_intensity);
            duration = new SubViewHolder(v, R.id.training_duration);
            distance = new SubViewHolder(v, R.id.training_distance);
            description = new SubViewHolder(v, R.id.training_description);
        }
    }

    private class SubViewHolder {
        private View parent;
        private View item;

        private SubViewHolder(View v, int id) {
            parent = v.findViewById(id);
            item = parent.findViewById(R.id.item);
        }
    }
}
