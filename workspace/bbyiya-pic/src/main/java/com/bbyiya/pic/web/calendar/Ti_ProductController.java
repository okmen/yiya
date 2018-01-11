package com.bbyiya.pic.web.calendar;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.TiDiscountdetailsMapper;
import com.bbyiya.dao.TiDiscountmodelMapper;
import com.bbyiya.dao.TiProductblessingsMapper;
import com.bbyiya.dao.TiProductcommentsMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductsextMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.TiDiscountdetails;
import com.bbyiya.model.TiDiscountmodel;
import com.bbyiya.model.TiProductcomments;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductsext;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiProductResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/ti_product")
public class Ti_ProductController  extends SSOController {
	/**
	 * 所有展示中的产品列表
	 */
	private static String KEY_CACHE_PRODUCTS="ti_productlistall_1124";
	/**
	 * 评论缓存KEY
	 */
	private static String KEY_BASE_CACHE_COMMENTS="ti_productCommets_base_1124";
	/**
	 * 缓存时间
	 */
	private static int CACHE_LONG=180;
	/**========================================================================*/
	
	@Autowired
	private TiProductsMapper productsMapper;
	@Autowired
	private TiUserdiscountsMapper mydiscountMapper;
	@Autowired
	private TiDiscountmodelMapper disModelMapper;
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
	public String prolist() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			//01 获取产品列表
			List<TiProductResult> proList=(List<TiProductResult>)RedisUtil.getObject(KEY_CACHE_PRODUCTS); 
			if(proList==null||proList.size()<=0){
				proList=productsMapper.findProductResultlist();
				RedisUtil.setObject(KEY_CACHE_PRODUCTS, proList, CACHE_LONG);
			}
			
