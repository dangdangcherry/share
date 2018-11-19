package com.itdlc.android.library.widget.xlistview.contract;

import com.itdlc.android.library.adapter.CommonAdapter;
import com.itdlc.android.library.adapter.ViewHolder;
import com.itdlc.android.library.base.IContract;
import com.itdlc.android.library.widget.xlistview.XListView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by felear on 2018/4/1.
 */

public class XlvContract {
    public interface View extends IContract.IView {
        int getItemRes();

        void reLoadData();

        void loadItemData(ViewHolder helper, int position, Object item);

        XListView getXListView();
    }

    public interface Presenter extends IContract.IPresenter {

        <T extends BaseXlvResp> void loadData(Observable<T> observable);

        int getPage();

        int getPageSize();

        void setPageSize(int pageSize);

        void setPage(int page);

        void setOnBeforeLoadDataListener(XlvPresenter.OnBeforeLoadDataListener onHttpSuccessListener);

        void setOnAfterLoadDataListener(XlvPresenter.OnAfterLoadDataListener afterLoadDataListener);

        CommonAdapter getAdapter();

        List<?> getData();
    }
}
