package com.itdlc.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by zackzhou on 2018/1/6.
 */

public class KeyboardUtil {

    private KeyboardUtil() {
    }


    /**
     * 关闭键盘
     *
     * @param context
     * @param v
     */
    public static void closeKeyBoard(Context context, View v) {
        if (v == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 关闭键盘（适用于activity界面）
     *
     * @param context
     */
    public static void closeKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 打开软键盘
     */
    public static void openKeyBoard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 禁止显示软键盘：并且显示光标
     */
    public static void hideSoftInputMode(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT < 11) {// 4.0以下版本
            editText.setInputType(InputType.TYPE_NULL);
        } else {// 4.0以上版本
            // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
