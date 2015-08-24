package com.michael.attackpoint;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        recList = (RecyclerView) getView().findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        new apLog(adapter).getLog();

        return inflater.inflate(R.layout.log_fragment, container, false);
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
        adapter = new MyAdapter(logInfoList);
        adapter.notifyDataSetChanged();
        recList.setAdapter(adapter);
    }

}
