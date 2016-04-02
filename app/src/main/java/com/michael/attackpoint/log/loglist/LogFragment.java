package com.michael.attackpoint.log.loglist;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.R;
import com.michael.attackpoint.log.data.LogRepositories;
import com.michael.attackpoint.log.data.LogRequest;
import com.michael.attackpoint.log.ViewHolder;
import com.michael.attackpoint.log.logdetail.LogDetailActivity;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.training.activity.AddTrainingActivity;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by michael on 8/23/15.
 */
public class LogFragment extends Fragment implements LogContract.View {
    private static final String DEBUG_TAG = "logfragment";
    public static final String USER_ID = "userid";

    private LogAdapter mAdapter;
    private LogContract.Presenter mPresenter;

    public static LogFragment newInstance (String user_id) {
        Bundle arguments = new Bundle();
        arguments.putString(USER_ID, user_id);
        LogFragment fragment = new LogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static LogFragment newInstance (Bundle args) {
        LogFragment fragment = new LogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new LogAdapter(new ArrayList<LogInfo>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadLog(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        int user = getArguments().getInt(USER_ID);
        mPresenter = new LogPresenter(LogRepositories.getRepoInstance(), this, user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_log, viewGroup, false);
        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.cardList);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // TODO Floating action button
        // TODO pull to refresh
        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadLog(true);
            }
        });

        return root;
    }

    /**
     * Listener for clicks on log entries.
     */
    LogItemListener mItemListener = new LogItemListener() {
        @Override
        public void onLogEntryClick(LogInfo clickedItem) {
            mPresenter.openEntryDetails(clickedItem);
        }
    };

    @Override
    public void setProgressIndicator(final boolean state) {
        Log.d(DEBUG_TAG, String.format(Locale.US, "setting refresh indicator to %s", (state)?"true":"false"));
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(state);
            }
        });
    }

    @Override
    public void showLog(List<LogInfo> log) {
        mAdapter.replaceData(log);
    }

    @Override
    public void showEntryDetail(String logId) {
        Log.d(DEBUG_TAG, String.format(Locale.US, "starting activity for logid %s", logId));
    }

    @Override
    public void showAddEntry() {
        Intent intent = new Intent(getActivity(), AddTrainingActivity.class);
        startActivity(intent);
    }

    public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
        private static final String DEBUG_TAG = "attackpoint.DrawerAdapter";

        private List<LogInfo> mEntries;
        private LogItemListener mItemListener;

        public LogAdapter(List<LogInfo> logList, LogItemListener listener) {
            setList(logList);
            mItemListener = listener;
        }

        public void replaceData(List<LogInfo> entries) {
            setList(entries);
            this.notifyDataSetChanged();
        }

        private void setList(List<LogInfo> entries) {
            mEntries = entries;
        }

        public LogInfo getItem(int position) {
            return mEntries.get(position);
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.card_log, viewGroup, false);

            return new LogViewHolder(itemView, mItemListener);
        }

        @Override
        public void onBindViewHolder(LogViewHolder logViewHolder, int position) {
            ViewHolder vh = logViewHolder.viewHolder;
            LogInfo entry = getItem(position);
            vh.setSnippet(entry);
        }

        public class LogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ViewHolder viewHolder;

            private LogItemListener mItemListener;

            public LogViewHolder(View itemView, LogItemListener listener) {
                super(itemView);
                viewHolder = new ViewHolder(itemView);

                mItemListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                ViewOverlay mask = v.getOverlay();
                int h = v.getHeight();
                int w = v.getWidth();
                ColorDrawable overlay = new ColorDrawable(Color.parseColor("#AAFFFFFF"));
                overlay.setBounds(0, h, w, 0);
                mask.add(overlay);

                int position = getAdapterPosition();
                LogInfo item = getItem(position);
                mItemListener.onLogEntryClick(item);
            }
        }
    }

    public interface LogItemListener {

        void onLogEntryClick(LogInfo clickedItem);
    }

}