package com.minxing365.vote.util;

import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 字符串验证类
 * @author liucl
 * @date 2018-7-4
 */
public class JnbEsbUtil {
    /**
     * 验证身份证号
     * @param idNo 居民身份证号 15位 或 18位 ，最后一位 可能是数字或者字母
     * @return 验证成功返回true ,验证失败返回 false
     */
    public static boolean checkIdCard(String idNo){
        String regex="[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idNo);
    }
    /**
     * 验证是否是数字
     * @param integer
     * @return
     */
    public static  boolean isInteger(String integer){
          Pattern pattern=Pattern.compile("^[-\\+]?[\\d]*$");
        return  pattern.matcher(integer).matches();
    }

    /**
     * 验证年月 2017
     * @param qryDate
     * @return
     */
    public static  boolean checkQryDate(String qryDate){
        String regex= "\\d{4}";
        return  Pattern.matches(regex,qryDate);
    }
    /**
     * 验证年月 2017-01
     * @param proDate
     * @return
     */
    public static  boolean checkProDate(String proDate){
        String regex= "\\d{4}-\\d{1,2}";
        return  Pattern.matches(regex,proDate);
    }

    /**
     * 验证年月日 1990-09-01
     * @param dataDate
     * @return
     */
    public static  boolean checkDateDate(String dataDate){
        String regex= "\\d{4}-\\d{1,2}-\\d{1,2}";
        return  Pattern.matches(regex,dataDate);
    }

    /**
     * 验证是否是两位数字
     * @param postId
     * @return
     */
    public static boolean checkDoubleNumber(String postId){
          String regex="\\d{2}";
          return  Pattern.matches(regex,postId);
    }
    public  static  boolean checkOneNumber(String type){
        String regex="\\d";
        return  Pattern.matches(regex,type);
    }
    public  static  boolean checkLowerCase(String lower){
        String regex="^[a-z]{1}$";
        return  Pattern.matches(regex,lower);
    }

    /**
     * 获取流水号
     * @return
     */
    public static  String CreateMsgId(){

        StringBuffer msgId=new StringBuffer();
        //系统号
        msgId.append("0322");
        //8位日期
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
      //  String dateStr= sdf.format(date);
        msgId.append(sdf.format(date));
        //9位时间
        SimpleDateFormat timesdf=new SimpleDateFormat("HHmmssSSS" );
        msgId.append(timesdf.format(date));
        //4位序号 随机数
        String num=  RandomStringUtils.random(4,"1234567890");
        msgId.append(num);
        return msgId.toString();
    }

    /**
     * 生成8位系统日期
     * 2018-09-09
     * @return
     */
    public  static  String CreateMsgDate(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
       String msgDate= sdf.format(date);
       return msgDate;
    }

    /**
     * 生成9位系统时间
     * @return
     */
    public static String CreateMsgTime(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss.SSS" );
      return sdf.format(date);
    }


    public static void  main(String[] args){
//          boolean result=false;
//          String s="2017";
//            if (s.length()>5){
//             System.out.println(checkIdCard(s));
//            }
//            System.out.println(result);
        String pm="56301,admin@jnbank";
        String[] users=pm.split(",");
        System.out.println("hahah ");
    }
}
