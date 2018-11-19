package com.itdlc.android.library.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by zackzhou on 2018/1/6.
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
