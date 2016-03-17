package com.michael.attackpoint.log.loglist;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.R;
import com.michael.attackpoint.log.data.LogRequest;
import com.michael.attackpoint.log.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/23/15.
 */
public class LogFragment extends Fragment {

    public static final String ARGUMENT_ID = "userid";

    private List<LogInfo> mLogList;
    //private RecyclerView mRecyclerView;
    private LogAdapter mAdapter;
    private Singleton mSingleton;
    private LogContract.Presenter mActionsListener;

    public static LogFragment newInstance (String user_id) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ID, user_id);
        LogFragment fragment = new LogFragment();
        fragment.setArguments(arguments);
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
        mActionsListener.loadNotes(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        mActionsListener = new NotesPresenter(Injection.provideNotesRepository(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View container = inflater.inflate(R.layout.fragment_log, viewGroup, false);
        RecyclerView recycler = (RecyclerView) container.findViewById(R.id.cardList);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // TODO initialize should actually initialize the network sequence and spawn an animation
        initializeData();
        initializeAdapter();

        //TODO should be handled differently
        mSingleton = Singleton.getInstance();
        getLog();

        return container;
    }

    /**
     * Listener for clicks on notes in the RecyclerView.
     */
    LogItemListener mItemListener = new LogItemListener() {
        @Override
        public void onNoteClick(LogInfo clickedItem) {
            mActionsListener.openEntryDetails(clickedItem);
        }
    };



    private void initializeData() {
        mLogList = new ArrayList<>();
    }

    private void initializeAdapter() {
        adapter = new LogAdapter(this ,logInfoList);
        adapter.notifyDataSetChanged();
        recList.setAdapter(adapter);
    }

    public void getLog() {
        int userID = (int) getArguments().get(USER_ID);
        if (userID > 0) {
            LogRequest request = new LogRequest(userID,
                    new Response.Listener<List<LogInfo>>() {
                        @Override
                        public void onResponse(List<LogInfo> response) {
                            adapter.setList(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Error handling
                    System.out.println("Something went wrong!");
                    error.printStackTrace();
                }
            });


            // Add the request to the queue
            singleton.add(request);
        }
    }

    public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
        private static final String DEBUG_TAG = "attackpoint.DrawerAdapter";

        private List<LogInfo> mEntries;
        private LogItemListener mItemListener;

        public LogAdapter(List<LogInfo> logList, LogItemListener listener) {
            setList(logList);
            mItemListener = listener;
        }

        public void updateList(List<LogInfo> entries) {
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
                mItemListener.onNoteClick(item);

                /*Intent intent = new Intent(fragment.getActivity(), LogDetailActivity.class);
                LogInfo loginfo = logInfoList.get((int) v.getTag());
                intent.putExtra(LogDetailActivity.DETAILS, loginfo.toJSON().toString());

                String name;
                if (loginfo instanceof Note) name = Note.NAME;
                else name = loginfo.NAME;

                intent.putExtra(LogDetailActivity.NAME, name);
                fragment.startActivity(intent);*/
            }
        }
    }

    public interface LogItemListener {

        void onNoteClick(LogInfo clickedItem);
    }

}