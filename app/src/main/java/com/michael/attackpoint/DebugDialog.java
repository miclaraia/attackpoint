package com.michael.attackpoint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.michael.attackpoint.discussion.Discussion;
import com.michael.attackpoint.discussion.DiscussionRequest;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogIntensity;
import com.michael.attackpoint.training.request.AddTrainingRequest;
import com.michael.attackpoint.training.request.UpdateTrainingRequest;
import com.michael.database.UserTable;
import com.michael.network.FavoriteUsersRequest;
import com.michael.network.MyCookieStore;
import com.michael.network.UserRequest;
import com.michael.objects.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/9/16.
 */
public class DebugDialog extends DialogFragment {
    private static final String DEBUG_TAG = "debug_menu";
    private Singleton mSingleton;
    private MyCookieStore mCookieStore;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSingleton = Singleton.getInstance();
        mCookieStore = mSingleton.getCookieStore();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        OptionsList options = new OptionsList();

        options.add(new Option("Add Training Test") {
            @Override
            void action() {
                LogInfo li = new LogInfo();
                try {
                    li.set(LogInfo.KEY_DISTANCE, new LogDistance(5, "km"));
                    li.set(LogInfo.KEY_INTENSITY, new LogIntensity(3));
                    li.set(LogInfo.KEY_DESCRIPTION, new LogDescription("Android app test"));
                    li.set(LogInfo.KEY_DURATION, new LogDuration(LogDuration.parseLog("5:00")));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                Request request = new AddTrainingRequest(li, new Response.Listener<LogInfo>() {
                    @Override
                    public void onResponse(LogInfo response) {
                        Log.d(DEBUG_TAG, "success");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        String response = new String(volleyError.networkResponse.data);
                        response = response.replaceAll("(\\r|\\n)", "");
                        VolleyLog.e(response);
                    }
                });
                mSingleton.add(request);
            }
        });

        options.add(new Option("Check Cookies") {
            @Override
            void action() {
                Log.d(DEBUG_TAG, mCookieStore.getAllCookies());
            }
        });

        options.add(new Option("Check Users") {
            @Override
            void action() {
                Log.d(DEBUG_TAG, UserTable.printAllUsers());
            }
        });

        options.add(new Option("Clear Users") {
            @Override
            void action() {
                UserTable.clearUsers();
            }
        });

        options.add(new Option("Check Favorites") {
            @Override
            void action() {
                Log.d(DEBUG_TAG, "Checking favorites");
                Request request = new FavoriteUsersRequest(
                        new FavoriteUsersRequest.UpdateCallback() {
                            @Override
                            public void go() {

                            }
                        }, new Response.Listener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        Log.d(DEBUG_TAG, "Got response");
                        for (User user : users) {
                            Log.d(DEBUG_TAG, user.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("Something went wrong!");
                        volleyError.printStackTrace();
                    }
                }
                );
                mSingleton.add(request);
            }
        });

        options.add(new Option("Discussion Test") {
            @Override
            void action() {
                Log.d(DEBUG_TAG, "Testing discussion request");
                Request request = new DiscussionRequest(1132702, new Response.Listener<Discussion>() {
                    @Override
                    public void onResponse(Discussion discussion) {
                        Log.d(DEBUG_TAG, "Got response");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(DEBUG_TAG, "Got error");
                        volleyError.printStackTrace();
                    }
                });
                mSingleton.add(request);
            }
        });

        options.add(new Option("User Test") {
            @Override
            void action() {
                Log.d(DEBUG_TAG, "Testing user request");
                Request request = new UserRequest(11778, new Response.Listener<User>() {
                    @Override
                    public void onResponse(User user) {
                        Log.d(DEBUG_TAG, "Got response");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.d(DEBUG_TAG, "Got Error");
                        e.printStackTrace();
                    }
                });
                mSingleton.add(request);
            }
        });

        options.add(new Option("test update log entry") {
            @Override
            void action() {
                Log.d(DEBUG_TAG, "trying to update log entry");
                LogInfo li = new LogInfo();

                try {
                    li.set(LogInfo.KEY_DISTANCE, new LogDistance(5, "km"));
                    li.set(LogInfo.KEY_INTENSITY, new LogIntensity(3));
                    li.set(LogInfo.KEY_DESCRIPTION, new LogDescription("Android app test"));
                    li.set(LogInfo.KEY_DURATION, new LogDuration(LogDuration.parseLog("10:00")));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                Request request = new UpdateTrainingRequest(li, new Response.Listener<LogInfo>() {
                    @Override
                    public void onResponse(LogInfo response) {
                        Log.d(DEBUG_TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });
            }
        });

        final OptionsList finalOptions = options;

        builder.setTitle("Select Activity Type");
        builder.setItems(options.toArray(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                Option item = finalOptions.get(i);
                item.action();
            }
        });
        return builder.create();
    }

    private static class OptionsList extends ArrayList<Option> {
        public String[] toArray() {
            String[] array = new String[size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = this.get(i).name;
            }
            return array;
        }
    }

    private abstract class Option {
        String name;
        abstract void action();

        private Option(String name) {
            this.name = name;
        }
    }
}
