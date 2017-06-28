package com.bbyiya.pic.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Controller
@RequestMapping(value = "/order")
public class OrderExtController  extends SSOController {
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private UUsersMapper userMapper;
	@Resource(name="baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderservice;
	
	
	/**
	 * 获取最近支付的订单（选择相册款式 页 用，随机显示下单用户）
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getorder")
	public String getbuyorderinfo(@RequestParam(required = false, defaultValue = "10")int count) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			List<Map<String, Object>> list = findlist(count);
			rq.setBasemodle(list);
			rq.setStatu(ReturnStatus.Success);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * O04-1 支付-获取支付信息(支付页)
	 * 获取支付信息
	 * @param payId（userOrderId）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPayInfoNew")
	public String getPayInfo(String payId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderservice.getPayOrderInfo(user.getUserId(), payId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	public List<Map<String, Object>> findlist(int size){
		String key="orderlastlist_key_"+size;
		List<Map<String, Object>> list=(List<Map<String, Object>>)RedisUtil.getObject(key);
		if(list==null||list.size()<=0){
			list=new ArrayList<Map<String,Object>>();
			PageHelper.startPage(1, size);
			List<OUserorders> resultlist = orderMapper.findOrderListLasts(Integer.parseInt(OrderStatusEnum.payed.toString())); 
			PageInfo<OUserorders> resultPage=new PageInfo<OUserorders>(resultlist);
			if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
				for (OUserorders oo : resultPage.getList()) {
					if(oo.getUserid().longValue()!=this.getLoginUser().getUserId()){
						OOrderproducts oproduct= oproductMapper.getOProductsByOrderId(oo.getUserorderid());
						if(oproduct!=null){
							Map<String, Object> mm=new HashMap<String, Object>();
							PProductstyles style= styleMapper.selectByPrimaryKey(oproduct.getStyleid());
							mm.put("propertyStr", style.getPropertystr());
							UUsers users= userMapper.selectByPrimaryKey(oo.getUserid());
							List<PMyproductsinvites>inList= inviteMapper.findListByCartId(oproduct.getCartid());
							if(inList!=null&&inList.size()>0){
								if(ValidateUtils.isIdentity(users.getIdentity(), UserIdentityEnums.branch)||ValidateUtils.isIdentity(users.getIdentity(), UserIdentityEnums.salesman)){
									UUsers inUsers=null;
									if(inList.get(0).getInviteuserid()!=null&&inList.get(0).getInviteuserid()>0){
										inUsers=userMapper.selectByPrimaryKey(inList.get(0).getInviteuserid());
									}else if (!ObjectUtil.isEmpty(inList.get(0).getInvitephone())) {
										inUsers=userMapper.getUUsersByPhone(inList.get(0).getInvitephone());
									}
									mm.put("nickname", inUsers==null?"":inUsers.getNickname());
								}
							}else {
								mm.put("nickname", users==null?"":users.getNickname());
							}
							list.add(mm);
						}	
					}
				}
			}
			RedisUtil.setObject(key, list, 300); 
		}
		return list;
	}
}
