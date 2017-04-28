package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorder;
import com.bbyiya.pic.service.IPic_PayMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.pay.WxAppPayUtils;
import com.bbyiya.utils.pay.WxPayParam;
import com.bbyiya.utils.pay.WxPayUtils;
import com.bbyiya.vo.ReturnModel;

@Service("pic_payMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_PayMgtServiceImpl implements IPic_PayMgtService {
	@Autowired
	private OPayorderMapper payMapper;

	/**
	 * 微信公众号支付 获取支付参数
	 * @param orderNo 支付id
	 * @param openid
	 * @param ip
	 * @return
	 */
	public ReturnModel getWxPayParam(String orderNo, String openid, String ip) {
		ReturnModel rq = new ReturnModel();
		if (ObjectUtil.isEmpty(orderNo) || ObjectUtil.isEmpty(ip)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误");
			return rq;
		}
		try {
			String prepay_id = "";
			double totalPrice = 0d;
			OPayorder payorder = payMapper.selectByPrimaryKey(orderNo);
			if (payorder != null) {
				totalPrice = payorder.getTotalprice();
				if (!ObjectUtil.isEmpty(payorder.getPrepayid())) {
					prepay_id = payorder.getPrepayid();
				}
			}
			// 随机字符串
			String nonceStr = WxPayUtils.genNonceStr();
			if (ObjectUtil.isEmpty(prepay_id)) {
				Map<String, Object> map = WxPayUtils.doInBackground(ip, openid, totalPrice, orderNo, nonceStr);
				if (map != null) {
					if (map.get("return_code").equals("SUCCESS")) {
						Object prepayID = map.get("prepay_id");
						if (prepayID != null) {
							prepay_id = map.get("prepay_id").toString();
							payorder.setPrepayid(prepay_id);
							payorder.setPrepaytime(new Date());
							payMapper.updateByPrimaryKey(payorder);
						} else {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson(map.get("err_code_des").toString());
							return rq;
						}
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson(JsonUtil.objectToJsonStr(map));
						return rq;
					}
				} else {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("系统错误");
					return rq;
				}
			}
			String timeStamp = String.valueOf(WxPayUtils.genTimeStamp());
			Map<String, String> resultMap = WxPayUtils.get_payParam(prepay_id, nonceStr, timeStamp);
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(JsonUtil.objectToJsonStr(resultMap));
		} catch (Exception e) {
			// TODO: handle exception
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(e.getMessage());
		}
		return rq;
	}
	
	
	public ReturnModel getWxAppPayParam(String orderNo, String ip) {
		ReturnModel rq = new ReturnModel();
		if (ObjectUtil.isEmpty(orderNo) || ObjectUtil.isEmpty(ip)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误");
			return rq;
		}
		try {
			String prepay_id = "";
			double totalPrice = 0d;
			OPayorder payorder = payMapper.selectByPrimaryKey(orderNo);
			if (payorder != null) {
				totalPrice = payorder.getTotalprice();
				if (!ObjectUtil.isEmpty(payorder.getPrepayid())) {
					prepay_id = payorder.getPrepayid();
				}
			}
			// 随机字符串
			String nonceStr = WxAppPayUtils.genNonceStr();
			if (ObjectUtil.isEmpty(prepay_id)) {
				Map<String, Object> map = WxAppPayUtils.doInBackground(ip, totalPrice, orderNo, nonceStr);
				if (map != null) {
					if (map.get("return_code").equals("SUCCESS")) {
						Object prepayID = map.get("prepay_id");
						if (prepayID != null) {
							prepay_id = map.get("prepay_id").toString();
							payorder.setPrepayid(prepay_id);
							payorder.setPrepaytime(new Date());
							payMapper.updateByPrimaryKey(payorder);
						} else {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson(map.get("err_code_des").toString());
							return rq;
						}
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson(JsonUtil.objectToJsonStr(map));
						return rq;
					}
				} else {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("系统错误");
					return rq;
				}
			}
			String timeStamp = String.valueOf(WxAppPayUtils.genTimeStamp());
			Map<String, String> resultMap = WxAppPayUtils.get_payParam(prepay_id, nonceStr, timeStamp);
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(JsonUtil.objectToJsonStr(resultMap));
		} catch (Exception e) {
			// TODO: handle exception
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(e.getMessage());
		}
		return rq;
	}

	public ReturnModel getWxCode_url(String orderNo, String ip){
		ReturnModel rq = new ReturnModel();
		if (ObjectUtil.isEmpty(orderNo) || ObjectUtil.isEmpty(ip)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误");
			return rq;
		}
		WxPayParam param=new WxPayParam();
		OPayorder payorder = payMapper.selectByPrimaryKey(orderNo);
		
		if (payorder != null) {
			param.setTotalprice(payorder.getTotalprice());
		}
		param.setOut_trade_no(orderNo);
		param.setSpbill_create_ip(ip);
		param.setProduct_id(orderNo); 
		param.setNonce_str(WxAppPayUtils.genNonceStr());
		param.setTrade_type("NATIVE"); 
		Map<String, Object> map = WxAppPayUtils.doInBackground_NATIVE(param);
		if (map != null) {
			if (map.get("return_code").equals("SUCCESS")) {
				Object code_url = map.get("code_url");
				String urlString="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+code_url;
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(urlString); 
			}
		}
		return rq;
	}
}
