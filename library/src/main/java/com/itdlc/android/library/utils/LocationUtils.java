package com.itdlc.android.library.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.itdlc.android.library.ApplicationContext;
import com.itdlc.android.library.sp.LocSp;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by felear on 2018/1/19.
 */
@SuppressLint("MissingPermission")
public class LocationUtils {

    private static final String TAG = "LocationUtils";
    private volatile static LocationUtils sInstance;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private Location mLocationCache;
    private Context mContext;
    private boolean isRegister;

    public static Location location_default = new Location(LocationManager.NETWORK_PROVIDER);

    private LocationUtils(Context context) {
        mContext = context;
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocationUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LocationUtils.class) {
                if (sInstance == null) {
                    sInstance = new LocationUtils(context);
                }
            }
        }
        return sInstance;
    }


    public void location() {
        //初始化AMapLocationClientOption对象

    }

    public Observable<Location> getLocationSingleAsync(final Activity activity) {
        return new RxPermissions(activity).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .toList().flatMapObservable(new Function<List<Boolean>, ObservableSource<Location>>() {
                    @Override
                    public ObservableSource<Location> apply(List<Boolean> booleans) throws Exception {
                        for (boolean accept : booleans) {
                            if (!accept) {
                                return Observable.just(location_default);
                            }
                        }
                        return Observable.create(new ObservableOnSubscribe<Location>() {
                            @Override
                            public void subscribe(final ObservableEmitter<Location> e) throws Exception {
                                //                                if (!isRegister) {
                                //                                    requestLocation();
                                //                                }
                                refreshLocationProvider();
                                if (mLocationProvider != null) {
                                    mLocationManager.requestSingleUpdate(mLocationProvider, new SimpleLocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            LocSp.getInstance(ApplicationContext.get()).setLat(location.getLatitude() + "");
                                            LocSp.getInstance(ApplicationContext.get()).setLng(location.getLongitude() + "");

                                            setLocation(location);
                                            e.onNext(location);
                                            e.onComplete();
                                            mLocationManager.removeUpdates(this);
                                        }
                                    }, Looper.getMainLooper()); // 单次请求，省电
                                } else {
                                   // e.onError(new Exception());
                                }
                            }
                        });
                    }
                });
    }

    private void refreshLocationProvider() {
        //1.获取位置管理器
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = mLocationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            mLocationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            mLocationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(mContext, "无法获取您当前位置，请打开系统定位", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "没有可用的位置提供器");
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = mLocationManager.getLastKnownLocation(mLocationProvider);
        if (location != null) {
            setLocation(location);
        }
    }

    public void requestLocation() {
        refreshLocationProvider();
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        mLocationManager.requestLocationUpdates(mLocationProvider, 0, 0, mLocationListener);
        //        mLocationManager.requestSingleUpdate(mLocationProvider, mLocationListener, Looper.getMainLooper()); // 单次请求，省电
        isRegister = true;
    }

    private void setLocation(Location location) {
        this.mLocationCache = location;
        String address = "lat：" + location.getLatitude() + ", lng：" + location.getLongitude();
        Log.w(TAG, address);
    }

    //获取经纬度
    public Location getLocation() {
        return mLocationCache;
    }

    // 移除定位监听
    public void removeLocationUpdatesListener() {
        // 需要检查权限,否则编译不过
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mLocationManager != null) {
            sInstance = null;
            mLocationManager.removeUpdates(mLocationListener);
            isRegister = false;
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    private LocationListener mLocationListener = new LocationListener() {

        /**
         * 当某个位置提供者的状态发生改变时
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        /**
         * 某个设备打开时
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 某个设备关闭时
         */
        @Override
        public void onProviderDisabled(String provider) {

        }

        /**
         * 手机位置发生变动
         */
        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();//精确度
            setLocation(location);
            synchronized (mOtherListener) {
                for (LocationListener listener : mOtherListener) {
                    listener.onLocationChanged(location);
                }
            }
        }
    };

    private final List<LocationListener> mOtherListener = new ArrayList<>();

    public void registerLocationListener(LocationListener listener) {
        synchronized (mOtherListener) {
            mOtherListener.add(listener);
        }
    }

    public void unRegisterLocationListener(LocationListener listener) {
        synchronized (mOtherListener) {
            mOtherListener.remove(listener);
        }
    }

    public static abstract class SimpleLocationListener implements LocationListener {


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}

