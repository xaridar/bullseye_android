// Elliot wrote
package com.example.bullseye_android.util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class ShowPassListener implements View.OnClickListener {

    private final EditText text;
    private final CheckBox showButton;

    public ShowPassListener(EditText editText, CheckBox toggle) {
        text = editText;
        showButton = toggle;
    }

    @Override
    public void onClick(View view) {
        int selection = text.getSelectionEnd();
        boolean checked;
        checked = showButton.isChecked();
        if(!checked){
            text.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            text.setTransformationMethod(null);
        }
        text.setSelection(selection);
    }
}
