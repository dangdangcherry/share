package com.itdlc.android.library.utils;

import com.google.gson.annotations.SerializedName;
import com.itdlc.android.library.widget.xlistview.contract.BaseXlvResp;

import java.util.List;

/**
 * Created by felear on 2018/5/3.
 */

public class ApkListBean extends BaseXlvResp {
    /**
     * list : [{"apk_id":1,"apk_title":"彩神手机端","version":"1","version_title":"1.0","logo":"http://apkmanager.cc/public/upload/apklogos/logo1.png","package":"com.itdlc.android.taxlottery","apk_route":"http://apkmanager.cc/public/upload/apks/彩神手机端.apk","category_id":2,"category_title":"商城"},{"apk_id":2,"apk_title":"秒链","version":"1","version_title":"1.0","logo":"http://apkmanager.cc/public/upload/apklogos/logo2.png","package":"com.itdlc.android.eye3d","apk_route":"http://apkmanager.cc/public/upload/apks/秒链.apk","category_id":2,"category_title":"商城"},{"apk_id":3,"apk_title":"秒链商家端","version":"1","version_title":"1.0","logo":"http://apkmanager.cc/public/upload/apklogos/logo3.png","package":"com.itdlc.android.eye3d.vip","apk_route":"http://apkmanager.cc/public/upload/apks/秒链商家端.apk","category_id":2,"category_title":"商城"},{"apk_id":5,"apk_title":"手机宝","version":"1","version_title":"1.0","logo":"http://apkmanager.cc/public/upload/apklogos/logo5.png","package":"com.itdlc.android.phoneparts","apk_route":"http://apkmanager.cc/public/upload/apks/手机宝.apk","category_id":2,"category_title":"商城"}]
     * list_count : 4
     */

    public DataEntity data;

    @Override
    public List<?> getData() {
        return data.list;
    }


    public static class DataEntity {
        public int list_count;
        /**
         * apk_id : 1
         * apk_title : 彩神手机端
         * version : 1
         * version_title : 1.0
         * logo : http://apkmanager.cc/public/upload/apklogos/logo1.png
         * package : com.itdlc.android.taxlottery
         * apk_route : http://apkmanager.cc/public/upload/apks/彩神手机端.apk
         * category_id : 2
         * category_title : 商城
         */

        public List<ListEntity> list;

        public static class ListEntity {
            public int apk_id;
            public String apk_title;
            public int version;
            public String version_title;
            public String logo;
            @SerializedName("package")
            public String packageX;
            public String apk_route;
            public int category_id;
            public String category_title;
            public String path;
            public boolean isInstall;
            public boolean isExist;
            public boolean update;
        }
    }
}
