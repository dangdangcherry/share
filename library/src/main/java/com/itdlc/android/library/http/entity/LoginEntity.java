package com.itdlc.android.library.http.entity;

import com.itdlc.android.library.base.BaseResp;

/**
 * Created by dangdang on 2018/7/6.
 */

public class LoginEntity extends BaseResp {


    /**
     * data : {"authorization":"eyJsamIiOiJKd3RVdGlsIiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWQiOiJVUzA1MzQ2MjUyMTQ0ODQ1MyIsImlzcyI6InJlc3RhcGl1c2VyIiwiYXVkIjoiMDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjYiLCJleHAiOjE1Mzg1MzI0MDcsIm5iZiI6MTUzNzkyNzYwN30.VbqlefOk5ZP7wv9q1zga93IATmaJi11cItK599ZSoBg","user":{"userId":"US053462521448453","nickName":"haoshao","usersPhone":"18688723564","usersPic":"uploads/pic_img/20180918/1537256197163.jpg","gender":1,"invitation":"IV953462521448453","wechat":"oeP335kqrqufM1dqfTFGuHeouLPM","facebook":null,"email":"892210435@qq.com","ctime":1537253692000,"loginCtime":null,"isFlag":1}}
     */

    public DataEntity data;

    public static class DataEntity {
        /**
         * authorization : eyJsamIiOiJKd3RVdGlsIiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWQiOiJVUzA1MzQ2MjUyMTQ0ODQ1MyIsImlzcyI6InJlc3RhcGl1c2VyIiwiYXVkIjoiMDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjYiLCJleHAiOjE1Mzg1MzI0MDcsIm5iZiI6MTUzNzkyNzYwN30.VbqlefOk5ZP7wv9q1zga93IATmaJi11cItK599ZSoBg
         * user : {"userId":"US053462521448453","nickName":"haoshao","usersPhone":"18688723564","usersPic":"uploads/pic_img/20180918/1537256197163.jpg","gender":1,"invitation":"IV953462521448453","wechat":"oeP335kqrqufM1dqfTFGuHeouLPM","facebook":null,"email":"892210435@qq.com","ctime":1537253692000,"loginCtime":null,"isFlag":1}
         */

        public String authorization;
        public UserEntity user;

        public static class UserEntity {
            /**
             * userId : US053462521448453
             * nickName : haoshao
             * usersPhone : 18688723564
             * usersPic : uploads/pic_img/20180918/1537256197163.jpg
             * gender : 1
             * invitation : IV953462521448453
             * wechat : oeP335kqrqufM1dqfTFGuHeouLPM
             * facebook : null
             * email : 892210435@qq.com
             * ctime : 1537253692000
             * loginCtime : null
             * isFlag : 1
             */

            public String userId;
            public String nickName;
            public String usersPhone;
            public String usersPic;
            public Integer gender;
            public String invitation;
            public String wechat;
            public String facebook;
            public String email;
            public long ctime;
            public Object loginCtime;
            public int isFlag;
            public String wxNickName;
            public String faceNickName;
        }
    }
}
