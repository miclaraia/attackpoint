package com.michael.attackpoint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.discussion.Adapter;
import com.michael.attackpoint.discussion.Comment;
import com.michael.attackpoint.discussion.Discussion;
import com.michael.attackpoint.discussion.Request;

import java.util.ArrayList;

public class DiscussionActivity extends AppCompatActivity {
    public final static String DISCUSSION_ID = "d_id";
    private ListView mListView;
    private Adapter mAdapter;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singleton = Singleton.getInstance();
        singleton.setActivity(this);

        setContentView(R.layout.activity_discussion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getDiscussion();
    }

    private void getDiscussion() {
        int id = getIntent().getExtras().getInt(DISCUSSION_ID);
        Request request = new Request(id, new Response.Listener<Discussion>() {
            @Override
            public void onResponse(Discussion discussion) {
                initList(discussion);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        singleton.add(request);
    }

    private void initList(Discussion discussion) {
        mListView = (ListView) findViewById(R.id.discussion);
        mAdapter = new Adapter(this, discussion.getComments());
        mListView.setAdapter(mAdapter);

        View header = inflateHeader(discussion);
        mListView.addHeaderView(header);

        //get recyclerview from layout
        /*mRecyclerView = (RecyclerView) findViewById(R.id.discussion);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayout);*/

        //create adapter and attach to recyclerview\
        /*mAdapter = new DrawerAdapter(this, discussion.getComments());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);*/
    }

    private View inflateHeader(Discussion discussion) {
        View header = LayoutInflater.from(this).inflate(R.layout.content_discussion_header, null);
        ViewHolder vh = new ViewHolder(header);
        vh.title.setText(discussion.getTitle());
        vh.category.setText(discussion.getCategory());

        return header;
    }

    private class ViewHolder {
        private TextView title;
        private TextView category;

        private ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.discussion_title);
            category = (TextView) v.findViewById(R.id.discussion_category);
        }
    }

}
