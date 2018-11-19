package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.StringUtils;
import com.itdlc.android.library.utils.SystemUtil;

import java.util.Locale;

public class SingleInputDialog extends Dialog implements View.OnClickListener {
    private String hint;
    private int maxLen;
    private int inputType = 1;
    private EditText editInput;
    private TextView tvTips;

    public SingleInputDialog(@NonNull Context context, String hint, int maxLen, int inputType) {
        super(context, R.style.Dialog_Base);
        this.hint = hint;
        this.maxLen = maxLen;
        this.inputType = inputType;
    }

    public SingleInputDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Base);
        this.hint = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_input);

        editInput = findViewById(R.id.et_input);
        if (maxLen > 0) {
            editInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        }
        editInput.setInputType(inputType);
        editInput.setHintTextColor(Color.GRAY);
        editInput.setHint(hint);
        editInput.setTextColor(Color.DKGRAY);
        TextView mTvConfirm = findViewById(R.id.tv_confirm);
        mTvConfirm.setOnClickListener(this);
        TextView mTvCancel = findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);
        String locale = SystemUtil.getSharedString("locale");
        String canl = "取消";
        if (Locale.ENGLISH.toString().equals(locale)) {
            canl = "Cancel";
        }
        mTvCancel.setText(canl);
        String chec = "确定";
        if (Locale.ENGLISH.toString().equals(locale)) {
            chec = "Confirm";
        } else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale)) {
            chec = "確定";
        }
        mTvConfirm.setText(chec);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            String input = StringUtils.trim(editInput.getText().toString());
            if (TextUtils.isEmpty(input)) {
                editInput.requestFocus();
                Toast.makeText(getContext(), R.string.verify_input_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (onConfirmClickListener != null) {
                onConfirmClickListener.onConfirm(input);
            }
        }
        dismiss();
    }

    public void show(String tips) {
        super.show();
        if (tvTips == null) {
            tvTips = findViewById(R.id.tv_tips);
        }
        tvTips.setText(tips);
        editInput.setText("");
    }

    public static interface OnConfirmClickListener {
        void onConfirm(String input);
    }

    private OnConfirmClickListener onConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        onConfirmClickListener = listener;
    }
}
