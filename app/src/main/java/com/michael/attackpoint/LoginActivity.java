package com.michael.attackpoint;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LoginActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "attackpoint.LoginA";
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        singleton = Singleton.getInstance();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // cancel
            case R.id.login_buttonC:
                Log.d(DEBUG_TAG, "Login dialog canceled");
                finish();
            // login
            case R.id.login_buttonL:
                Log.d(DEBUG_TAG, "logging into attackpoint");
                singleton.getLogin().login();
                finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
