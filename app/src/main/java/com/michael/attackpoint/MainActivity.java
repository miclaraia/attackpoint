package com.michael.attackpoint;import android.app.Activity;import android.content.Intent;import android.os.Bundle;import android.support.v4.widget.DrawerLayout;import android.util.Log;import android.view.Gravity;import android.view.Menu;import android.view.MenuItem;import android.view.View;import android.widget.AdapterView;import com.michael.attackpoint.adapters.DrawerAdapter;import android.widget.BaseAdapter;import android.widget.ListView;import android.widget.Toast;import com.michael.attackpoint.dialogs.LoginActivity;import com.michael.network.Login;import com.michael.objects.NavDrawer;import com.michael.objects.NavDrawerItem;import java.util.ArrayList;public class MainActivity extends Activity {    public final static String EXTRA_MESSAGE = "com.michael.Attackpoint.MESSAGE";    private static final String DEBUG_TAG = "attackpoint.Main";    private Login login;    private Singleton singleton;    private DrawerLayout drawerLayout;    private ListView drawerList;    private DrawerAdapter drawerAdapter;    private NavDrawer drawer;    private MainActivity self = this;    private ArrayList<NavDrawerItem> navMenuItems;    private Activity activity = this;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);        drawerList = (ListView) findViewById(R.id.left_drawer);        String[] navMenuNames = getResources().getStringArray(R.array.nav_drawer_names);        int[] navMenuIcons = getResources().getIntArray(R.array.nav_drawer_icons);        String[] navMenuGroups = getResources().getStringArray(R.array.nav_drawer_groups);        String[] navMenuActions = getResources().getStringArray(R.array.nav_drawer_action);        navMenuItems = new ArrayList<>();        navMenuItems.add(new NavDrawerItem(navMenuGroups[0], NavDrawerItem.TYPE_SEPERATOR));        for (int i = 0; i < navMenuNames.length; i++) {            NavDrawerItem item = new NavDrawerItem(navMenuNames[i],                    navMenuGroups[i], navMenuIcons[i], navMenuActions[i]);            if (i > 0 && !navMenuGroups[i].equals(navMenuGroups[i-1])) {                navMenuItems.add(new NavDrawerItem(navMenuGroups[i], NavDrawerItem.TYPE_SEPERATOR));            }            navMenuItems.add(item);        }        drawer = new NavDrawer(activity, drawerLayout, navMenuItems, drawerList);        singleton.setDrawer(drawer);        singleton = Singleton.getInstance();        login = singleton.getLogin();    }    // log in to attackpoint    public void login() {        Intent intent = new Intent(this, LoginActivity.class);        //startActivity(intent);        //login.login();    }    // log out from attackpoint by clearing cookie    public void logout() {        login.logout();    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.main, menu);        getActionBar().setElevation(0);        return true;    }        @Override    public boolean onOptionsItemSelected(MenuItem item) {        // Handle presses on the action bar items        switch (item.getItemId()) {            case R.id.action_search:                Log.d("info","menu action: action_search");                return true;            case R.id.action_settings:                Log.d("info","menu action: action_settings");                return true;            default:                return super.onOptionsItemSelected(item);        }    }}