<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <%@ include file="inc/head.jsp" %>
</head>

<body>
<%@ include file="inc/navbar.jsp"%>
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3" style="width: 200px;">
     <%@ include file="inc/nav.jsp"%>
    </div><!--/span-->
    <div class="span9">
      <%
        if(request.getAttribute("alarm") != null){
      %>
      <div class="alert fade in" style="margin-right: -95px;">
        <button type="button" class="close" data-dismiss="alert">×</button>
        <strong><%=request.getAttribute("alarm") %></strong>
      </div>
      <%
        }
      %>
      <div class="hero-unit">
        <table class="table table-bordered">
          <tr>
            <th style="vertical-align:middle;text-align:center">账号ID</th>
            <th style="vertical-align:middle;text-align:center">账号</th>
            <th style="vertical-align:middle;text-align:center">账号说明</th>
            <th colspan="3" style="vertical-align:middle;text-align:center">操作</th>
          </tr>
          <%
            MailServer_db ms = new MailServer_db();
            List<MailServer> slist = ms.getAccounts();
            for(int i = 0 ; i < slist.size() ; i++){
          %>
          <tr>
            <td style="vertical-align:middle;text-align:center"><%=slist.get(i).getServerId() %></td>
            <td style="vertical-align:middle;text-align:center"><%=slist.get(i).getMailAddress() %></td>
            <td style="vertical-align:middle;text-align:center"><%=slist.get(i).getText() %></td>
            <td style="vertical-align:middle;text-align:center"><a href="/queuequary?id=<%=slist.get(i).getServerId() %>">当前队列</a></td>
            <td style="vertical-align:middle;text-align:center"><a href="/historyquary?id=<%=slist.get(i).getServerId() %>">历史记录</a></td>
            <td style="vertical-align:middle;text-align:center"><a href="#myModal<%=slist.get(i).getServerId() %>" role="button" class="btn" data-toggle="modal">删除账号</a></td>
            <div id="myModal<%=slist.get(i).getServerId() %>" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">删除提示</h3>
              </div>
              <div class="modal-body">
                <p>是否删除账号<%=slist.get(i).getMailAddress() %>？</p>
              </div>
              <div class="modal-footer">
                <form action="/mailproxy/deleteaccount" method="post">
                  <input name="id" value="<%=slist.get(i).getServerId() %>" type="hidden">
                  <button type="submit" class="btn btn-danger">&nbsp;&nbsp;删除&nbsp;&nbsp;</button>
                  <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                </form>
              </div>
            </div>
          </tr>
          <%
            }
          %>


        </table>
      </div>

    </div><!--/row-->


  </div><!--/.fluid-container-->
</div>
<%@ include file="inc/footer.jsp"%>
  <!-- Le javascript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://cdn.bootcss.com/bootstrap/2.3.2/js/bootstrap.min.js"></script>
  <script>
    document.getElementById('account').className = 'active';
  </script>
</body>
</html>
