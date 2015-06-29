<%@ page import="model.*" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <%@ include file="inc/head.jsp" %>
</head>

<body>
<%@ include file="inc/navbar.jsp" %>

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3" style="width: 200px;">
      <%@ include file="inc/nav.jsp" %>
    </div><!--/span-->
    <div class="span9">
      <form class="form-inline" action="queuequary" method="post">
        <label class="inline-label">&nbsp;&nbsp;发件人&nbsp;</label>
        <select class="inline" name="mid">
          <option value="0">不限</option>
          <%
            List<MailServer> Accounts = (List<MailServer>)request.getAttribute("Accounts");
            for(int i = 0 ; i < Accounts.size() ; i++){
              if(request.getAttribute("cur_id")==Accounts.get(i).getServerId()){
          %>
          <option selected="selected" value="<%=Accounts.get(i).getServerId() %>"><%=Accounts.get(i).getMailAddress() %></option>
          <%
          }else{
          %>
          <option value="<%=Accounts.get(i).getServerId() %>"><%=Accounts.get(i).getMailAddress() %></option>
          <%
              }
            }
          %>
        </select>
        <label class="inline-label">&nbsp;&nbsp;收件人&nbsp;</label>
        <input type="text" class="input-large inline" name="recipient" value="<%=request.getAttribute("cur_recipient") %>">
        &nbsp;<button type="submit" class="btn">查询</button>
      </form>
      <div class="hero-unit">

        <table class="table table-bordered">
          <tr>
            <th style="vertical-align:middle;text-align:center">邮件ID</th>
            <th style="vertical-align:middle;text-align:center">发件人</th>
            <th style="vertical-align:middle;text-align:center">收件人</th>
            <th style="vertical-align:middle;text-align:center">状态</th>
            <th style="vertical-align:middle;text-align:center">时间</th>
              <th style="vertical-align:middle;text-align:center">主题</th>
            <th style="vertical-align:middle;text-align:center">操作</th>
          </tr>
          <%
            List<Map> mailList = (List<Map>)request.getAttribute("MailList");
            for(int i = 0 ; i < mailList.size() ; i++){
              Map mail = mailList.get(i);
          %>
          <tr>
            <td style="vertical-align:middle;text-align:center"><%=mail.get("MailId") %></td>
            <td style="vertical-align:middle;text-align:center"><%=new MailServer_db().getServer((Integer)mail.get("ServerId")).getMailAddress() %></td>
            <td style="vertical-align:middle;text-align:center"><%=mail.get("Recipient") %></td>
            <td style="vertical-align:middle;text-align:center">未发送</td>
            <td style="vertical-align:middle;text-align:center"><%=mail.get("Time") %></td>
            <td style="vertical-align:middle;text-align:center"><%=mail.get("Subject") %></td>
            <td style="vertical-align:middle;text-align:center"><a href="../View/MailDetail.jsp?id=<%=mail.get("MailId") %>&isSend=N">查看详情</a>&nbsp;<a href="../View/MailDetail.jsp?id=<%=mail.get("MailId") %>">重发</a></td>
          </tr>
          <%
            }
          %>
        </table>
      </div>

    </div><!--/row-->

    <%--<footer>--%>
    <%--<p>&copy; Company 2013</p>--%>
    <%--</footer>--%>

  </div><!--/.fluid-container-->
  </div>
  <!-- Le javascript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="../styles/js/bootstrap.js"></script>
  <script>
    document.getElementById("queue").className="active";
  </script>
</body>
</html>
