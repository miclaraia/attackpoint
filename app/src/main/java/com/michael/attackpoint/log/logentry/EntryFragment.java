package com.michael.attackpoint.log.logentry;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.account.Login;
import com.michael.attackpoint.log.ViewHolder;
import com.michael.attackpoint.log.addentry.activity.TrainingFragment;
import com.michael.attackpoint.log.data.LogRepositories;
import com.michael.attackpoint.log.loginfo.LogComment;
import com.michael.attackpoint.log.loginfo.LogComment.Comment;
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 4/16/16.
 */
public class EntryFragment extends Fragment implements EntryContract.View {
    private static final String USER_ID = "userid";
    private static final String LOG_ID = "logid";

    private EntryContract.Presenter mPresenter;
    private CommentsAdapter mAdapter;
    private ViewHolder mViewHolder;

    public static EntryFragment newInstance(int user_id, int log_id) {
        Bundle arguments = new Bundle();
        arguments.putInt(USER_ID, user_id);
        arguments.putInt(LOG_ID, log_id);

        EntryFragment fragment = new EntryFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    public static EntryFragment newInstance (Bundle args) {
        EntryFragment fragment = new EntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CommentsAdapter(new ArrayList<Comment>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadEntry();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        int user = args.getInt(USER_ID);
        int logId = args.getInt(LOG_ID);
        mPresenter = new EntryPresenter(LogRepositories.getRepoInstance(), this, user, logId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_entry, viewGroup, false);

        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.comments_list);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        initFloatingActionButton();
        mViewHolder = new ViewHolder(root);

        return root;
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            if (isMyAccount()) {
                // Own log, button opens window to add training
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.drawable.ic_playlist_add);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = getActivity();
                        Fragment fragment = TrainingFragment.newInstance();
                        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();

                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });
            } else {
                // Someone else's log, button opens window to add comment
                // TODO
                fab.setVisibility(View.GONE);
            }
        }
    }

    private boolean isMyAccount() {
        int myUser = Login.getInstance().getUserId();
        if (myUser == getArguments().getInt(USER_ID)) return true;
        return false;
    }

    @Override
    public void showSnackbar(String message) {

    }

    @Override
    public void ShowEntry(LogInfo logInfo) {
        mViewHolder.setFull(logInfo);

        LogComment logComment = (LogComment) logInfo.get(LogInfo.KEY_COMMENT);
        mAdapter.replaceData(logComment.get());
    }

    private class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        List<Comment> mItems;

        public CommentsAdapter(List<Comment> comments) {
            setList(comments);
        }

        public void replaceData(List<Comment> comments) {
            setList(comments);
            this.notifyDataSetChanged();
        }

        private void setList(List<Comment> entries) {
            mItems = entries;
        }

        public Comment getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.log_card_comment, parent, false);

            return new CommentViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = getItem(position);

            holder.title.setText(comment.getTitle());
            holder.author.setText(comment.getAuthor());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public Button button;
        public TextView title;
        public TextView author;

        public CommentViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.log_comment_button);
            title = (TextView) itemView.findViewById(R.id.log_comment_title);
            author = (TextView) itemView.findViewById(R.id.log_comment_author);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            
        }

        /*public CommentViewHolder(View parent) {
            button = (Button) parent.findViewById(R.id.log_comment_button);
            title = (TextView) parent.findViewById(R.id.log_comment_title);
            author = (TextView) parent.findViewById(R.id.log_comment_author);

            parent.setOnClickListener(this);
        }*/
    }
}
