package com.itdlc.android.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itdlc.android.library.R;
import com.itdlc.android.library.utils.StringUtils;

public class DoubleInputDialog extends Dialog {

    private EditText et_name;
    private EditText et_id;

    public DoubleInputDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_double_confirm);

        et_name = findViewById(R.id.edit_name);
        et_id = findViewById(R.id.edit_id);

        TextView btn_confirm = findViewById(R.id.tv_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = StringUtils.trim(et_name.getText().toString());
                if (TextUtils.isEmpty(name)) {
                    et_name.requestFocus();
                    Toast.makeText(getContext(), "请输入快递名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = StringUtils.trim(et_id.getText().toString());
                if (TextUtils.isEmpty(id)) {
                    et_id.requestFocus();
                    Toast.makeText(getContext(), "请输入快递编号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onConfirm(name, id);
                }
                dismiss();
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public static interface OnConfirmClickListener {
        void onConfirm(String name, String id);
    }

    private OnConfirmClickListener onConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        onConfirmClickListener = listener;
    }
}
