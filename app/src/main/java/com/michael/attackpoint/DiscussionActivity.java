package com.michael.attackpoint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.michael.attackpoint.discussion.Adapter;

public class DiscussionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
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

        mRecyclerView = (RecyclerView) findViewById(R.id.discussion);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        //TODO recyclerview is null becuase not logged in. Something in the view is null?
        mRecyclerView.setLayoutManager(linearLayout);

        mAdapter = new UsersAdapter(this, new ArrayList<User>());
        mAdapter.notifyDataSetChanged();
        getFavorites();

        mRecyclerView.setAdapter(mAdapter);
    }

}
