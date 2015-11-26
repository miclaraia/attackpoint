package com.michael.attackpoint.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.michael.attackpoint.R;

/**
 * Created by michael on 11/26/15.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.LogViewHolder> {

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public View vColor;
        public TextView vText;
        public TextView vDate;
        public TextView vTime;
        public TextView vDist;
        public TextView vPace;
        public TextView vSession;
        public TextView vComments;

        public FrameLayout vCard;

        public LogViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.log_type);
            vColor = v.findViewById(R.id.log_color);
            vText =  (TextView) v.findViewById(R.id.log_text);
            vDate = (TextView) v.findViewById(R.id.log_date);
            vTime = (TextView)  v.findViewById(R.id.log_time);
            vDist = (TextView) v.findViewById(R.id.log_distance);
            vPace = (TextView) v.findViewById(R.id.log_pace);
            vSession = (TextView) v.findViewById(R.id.log_session);
            vComments = (TextView) v.findViewById(R.id.log_comments);

            vCard = (FrameLayout) v.findViewById(R.id.log_container);
        }
    }
}
