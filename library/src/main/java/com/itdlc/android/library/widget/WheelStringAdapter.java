package com.itdlc.android.library.widget;

import com.contrarywind.adapter.WheelAdapter;

import java.util.List;

/**
 * Created by felear on 2018/8/16.
 */

public class WheelStringAdapter<T> implements WheelAdapter {

    private List<T> lstData;

    public WheelStringAdapter(List<T> lstData) {
        this.lstData = lstData;
    }

    @Override
    public int getItemsCount() {
        if (lstData != null) {
            return lstData.size();
        }
        return 0;
    }

    @Override
    public T getItem(int index) {

        if (lstData != null && lstData.size() > index) {
            return lstData.get(index);
        }

        return null;
    }

    @Override
    public int indexOf(Object o) {
        if (lstData != null) {
            return lstData.indexOf(o);
        }
        return 0;
    }
}
