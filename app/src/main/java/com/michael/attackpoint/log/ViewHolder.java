package com.michael.attackpoint.log;

import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.Note;

/**
 * Created by michael on 2/18/16.
 */
public class ViewHolder {
    public TextView vTitle;
    public View vColor;
    public TextView vText;
    public TextView vDate;

    public TextViewDetail vTime;
    public TextViewDetail vDist;
    public TextViewDetail vPace;
    public TextViewDetail vClimb;
    public TextViewDetail vEffort;

    public TextView vSession;
    public TextView vComments;

    public FrameLayout vCard;

    public ViewHolder(View v) {
        vTitle = (TextView) v.findViewById(R.id.log_type);
        vColor = v.findViewById(R.id.log_color);
        vText =  (TextView) v.findViewById(R.id.log_text);
        vDate = (TextView) v.findViewById(R.id.log_date);

        vTime = new TextViewDetail(v.findViewById(R.id.log_time));
        vDist = new TextViewDetail(v.findViewById(R.id.log_distance));
        vPace = new TextViewDetail(v.findViewById(R.id.log_pace));
        vClimb = new TextViewDetail(v.findViewById(R.id.log_climb));
        vEffort = new TextViewDetail(v.findViewById(R.id.log_effort));


        vSession = (TextView) v.findViewById(R.id.log_session);
        vComments = (TextView) v.findViewById(R.id.log_comments);

        vCard = (FrameLayout) v.findViewById(R.id.log_container);

        // TODO incorporate session time
        //vh.vSession.setText(li.session);
    }

    public void setDetails(LogInfo li) {
        LogInfo.Strings text = li.strings();

        //Sets title and its colors
        vTitle.setText(text.activity);
        vDate.setText(text.date);
        vColor.setBackgroundColor(text.color);

        //Sets log entry's meta data
        //skips these steps if entry is a note
        if (!(li instanceof Note)) {
            if (!li.get(LogInfo.KEY_DURATION).isEmpty()) vTime.setText(text.duration);
            if (!li.get(LogInfo.KEY_DISTANCE).isEmpty()) vDist.setText(text.distance);
            if (!li.get(LogInfo.KEY_PACE).isEmpty()) vPace.setText(text.pace);
            if (!li.get(LogInfo.KEY_CLIMB).isEmpty()) vClimb.setText(text.climb);
        }
    }

    public void setFull(LogInfo li) {
        setDetails(li);
        String text = li.get(LogInfo.KEY_DESCRIPTION).toString();
        vText.setText(Html.fromHtml(text));
    }

    public void setSnippet(LogInfo li) {
        setDetails(li);
        LogDescription ld = (LogDescription) li.get(LogInfo.KEY_DESCRIPTION);
        if (ld.isEmpty()) vText.setVisibility(View.GONE);
        else {
            String text = ld.toSnippet();
            vText.setText(Html.fromHtml(text));
        }
    }

    public static class TextViewDetail {
        private TextView mText;

        public TextViewDetail (View tv) {
            mText = (TextView) tv;
        }

        public void setText(String text) {
            mText.setText(text);
            mText.setVisibility(View.VISIBLE);
        }
    }
}
