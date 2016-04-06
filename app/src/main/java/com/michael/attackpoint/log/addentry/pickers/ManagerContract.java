package com.michael.attackpoint.log.addentry.pickers;

import android.app.FragmentManager;
import android.view.View.OnClickListener;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

/**
 * Created by michael on 4/5/16.
 */
public interface ManagerContract {

    interface Manager<T extends LogInfoItem> {
        void onClick();

        void setView(ViewHolder vh);
    }

    interface View {

        void setText(ViewHolder.SubViewHolder svh, String text);

        void setClickListener(ViewHolder.SubViewHolder svh, OnClickListener listener);

        FragmentManager getFragmentManager();
    }
}
