package com.michael.attackpoint.log.addentry.details;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract;

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

        public void setText(String text) {
            ((TextView) item).setText(text);
        }

        public void setClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }

        public void setEditTextListener(final ManagerContract.Activity activity) {

            final View fItem = item;
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fItem.requestFocusFromTouch();
                    InputMethodManager lManager = (InputMethodManager) activity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(fItem, 0);
                }
            });
        }

        public String getEditText() {
            return ((EditText) item).getText().toString();
        }
    }
}
