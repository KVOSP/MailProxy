package model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class Mail_db {
	Connection connecter;
	Statement statement;
   private void init() {
	    String driver=null;
   		String host=null;
   		String username=null;
   		String pwd=null;
   		String port=null;
   		String dbname=null;
   		Properties properties = new Properties();
        try {
        	InputStream in = Mail_db.class.getResourceAsStream("MySQL.properties");
            properties.load(in);
            driver=properties.getProperty("driver");
            host=properties.getProperty("mysql.servers.master.0.host");
            username=properties.getProperty("mysql.servers.master.0.username");
            pwd=properties.getProperty("mysql.servers.master.0.password");
            port=properties.getProperty("mysql.servers.master.0.port");
            dbname=properties.getProperty("mysql.servers.master.0.dbname");
			Class.forName(driver);		
			String url="jdbc:mysql://"+host+":"+port+"/"+dbname;
			connecter=DriverManager.getConnection(url,username,pwd);		
//	        if(!connecter.isClosed()) System.out.println("success in getConnetion");
	        statement = connecter.createStatement(); 
//	        String database="CREATE DATABASE if not exists email_Proxy  DEFAULT CHARSET=gbk";
//	        statement.executeUpdate(database);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
   }
   private void createEmailTable() throws SQLException{
	   init();
	   try {
		statement.executeUpdate
		   ("create table emailInfo "
		   		+ "(mailId integer NOT NULL PRIMARY KEY AUTO_INCREMENT,serverId integer,recipient varchar(30),"
		   		+ "subject varchar(30),content text,attachmenturl varchar(30),isSend char(1),"
		   		+ "sendTime  varchar(50))");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  finally{
		statement.close();
		connecter.close();
	}
	   
   }
	/*
	*得到一封邮件详细信息
	 */
	public Map getMailById(int MailId) throws SQLException{
		ResultSet rs=null;
		Map map = new HashMap();
		String select = "select * from emailInfo where mailId='" + MailId + "'";
		try {
			init();
			rs=statement.executeQuery(select);
			while(rs.next()){
				map.put("MailId",rs.getInt(1));
				map.put("ServerId",rs.getInt(2));
				map.put("Recipient",rs.getString(3));
				map.put("Subject",rs.getString(4));
				map.put("Content",rs.getString(5));
				map.put("Url",rs.getString(6));
				map.put("Time",rs.getString(8));
				map.put("Cc",rs.getString(9));
				map.put("Bcc",rs.getString(10));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			rs.close();
			statement.close();
			connecter.close();
		}
		return map;
	}
   /*
    * 根据查找信息得到邮件列表
    */
   public List<Map> getListByFilter(char state,int limitStart,int limitCount,int searchSend,String searchRes) throws SQLException{
	   ResultSet rs=null;
	   //用Map存储邮件基本信息和发送时间
	   List<Map>mailHistory=new LinkedList<Map>();
	   String select = "select * from emailInfo where isSend='" + state + "'";
	   if(searchSend!=0){
		   select += " and serverId = " + String.valueOf(searchSend);
	   }
	   if(searchRes!=null&&searchRes!=""){
		   select += " and recipient = '" + searchRes + "'";
	   }
	   select += " limit " + String.valueOf(limitStart*limitCount) + "," + String.valueOf(limitCount);
	   try {
		   init();
		   rs=statement.executeQuery(select);
		   while(rs.next()){
			   Map map = new HashMap();
			   map.put("MailId",rs.getInt(1));
			   map.put("ServerId",rs.getInt(2));
			   map.put("Recipient",rs.getString(3));
			   map.put("Subject",rs.getString(4));
			   map.put("Content",rs.getString(5));
			   map.put("Url",rs.getString(6));
			   map.put("Time",rs.getString(8));
			   map.put("Cc",rs.getString(9));
			   map.put("Bcc",rs.getString(10));
			   mailHistory.add(map);
		   }
		    
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }finally{
		   rs.close();
		   statement.close();
		   connecter.close();
	   }
      return mailHistory;
   }
//   /*
//    * ��ȡδ���͵��б�
//    */
//   public List<Mail> getUnSendMail() throws SQLException{
//	   ResultSet rs=null;
//	   List<Mail>mailHistory=new LinkedList<Mail>();
//	   try {
//		   init();
//		   rs=statement.executeQuery("select * from emailInfo where isSend='N' ");
//		   while(rs.next()){
//			   Mail mail=new Mail(rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5));
//			   mailHistory.add(mail);
//		   }
//
//	   } catch (SQLException e) {
//		   // TODO Auto-generated catch block
//		   e.printStackTrace();
//	   }finally{
//		   rs.close();
//		   statement.close();
//		   connecter.close();
//	   }
//      return mailHistory;
//   }
   public int insertEmail(Mail mail) throws SQLException{
	   init();
	   ResultSet rs=null;
	   int mailId=0;
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
	   try {
		   String sql="insert emailInfo (serverId ,recipient,subject ,content,attachmenturl ,isSend ,sendTime,cc ,bcc) values ('"+mail.getServerId()+"','"+mail.getRecipient()+"','"+mail.getSubject()+"','"+mail.getContent()+"','"+mail.getAttachment()+"','"+"N"+"','"+String.valueOf(df.format(new Date()))+"','"+mail.getCc()+"','"+mail.getBcc()+"')";
		   PreparedStatement pstmt = connecter.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		   pstmt.executeUpdate();
		   rs = pstmt.getGeneratedKeys();
		   rs.next();
           mailId=rs.getInt(1);
		   if(mail.getAttachment()!=null){
			   String setatt="update emailInfo set attachmenturl='http://email-attachment.oss-cn-hangzhou.aliyuncs.com/'"+String.valueOf(mailId)+"/"+mail.getAttachment()+" where mailId="+mailId;
			   pstmt.executeUpdate(setatt);
		   }
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   } finally{
		   if(rs!=null){
			   rs.close();
			   statement.close();
			   connecter.close();
		   }
	   }
	   return mailId;
   }
   /*
    * ���ʼ���Ϣ����Ϊ�ѷ���
    */
   public void changeMailState(int mailId){
	   init();
	   try {
		statement.executeUpdate("update emailInfo set isSend='Y' where mailId="+mailId);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{}
	   
   }
}
