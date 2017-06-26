package com.bbyiya.pic.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstyleexpMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.DMyproductdiscountmodel;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyleexp;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.service.pic.IBaseDiscountService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.PProductStyleResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/order")
public class OrderProductController extends SSOController {
	/**
	 * �Ż���Ϣ
	 */
	@Resource(name = "baseDiscountServiceImpl")
	private IBaseDiscountService discountService;
	@Autowired
	private PProductstylesMapper styleMapper;
	
	@Autowired
	private PProductstyleexpMapper styleExpMapper;
	@Autowired
	private PProductsMapper productMapper;
	@Autowired
	private PMyproductsMapper mycartMapper;
	@Autowired
	private PMyproductdetailsMapper detailMapper;
	
	/**
	 * ����ʽѡ��
	 * @param productId
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/styleSel")
	public String stylelist(@RequestParam(required = false, defaultValue = "0") long productId,@RequestParam(required = false, defaultValue = "0")long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			List<PProductStyleResult> stylelist = styleMapper.findStylesResultByProductId(productId);
			if (stylelist != null && stylelist.size() > 0) {
				List<DMyproductdiscountmodel> disList = discountService.findMycartDiscount(user.getUserId(), cartId);
				for (PProductStyleResult style : stylelist) {
					PProductstyleexp exp= styleExpMapper.selectByPrimaryKey(style.getStyleId());
					if(exp!=null){
						style.setSellCount(exp.getSalecount()); 
					}
					if (disList != null && disList.size() > 0) {
						for (DMyproductdiscountmodel dis : disList) {
							if (style.getStyleId().longValue() == dis.getStyleid().longValue()) {
								style.setDiscountAmount(dis.getAmount());
							}
						}
					}
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(stylelist); 
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("ò���Ҳ�����Ӧ������ʽ��"); 
			} 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ������Ʒ���飨�µ�ҳ��
	 * @param styleId
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/orderProInfo")
	public String orderProInfo(@RequestParam(required = false, defaultValue = "0") long styleId,long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			PProductstyles style = styleMapper.selectByPrimaryKey(styleId);
			PMyproducts mycart= mycartMapper.selectByPrimaryKey(cartId);
//			List<PMyproductdetails> details= detailMapper.findMyProductdetails(cartId);
			if (style != null) {
				List<DMyproductdiscountmodel> disList = discountService.findMycartDiscount(user.getUserId(), cartId);
				PProducts product = productMapper.selectByPrimaryKey(style.getProductid());
				if (product != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title", product.getTitle());
					map.put("branchUserId", product.getUserid());
					map.put("defaultImg", style.getDefaultimg());
					map.put("price", style.getPrice());
					map.put("styleId", styleId);
					map.put("productId", style.getProductid());
					map.put("propertystr", style.getPropertystr());
					map.put("mycartTitle", mycart.getTitle());
					long temp = styleId % 2; // ��ȡ�Ƿ��Ǻ������
					map.put("type", (int) temp);
					if(disList!=null&&disList.size()>0){
						for (DMyproductdiscountmodel dis : disList) {
							if(dis.getStyleid().longValue()==styleId){
								map.put("discountAmount", dis.getAmount());
							}
						}
					}
					rq.setBasemodle(map);
					rq.setStatu(ReturnStatus.Success);
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
