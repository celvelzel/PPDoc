package com.majy.ppdocapi.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.majy.ppdocapi.entity.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class OSSUtils
{
    @Value("${aliyun.oss.endpoint}")
    private String endPoint ;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyID ;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret ;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName ;


    public URL uploadFile(MultipartFile file) throws IOException
    {
        //获取文件输入流
        InputStream inputStream = file.getInputStream();

        //使用uuid（通用唯一识别码）构造唯一的文件名
        //获取文件名
        String originalFileName = file.getOriginalFilename();
        //获取文件后缀
        String extName = originalFileName.substring(originalFileName.lastIndexOf("."));
        //拼接新的文件名
        String newFileName = UUID.randomUUID().toString() + extName;
        log.info("获取到的文件名：{}", newFileName);

        //上传文件到OSS
        //创建OSSclient实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyID, accessKeySecret);
        ossClient.putObject(bucketName, newFileName, inputStream);

        return getFileUrl(newFileName);
    }

    public URL uploadFile(File file) throws IOException
    {
        //获取文件输入流
        InputStream inputStream = new FileInputStream(file);

        //使用uuid（通用唯一识别码）构造唯一的文件名
        //获取文件名
        String originalFileName = file.getName();
        //获取文件后缀
        String extName = originalFileName.substring(originalFileName.lastIndexOf("."));
        //拼接新的文件名
        String newFileName = UUID.randomUUID().toString() + extName;
        log.info("获取到的文件名：{}", newFileName);

        //上传文件到OSS
        //创建OSSclient实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyID, accessKeySecret);
        ossClient.putObject(bucketName, newFileName, inputStream);

        return getFileUrl(newFileName);
    }

    public URL getFileUrl(String fileName)
    {
        //返回文件在OSS中的URL
        URL signedUrl = null;
        try
        {
            // 生成签名URL
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);

            // 指定生成的签名URL过期时间，单位为毫秒。设置过期时间为365天。
            Date expiration = new Date(new Date().getTime() + 365 * 24 * 3600 * 1000L);
            // 设置过期时间
            request.setExpiration(expiration);

            //创建OSSclient实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyID, accessKeySecret);
            // 通过HTTP GET请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
            // 打印签名URL。
            log.info("文件在OSS中的URL：" + signedUrl);
            return signedUrl;
        } catch (OSSException oe)
        {
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.info("Error Message:" + oe.getErrorMessage());
            log.info("Error Code:" + oe.getErrorCode());
            log.info("Request ID:" + oe.getRequestId());
            log.info("Host ID:" + oe.getHostId());
        } catch (ClientException ce)
        {
            log.info("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.info("Error Message:" + ce.getMessage());
        }
        //文件访问路径
//        String url = endPoint.split("//")[0] + "//" + bucketName + "." + endPoint.split("//")[1] + "/" + fileName;
        return signedUrl;
    }

    //删除文件
    public void deleteFile(String fileName)
    {
        //创建OSSclient实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyID, accessKeySecret);
        //删除文件
        ossClient.deleteObject(bucketName, fileName);
    }

}
