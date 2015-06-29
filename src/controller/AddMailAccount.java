package controller;

import model.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddMailAccount")
public class AddMailAccount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        MailServer mServer = new MailServer(request.getParameter("smtpHost"),request.getParameter("mailAdress"),request.getParameter("accountPass"),request.getParameter("text"));
        try
        {
            new MailServer_db().addAccount(mServer);
            request.setAttribute("alarm", "添加成功！");                                        // 为request对象添加参数
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");    // 使用req对象获取RequestDispatcher对象
            dispatcher.forward(request, response);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            request.setAttribute("alarm", "添加失败！");                                        // 为request对象添加参数
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");    // 使用req对象获取RequestDispatcher对象
            dispatcher.forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
