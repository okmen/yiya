<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.ResourceBundle"%>
<%
    String mydomain = ResourceBundle.getBundle("domain").getString("mydomain");
    String sellerdomain = ResourceBundle.getBundle("domain").getString("sellerdomain");
    String okweidomain = ResourceBundle.getBundle("domain").getString("okweidomain");
    String joindomain = ResourceBundle.getBundle("domain").getString("joindomain");
    String setdomain = ResourceBundle.getBundle("domain").getString("setdomain");
%>
<style>
.mzh_gwqf_an {
	position: absolute;
	right: -20px;
	top: 3px;
	background: #fe4483;
	font-size: 12px;
	padding: 0px 7px;
	color: #fff;
	text-indent: 0px;
	line-height: 24px;
}
</style>
<div id="leftMenu" class="fl conter_left">
	<div class="p10">
		<div name="menu_top">
			<h2>
				<img class="ico_5" src="/statics/pic/img_lefticom001.png"><span name="node">订单管理</span><i></i>
			</h2>
			<ul>
				<li name="leaf_node" data="/order/reservelist"><a href="/order/buylist">我的购买订单</a></li>
				<c:if test="${userinfo.yunS ==1 || userinfo.batchS ==1 || userinfo.pth ==1 || userinfo.pph ==1 || userinfo.pthdls ==1 || userinfo.pthldd ==1 || userinfo.brandsupplyer == 1 || userinfo.deputyduke==1 || userinfo.brandagent==1 || userinfo.duke==1 || userinfo.brandcaptain==1 }">
					<li name="leaf_node" data="/seller/reservelist"><a href='/seller/buylist'>我的销售订单</a></li>
				</c:if>
				<li name="leaf_node"><a href='/sale/saleOrder'>我的分销订单</a></li>
			</ul>
		</div>
		<div name="menu_top">
			<h2>
				<img class="ico_6" src="/statics/pic/img_lefticom002.png"><span name="node">产品管理</span><i></i>
			</h2>
			<ul>
				<li name="leaf_node" data="myproductpage"><a href='/myProduct/list/Showing/0/0?isClick=1'>我的产品</a></li>
				<c:if test="${userinfo.yunS == 1 || userinfo.batchS == 1 || userinfo.pth ==1|| userinfo.pph ==1 ||userinfo.brandsupplyer == 1 }">
				<li name="leaf_node"><a href='/publishProduct/index'>发布产品</a></li>
				</c:if>
			</ul>
		</div>
		<c:if test="${userinfo.pth ==1 || userinfo.pph ==1 }">
			<div name="menu_top">
				<h2>
					<img class="ico_6" src="/statics/pic/img_lefticom002.png"><span name="node">招商需求</span><i></i>
				</h2>
				<ul>
					<li name="leaf_node"><a href='/demand/myDemandList'>我的招商需求</a></li>
					<li name="leaf_node"><a href='/demand/releasedemand'>发布招商需求</a></li>
				</ul>
			</div>
		</c:if>
		 <c:if test="${userinfo.yrz ==1 || userinfo.brz ==1 ||userinfo.yHrz ==1 || userinfo.bHrz ==1 || userinfo.rzFwd==1 }">
		<div name="menu_top">
			<h2>
				<img class="ico_6" src="/statics/pic/img_lefticom002.png"><span
					name="node">客户开发</span><i></i>
			</h2>
			<ul>
				<li name="leaf_node" ><a href='/myCustomerMgt/agentPage'>我发展的代理商</a></li>
				<li name="leaf_node"><a href='/myCustomerMgt/inloadPage'>我发展的落地店</a></li>
				<li name="leaf_node" ><a href='/demand/fondDemand'>发现招商需求</a></li>
			</ul>
		</div>
		</c:if> 
		<div name="menu_top">
			<h2>
				<img class="ico_3" src="/statics/pic/img_lefticom003.png"><span name="node">团队管理</span><i></i>
			</h2>
			<ul>				
				<!--<c:if test="${userinfo.brandsupplyer==1 }">
					<li name="leaf_node" data="/teamMgt/dukeList"><a href='/teamMgt/dukeList'>城主管理</a></li>
				</c:if>
				<c:if test="${userinfo.brandsupplyer==1 ||userinfo.duke==1  }">
					<li name="leaf_node" data="/teamMgt/deputyList"><a href='/teamMgt/deputyList'>城主候选人</a></li>
				</c:if>-->
				<c:if test="${userinfo.brandsupplyer == 1 || userinfo.deputyduke==1 || userinfo.brandagent==1 || userinfo.duke==1 || userinfo.brandcaptain==1  }">
					<li name="leaf_node" data="/teamMgt/agentList"><a href='/teamMgt/agentList'>代理商管理</a></li>
				</c:if>
				<li name="leaf_node" data="/teamMgt/myAgentList"><a href='/teamMgt/myAgentList'>我代理的品牌</a></li>
			</ul>
		</div>
		
		<div name="menu_top">
			<h2>
				<img class="ico_3" src="/statics/pic/img_lefticom003.png"><span name="node">渠道管理</span><i></i>
			</h2>
			<ul>
				<c:if test="${userinfo.weiID==8888 }">
				<li name="leaf_node"><a href='/relationMgt/fans'>铁杆朋友圈</a></li>
				</c:if>
				<li name="leaf_node"><a href='/relationMgt/upstream'>我的上游供应</a></li>
				<li name="leaf_node" data="downstreampage"><a href='/relationMgt/downstream/all'>我的下游分销</a></li>
				<c:if test="${userinfo.pph==1 ||userinfo.pth==1  }">
					<li name="leaf_node" data="/agent/agentDetails"><a href='/agent/agentIndex'>我的代理商</a></li>
				</c:if>
				<c:if test="${userinfo.pph==1 ||userinfo.pth==1  }">
					<li name="leaf_node" data="/productShop/getDownstreamStoreList/1"><a href='/productShop/getDownstreamStoreList/1'>我的落地店</a></li>
				</c:if>
				<c:if test="${userinfo.pph==1 ||userinfo.pth==1  }">
					<li name="leaf_node" data="/childrenAccount/getStaffChildrenAccountList"><a href='/childrenAccount/getStaffChildrenAccountList'>子账号管理</a></li>
				</c:if>
				<c:if test="${userinfo.pth==1  }">
					<li name="leaf_node" data="/childrenAccount/getSelfSCList"><a href='/childrenAccount/getSelfSCList'>平台子供应商 </a></li>
				</c:if>
				<c:if test="${userinfo.yrz==1 || userinfo.brz==1|| userinfo.yHrz==1|| userinfo.bHrz==1}">
					<li name="leaf_node" data="/childrenAccount/getRecommandSCList"><a href='/childrenAccount/getRecommandSCList'>推荐的供应商 </a></li>
				</c:if>
			</ul>
		</div>
		<div name="menu_top">
			<h2>
				<img class="ico_3" src="/statics/pic/img_lefticom004.png"><span name="node">店铺管理</span><i></i>
			</h2>
			<ul>

				<li name="leaf_node"><a href='/myShopMgt/homepage'>店铺轮播图</a></li>
				<li name="leaf_node"><a href='/shopClass/classList'>店铺分类设置</a></li>
				<c:if test="${userinfo.yunS ==1 || userinfo.batchS ==1 || userinfo.pth ==1|| userinfo.pph ==1 }">
					<li name="leaf_node"><a href='/userinfo/serviceQQ'>客服QQ设置</a></li>
				</c:if>
				<c:if test="${userinfo.yunS ==1 || userinfo.batchS ==1 || userinfo.pth ==1|| userinfo.pph ==1||userinfo.pthdls==1||userinfo.pthldd==1 || userinfo.brandsupplyer == 1}">
					<li name="leaf_node" data="/freight/addorupd"><a href='/freight/freightList'>运费模板设置</a></li>
				</c:if>
				<li name="leaf_node"><a href='<%=setdomain%>/userInfo/index'>基本信息设置</a></li>
			</ul>
		</div>
		<div name="menu_top">
			<h2>
				<img class="ico_3" src="/statics/pic/img_lefticom005.png"><span name="node">供应商</span><i></i>
			</h2>
			<ul>
				<c:if test="${userinfo.yunS == 1 || userinfo.batchS == 1 || userinfo.pth ==1|| userinfo.pph ==1}">
					<li name="leaf_node"><a href='/brand/brandlist'>品牌认证</a></li>
				</c:if>
				<c:if test="${userinfo.pph!=1&&userinfo.pth!=1  }">
				<li name="leaf_node"><a href='<%=joindomain%>/supplier/apply?w=${userinfo.weiID }'>供应商进驻</a></li>
				</c:if>
			</ul>
		</div>
		<c:if test="${!(userinfo.brandsupplyer == 1 || userinfo.deputyduke==1 || userinfo.brandagent==1 || userinfo.duke==1 || userinfo.brandcaptain==1)  }">
		<div name="menu_top">
			<h2>
				<img class="ico_3" src="/statics/pic/img_lefticom005.png"><span name="node">分享专题</span><i></i>
			</h2>
			<ul>
				<li name="leaf_node" data="/share/sharelist"><a href='/share/sharelist'>分享页列表</a></li>
				<li name="leaf_node" data="/share/sharecount"><a href='/share/sharecount'>分享页统计</a></li>
			</ul>
		</div>
		</c:if>
		<div name="menu_top">
			<c:if test="${userinfo.yunS == 1 || userinfo.batchS == 1 || userinfo.pth ==1|| userinfo.pph ==1}">
			<h2>
				<img class="ico3" src="/statics/pic/img_lefticom005.png"><span name="node">活动专题</span><i></i>
			</h2>
			<ul>
				<li name="leaf_node" style="position: relative;" data="/act/actlist"><a href="/act/actlist">限时抢购</a><span class="mzh_gwqf_an">购物全返</span></li>
				
			</ul>
			</c:if>
		</div>
	</div>
</div>

<input type="hidden" id="ceshi" value="${userinfo.pph }">