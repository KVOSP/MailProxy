package controller;

import model.Mail;
import tool.MailSender;
import model.Mail_db;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.MailServer_db;
import tool.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "SendMail")
public class SendMail extends HttpServlet {
    private final static String key = "nNgvTlMWYzLFyW2k";
    private final static String defaultServer = "cx-notice-infomation@joyport.com";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mailAddress = java.net.URLDecoder.decode(request.getParameter("sender"), "UTF-8");
        String recipient = java.net.URLDecoder.decode(request.getParameter("recipient"), "UTF-8");
        String subject = java.net.URLDecoder.decode(request.getParameter("subject"),"UTF-8");
        String content = java.net.URLDecoder.decode(request.getParameter("content"),"UTF-8");
        String cc = java.net.URLDecoder.decode((request.getParameter("cc")==null?"":request.getParameter("cc")), "UTF-8");
        String bcc = java.net.URLDecoder.decode((request.getParameter("bcc")==null?"":request.getParameter("bcc")), "UTF-8");
        String time = java.net.URLDecoder.decode(request.getParameter("time"), "UTF-8");
        String sign = java.net.URLDecoder.decode(request.getParameter("sign"), "UTF-8");
        if(!sign.equals(md5.MD5(mailAddress + recipient + time + key))){
            response.getWriter().write(unicode.chinaToUnicode("密钥错误!"));    //密钥sign=MD5(mailAddress + recipient + time + key)
        }else{
            if(mailAddress.equals("")||mailAddress==null){
            mailAddress = defaultServer;
            }
            String attachmentname = "null";
        if(request.getParameter("attachname")!=null&&request.getParameter("attachname")!="") {
            attachmentname = java.net.URLDecoder.decode(request.getParameter("attachname"), "UTF-8");
        }
        try{
            MailServer_db ms = new MailServer_db();
            Mail_db m = new Mail_db();
            int serverId = ms.getServerId(mailAddress);
            if(serverId!=0){
                Mail mail = new Mail(serverId,recipient,cc,bcc,subject,content,attachmentname);
                int mailId = m.insertEmail(mail);   //存入数据库，默认状态为未发送
                mail.setMailId(mailId);
                //附件上传阿里云
                if(!attachmentname.equals("null")){
                    String attachmentbit = base64.getFromBASE64(java.net.URLDecoder.decode(request.getParameter("attachment"), "UTF-8"));
                    File temp = File.createTempFile("pattern", ".suffix");
                    OutputStream out=new FileOutputStream(temp);
                    out.write(attachmentbit.getBytes());
                    out.close();
//                    File temp = new File("./src/attachment/"+String.valueOf(mailId)+":"+attachmentname);
//                    OutputStream out=new FileOutputStream(temp);
//                    out.write(attachmentbit.getBytes());
//                    out.close();
                    UploadAttachment.upload(mail, temp);
                    temp.deleteOnExit();
                }
                //发送邮件
                MailSender.sendEmail(mail);
                response.getWriter().write(unicode.chinaToUnicode("发送成功!"));
            }
        }catch (Exception e){
            e.printStackTrace();
            response.getWriter().write(unicode.chinaToUnicode("发送失败!"));
            //失败记录日志
            Logger log = LogManager.getLogger(SendMail.class);
            log.error(e);
            log.info("from: "+mailAddress+" recipient: "+recipient+" subject: "+subject+" content: "+content);
        }
    }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
