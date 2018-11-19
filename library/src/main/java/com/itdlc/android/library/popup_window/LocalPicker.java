package com.itdlc.android.library.popup_window;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.itdlc.android.library.R;
import com.itdlc.android.library.bean.CityBean;
import com.itdlc.android.library.widget.WheelStringAdapter;

import java.util.ArrayList;
import java.util.List;


public class LocalPicker extends LinearLayout {

    private WheelView wv_province;
    private WheelView wv_city;
    private WheelView wv_county;

    private List<CityBean> mListProvince = new ArrayList<>();
    private List<CityBean> mListCurCity = new ArrayList<>();
    private List<CityBean> mListCurCounty = new ArrayList<>();

    private static final String TAG = "LocalPicker";

    private List<CityBean> mListAllCityBean;
    private int miPoiProvince = 27;
    private int miPoiCity = 3;
    private int miPoiCounty;
    private ArrayList<String> lstNameProvince = new ArrayList<>();
    private ArrayList<String> lstNameCity = new ArrayList<>();
    private ArrayList<String> lstNameCounty = new ArrayList<>();

    public LocalPicker(Context context) {
        super(context);
    }

    public LocalPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(List<CityBean> data) {
        setData(data, 0, 0, 0);
    }

    public void setData(List<CityBean> data, int provinceId, int cityId, int countId) {
        // initView
        setBackgroundColor(0xffffffff);
        setOrientation(VERTICAL);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(HORIZONTAL);
        addView(linearLayout);

        mListAllCityBean = data;

        // 添加省集合
        for (int i = 0; i < mListAllCityBean.size(); i++) {
            CityBean cityBean = mListAllCityBean.get(i);
            if (cityBean.getFID() == 0) {
                mListProvince.add(cityBean);
            }

        }


        // 添加省数组
        for (int i = 0; i < mListProvince.size(); i++) {
            CityBean cityBean = mListProvince.get(i);
            lstNameProvince.add(cityBean.getSoftName());
            if (cityBean.getAREA_ID() == provinceId) {
                miPoiProvince = i;
            }
        }


        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
        // 省
        wv_province = new WheelView(getContext());
        wv_province.setAdapter(new WheelStringAdapter(lstNameProvince));
        wv_province.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_province.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_province.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_province.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_province.setCyclic(false);
        wv_province.setLayoutParams(params);
        linearLayout.addView(wv_province);

        // 市
        wv_city = new WheelView(getContext());
        wv_city.setAdapter(new WheelStringAdapter(lstNameCity));
        wv_city.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_city.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_city.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_city.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_city.setCyclic(false);
        wv_city.setLayoutParams(params);
        linearLayout.addView(wv_city);


        // 县
        wv_county = new WheelView(getContext());
        wv_county.setAdapter(new WheelStringAdapter(lstNameCounty));
        wv_county.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        wv_county.setTextColorCenter(getContext().getResources().getColor(R.color.colorAccent));
        wv_county.setTextColorOut(getContext().getResources().getColor(R.color.textSecondary));
        wv_county.setDividerColor(getContext().getResources().getColor(R.color.colorAccent));
        wv_county.setCyclic(false);
        wv_county.setLayoutParams(params);
        linearLayout.addView(wv_county);

        // 添加"省"监听
        wv_province.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                miPoiProvince = position;
                setCityData(0, 0);
            }
        });


        // 添加"市"监听
        wv_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                miPoiCity = position;
                setCountyData(0);
            }
        });

        wv_county.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                miPoiCounty = position;
            }
        });

        // 设置数据
        wv_province.setCurrentItem(miPoiProvince);
        setCityData(cityId, countId);
    }

    // 获取市集合
    private void setCityData(int cityId, int countId) {

        int fid = mListProvince.get(miPoiProvince).getAREA_ID();

        mListCurCity.clear();
        for (int i = 0; i < mListAllCityBean.size(); i++) {
            CityBean cityBean = mListAllCityBean.get(i);
            if (cityBean.getFID() == fid) {
                mListCurCity.add(cityBean);
            }
        }

        // 添加市数组
        if (miPoiProvince == 22) {
            miPoiCity = 4;
        } else {
            miPoiCity = 0;
        }
        lstNameCity.clear();
        for (int i = 0; i < mListCurCity.size(); i++) {
            CityBean cityBean = mListCurCity.get(i);
            lstNameCity.add(mListCurCity.get(i).getSoftName());
            if (cityBean.getAREA_ID() == cityId) {
                miPoiCity = i;
            }
        }
        wv_city.setCurrentItem(miPoiCity);

        setCountyData(countId);
    }

    // 获取县集合
    private void setCountyData(int countId) {

        int area_id = mListCurCity.get(miPoiCity).getAREA_ID();

        mListCurCounty.clear();
        for (int i = 0; i < mListAllCityBean.size(); i++) {
            CityBean cityBean = mListAllCityBean.get(i);
            if (cityBean.getFID() == area_id) {
                mListCurCounty.add(cityBean);
            }
        }
        // 添加省数组
        miPoiCounty = 0;
        lstNameCounty.clear();
        for (int i = 0; i < mListCurCounty.size(); i++) {

            CityBean cityBean = mListCurCounty.get(i);
            lstNameCounty.add(mListCurCounty.get(i).getSoftName());
            if (cityBean.getAREA_ID() == countId) {
                miPoiCounty = i;
            }
        }

        wv_county.setCurrentItem(miPoiCounty);

    }

    public String getLocalName(String join) {
        return mListProvince.get(miPoiProvince).getNAME() + join
                + mListCurCity.get(miPoiCity).getNAME() + join
                + mListCurCounty.get(miPoiCounty).getNAME();

    }

    public String getProvince() {
        return mListProvince.get(miPoiProvince).getNAME();
    }

    public String getCity() {
        return mListCurCity.get(miPoiCity).getNAME();
    }

    public String getCountry() {
        return mListCurCounty.get(miPoiCounty).getNAME();
    }

    public List<CityBean> getLocalBean() {

        ArrayList<CityBean> cityList = new ArrayList<>();
        cityList.add(mListProvince.get(miPoiProvince));
        cityList.add(mListCurCity.get(miPoiCity));
        cityList.add(mListCurCounty.get(miPoiCounty));
        return cityList;
    }

    public void setCurrentItem(int provinceId, int cityId, int countId) {
        // 省
        for (int i = 0; i < mListProvince.size(); i++) {
            if (mListProvince.get(i).getAREA_ID() == provinceId) {
                wv_province.setCurrentItem(i);
                miPoiProvince = i;
                setCityData(cityId, countId);
                break;
            }
        }

    }

    public void setCurrentItem(String provinceId, String cityId, String countId) {
        // 省

        setCurrentItem(Integer.parseInt(provinceId), Integer.parseInt(cityId),Integer.parseInt(countId));
    }

}
