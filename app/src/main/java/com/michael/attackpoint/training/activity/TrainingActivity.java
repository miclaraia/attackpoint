package com.michael.attackpoint.training.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogIntensity;
import com.michael.attackpoint.log.loginfo.LogSession;
import com.michael.attackpoint.training.details.ActivityManager;
import com.michael.attackpoint.training.details.DateManager;
import com.michael.attackpoint.training.details.DescriptionManager;
import com.michael.attackpoint.training.details.DistanceManager;
import com.michael.attackpoint.training.details.DurationManager;
import com.michael.attackpoint.training.details.IntensityManager;
import com.michael.attackpoint.training.details.SessionManager;
import com.michael.attackpoint.training.details.ViewHolder;

/**
 * Created by michael on 3/11/16.
 */
public abstract class TrainingActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "training";
    protected Managers mManagers;
    protected LogInfo mLogInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Singleton.getInstance().setActivity(this);

        ViewHolder vh = new ViewHolder(findViewById(R.id.training_parent));
        init(vh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setActivity(this);
    }

    protected void init(ViewHolder viewHolder) {
        mLogInfo = initLogInfo();
        mManagers = initManagers(viewHolder);

        final ViewHolder vh = viewHolder;

        View workout = vh.workout.parent;
        workout.setOnClickListener(new RelativeClickListener());
        Spinner workoutSpinner = (Spinner) vh.workout.item;
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(this,
                R.array.training_workout, android.R.layout.simple_spinner_item);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutSpinner.setAdapter(workoutAdapter);

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

    private void submitTraining() {
        mLogInfo = updateLogInfo(mLogInfo);

        Request request = performRequest(mLogInfo);
        Singleton.getInstance().add(request);
    }

    protected LogInfo updateLogInfo(LogInfo li) {
        // pickers
        li = mManagers.activity.updateLogInfo(li);
        li = mManagers.date.updateLogInfo(li);
        li = mManagers.duration.updateLogInfo(li);
        li = mManagers.intensity.updateLogInfo(li);
        li = mManagers.session.updateLogInfo(li);

        // edittexts
        li = mManagers.distance.updateLogInfo(li);
        li = mManagers.description.updateLogInfo(li);

        // Workout type
        /*Spinner spinner2 = (Spinner) vh.workout.item;
        String workout = spinner2.getSelectedItem().toString();
        li.set(LogInfo.KEY_WORKOUT, new LogWorkout(workout));*/

        return li;
    }

    protected abstract LogInfo initLogInfo();
    protected abstract Managers initManagers(ViewHolder vh);
    protected abstract Request performRequest(LogInfo li);

    protected class RelativeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            view.findViewById(R.id.item).performClick();
        }
    }

    protected static class Managers {
        protected ActivityManager activity;
        protected DateManager date;
        protected DurationManager duration;
        protected IntensityManager intensity;
        protected SessionManager session;

        protected DistanceManager distance;
        protected DescriptionManager description;

        protected Managers(Context context, ViewHolder vh) {
            activity = new ActivityManager(vh.activity, new LogInfoActivity());
            date = new DateManager(vh.date, new LogDate());
            duration = new DurationManager(vh.duration, new LogDuration());
            intensity = new IntensityManager(vh.intensity, new LogIntensity());
            session = new SessionManager(vh.session, new LogSession());

            distance = new DistanceManager(context, vh.distance, new LogDistance());
            description = new DescriptionManager(context, vh.description, new LogDescription());
        }

        protected Managers(Context context, ViewHolder vh, LogInfo li) {
            activity = new ActivityManager(vh.activity, li);
            date = new DateManager(vh.date, li);
            duration = new DurationManager(vh.duration, li);
            intensity = new IntensityManager(vh.intensity, li);
            session = new SessionManager(vh.session, li);

            distance = new DistanceManager(context, vh.distance, li);
            description = new DescriptionManager(context, vh.description, li);
        }
    }
}
