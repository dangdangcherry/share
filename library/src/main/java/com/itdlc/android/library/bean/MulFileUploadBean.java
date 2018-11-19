package com.itdlc.android.library.bean;

import com.google.gson.annotations.SerializedName;
import com.itdlc.android.library.base.BaseResp;
import com.itdlc.android.library.http.entity.TimeBean;

/**
 * Created by felear on 2018/3/23.
 */

public class MulFileUploadBean extends BaseResp {
    /**
     * data : {"entity":{"path":"video/recordVideo.mp4","size":"1.25MB","titleAlter":"recordVideo","titleOrig":"recordVideo","type":".mp4","uploadTime":{"date":16,"day":1,"hours":11,"minutes":34,"month":3,"nanos":240000000,"seconds":58,"time":1523849698240,"timezoneOffset":-480,"year":118}},"result":"上传成功"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * entity : {"path":"video/recordVideo.mp4","size":"1.25MB","titleAlter":"recordVideo","titleOrig":"recordVideo","type":".mp4","uploadTime":{"date":16,"day":1,"hours":11,"minutes":34,"month":3,"nanos":240000000,"seconds":58,"time":1523849698240,"timezoneOffset":-480,"year":118}}
         * result : 上传成功
         */

        public EntityBean entity;
        public String result;

        public static class EntityBean {
            /**
             * path : video/recordVideo.mp4
             * size : 1.25MB
             * titleAlter : recordVideo
             * titleOrig : recordVideo
             * type : .mp4
             * uploadTime : {"date":16,"day":1,"hours":11,"minutes":34,"month":3,"nanos":240000000,"seconds":58,"time":1523849698240,"timezoneOffset":-480,"year":118}
             */
            public String path;
            public String size;
            public String titleAlter;
            public String titleOrig;
            public String type;
            public TimeBean uploadTime;

        }
    }
}
