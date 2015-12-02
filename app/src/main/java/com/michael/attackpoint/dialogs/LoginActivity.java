package com.michael.attackpoint.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.network.LoginRequest;

public class LoginActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "attackpoint.LoginA";
    public static final String USERNAME = "username";
    private EditText username;
    private EditText password;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        singleton = Singleton.getInstance();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // cancel
            case R.id.login_buttonC:
                Log.d(DEBUG_TAG, "cancel pressed");
                finish();
                break;
            // login
            case R.id.login_buttonL:
                Log.d(DEBUG_TAG, "login pressed");
                String u = username.getText().toString();
                String p = password.getText().toString();
                //singleton.getLogin().login(u, p);
                LoginRequest request = new LoginRequest(u, p,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String username) {
                                singleton.getUserGroup().addUser(username);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                        error.printStackTrace();
                    }
                });
                singleton.add(request);
                finish();
                break;
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

    public void resultOK(String user) {
        Intent intent = getIntent();
        intent.putExtra(USERNAME, user);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            getParent().setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public void resultCancel() {
        if (getParent() == null) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            getParent().setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
}
