<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.ResourceBundle"%>
<%
    String mydomain = ResourceBundle.getBundle("domain").getString("mydomain");
	String sellerdomain = ResourceBundle.getBundle("domain").getString("sellerdomain");
	String walletdomain = ResourceBundle.getBundle("domain").getString("walletdomain");
	String setdomain = ResourceBundle.getBundle("domain").getString("setdomain");
	String okweidomain = ResourceBundle.getBundle("domain").getString("okweidomain");
	String verifydomain = ResourceBundle.getBundle("domain").getString("verifydomain");
%>

<script type="text/javascript">
	$(function() {
		//二维码推广
		$("[name=mzh_sjwd]").mouseover(function() {
			$(".div-posabues").show();
		}).mouseout(function() {
			$(".div-posabues").hide();
		});
	}); 
	//退出
	function ibsLogOut() {
		$.ajax({
			url : "/commons/outLogin",
			type : "get",
			async : false,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("服务器出现异常");
				window.location.href = "http://port.okwei.com/exit.aspx";
			},
			success : function(data) {
				clearCookie(); 
				window.location.href = "http://port.okwei.com/exit.aspx?back=http://www." + data + ".okwei.com";
			}
		});
	} 
	function clearCookie() {
		var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
		if (keys) {
			for (var i = keys.length; i--;)
				document.cookie = 'tiket=0; path=/;expires=' + new Date(0).toUTCString();
				document.cookie = 'tiket=0; domain=.okwei.com;path=/;expires=' + new Date(0).toUTCString(); 
		}
	}
	
	
	function addfavor() {
	    var url="http://${userinfo.weiID}.okwei.com";
	    var title="${userinfo.weiName }的微店";
	        var ua = navigator.userAgent.toLowerCase();
	        if(ua.indexOf("msie 8")>-1){
	            external.AddToFavoritesBar(url,title,'butao');  //IE8
	        }else{
	            try {
	                window.external.addFavorite(url, title);
	            } catch(e) {
	                try {
	                    window.sidebar.addPanel(title, url, "");  //firefox
	                } catch(e) {
	                    alert("加入收藏失败，请使用Ctrl+D进行添加");
	                }
	            }
	        }
	  
	    return false;
	}
</script>

<div class="head_top">
	<div class="head_marin">
		<div class="head_left fl">
			<a onclick="addfavor();" href="javascript:void(0);">收藏微店网</a>
		</div>
		<div class="head_right fr ft_sixc">
			<div class="fr po-re" name="mzh_sjwd">
				<a href="http://app.okwei.com" target="_blank" class="mzh_a">手机微店</a>
				<div class="div-posabues">
					<a href="http://app.okwei.com/" class="eweim-imgs fl"> <img src="http://base2.okimgs.com/images/xh-addtop010.png" class="fl w">
					</a>
				</div>
			</div>
			<a href="javascript:ibsLogOut();" class="mzh_a ba_no ml_10 fr">退出</a> <span class="ft_c6 fr lh_36">欢迎您，${userinfo.weiName }</span>
		</div>
	</div>
</div>
<div class="head_two">
	<div class="mar_au">
		<div class="head_logo fl">
			<a href="http://www.<%=okweidomain %>"><img src="http://base3.okimgs.com/images/xh_logo.gif"></a>
		</div>
		<div class="head_weid fl">
			<a target="_blank" href="http://www.<%=okweidomain %>/aboutus.html"><img src="http://base2.okimgs.com/images/xh_gaiban_cctv.png"></a>
			<h6 class="ft_8c hfont">微店概念发起者、领导者</h6>
		</div>
		<div class="fl navs">
			<ul class="fl">
				<li name="mainMenu" class="li_img"><a href="<%=mydomain%>/maininfo/maininfo">微店中心</a></li>
				<%-- <li name="mainMenu"><a href="<%=sellerdomain%>/order/buylist">供应管理</a></li> --%>
				<li name="mainMenu"><a href="<%=verifydomain%>/verify/index">认证管理 </a></li>
				<li name="mainMenu"><a href="<%=walletdomain%>/walletMgt/index">钱包</a></li>
				<li name="mainMenu"><a href="<%=setdomain%>/userInfo/index">个人设置</a></li>
			</ul>
			<div class="mail fl">
				<a href="<%=mydomain%>/maininfo/ing">信息（<span>0</span>）
				</a>
			</div>
		</div>
		<div class="fr bottom bg_89 bg_ico_one">
			<a href="http://www.${userinfo.weiID }.<%=okweidomain %>/yun/danpin.html">云端产品库</a>
		</div>
		<div class="fr bottom bg_75 bg_ico_two">
			<a href="http://www.${userinfo.weiID }.<%=okweidomain %>/gy.html">供应商进驻</a>
		</div>
	</div>
</div>
<!-- 默认选中导航菜单 -->
<script type="text/javascript">
	
</script>


