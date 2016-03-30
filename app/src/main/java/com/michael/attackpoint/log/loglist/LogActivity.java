package com.michael.attackpoint.log.loglist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.michael.attackpoint.R;
import com.michael.attackpoint.util.Singleton;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Singleton.getInstance().setActivity(this);

        if (savedInstanceState == null) {
            initFragment(LogFragment.newInstance(getIntent().getExtras()));
        }
    }

    public void initFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
