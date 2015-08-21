package com.michael.attackpoint;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.objects.LogInfo;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.LogViewHolder> {
    private static final String DEBUG_TAG = "attackpoint.MyAdapter";

    private List<LogInfo> logInfoList;
    private LogViewHolder logViewHolder;

    public MyAdapter(List<LogInfo> logInfoList) {
        this.logInfoList = logInfoList;
    }

    public void updateList(List<LogInfo> update) {
        logInfoList.addAll(update);
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
        //logViewHolder.vTitle.setTextColor(li.contrast);
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
        logViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "card clicked", Toast.LENGTH_SHORT).show();
                ViewOverlay mask = v.getOverlay();
                int h = v.getHeight();
                int w = v.getWidth();
                ColorDrawable overlay = new ColorDrawable(Color.parseColor("#AAFFFFFF"));
                overlay.setBounds(0,h,w,0);
                mask.add(overlay);
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
        protected TextView vTitle;
        protected View vColor;
        protected TextView vText;
        protected TextView vTime;
        protected TextView vDist;
        protected TextView vPace;
        protected TextView vSession;
        protected TextView vComments;

        protected FrameLayout vCard;

        public LogViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.log_type);
            vColor = v.findViewById(R.id.log_color);
            vText =  (TextView) v.findViewById(R.id.log_text);
            vTime = (TextView)  v.findViewById(R.id.log_time);
            vDist = (TextView) v.findViewById(R.id.log_distance);
            vPace = (TextView) v.findViewById(R.id.log_pace);
            vSession = (TextView) v.findViewById(R.id.log_session);
            vComments = (TextView) v.findViewById(R.id.log_comments);

            vCard = (FrameLayout) v.findViewById(R.id.log_container);
        }
    }
}