package com.michael.attackpoint.log;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.michael.attackpoint.LogDetailActivity;
import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.LogViewHolder> {
    private static final String DEBUG_TAG = "attackpoint.Adapter";

    private List<LogInfo> logInfoList;
    private Fragment fragment;

    public Adapter(Fragment fragment, List<LogInfo> logInfoList) {
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
    public void onBindViewHolder(LogViewHolder lvh, int i) {
        ViewHolder vh = lvh.vh;
        LogInfo li = logInfoList.get(i);
        
        vh.setDetails(li);

        // TODO proper animation for click
        //Attach click listener to each card and define click behavior
        vh.vCard.setTag(i);
        vh.vCard.setOnClickListener(new View.OnClickListener() {
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
        public ViewHolder vh;

        public LogViewHolder(View v) {
            super(v);
            vh = new ViewHolder(v);
        }
    }
}