package com.michael.attackpoint;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michael.attackpoint.adapters.LogAdapter;
import com.michael.network.apLog;
import com.michael.objects.LogInfo;

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
        new apLog(adapter).getLog();

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
        /*logInfoList.add(new LogInfo("Test 1<br>Test2<strong>test3</strong>", "Bike", "13.44", "km", "44:56"));
        logInfoList.add(new LogInfo("Test 2", "Run", "1.2", "km", "13:33"));
        logInfoList.add(new LogInfo("Test 3", "Core","1","b","10:00"));
        logInfoList.add(new LogInfo("Test 4", "Core","1","b","10:00"));
        logInfoList.add(new LogInfo("Test 5", "Core","1","b","10:00"));
        logInfoList.add(new LogInfo("Test 6", "Core","1","b","10:00"));
        logInfoList.add(new LogInfo("Test 7", "Core","1","b","10:00"));
        logInfoList.add(new LogInfo("Test 8", "Core","1","b","10:00"));*/
    }

    private void initializeAdapter() {
        adapter = new LogAdapter(logInfoList);
        adapter.notifyDataSetChanged();
        recList.setAdapter(adapter);
    }

}
