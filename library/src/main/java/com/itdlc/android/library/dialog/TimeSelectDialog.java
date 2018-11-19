package com.itdlc.android.library.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itdlc.android.library.R;
import com.itdlc.android.library.widget.FsDatePicker;

import java.util.Calendar;

/**
 * 底部弹出dialog，recycler加载数据
 * Created by jjs on 2018/4/9.
 */

public class TimeSelectDialog extends BottomSheetDialogFragment {
    private FsDatePicker picker;
    private OnCheckListener mCheckListener;
    Calendar calendar = Calendar.getInstance();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置默认白色背景为透明
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_time, container);
        rootView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectDialog.this.dismiss();
            }
        });
        rootView.findViewById(R.id.tv_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckListener != null) {
                    mCheckListener.onCheck(picker.getTimeLong());
                }
                TimeSelectDialog.this.dismiss();
            }
        });
        picker = rootView.findViewById(R.id.fs);

      //  picker.setMinDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.setTime(System.currentTimeMillis());
        picker.setMinDate(2000, 0, 1);

        if (!isDay()){
            picker.setVisibility(View.GONE);
        }
        //picker.setWeelHoldColor(Color.TRANSPARENT);
        return rootView;
    }

    public void setOnCheckListener(OnCheckListener checkListener) {
        mCheckListener = checkListener;
    }

    public interface OnCheckListener {
        void onCheck(long time);
    }

    private boolean isDay = true;
    public boolean  isDay(){
        return isDay;
    }
    public void setIsDay(boolean isDay){
        this.isDay = isDay;
    }
}
