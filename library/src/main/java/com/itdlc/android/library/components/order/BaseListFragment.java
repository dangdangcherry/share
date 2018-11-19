package com.itdlc.android.library.components.order;

import android.os.Bundle;
import android.view.View;

import com.itdlc.android.library.R;
import com.itdlc.android.library.base.BaseMvpFragment;
import com.itdlc.android.library.widget.xlistview.XListView;
import com.itdlc.android.library.widget.xlistview.contract.XlvContract;
import com.itdlc.android.library.widget.xlistview.contract.XlvPresenter;


/**
 * Created by jjs on 2018/5/11.
 */

public abstract class BaseListFragment extends BaseMvpFragment<XlvContract.Presenter> implements XlvContract.View {
    protected XListView mXListView;

    protected String mStatus;

    public BaseListFragment newInstance(String status) {
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        this.setArguments(bundle);
        return this;
    }

    @Override
    protected XlvContract.Presenter createPresenter() {
        return new XlvPresenter(this);
    }

    @Override
    protected void initialView(View view) {
        mXListView = view.findViewById(R.id.x_list_view);
        super.initialView(view);
        mStatus = getArguments().getString("status");

    }

    @Override
    public void freshData() {
        reLoadData();
    }

    @Override
    public XListView getXListView() {
        return mXListView;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_x_list_view;
    }
}
