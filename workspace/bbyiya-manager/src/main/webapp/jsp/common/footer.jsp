<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
String walletdomain = ResourceBundle.getBundle("domain").getString(
		"joindomain");
%>
<iframe src="<%=walletdomain %>/jsp/home/footers.html?w=${userinfo.weiID }" iframeborder="0" scrolling="no" style="width:100%;height:366px;display: inline-block;float:left;border:0px;"></iframe>
<%-- 
<!-- 底部 -->
<div class="blank"></div>
<div class="bottom_di">
	<div class="wdwrap">
		<div class="bottom-text">
			<div class="weix_jyi fl">
				<ul>
					<li class="icon_one"><a href="javascript:void(0);">微信支付</a></li>
					<li class="icon_two"><a href="http://www.okwei.com/jy.html#qt">七天包退</a></li>
					<li class="icon_thr"><a href="http://www.okwei.com/jy.html#db">担保交易</a></li>
				</ul>
			</div>
			<dl style="margin-left: 82px;">
				<dt>关于微店网</dt>
				<dd>
					<a href="http://www.${userinfo.weiID }.okwei.com/us.html#wh"
						target="_blank">企业文化</a>
				</dd>
				<dd>
					<a href="http://www.${userinfo.weiID }.okwei.com/us.html#us"
						target="_blank">联系我们</a>
				</dd>
				<dd class="bg_blue">
					<a href="http://join.okwei.com/supplier/apply"
						target="_blank">供应商进驻</a>
				</dd>
			</dl>
			<dl style="margin-left: 82px;">
				<dt>微店网客服</dt>
				<dd>
					<p>4006-136-086</p>
					<span>周一至周五（9:00-18:00）</span>
					<a href="http://wpa.qq.com/msgrd?v=3&uin=2792985013&site=qq&menu=yes" target="_blank" ><div class="QQbtn">企业QQ留言</div></a>
				</dd>
			</dl>
			<dl style="margin-left: 82px;">
				<dt>微店网招商</dt>
				<dd>专线：0755-32971768</dd>
        		<dd>专线：0755-33100081</dd>
        		<dd>专线：0755-33275303</dd>
			</dl>
			<dl style="margin-left: 82px; float: left; display: inline;">
				<dt>手机微店网</dt>
				<dd>
					<img src="http://base.okwei.com/images/wd-wx1.png" width="80"
						height="80" alt="手机微店网" />
				</dd>
			</dl>
			<dl style="margin-left: 60px; float: left; display: inline;">
				<dt>微店网服务号</dt>
				<dd>
					<img src="http://base.okwei.com/images/wd-wx.png" width="80"
						height="80" alt="微店网服务号" />
				</dd>
			</dl>
			<dl style="float: right; display: inline;">
				<dt>微店网APP下载</dt>
				<dd>
					<img src="http://base2.okimgs.com/images/wd-wx2.png" width="80"
						height="80" alt="微店网服务号" />
				</dd>
			</dl>
		</div>
	</div>
	<div class="clear"></div>
</div>
<div class="foot fl">
	<div class="wdwrap">
		<ul class="foot-text">
			<li><span>Copyright2012深圳市云商微店网络技术有限公司</span><span>地址：深圳市罗湖区国威路68号互联网产业园2栋5层</span></li>
			<li><span>电话：0755-33100081</span><span>传真：0755-23910940</span><span>E-mail：<a
					href="mailto:kefu@okwei.com" title="E-mail">kefu@okwei.com</a></span></li>
			<li><span>经营许可证：粤ICP备<a
					href="http://www.okwei.com/aboutus.html#sz" title="经营许可证"
					target="_blank" style="text-decoration: underline;">10203293号-3</a></span>
				<span>增值电信业务经营许可证：<a
					href="http://www.okwei.com/aboutus.html#dxzzfw" title="增值电信业务经营许可证"
					target="_blank" style="text-decoration: underline;">粤B2-20130803</a>
			</span></li>
			<li><a href="http://www.okwei.com/lawyer.aspx" target="_blank"
				title="法律顾问">法律顾问：广东德盈律师事务所 欧雄灿</a></li>
		</ul>
		<p class="foot-pic">
			<a target="_blank" href="http://www.okwei.com/aboutus.html#sz"
				title="营业执照"><img alt="营业执照"
				src="http://base.okwei.com/images/foot1.png"></a> <a
				target="_blank" href="http://www.okwei.com/aboutus.html#sz"
				title="税务登记"><img alt="税务登记"
				src="http://base.okwei.com/images/foot2.png"></a> <a
				target="_blank" href="http://www.okwei.com/aboutus.html#kxrz"
				title="工商网监"><img alt="工商网监"
				src="http://base.okwei.com/images/foot3.png"></a> <a
				target="_blank" href="http://www.okwei.com/aboutus.html#kxrz"
				title="众信网络身份现场认证"><img alt="众信网络身份现场认证"
				src="http://base.okwei.com/images/foot4.png"></a> <a
				target="_blank" href="http://www.okwei.com/aboutus.html#kxrz"
				title="可信网站认证"><img alt="可信网站认证"
				src="http://base.okwei.com/images/foot5.png"></a> <a
				target="_blank" href="http://www.okwei.com/aboutus.html#dxzzfw"
				title="电信增值服务许可证"><img alt="电信增值服务许可证"
				src="http://base.okwei.com/images/foot6.png"></a> <a
				 href="javascript:void(0);" title="财付通特约商户"><img
				alt="财付通特约商户" src="http://base.okimgs.com/images/foot7-1.png"></a>
			<a  href="javascript:void(0);" title="银联特约商户"><img
				alt="银联特约商户" src="http://base2.okimgs.com/images/foot8-1.png"></a>
		</p>
	</div>
</div>
 --%>