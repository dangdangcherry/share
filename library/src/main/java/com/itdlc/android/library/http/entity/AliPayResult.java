package com.itdlc.android.library.http.entity;

import com.itdlc.android.library.base.BaseResp;

/**
 * Created by felear on 2018/1/24.
 */

public class AliPayResult extends BaseResp {


    /**
     * aliData : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018031202356871&biz_content=%7B%22body%22%3A%22%E8%BF%AA%E5%B0%94%E8%A5%BF%E7%A7%91%E6%8A%80%E8%82%A1%E4%BB%BD%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%22%2C%22out_trade_no%22%3A%22RE821007901239%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%AE%A2%E5%8D%95%E7%BB%93%E7%AE%97%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%2230.0%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpopularparking.dlc-sz.com%2Fzfb%2Fnotify&sign=cuWFgEzIrfzbSfosE0QXPugffZoyNbqNv9Gd4PvMOqTZ4NgsEM7iReQKh8sPzz9Yr3QjihDDv882OfUXbMjbcD4LmuIleF0itU23rDQNYbLsafwbfiJQZklcditGbE%2Fr1AbpiMjWPP1lDVmgyyHQalHXGKmH1g3cBaVug8Wfmsrbj9ep18RcfvaGJxsZIWdjVf%2FOqtkDBg88im%2FF2Rzcd1m3AErFxVxYG8VMCVBMtdUVt544SUSc8qY8SZShzkBFo2d4wXKk6evNykkrPPdkdkqmeWKCDJ0YiGmowYTYqWf22%2FEUk9S%2BWDFzpMrTBQnDtfqwgIj5xzW7LJJfMoCrvQ%3D%3D&sign_type=RSA2&timestamp=2018-07-19+14%3A31%3A58&version=1.0
     */

    public DataEntity data;

    public static class DataEntity {
        public String aliData;
    }
}
