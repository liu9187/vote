package com.minxing365.vote.service.impl;


import com.minxing.client.app.AppAccount;
import com.minxing.client.app.OcuMessageSendResult;
import com.minxing.client.model.MxException;
import com.minxing.client.ocu.Article;
import com.minxing.client.ocu.ArticleMessage;
import com.minxing365.vote.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Component;

/**
 * 发送消息
 */
@Component
public class PushMessage {
    private static Logger log = org.apache.log4j.Logger.getLogger( PushMessage.class );
    //公众号id
    public static  String ocuID= PropertiesUtil.getValue( "OcuID");
    //公众号秘钥
    public static  String ocuSecret= PropertiesUtil.getValue( "OcuSecret");
    //敏行服务器地址
    public static  String mxDomain=PropertiesUtil.getValue("mx.domain" ) ;
    //acctoken
    public  static  String mxAccToken =PropertiesUtil.getValue( "mx.accToken" );
    //社区id
    public  static  String networkId =PropertiesUtil.getValue( "network.id");
    //appid
    public  static  String appId =PropertiesUtil.getValue( "appId");

    /**
     * 推送消息
     *
     * @param title       标题
     * @param description 内容
     * @param id 主键id
     * @return
     */
    public static OcuMessageSendResult sendOcuMessageToUsers(String title, String description,String id,Long endTime) {
        OcuMessageSendResult result = null;
          StringBuilder appUrlStr=new StringBuilder( );
             appUrlStr.append( "launchApp://" ).append( appId ).append( "$$#/index?id=").append( id ).append("&endTime=").append(endTime);
            String appUrl=appUrlStr.toString();
          log.info( "-----appUrl:" +appUrl);
        try {
            AppAccount appAccount = AppAccount.loginByAccessToken( mxDomain, mxAccToken );
            ArticleMessage message = new ArticleMessage();
            Article article = new Article( title, description, "", null, appUrl );
            message.addArticle( article );
            result = appAccount.sendOcuMessageToUsers( networkId, null, message, ocuID, ocuSecret );

        } catch (MxException e) {
            log.error( "=====发送请求异常=====", e );
        }
          log.info("------------发送结束------");
        return result;
    }


    public static void main(String[] args) {

        String title = "异常处理";
        String description = "数据同步异常";
        //sendOcuMessageToUsers( title, description );
        System.out.println( "成功" );

    }

}
