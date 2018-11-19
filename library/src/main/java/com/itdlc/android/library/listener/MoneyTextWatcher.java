package com.itdlc.android.library.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by ni on 2018/1/25.
 */

public class MoneyTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int dotIndex = s.toString().indexOf(".");
        // 小数点后保留两位
        if (s.toString().contains(".")) {
            if (s.length() - 1 - dotIndex > 2) {
                s.delete(s.toString().indexOf(".") + 3, s.length());
            }
        }

        if (dotIndex == 0) {
            s.clear();
        }

        if (s.toString().startsWith("0") && dotIndex != 1 && s.length() > 1) {
            s.delete(0, 1);
        }

    }
}
