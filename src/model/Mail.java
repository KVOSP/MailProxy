package model;

public class Mail {
    private Integer mailId; //邮件ID，自增
    private Integer serverId;
    private String recipient;
    private String cc;      //抄送
    private String bcc;     //密送
    private String subject;
    private String content;
    private String attachment;//只保存附件名，因为附件实际存储在阿里云OSS，保存路径为bucketName/MailID/附件名
    //构造函数
    public Mail(Integer ServerId,String recipient,String cc,String bcc, String subject, String content,
                String attachment){
        this.serverId=ServerId;
        this.recipient = recipient;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.content = content;
        this.attachment = attachment;
    }
    public Integer getMailId() {
        return mailId;
    }
    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }
    public Integer getServerId() {
        return serverId;
    }
    public String getRecipient() {
        return recipient;
    }
    public String getCc(){
        return cc;
    }
    public String getBcc(){
        return bcc;
    }
    public String getSubject() {
        return subject;
    }
    public String getContent() {
        return content;
    }
    public String getAttachment() {
        return attachment;
    }
    public String toString(){
        return "mailId"+mailId+"from: "+serverId+" recipient: "+recipient+" subject: "+subject+" content: "+content;
    }
}
