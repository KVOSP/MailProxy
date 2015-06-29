<%@ page import="model.*" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <%@ include file="inc/head.jsp" %>
  <link href="../styles/css/hero.css" rel="stylesheet">
</head>

<body>

<%@ include file="inc/navbar.jsp" %>

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3" style="width: 200px;">
      <%@ include file="inc/nav.jsp" %>
    </div><!--/span-->
    <div class="span9">
      <div class="hero-unit">
        <%
          String mid = request.getParameter("id");
          Map mail = new Mail_db().getMailById(Integer.parseInt(mid));
        %>
        <label>收件人</label>
        <input type="text" readonly="readonly" value="<%=mail.get("Recipient") %>" class="input-block-level">
        <%
          if(mail.get("Cc")!=null&&mail.get("Cc")!=""){
        %>
        <label>抄送</label>
        <input type="text" readonly="readonly" value="<%=mail.get("Cc") %>" class="input-block-level">
        <%
          }if(mail.get("Bcc")!=null&&mail.get("Bcc")!=""){
        %>
        <label>密送</label>
        <input type="text" readonly="readonly" value="<%=mail.get("Bcc") %>" class="input-block-level">
        <%
          }
        %>
        <label>主题</label>
        <input type="text" readonly="readonly" value="<%=mail.get("Subject") %>" class="input-block-level">
        <label>正文</label>
        <textarea readonly="readonly" class="input-block-level"><%=mail.get("Content") %></textarea>
        <label>附件</label>
        <a href="<%=mail.get("Url") %>"><%=mail.get("Url") %></a>
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
    <%
      if(request.getParameter("isSend").equals("N")){
    %>
    document.getElementById("queue").className="active";
    <%
      }if(request.getParameter("isSend").equals("Y")){
    %>
    document.getElementById("history").className="active";
    <%
      }
    %>
  </script>
</body>
</html>
