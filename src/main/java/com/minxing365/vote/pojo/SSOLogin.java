package com.minxing365.vote.pojo;

/**
 * sso同意验证web应用
 */
public class SSOLogin {
    private String ssoToken;
    private String appId;

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
