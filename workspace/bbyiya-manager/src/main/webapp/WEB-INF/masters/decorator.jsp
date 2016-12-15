<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><sitemesh:write property='title'/></title>
		<jsp:include page="/jsp/common/scriptstyle.jsp" />
		<sitemesh:write property='head'/>
	</head>
	<body style="background: #f3f3f3;">
		<div id="header">
			<jsp:include page="/jsp/common/header.jsp"/>
		</div>
		<div id="center" class="content mar_au">
			<jsp:include page="/jsp/common/breadcrumb.jsp"/>
			<jsp:include page="/jsp/common/left.jsp"/>
			<div id="navTab" class="fr conter_right">
				<sitemesh:write property='body' />
			</div>
		</div>
		<!-- 底部 -->
		<div id="footer">
			<jsp:include page="/jsp/common/footer.jsp" />
		</div>
	</body>
</html>
