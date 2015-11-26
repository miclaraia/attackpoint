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

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        View view = inflater.inflate(R.layout.fragment_users, container,false);

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

    private void setUsers(List<User> users) {
        mAdapter.setList(users);
    }

    private void updateUser(int position, User user) {
        mAdapter.updateList(position, user);
    }

    public void getFavorites() {
        List<User> favorites;
        FavoriteUsersRequest request = new FavoriteUsersRequest(
                new Response.Listener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        setUsers(users);
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
}
