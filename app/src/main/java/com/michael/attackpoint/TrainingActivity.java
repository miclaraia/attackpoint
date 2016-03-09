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
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogInfoItem;
import com.michael.attackpoint.log.loginfo.LogIntensity;
import com.michael.attackpoint.log.loginfo.LogSession;
import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.training.AddTrainingRequest;
import com.michael.attackpoint.training.details.ActivityManager;
import com.michael.attackpoint.training.details.DateManager;
import com.michael.attackpoint.training.details.DurationManager;
import com.michael.attackpoint.training.details.IntensityManager;
import com.michael.attackpoint.training.details.SessionManager;
import com.michael.attackpoint.training.details.ViewHolder;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "training";
    private Managers mManagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Singleton.getInstance().setActivity(this);

        final ViewHolder vh = new ViewHolder(findViewById(R.id.training_parent));
        mManagers = new Managers(vh);

        View workout = vh.workout.parent;
        workout.setOnClickListener(new RelativeClickListener());
        Spinner workoutSpinner = (Spinner) vh.workout.item;
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_workout, android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutAdapter);

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

        // Activity type
        li.set(LogInfo.KEY_ACTIVITY, mManagers.activity.getDetail());
        // Date
        li.set(LogInfo.KEY_DATE, mManagers.date.getDetail());
        // Duration
        li.set(LogInfo.KEY_DURATION, mManagers.duration.getDetail());
        // Intensity
        li.set(LogInfo.KEY_INTENSITY, mManagers.intensity.getDetail());

        // Workout type
        /*Spinner spinner2 = (Spinner) vh.workout.item;
        String workout = spinner2.getSelectedItem().toString();
        li.set(LogInfo.KEY_WORKOUT, new LogWorkout(workout));*/

        // Distance
        TextView distance = (TextView) vh.distance.item;
        // TODO implement proper unit selection
        String d = distance.getText().toString();
        if (!d.equals(""))
            li.set(LogInfo.KEY_DISTANCE, new LogDistance(Float.parseFloat(d), "km"));

        // Description
        EditText description = (EditText) vh.description.item;
        li.set(LogInfo.KEY_DESCRIPTION, new LogDescription(description.getText().toString()));

        Request request = new AddTrainingRequest(li, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                Log.d(DEBUG_TAG, aBoolean.toString());
                finish();
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

    private static class Managers {
        private ActivityManager activity;
        private DateManager date;
        private DurationManager duration;
        private IntensityManager intensity;
        private SessionManager session;

        private Managers(ViewHolder vh) {
            activity = new ActivityManager(vh.activity, new LogInfoActivity());
            date = new DateManager(vh.date, new LogDate());
            duration = new DurationManager(vh.duration, new LogDuration());
            intensity = new IntensityManager(vh.intensity, new LogIntensity());
            session = new SessionManager(vh.session, new LogSession());
        }
    }
}
