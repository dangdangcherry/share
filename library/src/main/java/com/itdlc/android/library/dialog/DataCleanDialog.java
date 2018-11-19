package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.DataCleanManager;

/**
 * 缓存清除对话框
 */

public class DataCleanDialog extends Dialog {

    public DataCleanDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_data_clean);

        TextView tvCacheSize = findViewById(R.id.tv_cache_size);
        Button btnClear = findViewById(R.id.btn_clear);

        tvCacheSize.setText(DataCleanManager.getTotalCacheSize(getContext()));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClearFinish();
            }
        });
    }

    @UiThread
    @CallSuper
    public void onClearFinish() {
        dismiss();
        DataCleanManager.clearAllCache(getContext());
        new MessageDialog(getContext(), R.string.clear_finish).show();
    }
}
