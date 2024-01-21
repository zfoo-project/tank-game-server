package com.zfoo.tank.common.util;


import com.zfoo.net.util.security.AesUtils;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.scheduler.util.TimeUtils;

import java.util.List;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-11-11 20:04
 */
public abstract class HttpLoginUtils {

    public static final long TOKEN_EXPIRE_TIME = 24 * TimeUtils.MILLIS_PER_HOUR;

    public static String toToken(long uid, String name) {
        var expireTime = TimeUtils.now() + TOKEN_EXPIRE_TIME;
        var loginInfo = new HttpLoginInfo(uid, expireTime, name);
        var json = JsonUtils.object2String(loginInfo);
        return AesUtils.getEncryptString(json);
    }

    public static HttpLoginInfo fromToken(String encryptString) {
        var json = AesUtils.getDecryptString(encryptString);
        var loginInfo = JsonUtils.string2Object(json, HttpLoginInfo.class);
        return loginInfo;
    }


    public static class HttpLoginRequest {
        private String token;
        private String type;
        private String platform;
        private String version;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class HttpLoginResult {
        private String myToken;
        private long timestamp;
        private List<String> connectUrls;

        public HttpLoginResult() {
        }

        public HttpLoginResult(String myToken, long timestamp, List<String> connectUrls) {
            this.myToken = myToken;
            this.timestamp = timestamp;
            this.connectUrls = connectUrls;
        }

        public String getMyToken() {
            return myToken;
        }

        public void setMyToken(String myToken) {
            this.myToken = myToken;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public List<String> getConnectUrls() {
            return connectUrls;
        }

        public void setConnectUrls(List<String> connectUrls) {
            this.connectUrls = connectUrls;
        }
    }


    public static class HttpLoginInfo {
        private long uid;
        private long expireTime;
        private String name;

        public HttpLoginInfo() {
        }

        public HttpLoginInfo(long uid, long expireTime, String name) {
            this.uid = uid;
            this.expireTime = expireTime;
            this.name = name;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
