package com.michael.attackpoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.michael.attackpoint.log.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.Note;

public class LogDetailActivity extends Activity {
    private static final String DEBUG_TAG = "ap.logDetailActivity";
    public static final String DETAILS = "json_details";
    public static final String NAME = "loginfo_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);
        Singleton.getInstance().setActivity(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra(NAME);
        String json = intent.getStringExtra(DETAILS);

        LogInfo li;
        if (name.equals(Note.NAME)) li = new Note(json);
        else li = new LogInfo(json);

        View view = findViewById(R.id.log_details);
        ViewHolder vh = new ViewHolder(view);
        vh.setFull(li);

        Log.d(DEBUG_TAG, json);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
