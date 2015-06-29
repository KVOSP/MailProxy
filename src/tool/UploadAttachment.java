package tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import model.Mail;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

public class UploadAttachment {
    private static  String ACCESS_ID ;
    private static  String ACCESS_KEY ;
    private static  String OSS_ENDPOINT;
    private static OSSClient client;
    private static String filePath="./src/attachment/";
    private static String bucketName="email-attachment";
    private static void init(){
        Properties properties = new Properties();
        InputStream in = UploadAttachment.class.getResourceAsStream("OSSClient.properties");
        try {
            properties.load(in);
            ACCESS_ID=properties.getProperty("keyId");
            ACCESS_KEY=properties.getProperty("keySecret");
            OSS_ENDPOINT=properties.getProperty("OSS_ENDPOINT");
            client=new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
        } catch (IOException e1) {

        }

    }
    /*
    * 确认Bucket存在
    */
    private static void ensureBucket(){
        try {
            init();
            client.createBucket(bucketName);
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())) {
                throw e;
            }
        }
    }
    /*
    * 上传邮件附件
    */
    public static void upload(Mail mail,File Attachment) throws FileNotFoundException{
        init();
        ensureBucket();
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(mail.getAttachment().length());
        InputStream input =new FileInputStream(Attachment);
        PutObjectResult rtag=client.putObject(bucketName,String.valueOf(mail.getMailId())+"/"+mail.getAttachment(), input, objectMeta);
        System.out.println(rtag.getETag());
    }
    /*
     * 在发送邮件时从阿里云临时下载到本地
     */
    public static void download(Mail mail)throws OSSException, ClientException {
        init();
        ensureBucket();
        client.getObject(new GetObjectRequest(bucketName,String.valueOf(mail.getMailId())+"/"+mail.getAttachment()),new File(filePath+String.valueOf(mail.getMailId())+"/"+mail.getAttachment()));
    }
}
