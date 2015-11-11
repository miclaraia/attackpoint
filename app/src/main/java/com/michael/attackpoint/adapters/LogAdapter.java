package com.michael.attackpoint.adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.attackpoint.LogDetailActivity;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.objects.LogInfo;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private static final String DEBUG_TAG = "attackpoint.LogAdapter";

    private List<LogInfo> logInfoList;
    private Fragment fragment;
    private LogViewHolder logViewHolder;

    public LogAdapter(Fragment fragment, List<LogInfo> logInfoList) {
        this.logInfoList = logInfoList;
        this.fragment = fragment;
    }

    public void updateList(List<LogInfo> update) {
        logInfoList.addAll(update);
        this.notifyDataSetChanged();
    }

    public void setList(List<LogInfo> update) {
        logInfoList = update;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return logInfoList.size();
    }

    @Override
    public void onBindViewHolder(LogViewHolder logViewHolder, int i) {
        LogInfo.Strings li = logInfoList.get(i).strings();

        //Sets title and its colors
        logViewHolder.vTitle.setText(li.type);
        logViewHolder.vDate.setText(li.date);
        logViewHolder.vColor.setBackgroundColor(li.color);
        //logViewHolder.vColor.setBackgroundColor(li.color);

        //Sets snippet text, removes view if no text
        if (li.snippet == null || li.snippet == "" || li.snippet.length() == 0) logViewHolder.vText.setVisibility(View.GONE);
        else logViewHolder.vText.setText(li.snippet);

        //Sets log entry's meta data
        logViewHolder.vDist.setText(li.distance);
        logViewHolder.vPace.setText(li.pace);
        logViewHolder.vTime.setText(li.time);

        // TODO incorporate comments and session time
        //logViewHolder.vComments.setText(li.comments);
        //logViewHolder.vSession.setText(li.session);


        // TODO proper animation for click
        //Attach click listener to each card and define click behavior
        logViewHolder.vCard.setTag(i);
        logViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewOverlay mask = v.getOverlay();
                int h = v.getHeight();
                int w = v.getWidth();
                ColorDrawable overlay = new ColorDrawable(Color.parseColor("#AAFFFFFF"));
                overlay.setBounds(0, h, w, 0);
                mask.add(overlay);

                Intent intent = new Intent(fragment.getActivity(), LogDetailActivity.class);
                LogInfo loginfo = logInfoList.get((int) v.getTag());
                intent.putExtra(LogDetailActivity.DETAILS, loginfo.toString());
                fragment.startActivity(intent);

            }
        });
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.log_item, viewGroup, false);

        return new LogViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

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