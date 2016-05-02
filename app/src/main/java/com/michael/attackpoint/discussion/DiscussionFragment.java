package com.michael.attackpoint.discussion;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.discussion.DiscussionContract.*;
import com.michael.attackpoint.discussion.DiscussionContract.View;
import com.michael.attackpoint.log.ViewHolder;
import com.michael.attackpoint.log.data.LogRepositories;
import com.michael.attackpoint.log.logentry.EntryPresenter;
import com.michael.attackpoint.log.loginfo.LogComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 4/26/16.
 */
public class DiscussionFragment extends Fragment implements View {

    private Presenter mPresenter;
    private DiscussionAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mAdapter = new DiscussionAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.loadEntry();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
//        int user = args.getInt(USER_ID);
//        int logId = args.getInt(LOG_ID);
//        mPresenter = new EntryPresenter(LogRepositories.getRepoInstance(), this, user, logId);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                                          Bundle savedInstanceState) {

        android.view.View root = inflater.inflate(R.layout.fragment_entry, viewGroup, false);

        /*RecyclerView recycler = (RecyclerView) root.findViewById(R.id.comments_list);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        ListView listView = (ListView) root.findViewById(R.id.discussion_list);
        listView.setAdapter(mAdapter);

//        View header = inflateHeader(discussion);
//        listView.addHeaderView(header);
//
//        mViewHolder = new com.michael.attackpoint.log.ViewHolder(root);

        return root;
    }

//    private View inflateHeader(Discussion discussion) {
//        View header = LayoutInflater.from(this).inflate(R.layout.content_discussion_header, null);
//        ViewHolder vh = new ViewHolder(header);
//        vh.title.setText(discussion.getTitle());
//        vh.category.setText(discussion.getCategory());
//
//        return header;
//    }

    public static class DiscussionAdapter extends BaseAdapter {
        private static final String DEBUG_TAG = "discussion.A";

        private List<Comment> mComments;

        public DiscussionAdapter(List<Comment> comments) {
            mComments = comments;
        }

        public DiscussionAdapter(Activity activity) {
            mComments = new ArrayList<Comment>();
        }

        public void updateList(int position, Comment comment) {
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
        }

        public void setList(List<Comment> update) {
            mComments = update;
            this.notifyDataSetChanged();
        }

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
                convertView = inflater.inflate(R.layout.adapter_comment, null);
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

    private static class ViewHolder {
        private TextView title;
        private TextView category;

        private ViewHolder(android.view.View v) {
            title = (TextView) v.findViewById(R.id.discussion_title);
            category = (TextView) v.findViewById(R.id.discussion_category);
        }
    }
}
