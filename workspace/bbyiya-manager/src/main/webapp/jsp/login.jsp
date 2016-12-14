<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>咿呀-用户登陆</title>
<script type="text/javascript" src="http://base1.okwei.com/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/statics/js/cookieHelper.js"></script>
<link rel="stylesheet" type="text/css" href="/statics/css/base.css" />
</head>
<body>
	
	<div class="mzh_dljm_right_dl">
		<ul>
			<li class="mzh_dljm_left_1" style="margin-bottom: 27px; display: inline;">用户登录</li>
			<li><input class="mzh_dljm_right_dl_2" id="txtUsername" type="text" placeholder="用户名" style="color: rgb(153, 153, 153);"></li>
			<li><input class="mzh_dljm_right_dl_2" id="txtPwd" type="password" placeholder="密码" style="color: rgb(153, 153, 153);"></li>
			<li><span id="btnLogin" class="mzh_dljm_right_dl_3">登&nbsp;&nbsp;&nbsp;录</span></li>
			<li>${msg }</li>
		</ul>
	</div>
	<jsp:include page="/usercontrol/footer.jsp" flush="true">
		<jsp:param name="step" value="1" />
	</jsp:include>
</body>
<script type="text/javascript">
	$(function() {
		$("#btnLogin").click(function() {
			var userno = $("#txtUsername").val();
			var pwd = $("#txtPwd").val();
			$.ajax({
				type : 'POST',
				url : "loginAjax",
				data : {
					username : userno,
					pwd : pwd
				},
				success : function(datas) {
					window.location.href = "loginsuccess";
				},
				error : function() {
					alert("系统错误");
				}
			});
		})
	})
</script>
</html>