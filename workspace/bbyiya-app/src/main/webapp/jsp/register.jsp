<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微店网-用户注册</title>
<!-- <script type="text/javascript" src="/statics/js/jquery-1.7.1.min.js"></script> -->
<script type="text/javascript" src="http://base1.okwei.com/js/jquery-1.7.1.min.js"></script>
</head>
<body>
	<div>
		<table>
		<tr>
			<td>用户名：<input class="mzh_dljm_right_dl_2" id="txtUsername" type="text" placeholder="用户名"></td>
		</tr>
		<tr>
			<td>手机号：<input class="mzh_dljm_right_dl_2" id="txtPhone" type="text" placeholder="手机"></td>
		</tr>
		<tr>
			<td>密码：<input class="mzh_dljm_right_dl_2" id="txtPassword" type="password" placeholder="密码"></td>
		</tr>
		<tr>
			<td><input class="mzh_dljm_right_dl_2" id="btnRegister" type="button" value="注册"></td>
		</tr>
		</table>
	</div>
</body>


<script type="text/javascript">
	$(function(){
		$("#btnRegister").click(function(){
			var username=$("#txtUsername").val();
			var pwd=$("#txtPassword").val();
			var phone=$("#txtPhone").val();
			$.ajax({
				type : 'POST',
				url : "/login/registerAjax",
				data : {
					username : username,pwd:pwd,phone:phone
				},
				success : function(datas) {
					if(datas.Statu=="Success"){
						alert("注册成功");
					}else{
						alert(datas.StatusReson);
					}
				},
				error:function(){
					alert("系统错误");
				}
			});
		})
	})
</script>
</html>