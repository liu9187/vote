package com.minxing365.vote.util;

import com.mysql.jdbc.Constants;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;

/**
 * 图片工具类
 */
public class ImgUtils {
    /**
     *保存文件，直接以multipartFile形式
     * @param multipartFile
     * @param path  文件保存绝对路径
     * @return
     * @throws IOException
     */
    public static String saveImg(MultipartFile multipartFile, String path) throws IOException {
        File file=new File( path );
          if (! file.exists()){
              file.mkdir();
          }
     FileInputStream fileInputStream= (FileInputStream) multipartFile.getInputStream();
          String fileName= JnbEsbUtil.CreateMsgId()+".png";
        BufferedOutputStream bos=new BufferedOutputStream( new FileOutputStream( path + File.separator + fileName ) );
        byte[] bs = new byte[1024];
        int len;
        while ((len=fileInputStream.read()) !=-1){
               bos.write( bs,0,len );
        }
        bos.flush();
        bos.close();
        return fileName;
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile+File.separator+"img"+File.separator);

    }
}
