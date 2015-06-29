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
import java.util.List;
import java.util.Map;

@WebServlet(name = "QueueQuary")
public class QueueQuary extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.sendRedirect("View/MailQueue.jsp?id="+request.getParameter("mid")+"&recipient="+request.getParameter("recipient"));
        int serverId = request.getParameter("id")==null?0:Integer.parseInt(request.getParameter("id"));
        String recipient = request.getParameter("recipent")==null?"":request.getParameter("recipent");
        try{
            request.setAttribute("cur_id",serverId);
            request.setAttribute("cur_recipent",recipient);
            request.setAttribute("Accounts", new MailServer_db().getAccounts());
            List<Map> MailListMap = new Mail_db().getListByFilter('N', 0, 20, serverId, recipient);
            request.setAttribute("MailList",MailListMap);
            ServletContext application = this.getServletContext();//this是这个页面
            RequestDispatcher rd = application.getRequestDispatcher("/View/MailQueue.jsp");    // 使用req对象获取RequestDispatcher对象
            rd.forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
