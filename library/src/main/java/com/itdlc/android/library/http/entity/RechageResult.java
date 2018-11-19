package com.itdlc.android.library.http.entity;

import com.itdlc.android.library.base.BaseResp;

/**
 * Created by felear on 2018/1/24.
 */

public class RechageResult extends BaseResp {


    /**
     * obj : {"data":"alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018031202356871&biz_content=%7B%22body%22%3A%22%E5%B0%8F%E4%BF%9D%E5%95%86%E8%B4%B8%22%2C%22out_trade_no%22%3A%22OU1631084462032%22%2C%22product_code%22%3A%22product_code%22%2C%22subject%22%3A%22%E5%B0%8F%E4%BF%9D%E5%95%86%E8%B4%B8%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%EF%BC%9A%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%2210%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fxiaobaosm.j.xiaozhuschool.com%2Fpay%2Fzfb%2Fnotify&sign=sV448jK2CwoH1In8OVP0niJR9UoRRWaV7mJ2VjJPPs7FLn6ijLIWR1K1NHRmlUpbHMCLggM4XcpBD6trdfuqoH6vNW1XiXm57Io5QXegmKXIzKiHTrNfMdvYvaMXPEsXxs3H6A7iQWyr4o6OonBm2%2BlzkmSG5pANOjMnwV5gsqt%2BfTAxUvPL%2BPTcivPAS%2FdbUl1tAqSQlChAXnRPezkGeaIOGHPxsbEO0u%2F7bIqpdi5YqnQ2tNtHelIvH3TKXK5QhDCHbWv83KPbDIetsIsfEiU2zlDWwwuPPvZ7scqLR8A5CLg26NjvDtdAflj3RALp36p1h0pqkkyksECjG7Ryug%3D%3D&sign_type=RSA2&timestamp=2018-04-26+14%3A59%3A57&version=1.0"}
     */

    public ObjBean obj;

    public static class ObjBean {
        /**
         * data : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018031202356871&biz_content=%7B%22body%22%3A%22%E5%B0%8F%E4%BF%9D%E5%95%86%E8%B4%B8%22%2C%22out_trade_no%22%3A%22OU1631084462032%22%2C%22product_code%22%3A%22product_code%22%2C%22subject%22%3A%22%E5%B0%8F%E4%BF%9D%E5%95%86%E8%B4%B8%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%EF%BC%9A%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%2210%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fxiaobaosm.j.xiaozhuschool.com%2Fpay%2Fzfb%2Fnotify&sign=sV448jK2CwoH1In8OVP0niJR9UoRRWaV7mJ2VjJPPs7FLn6ijLIWR1K1NHRmlUpbHMCLggM4XcpBD6trdfuqoH6vNW1XiXm57Io5QXegmKXIzKiHTrNfMdvYvaMXPEsXxs3H6A7iQWyr4o6OonBm2%2BlzkmSG5pANOjMnwV5gsqt%2BfTAxUvPL%2BPTcivPAS%2FdbUl1tAqSQlChAXnRPezkGeaIOGHPxsbEO0u%2F7bIqpdi5YqnQ2tNtHelIvH3TKXK5QhDCHbWv83KPbDIetsIsfEiU2zlDWwwuPPvZ7scqLR8A5CLg26NjvDtdAflj3RALp36p1h0pqkkyksECjG7Ryug%3D%3D&sign_type=RSA2&timestamp=2018-04-26+14%3A59%3A57&version=1.0
         */

        public String data;
    }
}
