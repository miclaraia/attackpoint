package com.michael.attackpoint.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.michael.attackpoint.LogFragment;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.database.CookieTable;
import com.michael.objects.User;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by michael on 11/26/15.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private static final String DEBUG_TAG = "ap.UserAdapater";

    private List<User> mUserList;
    private Fragment mFragment;

    public UsersAdapter(Fragment fragment, List<User> userList) {
        mUserList = userList;
        mFragment = fragment;
    }

    public void updateList(int position, User user) {
        mUserList.set(position, user);
        this.notifyDataSetChanged();
    }

    public void updateList(User user) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (user.getId() == mUserList.get(i).getId()) {
                updateList(i, user);
                break;
            }
        }
    }

    public void setList(List<User> update) {
        mUserList = update;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        final User user = mUserList.get(i);
        User.Strings strings = user.strings();

        vh.vUser.setText(strings.username);
        vh.vName.setText(strings.name);
        vh.vLocation.setText(strings.location);
        vh.vYear.setText(strings.year);


        // TODO proper animation for click
        //Attach click listener to each card and define click behavior
        vh.vCard.setTag(i);
        vh.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewOverlay mask = v.getOverlay();
                int h = v.getHeight();
                int w = v.getWidth();
                ColorDrawable overlay = new ColorDrawable(Color.parseColor("#AAFFFFFF"));
                overlay.setBounds(0, h, w, 0);
                mask.add(overlay);

                Log.d(DEBUG_TAG, "opening log of user " + user.getName());
                Fragment fragment = new LogFragment();
                Bundle extras = new Bundle();
                extras.putInt(LogFragment.USER_ID, user.getId());
                fragment.setArguments(extras);

                Activity activity = Singleton.getInstance().getActivity();
                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("asdf");

                // Commit the transaction
                transaction.commit();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.user_card, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vUser;
        public TextView vName;
        public TextView vLocation;
        public TextView vYear;

        public FrameLayout vCard;

        public ViewHolder(View v) {
            super(v);
            vUser = (TextView) v.findViewById(R.id.usercard_username);
            vName = getTextView(v, R.id.usercard_realname);
            vLocation = getTextView(v, R.id.usercard_location);
            vYear = getTextView(v, R.id.usercard_year);

            vCard = (FrameLayout) v.findViewById(R.id.usercard_container);
        }

        public TextView getTextView (View v, int id) {
            View layout = v.findViewById(id);
            TextView t = (TextView) layout.findViewById(R.id.usercard_detail);
            return t;
        }
    }
}
