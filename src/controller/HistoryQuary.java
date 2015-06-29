package controller;

import model.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "HistoryQuary")
public class HistoryQuary extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.sendRedirect("View/MailHistory.jsp?id="+request.getParameter("mid")+"&recipient="+request.getParameter("recipient"));
        int serverId = request.getParameter("id")==null?0:Integer.parseInt(request.getParameter("id"));
        String recipient = request.getParameter("recipent")==null?"":request.getParameter("recipent");
        try{
            request.setAttribute("cur_id",serverId);
            request.setAttribute("cur_recipent",recipient);
            request.setAttribute("Accounts", new MailServer_db().getAccounts());
            List<Map> MailListMap = new Mail_db().getListByFilter('Y', 0, 20, serverId, recipient);
            request.setAttribute("MailList",MailListMap);
            ServletContext application = this.getServletContext();//this是这个页面
            RequestDispatcher rd = application.getRequestDispatcher("/View/MailHistory.jsp");    // 使用req对象获取RequestDispatcher对象
            rd.forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
