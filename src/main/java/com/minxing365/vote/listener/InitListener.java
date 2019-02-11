package com.minxing365.vote.listener;

import com.minxing365.vote.dao.InitMapper;
import com.minxing365.vote.util.PropertiesUtil;
import com.minxing365.vote.webService.SSOLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

@Component
public class InitListener {
//    @Autowired
//    private InitMapper initMapper;

    Logger logger = LoggerFactory.getLogger(InitListener.class);
    public void init(){
        logger.info("-------start init vote-----进入初始化设置----");

//        try {
//             String address=PropertiesUtil.getValue("login.address");
//            Endpoint.publish(address,new SSOLoginService());
//            logger.info("===sso登录验证接口发布成功");
//        }catch (Exception e){
//             logger.error("<<<<<<sso登录验证接口发布失败");
//        }
    }
}
