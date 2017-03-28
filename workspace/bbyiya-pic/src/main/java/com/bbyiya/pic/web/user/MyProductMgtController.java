package com.bbyiya.pic.web.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user/myProduct")
public class MyProductMgtController extends SSOController {
	@Resource(name = "pic_productService")
	private IPic_ProductService proService;
	
	
}
