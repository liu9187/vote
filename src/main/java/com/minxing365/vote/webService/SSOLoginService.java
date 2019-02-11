package com.minxing365.vote.webService;

import com.alibaba.fastjson.JSONObject;
import com.minxing.client.organization.User;
import com.minxing365.vote.pojo.SSOLogin;
import com.minxing365.vote.pojo.SysMsgHeader;
import com.minxing365.vote.queue.SSOLoginQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@WebService
public class SSOLoginService {
    private Logger log = LoggerFactory.getLogger(SSOLoginService.class);

    @WebMethod
    public String verifyAppSSOToken(@WebParam(name = "sysMsgHeader", header = true) SysMsgHeader sysMsgHeader, @WebParam(name = "ssoLogin") SSOLogin ssoLogin) throws ExecutionException {
        JSONObject js = new JSONObject();
        js.put("ssoToken", ssoLogin.getSsoToken());
        js.put("appId", ssoLogin.getAppId());
        User user =null;
        //验证header信息
        if (null == sysMsgHeader.getClientCd() || "".equals(sysMsgHeader.getClientCd()) || null == sysMsgHeader.getMsgId() || "".equals(sysMsgHeader.getMsgId()) ||
                null == sysMsgHeader.getOperation() || "".equals(sysMsgHeader.getOperation()) || null == sysMsgHeader.getServiceCd() || "".equals(sysMsgHeader.getServiceCd()) ||
                null == sysMsgHeader.getTranCode() || "".equals(sysMsgHeader.getTranCode())) {
            sysMsgHeader.setResText("关键参数缺少，交易失败");
            js.put("sysMsgHeader", sysMsgHeader);
            js.put("user", user);
            return js.toJSONString();
        }

        try {
            SSOLoginQueue.getInstance().put(ssoLogin);
            Future<User> future = SSOLoginQueue.getInstance().start();
            user = future.get();
        } catch (InterruptedException e) {
            log.error("数据正在添加到队列当中...", e);
        }
        js.put("sysMsgHeader", sysMsgHeader);
        js.put("user", user);
        return js.toJSONString();
    }
}
