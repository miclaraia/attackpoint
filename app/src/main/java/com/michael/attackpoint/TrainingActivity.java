package com.michael.attackpoint;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.Duration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.training.AddTrainingRequest;
import com.michael.attackpoint.training.details.DateManager;
import com.michael.attackpoint.training.details.DurationManager;
import com.michael.attackpoint.training.details.IntensityManager;
import com.michael.attackpoint.training.details.ViewHolder;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "training";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Singleton.getInstance().setActivity(this);

        final ViewHolder vh = new ViewHolder(findViewById(R.id.training_parent));

        // initialize date
        DateManager date = new DateManager(vh.date, new LogDate());

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
        IntensityManager intensity = new IntensityManager(vh.intensity, 0);

        // initialize duration number picker
        DurationManager duration = new DurationManager(vh.duration, new Duration());

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

        // initialize submit button
        Button submit = vh.submit;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "submit button clicked, submitting training");
                submitTraining();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setActivity(this);
    }

    private void submitTraining() {
        ViewHolder vh = new ViewHolder(findViewById(R.id.training_parent));
        LogInfo li = new LogInfo();

        // Date
        DateManager dateManager = (DateManager) vh.date.parent.getTag();
        li.setDate(dateManager.getDetail());

        // Activity type
        Spinner spinner = (Spinner) vh.activity.item;
        li.setType(spinner.getSelectedItem().toString());

        // Workout type
        Spinner spinner2 = (Spinner) vh.workout.item;
        li.setWorkout(spinner2.getSelectedItem().toString());

        // Intensity
        IntensityManager intensity = (IntensityManager) vh.intensity.parent.getTag();
        li.setIntensity(intensity.getDetail());

        // Duration
        DurationManager duration = (DurationManager) vh.duration.parent.getTag();
        li.setDuration(duration.getDetail());

        // Distance
        TextView distance = (TextView) vh.distance.item;
        // TODO implement proper unit selection
        li.setDistance("" + distance.getText(), "km");

        // Description
        EditText description = (EditText) vh.description.item;
        li.setText(description.toString());

        Request request = new AddTrainingRequest(li, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                Log.d(DEBUG_TAG, aBoolean.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
        Log.d(DEBUG_TAG, "finished creating training request");
        Singleton.getInstance().add(request);
    }

    private class RelativeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            view.findViewById(R.id.item).performClick();
        }
    }
}
