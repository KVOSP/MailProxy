package model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MailServer_db {
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
			InputStream in = MailServer_db.class.getResourceAsStream("MySQL.properties");
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
	        if(!connecter.isClosed()) System.out.println("success in getConnetion");
	        statement = connecter.createStatement(); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//
	}
	public void createServerAccount() throws SQLException{
	   init();
	   try {
		   statement.executeUpdate
		   ("create table MailServerAccount "
		   		+ "(serverId integer NOT NULL PRIMARY KEY AUTO_INCREMENT,smtpHost varchar(30),mailAddress varchar(300),passwd varchar(30),text VARCHAR (300))");
	     System.out.println("create table MailServerAccount success ");
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }  finally{
		   statement.close();
		   connecter.close();
	   }
	}

	public List<MailServer> getAccounts() throws SQLException{
	   ResultSet rs=null;
	   List<MailServer> serverAcount=new LinkedList<MailServer>();
	   try {
		   init();
		   rs=statement.executeQuery("select * from MailServerAccount");
		   while(rs.next()){
			   MailServer m=new MailServer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
			   serverAcount.add(m);
		   }
		    
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }finally{
		   if(rs!=null){
			   rs.close();
			   statement.close();
			   connecter.close();
		   }

	   }
	   return serverAcount;
	}
	public void addAccount(MailServer server) throws SQLException{
		init();
		try {
//			System.out.println("insert  MailServerAccount (smtpHost ,mailAddress,passwd) values ('"+server.getSmtpHost()+"','"+server.getMailAddress()+"','"+server.getPasswd()+"')");
		   statement.executeUpdate("insert  MailServerAccount (smtpHost ,mailAddress,passwd,text) values ('"+server.getSmtpHost()+"','"+server.getMailAddress()+"','"+server.getPasswd()+"','"+server.getText()+"')");
		   
	    }catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	    }finally{
		   statement.close();
		   connecter.close();
	   }
	   
	}
	public boolean deleteServer(Integer serverId){
		init();
		 int flag=0;
		 try {
			flag=statement.executeUpdate("delete from MailServerAccount where serverId="+serverId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(flag==0) return false;
		 else return true;
		 
	}
	public MailServer getServer(Integer serverId) throws SQLException{
		MailServer server=null;
		ResultSet rs=null;
		init();
		try {
			rs=statement.executeQuery("select * from MailServerAccount where serverId="+serverId);
			rs.next();
			server=new MailServer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
				statement.close();
				connecter.close();
			}
		}
		return server;
	}
	public int getServerId(String MailAddress) throws SQLException{
		ResultSet rs = null;
		int ServerId = 0;
		init();
		try{
			rs = statement.executeQuery("select * from MailServerAccount");
//			rs = statement.executeQuery("select serverId from MailServerAccount where mailAddress='"+MailAddress+"'");
			while(rs.next()){
				if(rs.getString(3).equals(MailAddress)){
					ServerId = rs.getInt(1);
					break;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
				statement.close();
				connecter.close();
			}
		}
		return ServerId;
	}
//	public static void main(String []args) throws SQLException{
//		MailServer ms=new MailServer("smtp.126.com","fox14798@126.com","ttccspy","vjhjh");
//		MailServer_db md=new MailServer_db();
//		md.addAccount(ms);
//		List <MailServer> ser=md.getAccounts();
//		System.out.println(ser.get(1).getMailAddress());
//	}
}
