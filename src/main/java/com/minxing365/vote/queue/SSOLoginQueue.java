package com.minxing365.vote.queue;

import com.minxing.client.app.AppAccount;
import com.minxing.client.model.MxVerifyException;
import com.minxing.client.organization.User;
import com.minxing365.vote.controller.VoteController;
import com.minxing365.vote.pojo.SSOLogin;
import com.minxing365.vote.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * sso验证登录队列
 */
public class SSOLoginQueue extends LinkedBlockingQueue<SSOLogin> {
    private Logger log = LoggerFactory.getLogger(SSOLoginQueue.class);
    //线程池
    private static ExecutorService es = Executors.newFixedThreadPool(10);
    //单例
    private static SSOLoginQueue sq = new SSOLoginQueue();
    //是否完成的标志
    private boolean flag = false;
    //域名
    private String mxDomain = PropertiesUtil.getValue("mx.domain");
    //接入端token
    private String mxAccToken=PropertiesUtil.getValue("mx.accToken");

    //获取实例
    public static SSOLoginQueue getInstance() {
        return sq;
    }

    /**
     * 队列监听启动
     */
    public Future<User> start() {
        if (!this.flag) {
            this.flag = true;
        } else {
            throw new IllegalArgumentException("队列处于启动状态，不允许重复启动");
        }
        //开启业务
        Future<User> future = null;
        while (flag) {
            //使用阻塞模式获取队列
            try {
                //获取数据
                SSOLogin ssoLogin = take();
                //处理业务
                future = es.submit(() -> doBusiness(ssoLogin));
            } catch (InterruptedException e) {
                 log.error("<<<<<<soo验证获取队列信息失败",e);
            }

        }
        return future;

    }

    /**
     * 停止队列监听
     */
    public void stop() {
        this.flag = false;
    }

    /**
     * 业务处理方法
     */
    private User doBusiness(SSOLogin ssoLogin) {
        log.info("sso验证进入业务处理方法,传入参数：ssoToken="+ssoLogin.getSsoToken()+"; appId="+ssoLogin.getAppId());
        AppAccount appAccount = AppAccount.loginByAccessToken(mxDomain, mxAccToken);
        User user = new User();
        //ssoToken:ZgAAAEJM7fuAmR1rv-3aLbGSn51MjaScqwtXpzL2y42L4mK7
        //appId:f7eb0d019ab3886780c075e6f26d424c
        try {
             if (null==ssoLogin.getSsoToken()||"".equals(ssoLogin.getSsoToken())
                     ||null==ssoLogin.getAppId()||"".equals(ssoLogin.getAppId())){
                 return null;
             }else {
                 user = appAccount.verifyAppSSOToken(ssoLogin.getSsoToken(), ssoLogin.getAppId());
             }

        } catch (MxVerifyException e) {
             log.error("<<<<sso登录验证调用敏行接口失败",e);
        }
        return user;
    }
}
