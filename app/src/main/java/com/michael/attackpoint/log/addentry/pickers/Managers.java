package com.michael.attackpoint.log.addentry.pickers;

import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Manager;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogIntensity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 4/13/16.
 */
public class Managers {
    protected Manager activity;
    protected Manager date;
    protected Manager description;
    protected Manager distance;
    protected Manager duration;
    protected Manager intensity;

    public Managers(Activity activityContract) {
        activity = new ActivityManager(activityContract, new LogInfoActivity());
        date = new DateManager(activityContract, new LogDate());
        duration = new DurationManager(activityContract, new LogDuration());
        intensity = new IntensityManager(activityContract, new LogIntensity());
        distance = new DistanceManager(activityContract, new LogDistance());
        description = new DescriptionManager(activityContract, new LogDescription());
    }

    public Managers(Activity context, LogInfo logInfo) {
        activity = new ActivityManager(context, (LogInfoActivity) logInfo.get(LogInfo.KEY_ACTIVITY));
        date = new DateManager(context, (LogDate) logInfo.get(LogInfo.KEY_DATE));
        duration = new DurationManager(context, (LogDuration) logInfo.get(LogInfo.KEY_DURATION));
        intensity = new IntensityManager(context, (LogIntensity) logInfo.get(LogInfo.KEY_INTENSITY));
        distance = new DistanceManager(context, (LogDistance) logInfo.get(LogInfo.KEY_DISTANCE));
        description = new DescriptionManager(context, (LogDescription) logInfo.get(LogInfo.KEY_DESCRIPTION));
    }

    private List<Manager> getManagerList() {
        List<Manager> list = new ArrayList<>();
        list.add(activity);
        list.add(date);
        list.add(description);
        list.add(distance);
        list.add(duration);
        list.add(intensity);

        return list;
    }

    public LogInfo updateLogInfo(LogInfo logInfo) {
        for (Manager manager : getManagerList()) {
            logInfo = manager.updateLoginfo(logInfo);
        }

        return logInfo;
    }

    public interface ManagerAction {
        void action(Manager manager);
    }

    public void action(ManagerAction managerAction) {
        for (Manager manager : getManagerList()) {
            managerAction.action(manager);
        }
    }
}
