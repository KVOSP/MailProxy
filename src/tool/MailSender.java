package tool;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import model.*;

public class MailSender {
	public static boolean sendEmail(Mail mail) {
		MimeMessage message;
	    Session session;
	    Transport transport = null; 
	    String mailHost="";
	    String sender_username="";
	    String sender_password="";    
	    Properties properties = new Properties();
		InputStream in = MailSender.class.getResourceAsStream("MailServer.properties");
         try {
             properties.load(in);
             Integer id=mail.getServerId();
             MailServer server=new MailServer_db().getServer(id);
             mailHost=server.getSmtpHost();
             sender_username =server.getMailAddress();
             sender_password = server.getPasswd();
             properties.setProperty("mail.smtp.host",mailHost);
             properties.setProperty("mail.sender.username",sender_username);
             properties.setProperty("mail.sender.password",sender_password);
             session = Session.getInstance(properties);
             message = new MimeMessage(session);
             // 设置发件人
             InternetAddress from = new InternetAddress(sender_username);
             String nick="";
             try {
                 nick=javax.mail.internet.MimeUtility.encodeText("内网系统");
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }
             message.setFrom(new InternetAddress(nick+" <"+from+">"));
             //设置收件人，多个用;分隔
             String recipient = mail.getRecipient();
             String []recipients=recipient.split(";");
             if(recipients.length>1){
                 InternetAddress[] toall = new InternetAddress[recipients.length];
                 for(int i=0;i<recipients.length;i++){
                     System.out.println(recipients[i]);
                     System.out.println(i);
                     toall[i] = new InternetAddress(recipients[i]);
                 }
                 message.setRecipients(Message.RecipientType.TO, toall);
             }else{
                 InternetAddress to = new InternetAddress(recipient);
                 message.setRecipient(Message.RecipientType.TO, to);
             }
//             System.out.println(recipient+recipients[0]+"cc");
             //抄送人，可多人
             String cc = mail.getCc();
             if(cc.length()>1&&!cc.equals("null")) {
                 String[] ccs = cc.split(";");
                 if (ccs.length > 1) {
                     InternetAddress[] ccall = new InternetAddress[ccs.length];
                     for (int i = 0; i < ccs.length; i++) {
                         System.out.println(ccs[i]);
                         System.out.println(i);
                         ccall[i] = new InternetAddress(ccs[i]);
                     }
                     message.setRecipients(Message.RecipientType.CC, ccall);
                 } else {
                     InternetAddress ccto = new InternetAddress(cc);
                     message.setRecipient(Message.RecipientType.CC, ccto);
                 }
             }
//             System.out.println(cc);
             //密送
             String bcc = mail.getBcc();
             if(bcc.length()>1&&!bcc.equals("null")) {
                 String[] bccs = bcc.split(";");
                 if (bccs.length > 1) {
                     InternetAddress[] bccall = new InternetAddress[bccs.length];
                     for (int i = 0; i < bccs.length; i++) {
                         System.out.println(bccs[i]);
                         System.out.println(i);
                         bccall[i] = new InternetAddress(bccs[i]);
                     }
                     message.setRecipients(Message.RecipientType.BCC, bccall);
                 } else {
                     InternetAddress bccto = new InternetAddress(bcc);
                     message.setRecipient(Message.RecipientType.BCC, bccto);
                 }
             }
             // 设置邮件主题
             message.setSubject(javax.mail.internet.MimeUtility.encodeText(mail.getSubject()));
             // 多媒体内容
             Multipart multipart = new MimeMultipart();     
             // 邮件header
             BodyPart contentPart = new MimeBodyPart();
             contentPart.setContent(mail.getContent(), "text/html;charset=UTF8");
             multipart.addBodyPart(contentPart);
             // 邮件附件
             if (mail.getAttachment()!= null&&mail.getAttachment()!="null") {
                 UploadAttachment.download(mail);   //下载到attachment文件夹
                 BodyPart attachmentBodyPart = new MimeBodyPart();
                 DataSource source = new FileDataSource("./src/attachment/"+String.valueOf(mail.getMailId())+"/"+mail.getAttachment());
                 attachmentBodyPart.setDataHandler(new DataHandler(source));
                 attachmentBodyPart.setFileName(MimeUtility.encodeWord(mail.getAttachment()));
                 multipart.addBodyPart(attachmentBodyPart);
             }
             message.setContent(multipart);
             message.saveChanges();
             transport = session.getTransport("smtp");
             transport.connect(mailHost, sender_username, sender_password);
//             System.out.println(message.getAllRecipients().toString());
             transport.sendMessage(message, message.getAllRecipients());
             System.out.println("send success!");
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         } finally {
             if (transport != null) {
                 try {
                     transport.close();
                 } catch (MessagingException e) {
                     e.printStackTrace();
                 }finally{
                	
                 }
             }
         }
         return true;
    }
}
