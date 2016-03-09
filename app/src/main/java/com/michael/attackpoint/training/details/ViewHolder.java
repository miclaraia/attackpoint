package com.michael.attackpoint.training.details;

import android.view.View;
import android.widget.Button;

import com.michael.attackpoint.R;

/**
 * Created by michael on 2/25/16.
 */
public class ViewHolder {
    public SubViewHolder date;
    public SubViewHolder session;
    public SubViewHolder activity;
    public SubViewHolder workout;
    public SubViewHolder intensity;
    public SubViewHolder duration;
    public SubViewHolder distance;
    public SubViewHolder description;
    public Button submit;

    public ViewHolder(View v) {
        date = new SubViewHolder(v, R.id.training_date);
        session = new SubViewHolder(v, R.id.training_session);
        activity = new SubViewHolder(v, R.id.training_activity);
        workout = new SubViewHolder(v, R.id.training_workout);
        intensity = new SubViewHolder(v, R.id.training_intensity);
        duration = new SubViewHolder(v, R.id.training_duration);
        distance = new SubViewHolder(v, R.id.training_distance);
        description = new SubViewHolder(v, R.id.training_description);

        submit = (Button) v.findViewById(R.id.training_submit);
    }

    public class SubViewHolder {
        public View parent;
        public View item;

        public SubViewHolder(View v, int id) {
            parent = v.findViewById(id);
            item = parent.findViewById(R.id.item);
        }
    }
}
