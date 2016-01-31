package com.michael.attackpoint;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.adapters.UsersAdapter;
import com.michael.network.FavoriteUsersRequest;
import com.michael.network.UserRequest;
import com.michael.objects.User;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;
    private Singleton singleton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        singleton = Singleton.getInstance();
        //TODO should be handled differently
        singleton.setFragment(this);

        View view = inflater.inflate(R.layout.fragment_users, container,false);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        mRecyclerView.setLayoutManager(linearLayout);

        mAdapter = new UsersAdapter(this, new ArrayList<User>());
        mAdapter.notifyDataSetChanged();
        getFavorites();

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setUsers(ArrayList<User> users) {
        mAdapter.setList(users);
        //updateUsers(users);
    }

    private void updateUser(int position, User user) {
        mAdapter.updateList(position, user);
    }

    private void updateUser(User user) {
        mAdapter.updateList(user);
    }

    public void getFavorites() {
        List<User> favorites;
        FavoriteUsersRequest request = new FavoriteUsersRequest(new FavoriteUsersRequest.UpdateCallback() {
            @Override
            public void go() {
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.Listener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        setUsers((ArrayList) users);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                    }
                }
        );
        singleton.add(request);
    }

    public void updateUsers(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            UserRequest request = new UserRequest(user.getId(),
                    new Response.Listener<User>() {
                        @Override
                        public void onResponse(User user) {
                            updateUser(user);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            e.printStackTrace();
                        }
            });
            singleton.add(request);
        }
    }
}
