package com.michael.attackpoint.log.addentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.michael.attackpoint.R;

/**
 * Created by michael on 8/27/15.
 */
public class IntensityPicker extends TrainingPicker {
    private static final String DEBUG_TAG = "attackpoint.NP";
    private NumberPicker np;
    private Dialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);
        builder.setTitle("Select an Intensity");

        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.picker_number, null);

        Activity activity = getActivity();
        view.findViewById(R.id.dialog_buttonA).setOnClickListener(new ClickListener(activity));
        view.findViewById(R.id.dialog_buttonC).setOnClickListener(new ClickListener(activity));

        builder.setView(view);
        np = (NumberPicker) view.findViewById(R.id.np_picker);
        np.setMaxValue(5);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setValue(3);
        mDialog = builder.create();
        return mDialog;
    }


    private class ClickListener implements View.OnClickListener {
        private TextView intensity;

        public ClickListener(Activity activity) {
            intensity = (TextView) activity
                    .findViewById(R.id.training_intensity).findViewById(R.id.item);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // cancel
                case R.id.dialog_buttonC:
                    Log.d(DEBUG_TAG, "cancel pressed");
                    mDialog.dismiss();
                    break;
                // login
                case R.id.dialog_buttonA:
                    Log.d(DEBUG_TAG, "accept pressed");

                    mManager.updateDetail(np.getValue());

                    mDialog.dismiss();
                    break;
            }
        }
    }


}
