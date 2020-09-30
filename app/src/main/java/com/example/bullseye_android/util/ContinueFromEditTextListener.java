package com.example.bullseye_android.util;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

public class ContinueFromEditTextListener implements TextView.OnEditorActionListener {

    private Button btn;

    public ContinueFromEditTextListener(Button btn) {
        this.btn = btn;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if ((keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) || i == EditorInfo.IME_ACTION_DONE) {
            btn.performClick();
            return true;
        }
        return false;
    }
}
