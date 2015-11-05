package com.michael.attackpoint;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.michael.attackpoint.adapters.LogAdapter;
import com.michael.database.CookieTable;
import com.michael.network.NetworkLog;
import com.michael.objects.LogInfo;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/23/15.
 */
public class LogFragment extends Fragment {

    private List<LogInfo> logInfoList;
    private RecyclerView recList;
    private LogAdapter adapter;
    private LinearLayoutManager linearLayout;
    private Singleton singleton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container,false);
        recList = (RecyclerView) view.findViewById(R.id.cardList);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        linearLayout = new LinearLayoutManager(activity);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void initializeData() {
        logInfoList = new ArrayList<>();
    }

    private void initializeAdapter() {
        adapter = new LogAdapter(this ,logInfoList);
        adapter.notifyDataSetChanged();
        recList.setAdapter(adapter);
    }

    public void getLog() {
        String userID = CookieTable.getCurrentID();
        if (userID != null) {
            NetworkLog request = new NetworkLog(userID,
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
