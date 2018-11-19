package com.itdlc.android.library.widget.xlistview.contract;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.itdlc.android.library.R;
import com.itdlc.android.library.adapter.CommonAdapter;
import com.itdlc.android.library.adapter.ViewHolder;
import com.itdlc.android.library.base.BasePresenter;
import com.itdlc.android.library.helper.CommonObserver;
import com.itdlc.android.library.utils.StringUtils;
import com.itdlc.android.library.widget.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by felear on 2018/4/1.
 */

public class XlvPresenter extends BasePresenter<XlvContract.View> implements XlvContract.Presenter {

    private XListView mXListView;
    protected int miPage = 1;
    protected int miPageSize = 10;
    protected int miRealPageSize;
    protected ArrayList<Object> mLstData = new ArrayList<>();
    private CommonAdapter<Object> mAdapter;
    private int miCount = 1;
    private int miSpace;
    private LinearLayout.LayoutParams mItemParams;
    private OnBeforeLoadDataListener mOnBeforeLoadDataListener;
    private OnAfterLoadDataListener mOnAfterLoadDataListener;

    public XlvPresenter(XlvContract.View view) {
        super(view);
    }

    /**
     * @param view
     * @param count 横向数量
     * @param space 间距 单位px
     */
    public XlvPresenter(XlvContract.View view, int count, int space) {
        super(view);
        miCount = count;
        miSpace = space;
    }

    @Override
    protected void start() {
        mXListView = mView.getXListView();
        mXListView.setPullLoadEnable(false);
        mXListView.setPullRefreshEnable(true);
        mXListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                miPage = 1;
                mView.reLoadData();
            }

            @Override
            public void onLoadMore() {
                miPage++;
                mView.reLoadData();
            }
        });

        if (miCount == 1) {
            mAdapter = new CommonAdapter<Object>(mXListView.getContext(), mLstData, mView.getItemRes()) {
                @Override
                public void convert(ViewHolder helper, int position, Object item) {
                    mView.loadItemData(helper, position, item);
                }
            };
        } else if (miCount > 1) {
            mItemParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            mItemParams.leftMargin = miSpace;
            mAdapter = new CommonAdapter<Object>(mXListView.getContext(), mLstData, R.layout.item_mul_xlistview) {
                @Override
                public void convert(ViewHolder helper, int position, Object object) {

                    ArrayList<Object> item = (ArrayList<Object>) object;
                    LinearLayout layout = (LinearLayout) helper.getConvertView();
                    // 设置布局
                    if (layout.getChildCount() == 0) {
                        layout.setPadding(0, miSpace, miSpace, 0);
                    }
                    for (int i = 0; i < miCount; i++) {
                        View convertView = null;
                        ViewHolder viewHolder = null;
                        if (layout.getChildCount() > i) {
                            convertView = layout.getChildAt(i);
                            viewHolder = ViewHolder.get(mXListView.getContext(), convertView, mXListView, mView.getItemRes(), position * miCount + i);
                        } else {
                            viewHolder = ViewHolder.get(mXListView.getContext(), null, mXListView, mView.getItemRes(), position * miCount + i);
                            viewHolder.getConvertView().setLayoutParams(mItemParams);
                            convertView = viewHolder.getConvertView();
                            layout.addView(convertView);
                        }

                        if (item.size() > i) {
                            convertView.setVisibility(View.VISIBLE);
                            mView.loadItemData(viewHolder, position, item.get(i));
                        } else {
                            convertView.setVisibility(View.INVISIBLE);
                        }

                    }
                }
            };
        }
        mXListView.setAdapter(mAdapter);
    }

    @Override
    public <T extends BaseXlvResp> void loadData(Observable<T> observable) {
        if (observable == null) {
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<T>(mView, true) {
                    @Override
                    protected void onAccept(T t) {
                        List<?> data = t.getData();

                        if (mOnBeforeLoadDataListener != null) {
                            mOnBeforeLoadDataListener.onResult(t);
                        }
                        if (miPage == 1) {
                            mLstData.clear();
                            mXListView.setPullLoadEnable(true);
                            miRealPageSize = data.size();
                        }

                        // 移除多余数据
                        int size = miRealPageSize * (miPage - 1);
                        try {
                            while (mLstData.size() * miCount > size) {

                                Object remove = mLstData.get(mLstData.size() - 1);
                                if (miCount > 1) {
                                    List<Object> ts = (List<Object>) remove;
                                    if ((mLstData.size() - 1) * miCount + ts.size() <= size) {
                                        break;
                                    }
                                    ts.remove(ts.size() - 1);
                                    if (ts.size() > 0) {
                                        continue;
                                    }
                                }
                                mLstData.remove(remove);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (data != null && data.size() > 0) {
                            if (miCount == 1) {
                                mLstData.addAll(data);
                            } else {

                                // 补全数据
                                if (mLstData.size() > 0) {
                                    List<Object> ts = (ArrayList<Object>) mLstData.get(mLstData.size() - 1);
                                    if (ts.size() < miCount) {
                                        for (int i = ts.size(); i < miCount; i++) {
                                            ts.add(data.remove(0));
                                            if (data.size() == 0) {
                                                break;
                                            }
                                        }
                                    }
                                }

                                // 添加数据
                                for (int i = 0; i <= (data.size() - 1) / miCount; i++) {
                                    ArrayList<Object> list = new ArrayList<>();
                                    for (int j = 0; j < miCount; j++) {
                                        if (i * miCount + j < data.size()) {
                                            list.add(data.get(i * miCount + j));
                                        }
                                    }
                                    if (list.size() > 0) {
                                        mLstData.add(list);
                                    }
                                }
                            }
                        }
                        if (mOnAfterLoadDataListener!=null){
                            mOnAfterLoadDataListener.onResult();
                        }

                        if (data == null || data.size() < miPageSize) {
                            mXListView.setPullLoadEnable(false);
                        }
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onComplete() {
                        mXListView.stopRefresh();
                        mXListView.stopLoadMore();
                        mXListView.setRefreshTime(StringUtils.getCurrentRefreshTime());
                    }
                });
    }

    @Override
    public int getPage() {
        return miPage;
    }

    @Override
    public int getPageSize() {
        return miPageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        miPageSize = pageSize;
    }

    @Override
    public void setPage(int page) {
        this.miPage = page;
    }

    /**
     * 所有数据处理完成后调用
     */
    @Override
    public void setOnBeforeLoadDataListener(OnBeforeLoadDataListener onHttpSuccessListener) {
        this.mOnBeforeLoadDataListener = onHttpSuccessListener;
    }

    @Override
    public void setOnAfterLoadDataListener(OnAfterLoadDataListener afterLoadDataListener) {
        this.mOnAfterLoadDataListener = afterLoadDataListener;
    }

    @Override
    public CommonAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public List<?> getData() {
        return mLstData;
    }

    @Override
    public void bind() {

    }

    /**
     * 数据处理前调用
     */
    public interface OnBeforeLoadDataListener {
        void onResult(BaseXlvResp resp);
    }

    public interface OnAfterLoadDataListener {
        void onResult();
    }

}
