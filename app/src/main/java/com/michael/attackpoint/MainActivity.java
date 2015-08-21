package com.michael.attackpoint;import android.app.Activity;import android.os.Bundle;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.util.Log;import android.view.Menu;import android.view.MenuItem;import android.widget.Toast;import com.michael.network.Login;import com.michael.network.apLog;import com.michael.objects.LogInfo;import java.util.ArrayList;import java.util.List;public class MainActivity extends Activity {    public final static String EXTRA_MESSAGE = "com.michael.Attackpoint.MESSAGE";    private List<LogInfo> logInfoList;    private RecyclerView recList;    private MyAdapter adapter;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        recList = (RecyclerView) findViewById(R.id.cardList);        LinearLayoutManager llm = new LinearLayoutManager(this);        llm.setOrientation(LinearLayoutManager.VERTICAL);        recList.setLayoutManager(llm);        initializeData();        initializeAdapter();        //apNet ap = new apNet(0);        new apLog(adapter).getLog();        new Login().login();    }    // log in to attackpoint    private void login() {        Toast.makeText(this, "login pressed", Toast.LENGTH_LONG);    }    // log out from attackpoint by clearing cookie    private void logout() {        Toast.makeText(this, "logout pressed", Toast.LENGTH_LONG);    }    private void initializeData() {        logInfoList = new ArrayList<>();        /*logInfoList.add(new LogInfo("Test 1<br>Test2<strong>test3</strong>", "Bike", "13.44", "km", "44:56"));        logInfoList.add(new LogInfo("Test 2", "Run", "1.2", "km", "13:33"));        logInfoList.add(new LogInfo("Test 3", "Core","1","b","10:00"));        logInfoList.add(new LogInfo("Test 4", "Core","1","b","10:00"));        logInfoList.add(new LogInfo("Test 5", "Core","1","b","10:00"));        logInfoList.add(new LogInfo("Test 6", "Core","1","b","10:00"));        logInfoList.add(new LogInfo("Test 7", "Core","1","b","10:00"));        logInfoList.add(new LogInfo("Test 8", "Core","1","b","10:00"));*/    }    private void initializeAdapter() {        adapter = new MyAdapter(logInfoList);        adapter.notifyDataSetChanged();        recList.setAdapter(adapter);    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.main, menu);        return true;    }        @Override    public boolean onOptionsItemSelected(MenuItem item) {        // Handle presses on the action bar items        switch (item.getItemId()) {            case R.id.action_search:                Log.d("info","menu action: action_search");                return true;            case R.id.action_settings:                Log.d("info","menu action: action_settings");                return true;            default:                return super.onOptionsItemSelected(item);        }    }}