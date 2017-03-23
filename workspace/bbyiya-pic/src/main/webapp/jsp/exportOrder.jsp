<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="JavaScript" src="../static/js/jquery-3.1.1.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
$(function(){ 
    $("#exportOrder").click(function(){ 
    	 $.ajax({ 
             url: "/pbs/order/orderExportExcel",
             type: 'post',
             data: {orderNo:'2017032100341608901'},
             success: function(data){ 
                window.location.href = "/pbs/order/download?path=" + encodeURI(data.BaseModle) ; //执行下载操作
                 
             },
             error: function(data,err){ 
            	 alert("请求出错处理...");
             }
         });     	
    });
    
});

</script>
</head>
<body>
<a href="javascript:void(0)" id="exportOrder"/>导出excel</a>
</body>
</html>