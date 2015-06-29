package model;

public class MailServer {
	private Integer serverId;
    private String smtpHost;
	private String mailAddress;
    private String passwd;
	private String text;
    public MailServer(String smtpHost,String mailAddress,String passwd,String text){
    	this.smtpHost=smtpHost;
    	this.mailAddress=mailAddress;
		this.passwd= convert(passwd);
		this.text = text;
    }
    public MailServer(Integer serverId,String smtpHost,String mailAddress,String passwd,String text){
    	this.serverId=serverId;
    	this.smtpHost=smtpHost;
    	this.mailAddress=mailAddress;
    	this.passwd=convert(passwd);
		this.text = text;
    }
    public String getSmtpHost() {
		return smtpHost;
	}
	public Integer getServerId() {
		return serverId;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public String getPasswd() {
		return convert(passwd);
	}
	public String getText(){
		return text;
	}
	public static String convert(String inStr){

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++){
			a[i] = (char) (a[i] ^ 'p');
		}
		String s = new String(a);
		return s;
	}
}
