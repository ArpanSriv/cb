package com.arpan.collegebroker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class BetterToggleButton extends ToggleButton {
    private CompoundButton.OnCheckedChangeListener myListener = null;
    private CheckBox myCheckBox;

    public BetterToggleButton(Context context) {
        super(context);
    }

    public BetterToggleButton(Context context, AttributeSet attributeSet) {
        this(context);
    }

    // assorted constructors here...

    @Override
    public void setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener listener) {
        if (listener != null) {
            this.myListener = listener;
        }
        myCheckBox.setOnCheckedChangeListener(listener);
    }

    public void silentlySetChecked(boolean checked) {
        toggleListener(false);
        myCheckBox.setChecked(checked);
        toggleListener(true);
    }

    private void toggleListener(boolean on) {
        if (on) {
            this.setOnCheckedChangeListener(myListener);
        } else {
            this.setOnCheckedChangeListener(null);
        }
    }
}
