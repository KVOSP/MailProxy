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
        <form name="addaccount" action="addaccount" method="post" >
          <fieldset>
            <label>smtp地址</label>
            <input type="text" name="smtpHost" placeholder="smtp.example.com">
            <label>邮箱地址</label>
            <input type="text" name="mailAdress" placeholder="example@joyport.com">
            <label>密码</label>
            <input type="password" name="accountPass" placeholder="6—20位，不可包含特殊字符">
            <label>账号说明</label>
            <textarea name="text"></textarea>
            <label></label>
            <button type="submit" class="btn btn-primary">添加</button>
          </fieldset>
        </form>
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
  document.getElementById("addaccount").className="active";
</script>
</body>
</html>
