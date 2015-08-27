package com.michael.attackpoint.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;

/**
 * Created by michael on 8/27/15.
 */
public class NumberPickerDialog extends Activity {
    private static final String DEBUG_TAG = "attackpoint.NP";
    private NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Select Intensity Level");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_picker);
        np = (NumberPicker) findViewById(R.id.np_picker);
        np.setMaxValue(5);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setValue(3);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // cancel
            case R.id.dialog_buttonC:
                Log.d(DEBUG_TAG, "cancel pressed");
                finish();
                break;
            // login
            case R.id.dialog_buttonA:
                Log.d(DEBUG_TAG, "accept pressed");
                TextView intensity = (TextView) findViewById(R.id.training_intensity_text);
                intensity.setText("" + np.getValue());
                finish();
                break;
        }
    }


}
