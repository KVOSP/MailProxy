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

@WebServlet(name = "DeleteAccount")
public class DeleteAccount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// 设置响应内容类型
        try
        {
            MailServer_db ms = new MailServer_db();
            if(ms.deleteServer(Integer.parseInt(request.getParameter("id")))){
//                response.sendRedirect("../index.jsp?alarm=y");
                request.setAttribute("alarm", "删除成功！");                                        // 为request对象添加参数
                ServletContext application = this.getServletContext();//this是这个页面
                RequestDispatcher rd = application.getRequestDispatcher("/index.jsp");    // 使用req对象获取RequestDispatcher对象
                rd.forward(request, response);                                          // 使用RequestDispatcher对象在服务器端向目的路径跳转
            }else{
//                response.sendRedirect("../index.jsp?alarm=n");
                request.setAttribute("alarm", "删除失败！");                                        // 为request对象添加参数
                ServletContext application = this.getServletContext();//this是这个页面
                RequestDispatcher rd = application.getRequestDispatcher("/index.jsp");    // 使用req对象获取RequestDispatcher对象
                rd.forward(request, response);                                                // 使用RequestDispatcher对象在服务器端向目的路径跳转
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
