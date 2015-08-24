package com.michael.attackpoint;import android.app.Activity;import android.app.FragmentManager;import android.app.FragmentTransaction;import android.content.Intent;import android.os.Bundle;import android.support.v4.app.FragmentActivity;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.util.Log;import android.view.Menu;import android.view.MenuItem;import android.view.View;import android.widget.Toast;import com.michael.network.Login;import com.michael.network.apLog;import com.michael.objects.LogInfo;import java.util.ArrayList;import java.util.List;public class MainActivity extends Activity {    public final static String EXTRA_MESSAGE = "com.michael.Attackpoint.MESSAGE";    private Login login;    private Singleton singleton;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        singleton = Singleton.getInstance();        login = singleton.getLogin();    }    // log in to attackpoint    public void login(View view) {        Toast.makeText(this, "login pressed", Toast.LENGTH_LONG).show();        Intent intent = new Intent(this, LoginActivity.class);        startActivity(intent);        //login.login();    }    // log out from attackpoint by clearing cookie    public void logout(View view) {        Toast.makeText(this, "logout pressed", Toast.LENGTH_LONG).show();        login.logout();    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.main, menu);        return true;    }        @Override    public boolean onOptionsItemSelected(MenuItem item) {        // Handle presses on the action bar items        switch (item.getItemId()) {            case R.id.action_search:                Log.d("info","menu action: action_search");                return true;            case R.id.action_settings:                Log.d("info","menu action: action_settings");                return true;            default:                return super.onOptionsItemSelected(item);        }    }}