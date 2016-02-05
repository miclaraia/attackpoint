package com.michael.attackpoint.discussion;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.michael.attackpoint.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by michael on 2/3/16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private static final String DEBUG_TAG = "discussion.A";

    private List<Comment> mComments;
    private Activity mActivity;

    public Adapter(Activity activity, List<Comment> comments) {
        mActivity = activity;
        mComments = comments;
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
    public int getItemCount() {
        return mComments.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        final Comment comment = mComments.get(i);
        vh.user.setText(comment.getUser());
        vh.date.setText(comment.getDate());
        vh.content.setText(comment.getText());

        // colors comment background alternating light/dark
        int color = 0;
        if ((i & 1) == 0) {
            color = mActivity.getResources().getColor(R.color.colorCardDialog);
        } else {
            color = mActivity.getResources().getColor(R.color.colorBackground);
        }
        vh.container.setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.adapter_comment, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user;
        public TextView date;
        public TextView content;
        public RelativeLayout container;

        public ViewHolder(View v) {
            super(v);
            user = (TextView) v.findViewById(R.id.comment_user);
            date = (TextView) v.findViewById(R.id.comment_date);
            content = (TextView) v.findViewById(R.id.comment_content);
            container = (RelativeLayout) v.findViewById(R.id.comment_container);
        }
    }
}
