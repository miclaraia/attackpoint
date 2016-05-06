package com.michael.attackpoint.discussion;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.discussion.DiscussionContract.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 4/26/16.
 */
public class DiscussionFragment extends Fragment implements DiscussionContract.View {
    public static final String DISCUSSION_ID = "discussionid";

    private Presenter mPresenter;
    private DiscussionAdapter mAdapter;
    private ListView mListView;

    public static DiscussionFragment newInstance(int discussionId) {
        Bundle args = new Bundle();
        args.putInt(DISCUSSION_ID, discussionId);

        DiscussionFragment fragment = new DiscussionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DiscussionFragment newInstance(Bundle args) {
        DiscussionFragment fragment = new DiscussionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DiscussionAdapter(new ArrayList<Comment>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadDiscussion();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        int id = args.getInt(DISCUSSION_ID);
        mPresenter = new DiscussionPresenter(DiscussionRepositories.getRepoInstance(), this, id);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                                          Bundle savedInstanceState) {

        android.view.View root = inflater.inflate(R.layout.fragment_discussion, viewGroup, false);

        /*RecyclerView recycler = (RecyclerView) root.findViewById(R.id.comments_list);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        mListView = (ListView) root.findViewById(R.id.discussion_list);
        mListView.setAdapter(mAdapter);

        return root;
    }

    View.OnClickListener mItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO
        }
    };

    @Override
    public void setProgressIndicator(final boolean state) {
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
    public void showSnackbar(String message) {
        CoordinatorLayout coordinator = (CoordinatorLayout)
                getActivity().findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showDiscussion(Discussion discussion) {
        mListView.addHeaderView(inflateHeader(discussion));
        mAdapter.setList(discussion.getComments());
    }

    @Override
    public void showNewComment() {
        // TODO activity to add new comment
    }

    private View inflateHeader(Discussion discussion) {
        Context context = getActivity();
        LayoutInflater inflater = LayoutInflater.from(context);
        View header = inflater.inflate(R.layout.discussion_header, null);

        TextView title = (TextView) header.findViewById(R.id.discussion_title);
        TextView category = (TextView) header.findViewById(R.id.discussion_category);

        title.setText(discussion.getTitle());
        category.setText(discussion.getCategory());

        return header;
    }


    public static class DiscussionAdapter extends BaseAdapter {
        private static final String DEBUG_TAG = "discussion.A";

        private List<Comment> mComments;
        private View.OnClickListener mListener;

        public DiscussionAdapter(List<Comment> comments, View.OnClickListener listener) {
            mComments = comments;
            mListener = listener;
        }

        public void replaceData(List<Comment> comments) {
            setList(comments);
            this.notifyDataSetChanged();
        }

        private void setList(List<Comment> comments) {
            mComments = comments;
        }

        /*public void updateList(int position, Comment comment) {
            mComments.set(position, comment);
            this.notifyDataSetChanged();
        }

        public void updateList(Comment comment) {
            for (int i = 0; i < mComments.size(); i++) {
                if (comment.getId() == mComments.get(i).getId()) {
                    updateList(i, comment);
                    break;
                }
            }
        }*/

        @Override
        public int getCount() {
            return mComments.size();
        }

        @Override
        public Object getItem(int position) {
            return mComments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mComments.get(position).getId();
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.discussion_comment, null);
            }

            //set text inside convertView
            ItemViewHolder vh = new ItemViewHolder(convertView);
            final Comment comment = mComments.get(position);
            vh.user.setText(comment.getUsername());
            vh.date.setText(comment.getDate());
            vh.content.setText(comment.getText());

            // colors comment background alternating light/dark
            int color = 0;
            Resources res = parent.getContext().getResources();
            if ((position & 1) == 0) {
                color = res.getColor(R.color.colorCardDialog);
            } else {
                color = res.getColor(R.color.colorBackground);
            }
            vh.container.setBackgroundColor(color);
            vh.container.setOnClickListener(mListener);

            return convertView;
        }

        public static class ItemViewHolder {
            public TextView user;
            public TextView date;
            public TextView content;
            public RelativeLayout container;

            public ItemViewHolder(android.view.View v) {
                user = (TextView) v.findViewById(R.id.comment_user);
                date = (TextView) v.findViewById(R.id.comment_date);
                content = (TextView) v.findViewById(R.id.comment_content);
                container = (RelativeLayout) v.findViewById(R.id.comment_container);
            }
        }
    }
}
