package com.bbyiya.pic.web.notify;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.service.pic.IBaseOrderMgtService;

@Controller
@RequestMapping(value = "/wxpay")
public class WxNotifyController {
	@Resource(name = "baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderMgtService;

	/**
	 * 微信支付回写地址
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/WxPayNotify", method = { RequestMethod.POST, RequestMethod.GET })
	public String wxpayNotify(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		
		return "";
	}
}
