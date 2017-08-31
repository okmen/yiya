package com.bbyiya.pic.web.calendar;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiDiscountdetailsMapper;
import com.bbyiya.dao.TiProductcommentsMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductsextMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.TiDiscountdetails;
import com.bbyiya.model.TiProductcomments;
import com.bbyiya.model.TiProductsext;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.model.UUsers;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiProductResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/ti_product")
public class Ti_ProductController  extends SSOController {
	@Autowired
	private TiProductsMapper productsMapper;
	@Autowired
	private TiUserdiscountsMapper mydiscountMapper;
	@Autowired
	private TiDiscountdetailsMapper discountDetailsMapper;
	@Autowired
	private TiProductsextMapper proExtMapper;
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiProductcommentsMapper commentMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	
	/**
	 * 挂历列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/prolist")
	public String transferPage() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			List<TiProductResult> proList=productsMapper.findProductResultlist();
			List<TiUserdiscounts> discounts= mydiscountMapper.findMyDiscounts(user.getUserId());
			if(discounts!=null&&discounts.size()>0){
				//具体优惠信息
				List<TiDiscountdetails> dislist= discountDetailsMapper.findDiscountlist(discounts.get(0).getDiscountid());
				if(dislist!=null&&dislist.size()>0){
					if(proList!=null&&proList.size()>0){
						for (TiProductResult pro : proList) {
							//目前默认为折扣
							pro.setDiscountType(1); 
							for (TiDiscountdetails dis : dislist) {
								//商品表productId为默认款式styleId
								if(pro.getProductid().longValue()==dis.getStyleid().longValue()){
									pro.setDiscount(dis.getDiscount());
								}
							}
						}
					}
				}
			}
			rq.setBasemodle(proList); 
			rq.setStatu(ReturnStatus.Success);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 产品详情 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/productInfo")
	public String productInfo(long productId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductResult product=productsMapper.getResultByProductId(productId);
			if(product!=null){
				List<TiProductstyles> styleList=styleMapper.findStylelistByProductId(productId);
				product.setStylelist(styleList);  
				//产品销量
				TiProductsext ext= proExtMapper.selectByPrimaryKey(productId);
				if(ext!=null){
					product.setSaleCount(ext.getSales()==null?0:ext.getSales().intValue());
					product.setCommentsCount(ext.getCommentcount()==null?0:ext.getCommentcount().intValue());
				}
			}
			rq.setBasemodle(product); 
			rq.setStatu(ReturnStatus.Success);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 产品评论列表
	 * @param productId
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/proComments")
	public String proComments(long productId,int index ,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PageHelper.startPage(index, size);
			List<TiProductcomments> commentlist=commentMapper.findListByProductId(productId);
			PageInfo<TiProductcomments> resultPage=new PageInfo<TiProductcomments>(commentlist);
			rq.setBasemodle(resultPage); 
			rq.setStatu(ReturnStatus.Success);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 新增评论
	 * @param productId
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addComments")
	public String addComments(String userOrderId,String content,String imgJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(!(ObjectUtil.isEmpty(userOrderId)||ObjectUtil.isEmpty(content))){
			  try {
					List<OOrderproducts> oproducts = oproductMapper.findOProductsByOrderId(userOrderId);
					if (oproducts != null) {
						TiProductcomments model = new TiProductcomments();
						model.setContent(content);
						model.setCreatetime(new Date());
						model.setHeadimg(user.getHeadImg());
						model.setOrderproductid(oproducts.get(0).getOrderproductid()); 
						model.setProductid(oproducts.get(0).getProductid());
						model.setStyleid(oproducts.get(0).getStyleid());
						model.setUserid(user.getUserId());
						if (!ObjectUtil.isEmpty(imgJson)) {
							model.setImgjson(imgJson);
						}
						commentMapper.insert(model);
					}
				} catch (Exception e) {
					
				}
			}
			rq.setStatu(ReturnStatus.Success);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