			//我的优惠
			TiDiscountmodel disModel= getDiscountList(user.getUserId());
			if(disModel!=null&&disModel.getDetails()!=null) {
				for (TiProductResult pro : proList) {
					pro.setDiscountType(disModel.getType());
					pro.setDiscountName(disModel.getTitle()); 
					for (TiDiscountdetails dd : disModel.getDetails()) {
						// 商品表productId为默认款式styleId
						if (pro.getProductid().longValue() == dd.getProductid().longValue()) {
							pro.setDiscount(dd.getDiscount());
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
				//产品展示图集
				if(!ObjectUtil.isEmpty(product.getImgjson())){
					List<ImageInfo> imList= (List<ImageInfo>)JsonUtil.jsonToList(product.getImgjson());
					if(imList!=null&&imList.size()>0){
						product.setImglist(imList); 
					}
				}
				//产品描述图集
				if(!ObjectUtil.isEmpty(product.getDescriptionimgjson())){
					List<ImageInfo> imList= (List<ImageInfo>)JsonUtil.jsonToList(product.getDescriptionimgjson());
					if(imList!=null&&imList.size()>0){
						product.setDescriptionImglist(imList); 
					}
				}
				//产品的款式列表
				List<TiProductstyles> styleList=styleMapper.findStylelistByProductId(productId);
				product.setStylelist(styleList);  
				//产品销量
				TiProductsext ext= proExtMapper.selectByPrimaryKey(productId);
				if(ext!=null){
					product.setSaleCount(ext.getSales()==null?0:ext.getSales().intValue());
					product.setCommentsCount(ext.getCommentcount()==null?0:ext.getCommentcount().intValue());
				}
				//产品优惠信息
				TiDiscountmodel disModel= getDiscountList(user.getUserId());
				if(disModel!=null&&disModel.getDetails()!=null) {
					product.setDiscountType(disModel.getType());
					product.setDiscountName(disModel.getTitle());
					for (TiDiscountdetails dd : disModel.getDetails()) {
						// 商品表productId为默认款式styleId
						if (product.getProductid().longValue() == dd.getProductid().longValue()) {
							product.setDiscount(dd.getDiscount());
						}
					}
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
	 * 产品款式列表
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/productStyleList")
	public String productStyleList(long productId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			//产品的款式列表
			List<TiProductstyles> styleList=styleMapper.findStylelistByProductId(productId);
			rq.setBasemodle(styleList);
			rq.setStatu(ReturnStatus.Success); 
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@Autowired
	private TiProductblessingsMapper blessMapper;
	
	/**
	 * 产品的款式信息 （制作红包用）
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTiProductInfo")
	public String getTiProductInfo(long productId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			//产品的款式列表
			List<TiProductstyles> styleList=styleMapper.findStylelistByProductId(productId);
			Map<String, Object> mapResult=new HashMap<String, Object>();
			mapResult.put("stylelist", styleList);
			TiProducts product= productsMapper.selectByPrimaryKey(productId);
			if(product!=null){
				if(product.getCateid()!=null){
					if(product.getCateid()==5){
						mapResult.put("blessinglist", blessMapper.findAll());
						for (TiProductstyles ss : styleList) {
							if(ss.getStyleid().longValue()==2801){
								List<Map<String, String>> imglist=new ArrayList<Map<String,String>>();
								for(int i=1;i<=4;i++){
									Map<String, String> map=new HashMap<String, String>();
									map.put("frontImg", "http://document.bbyiya.com/editfrontStyle2801_2018_0105_"+i+".jpg");
									map.put("bgImg", "http://document.bbyiya.com/editbgStyle2801_2018_0105_"+i+".jpg");
									imglist.add(map);
								}
								ss.setBgImglist(imglist);
							}else if(ss.getStyleid().longValue()==2802){
								List<Map<String, String>> imglist=new ArrayList<Map<String,String>>();
								for(int i=1;i<=4;i++){
									Map<String, String> map=new HashMap<String, String>();
									map.put("frontImg", "http://document.bbyiya.com/editfrontStyle2802_2018_0105_"+i+".jpg");
									map.put("bgImg", "http://document.bbyiya.com/editbgStyle2802_2018_0105_"+i+".jpg");
									imglist.add(map);
								}
								ss.setBgImglist(imglist);
							}
						}
					}
				}
			}
			rq.setBasemodle(mapResult);
			rq.setStatu(ReturnStatus.Success); 
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 款式详情-下单页用
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/orderStyleInfo")
	public String orderStyleInfo(long styleId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			Map<String, Object> map = new HashMap<String, Object>();
			TiProductstyles style= styleMapper.selectByPrimaryKey(styleId);
			if(style!=null){
				TiProducts product=productsMapper.selectByPrimaryKey(style.getProductid());
				if(product!=null) {
					map.put("title", product.getTitle());
					map.put("defaultImg", style.getDefaultimg());
					map.put("price", style.getPrice());
					map.put("styleId", styleId);
					map.put("productId", style.getProductid());
					map.put("propertystr", style.getDescription());
					
					TiDiscountmodel disModel= getDiscountList(user.getUserId());
					if(disModel!=null&&disModel.getDetails()!=null){
						for (TiDiscountdetails dd : disModel.getDetails()) {
							if(dd.getProductid().longValue()==style.getProductid()){
								map.put("discount", dd.getDiscount());
								map.put("discountType", disModel.getType());
								map.put("discountName", disModel.getTitle());
							}
						}
					}
					rq.setBasemodle(map);
					rq.setStatu(ReturnStatus.Success);
				}
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 我的优惠信息
	 * @param userId
	 * @return
	 */
	private TiDiscountmodel getDiscountList(Long userId){
		List<TiUserdiscounts> discounts = mydiscountMapper.findMyDiscounts(userId);
		if (discounts != null && discounts.size() > 0) {
			TiDiscountmodel model= disModelMapper.selectByPrimaryKey(discounts.get(0).getDiscountid());
			if(model!=null){
				// 具体优惠信息
				List<TiDiscountdetails> dislist = discountDetailsMapper.findDiscountlist(discounts.get(0).getDiscountid());
				if(dislist!=null&&dislist.size()>0){
					model.setDetails(dislist);
					return model;
				}
			}
		}
		return null;
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
			String cacheKey=KEY_BASE_CACHE_COMMENTS+"p"+productId+"_index"+index+"_size"+size;
			//获取缓存数据
			PageInfo<TiProductcomments> resultPage=(PageInfo<TiProductcomments>)RedisUtil.getObject(cacheKey);
			if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
				rq.setBasemodle(resultPage);
			}else {
				PageHelper.startPage(index, size);
				List<TiProductcomments> commentlist=commentMapper.findListByProductId(productId);
				resultPage=new PageInfo<TiProductcomments>(commentlist);
				if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
					RedisUtil.setObject(cacheKey, resultPage, CACHE_LONG); 
				}
				rq.setBasemodle(resultPage); 
			}
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
						model.setNickname(user.getNickName());
						model.setStyledescription(oproducts.get(0).getPropertystr()); 
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
