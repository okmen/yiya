package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.enums.WechatMsgEnums;
import com.bbyiya.common.vo.WechatMsgVo;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.pic.service.IPic_PayMgtService;
import com.bbyiya.pic.utils.WxPublicUtils;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.WechatMsgUtil;
import com.bbyiya.utils.pay.WxPaySubUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/tipay")
public class TiPayController extends SSOController {
	@Resource(name = "pic_payMgtService")
	private IPic_PayMgtService payService;

	@Autowired
	private UOtherloginMapper otherMapper;
	
	
	/**
	 * P01 获取微信支付参数
	 * 
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
	@RequestMapping(value = "/getWxPayParam")
	public String getWxPayParam(String payId) throws MapperException {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			UOtherlogin otherlogin = otherMapper.getWxloginByUserId(user.getUserId());
			if (otherlogin != null) {
				String ipAddres = super.getIpStr();
				rq = payService.getSubWxPayParam(payId, otherlogin.getOpenid(), ipAddres);
			} else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("微信未登录");
			}

		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
