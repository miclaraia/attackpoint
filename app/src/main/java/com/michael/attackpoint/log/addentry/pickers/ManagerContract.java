package com.michael.attackpoint.log.addentry.pickers;

import android.app.FragmentManager;
import android.content.Context;
import android.view.View;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;
import com.michael.attackpoint.log.loglist.LogContract;

/**
 * Created by michael on 4/5/16.
 */
public interface ManagerContract {

    interface Manager {

        void setView(ViewHolder vh);

        void setItem(LogInfoItem item);

        LogInfoItem getItem();

        LogInfo updateLoginfo(LogInfo li);

        void setClickListener();
    }

    interface Activity {

        ViewHolder getViewHolder();

        FragmentManager getFragmentManager();

        Context getContext();
    }

    interface Picker  {
        void setManager(Manager manager);
    }
}
