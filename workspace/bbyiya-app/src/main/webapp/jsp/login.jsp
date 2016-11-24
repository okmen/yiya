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
	cc:	${isok }
	<div class="mzh_dljm_right_dl">
		<ul>
			<li class="mzh_dljm_left_1" style="margin-bottom: 27px; display: inline;">微店账号登录</li>
			<li id="litip" style="color: #f10; margin: -10px 0 5px; display: none;">
				<img src="/statics/images/m_jingzhi.png">&nbsp;&nbsp;<span>请输入正确的微店号或手机号</span>
			</li>
			<li><input class="mzh_dljm_right_dl_2" id="txtUserno" type="text" placeholder="微店号/手机" style="color: rgb(153, 153, 153);"></li>
			<li><input class="mzh_dljm_right_dl_2" id="txtPwd" type="password" placeholder="密码" style="color: rgb(153, 153, 153);"></li>
			<li><span id="btnLogin" class="mzh_dljm_right_dl_3">登&nbsp;&nbsp;&nbsp;录</span></li>
			<li><a href="register"><span id="btnreg" class="mzh_dljm_right_dl_4" style="margin-bottom: 80px; display: inline;">注&nbsp;&nbsp;&nbsp;册</span></a></li>
		</ul>
	</div>
	<jsp:include page="/usercontrol/footer.jsp" flush="true">
		<jsp:param name="step" value="1" />
	</jsp:include>
</body>
<script type="text/javascript">
	$(function() {
		$("#btnLogin").click(function() {
			var userno = $("#txtUserno").val();
			var pwd = $("#txtPwd").val();
			$.ajax({
				type : 'POST',
				url : "/login/loginAjax",
				data : {
					userno : userno,
					pwd : pwd
				},
				success : function(datas) {
					if (datas.Statu == "Success") {
						var ticket = datas.BaseModle.ticket;
						setCookie("ticket", ticket, 3600);
						window.location.href = "/user/userinfo";
					} else {
						alert(datas.StatusReson);
					}
				},
				error : function() {
					alert("系统错误");
				}
			});
		})
	})
</script>
</html>