package com.michael.attackpoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.michael.attackpoint.adapters.LogAdapter;
import com.michael.attackpoint.loginfo.LogInfo;

public class LogDetailActivity extends Activity {
    private static final String DEBUG_TAG = "ap.logDetailActivity";
    public static final String DETAILS = "json_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);
        Singleton.getInstance().setActivity(this);

        Intent intent = getIntent();
        String data = intent.getStringExtra(DETAILS);
        setDetails(data);

        Log.d(DEBUG_TAG, data);
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

    private void setDetails(String json) {
        View view = findViewById(R.id.log_details);
        LogAdapter.LogViewHolder vh = new LogAdapter.LogViewHolder(view);
        LogInfo loginfo = new LogInfo(json);
        LogInfo.Strings data = loginfo.strings();

        vh.vTitle.setText(data.type);
        vh.vColor.setBackgroundColor(data.color);

        //Sets snippet text, removes view if no text
        if (data.text == null || data.text.length() <= 0) vh.vText.setVisibility(View.GONE);
        else vh.vText.setText(data.text);

        //sets date
        vh.vDate.setText(data.date);

        //Sets log entry's meta data
        vh.vDist.setText(data.distance);
        vh.vPace.setText(data.pace);
        vh.vTime.setText(data.time);
    }
}
