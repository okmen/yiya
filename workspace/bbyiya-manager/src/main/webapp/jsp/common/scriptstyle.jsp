<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

	Date curDate = new Date();
	pageContext.setAttribute("VERSION", curDate.getTime(), PageContext.APPLICATION_SCOPE);
	pageContext.setAttribute("JSESSIONID", session.getId(), PageContext.SESSION_SCOPE);
%>
<script>
	window.basePath = "<%=basePath %>";
</script>
<title>个人后台首页</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- 前端css库 start-->
<link rel="stylesheet" type="text/css" href="/statics/css/glbdy.css?_=${VERSION}" />
<link rel="stylesheet" type="text/css" href="/statics/css/index.css?_=${VERSION}" /> 
<link rel="stylesheet" type="text/css" href="/statics/css/m_shouji.css?_=${VERSION}" /> 
<link rel="stylesheet" type="text/css" href="/statics/css/mzh_dd.css?_=${VERSION}" />
<link rel="stylesheet" type="text/css" href="/statics/css/mzh_dd_ddxq.css?_=${VERSION}" />
<link rel="stylesheet" type="text/css" href="/statics/css/xh_xq.css?_=${VERSION}" />
<link rel="stylesheet" type="text/css" href="/statics/css/pagination.css?_=${VERSION}" />
<link rel="stylesheet" type="text/css" href="/statics/js/lib/jquery-ui-1.11.4.custom/jquery-ui.css?_=${VERSION}" />
<!-- 前端css库 end-->

<script type="text/javascript" src="/statics/js/jquery-1.7.1.min.js?_=${VERSION}"></script>
<script type="text/javascript" src="/statics/js/common/spin.min.js?_=${VERSION}"></script>
<script type="text/javascript" src="/statics/js/common/extends_fn.js?_=${VERSION}"></script>
<script type="text/javascript" src="/statics/js/common/common.js?_=${VERSION}"></script>
<script type="text/javascript" src="/statics/js/common/pagination.js?_=${VERSION}"></script> 
<script type="text/javascript" src="/statics/js/layer/layer.min.js?_=${VERSION}"></script>
<script type="text/javascript">
function alert(msg,bool){
	if(bool){
		layer.msg(msg, 2, 1);//绿色的钩钩
	}else{
		layer.msg(msg, 2, 8);//不高兴的脸
	}	
}
</script>
<!-- 前端js库 end-->
