package com.michael.attackpoint.log.loglist;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.ViewHolder;
import com.michael.attackpoint.log.logdetail.LogDetailActivity;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.Note;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private static final String DEBUG_TAG = "attackpoint.DrawerAdapter";

    private List<LogInfo> logInfoList;
    private Fragment fragment;

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
    public void onBindViewHolder(LogViewHolder lvh, int i) {
        ViewHolder vh = lvh.vh;
        LogInfo li = logInfoList.get(i);
        
        vh.setSnippet(li);

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
                intent.putExtra(LogDetailActivity.DETAILS, loginfo.toJSON().toString());

                String name;
                if (loginfo instanceof Note) name = Note.NAME;
                else name = loginfo.NAME;

                intent.putExtra(LogDetailActivity.NAME, name);
                fragment.startActivity(intent);

            }
        });
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_log, viewGroup, false);

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