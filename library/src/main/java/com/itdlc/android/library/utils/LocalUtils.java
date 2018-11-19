package com.itdlc.android.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itdlc.android.library.bean.CityBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felear on 2017/8/25 0025.
 */

public class LocalUtils {

    private final static List<CityBean> mListAllCityBean = new ArrayList<>();
    private final static List<CityBean> mListProvince = new ArrayList<>();
    private final static List<CityBean> mListCurCity = new ArrayList<>();
    private final static List<CityBean> mListCurCounty = new ArrayList<>();
    private final static List<CityBean> mListHotCity = new ArrayList<>();
    private final static String[] defaultHot = {"北京", "深圳", "广州", "上海", "成都", "惠州", "长沙", "温州"};
    private static CityBean mCurCity = null;
    private static Context mContext;
    private static final String TAG = "LocalUtils";

    public static void setLocalList(Context context, final String data) {

        mContext = context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LocalUtils.class) {
                    // 读取本地数据
                    String json = null;
                    try {
                        InputStream open = mContext.getAssets().open(data);
                        ByteArrayOutputStream mBos = new ByteArrayOutputStream();
                        int len;
                        byte buffer[] = new byte[1024];
                        while ((len = open.read(buffer)) != -1) {
                            mBos.write(buffer, 0, len);
                        }
                        json = mBos.toString();
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage() + "");
                    }

                    List<CityBean> list = new Gson().fromJson(json, new TypeToken<List<CityBean>>() {
                    }.getType());

                    if (list != null) {
                        mListAllCityBean.addAll(list);
                    }

                    for (int i = 0; i < mListAllCityBean.size(); i++) {
                        CityBean cityBean = mListAllCityBean.get(i);
                        if (cityBean.getNAME().length() > 7) {
                            cityBean.setSoftName(cityBean.getNAME().substring(0, 7) + "..");
                        } else {
                            cityBean.setSoftName(cityBean.getNAME());
                        }

                        // 添加省集合
                        if (cityBean.getFID() == 0) { // FID = 0 的为省
                            mListProvince.add(cityBean);
                        }
                    }

                    // FID 与省集合的 AREA_ID 匹配，即归属于市
                    for (int i = 0; i < mListAllCityBean.size(); i++) {
                        CityBean unknown = mListAllCityBean.get(i);
                        if (unknown.getFID() == 0) {
                            continue;
                        }
                        for (int j = 0; j < mListProvince.size(); j++) {
                            CityBean province = mListProvince.get(j);
                            if (unknown.getFID() == province.getAREA_ID()) {
                                mListCurCity.add(unknown);
                            }
                        }
                    }

                    // FID 与市集合中的AREA_ID 匹配，即归属于区
                    for (int i = 0; i < mListAllCityBean.size(); i++) {
                        CityBean unknown = mListAllCityBean.get(i);
                        if (unknown.getFID() == 0) {
                            continue;
                        }
                        for (int j = 0; j < mListCurCity.size(); j++) {
                            CityBean city = mListCurCity.get(j);
                            if (unknown.getFID() == city.getAREA_ID()) {
                                mListCurCounty.add(city);
                            }
                        }
                    }
                }
            }
        }).start();

    }

    public static synchronized List<CityBean> getLocalList() {
        Log.d(TAG, mListAllCityBean.size() + "");
        return mListAllCityBean;
    }

    // 获取城市列表
    public static synchronized List<CityBean> getCityList() {
        return mListCurCity;
    }

    // 获取区列表
    public static synchronized List<CityBean> getCountyList() {
        return mListCurCounty;
    }

    // 获取省列表
    public static synchronized List<CityBean> getProvinceList() {
        return mListProvince;
    }

    // 根据经纬度获取当前城市
    public static synchronized CityBean findCity(double lat, double lng) {
        CityBean mCurCity = null;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < mListCurCity.size(); i++) {
            CityBean cityBean = mListCurCity.get(i);
            double tmp = MapDistance.getDistance(cityBean.getLATITUDE(), cityBean.getLONGITUDE(), lat, lng);
            if (tmp < distance) {
                mCurCity = cityBean;
                distance = tmp;
            }
        }
        mCurCity.setLATITUDE(lat);
        mCurCity.setLONGITUDE(lng);
        return mCurCity;
    }

    public static String getLocalName(String provinceId, String cityId, String countyId, String join) {
        if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(countyId)) {
            return "";
        }
        return getLocalName(Integer.parseInt(provinceId), Integer.parseInt(cityId), Integer.parseInt(countyId), join);
    }

    public static String getLocalName(int provinceId, int cityId, int countyId, String join) {
        String province = null;
        String city = null;
        String county = null;
        Log.d(TAG, "getLocalName: " + mListAllCityBean.size());
        for (int i = 0, j = 0; i < mListAllCityBean.size() && j < 3; i++) {
            CityBean cityBean = mListAllCityBean.get(i);
            if (cityBean.getAREA_ID() == provinceId) {
                province = cityBean.getNAME();
                j++;
            }
            if (cityBean.getAREA_ID() == cityId) {
                city = cityBean.getNAME();
                j++;
            }
            if (cityBean.getAREA_ID() == countyId) {
                county = cityBean.getNAME();
                j++;
            }
        }

        return province + join + city + join + county;
    }

    public static List<CityBean> getCityBean(String... ids) {

        int[] ints = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            ints[i] = Integer.parseInt(ids[i]);
        }
        return getCityBean(ints);
    }

    public static List<CityBean> getCityBean(int... ids) {
        ArrayList<CityBean> cityBeans = new ArrayList<>();
        for (int id : ids) {
            for (int i = 0; i < mListAllCityBean.size(); i++) {
                CityBean cityBean = mListAllCityBean.get(i);
                if (id == cityBean.getAREA_ID()) {
                    cityBeans.add(cityBean);
                    break;
                }
            }
        }
        return cityBeans;
    }

    public static List<CityBean> getHotCity(String[] hotStrs) {
        if (mListHotCity.size() == 0 || hotStrs != null) {
            if (hotStrs == null) {
                hotStrs = defaultHot;
            }
            for (String hotStr : hotStrs) {
                for (int i = 0; i < getCityList().size(); i++) {
                    if (getCityList().get(i).getNAME().contains(hotStr)) {
                        mListHotCity.add(getCityList().get(i));
                        break;
                    }
                }
            }
        }
        return mListHotCity;
    }

    public static void setCurCity(CityBean city) {
        mCurCity = city;
    }

    public static CityBean getCurCity() {
        return mCurCity;
    }

}
