package com.michael.attackpoint;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.log.Adapter;
import com.michael.attackpoint.log.Request;
import com.michael.attackpoint.log.loginfo.LogInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/23/15.
 */
public class LogFragment extends Fragment {

    public static final String USER_ID = "userid";

    private List<LogInfo> logInfoList;
    private RecyclerView recList;
    private Adapter adapter;
    private Singleton singleton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container,false);
        recList = (RecyclerView) view.findViewById(R.id.cardList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recList.setLayoutManager(linearLayout);

        // TODO initialize should actually initialize the network sequence and spawn an animation
        initializeData();
        initializeAdapter();

        //TODO should be handled differently
        singleton = Singleton.getInstance();
        singleton.setFragment(this);
        getLog();

        return view;
    }



    private void initializeData() {
        logInfoList = new ArrayList<>();
    }

    private void initializeAdapter() {
        adapter = new Adapter(this ,logInfoList);
        adapter.notifyDataSetChanged();
        recList.setAdapter(adapter);
    }

    public void getLog() {
        int userID = (int) getArguments().get(USER_ID);
        if (userID > 0) {
            Request request = new Request(userID,
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

}
