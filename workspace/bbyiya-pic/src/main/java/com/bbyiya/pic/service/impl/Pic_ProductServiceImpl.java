package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PProductdetailsMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.PStylecoordinateMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PProductdetails;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PScenes;
import com.bbyiya.model.PStylecoordinate;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.dao.IMyProductDetailsDao;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.dao.IPic_ProductDao;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.pic.vo.product.MyProductsDetailsResult;
import com.bbyiya.pic.vo.product.MyProductsResult;
import com.bbyiya.pic.vo.product.ProductSampleResultVO;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.product.ProductSampleVo;

@Service("pic_productService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_ProductServiceImpl implements IPic_ProductService {

	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductdetailsMapper detailMapper;

	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;

	@Autowired
	private UUsersMapper usersMapper;

	@Autowired
	private PStylecoordinateMapper styleCoordMapper;
	@Autowired
	private PStylecoordinateitemMapper styleCoordItemMapper;
	@Autowired
	private PScenesMapper sceneMapper;
	
	/*----------------pic-dao----------------*/
	@Autowired
	private IMyProductsDao myProductsDao;
	@Autowired
	private IMyProductDetailsDao mydetailDao;
	@Autowired
	private IPic_ProductDao productDao;

	public ReturnModel getProductSamples(Long productId) {
		ReturnModel rq = new ReturnModel();
		ProductSampleVo product = productsMapper.getProductBaseVoByProId(productId);
		List<PProductdetails> details = detailMapper.findDetailsByProductId(productId);
		if (details != null & details.size() > 0) {
			product.setSampleItems(details);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(product);
		return rq;
	}
	
	public ReturnModel getProductSamplelist(Long productId) {
		ReturnModel rq = new ReturnModel();
		PProducts products= productsMapper.selectByPrimaryKey(productId);
		if(products!=null){
			List<ProductSampleResultVO> list=productDao.findProductSamplesByProductId(productId);
			if(list!=null&&list.size()>0){
				for (ProductSampleResultVO sam : list) {
					sam.setMyWorks(getMyProductsResult(sam.getCartid()));  
				}
			}
			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	public ReturnModel saveOrEdit_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		Long cartIdTemp = 0l;
		if (param != null) {
			if (param.getCartid() != null && param.getCartid() > 0) {// 更新
				cartIdTemp = param.getCartid();
				PMyproducts myproducts = myMapper.selectByPrimaryKey(param.getCartid());
				if (myproducts != null && myproducts.getUserid() != null && myproducts.getUserid().longValue() == userId) {// 修改
					if (!ObjectUtil.isEmpty(param.getTitle())) {
						myproducts.setTitle(param.getTitle());
					}
					if (!ObjectUtil.isEmpty(param.getAuthor())) {
						myproducts.setAuthor(param.getAuthor());
					}
					// 更新用户作品基本信息
					myMapper.updateByPrimaryKeySelective(myproducts);
					if (param.getDetails() != null && param.getDetails().size() > 0) {
						myDetaiMapper.deleteByCartId(param.getCartid());
						int maxSort = 0;
						for (PMyproductdetails de : param.getDetails()) {
							de.setCartid(param.getCartid());
							
							de.setCreatetime(new Date());
							if (de.getSort() == null) {
								de.setSort(maxSort);// 设置排序
							}
							myDetaiMapper.insert(de);
							maxSort++;
						}
					}
				}else {
					rq.setStatu(ReturnStatus.SystemError_1);
					rq.setStatusreson("没有权限编辑别人的作品");
					return rq;
				}
			} else {// 新增
				if(param.getProductid()==null||param.getProductid()<=0){
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("");
					return rq;
				}
				PMyproducts myproduct = myMapper.getMyProductsByProductId(userId, param.getProductid(), Integer.parseInt(MyProductStatusEnum.ok.toString()));
				if (myproduct == null) {
					myproduct = new PMyproducts();
					myproduct.setAuthor(param.getAuthor());
					myproduct.setTitle(param.getTitle());
					myproduct.setUserid(userId);
					myproduct.setProductid(param.getProductid());
					myproduct.setCreatetime(new Date());
					myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
					myMapper.insertReturnId(myproduct);
				}
				cartIdTemp = myproduct.getCartid();
				if (param.getDetails() != null && param.getDetails().size() > 0) {
					int sort = 0;
					for (PMyproductdetails de : param.getDetails()) {
						de.setCreatetime(new Date());
						if (de.getSort() == null) {
							de.setSort(sort);// 设置排序
						}
						de.setCartid(myproduct.getCartid());
						myDetaiMapper.insert(de);
						sort++;
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cartId", cartIdTemp);
		rq.setBasemodle(map);
		return rq;
	}
	
	public ReturnModel Edit_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		Long cartIdTemp = 0l;
		if (param != null) {
			if((param.getCartid()==null||param.getCartid()<=0)&&(param.getProductid()==null||param.getProductid()<=0)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("没有选择款式");
				return rq;
			}
			if (param.getCartid() != null && param.getCartid() > 0) {// 更新
				cartIdTemp = param.getCartid();
				PMyproducts myproducts = myMapper.selectByPrimaryKey(param.getCartid());
				if (myproducts != null && myproducts.getUserid() != null && myproducts.getUserid().longValue() == userId) {// 修改
					if (!ObjectUtil.isEmpty(param.getTitle())) {
						myproducts.setTitle(param.getTitle());
					}
					if (!ObjectUtil.isEmpty(param.getAuthor())) {
						myproducts.setAuthor(param.getAuthor());
					}
					// 更新用户作品基本信息
					myMapper.updateByPrimaryKeySelective(myproducts);
					if (param.getDetails() != null && param.getDetails().size() > 0) {
						for (PMyproductdetails de : param.getDetails()) {
							if(de.getPdid()!=null&&de.getPdid()>0){
								if(!ObjectUtil.isEmpty(de.getImgurl())){
									myDetaiMapper.updateByPrimaryKeySelective(de);
								}
							}
						}
					}
				}else {
					rq.setStatu(ReturnStatus.SystemError_1);
					rq.setStatusreson("没有权限编辑别人的作品");
					return rq;
				}
			} else {// 新增
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请输入作品Id");
				return rq;
			}
		}
		rq.setStatu(ReturnStatus.Success);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cartId", cartIdTemp);
		rq.setBasemodle(map);
		return rq;
	}

	/**
	 * 我的作品列表
	 * 
	 * @return
	 */

	public ReturnModel findMyProlist(Long userId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			List<MyProductResultVo> list = new ArrayList<MyProductResultVo>();
			List<MyProductResultVo> mylist = myMapper.findMyProductslist(userId, Integer.parseInt(MyProductStatusEnum.ok.toString()));
			if (mylist != null && mylist.size() > 0) {
				for (MyProductResultVo item : mylist) {
					PProducts products = productsMapper.selectByPrimaryKey(item.getProductid());
					if (products != null) {
						item.setHeadImg(products.getDefaultimg());
					}
					// 作品详情（图片集合）
					List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(item.getCartid());
					int i = 0;
					if (detailslist != null && detailslist.size() > 0) {
						for (PMyproductdetails dd : detailslist) {
							if (!ObjectUtil.isEmpty(dd.getImgurl())) {
								i++;
							}
						}
					}
					item.setCount(i);
				}
				list.addAll(mylist);
			}
			// 我的订单列表
			List<MyProductResultVo> myOrderlist = myMapper.findMyProductslist(userId, Integer.parseInt(MyProductStatusEnum.ordered.toString()));
			if (myOrderlist != null && myOrderlist.size() > 0) {
				for (MyProductResultVo oo : myOrderlist) {
					oo.setIsOrder(1);
					oo.setCount(12);
					PProducts products = productsMapper.selectByPrimaryKey(oo.getProductid());
					if (products != null) {
						oo.setHeadImg(products.getDefaultimg());
					}
				}
				list.addAll(myOrderlist);
			}
			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	public ReturnModel deleMyProduct(Long userId, Long cartId){
		ReturnModel rq=new ReturnModel();
		PMyproducts myproducts= myMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null&&myproducts.getUserid()!=null&&myproducts.getUserid().longValue()==userId){
			if(myproducts.getStatus()!=null&&myproducts.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("已下单的作品暂不支持删除操作！");
				return rq;
			}
			myMapper.deleteByPrimaryKey(cartId);
			mydetailDao.deleMyProductDetailsByCartId(cartId); 
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("删除成功");
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("作品不存在（或者无法删除）");
		}
		return rq;
	}
	
	/**
	 * 我的作品详情 （用户操作页 ）
	 *  需要登录
	 * @return
	 */
	public ReturnModel getMyProductInfo(Long userId, Long cartId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			MyProductsResult myproduct = myProductsDao.getMyProductResultVo(cartId);
			if (myproduct != null&&myproduct.getUserid().longValue()==userId) {
				PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
				if (product != null) {
					myproduct.setDescription(product.getDescription());
				}
				List<MyProductsDetailsResult> arrayList =  mydetailDao.findMyProductDetailsResult(cartId);
				if (arrayList != null && arrayList.size() > 0) {
					String base_code = userId + "-" + myproduct.getCartid();
					int i = 1;
					for (MyProductsDetailsResult dd : arrayList) {
						if(dd.getSceneid()!=null&&dd.getSceneid()>0){
							dd.setPrintcode(base_code + "-" + String.format("%02d", dd.getSceneid()) + "-" + String.format("%02d", i));																										// 打印编号				
							PScenes scene= sceneMapper.selectByPrimaryKey(dd.getSceneid().longValue());
							if(scene!=null){
								dd.setSceneDescription(scene.getContent());
								dd.setSceneTitle(scene.getTitle()); 
							}
						}
						i++;
					}
					myproduct.setDetailslist(arrayList);
				}
				rq.setBasemodle(myproduct);
			}else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("不可编辑的作品");
			}
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	private MyProductsResult getMyProductsResult(MyProductsResult myproduct,Long userId, Long productId){
		if(myproduct==null){
			myproduct =new MyProductsResult();
			myproduct.setUserid(userId);
			myproduct.setProductid(productId);
			PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
			if (product != null) {
				myproduct.setDescription(product.getDescription());
			}
		}
		List<MyProductsDetailsResult> imglist = new ArrayList<MyProductsDetailsResult>();
		for (int i = 0; i < 12; i++) {
			MyProductsDetailsResult dd = new MyProductsDetailsResult();
			dd.setSort(i);
			imglist.add(dd);
		}
		myproduct.setDetailslist(imglist);
		return myproduct;
	}
	
	public ReturnModel getMyProductByProductId(Long userId, Long productId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			//我的作品
			MyProductsResult myproduct = myProductsDao.getMyProductResultByProductId(userId, productId, Integer.parseInt(MyProductStatusEnum.ok.toString()));	
			if (myproduct != null && myproduct.getUserid().longValue() == userId) {
				PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
				if (product != null) {
					myproduct.setDescription(product.getDescription());
				}
				List<MyProductsDetailsResult> arrayList = mydetailDao.findMyProductDetailsResult(myproduct.getCartid());
				if (arrayList != null && arrayList.size() > 0) {
					String base_code = userId + "-" + myproduct.getCartid();
					int i = 1;
					for (MyProductsDetailsResult dd : arrayList) {
						if(dd.getSceneid()!=null&&dd.getSceneid()>0){
							dd.setPrintcode(base_code + "-" + String.format("%02d", dd.getSceneid()) + "-" + String.format("%02d", i)); // 打印编号								
						}
						i++;
					}
					myproduct.setDetailslist(arrayList);
				} else {
					myproduct=getMyProductsResult(myproduct,userId,productId);
				}
				rq.setBasemodle(myproduct);
			}else {
				myproduct=getMyProductsResult(myproduct,userId,productId);
				rq.setBasemodle(myproduct);
			}
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	
	
	/**
	 * 前端 作品详情 （分享页 ）
	 * 
	 */
	public ReturnModel getMyProductInfo(Long cartId) {
		ReturnModel rq = new ReturnModel();
		MyProductsResult myproduct=getMyProductsResult(cartId);
		if(myproduct!=null){
			rq.setBasemodle(myproduct);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	

	
	
	/**
	 * 通过作品Id获取作品详细
	 * @param cartId
	 * @return
	 */
	public MyProductsResult getMyProductsResult(Long cartId){
		MyProductsResult myproduct=myProductsDao.getMyProductResultVo(cartId);
		if (myproduct != null) {
			PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
			if (product != null) {
				myproduct.setDescription(product.getDescription());
			}			
			List<MyProductsDetailsResult> list=mydetailDao.findMyProductDetailsResult(cartId);
			if(list!=null&&list.size()>0){
				for (MyProductsDetailsResult detail : list) {
					PScenes scene= sceneMapper.selectByPrimaryKey(detail.getSceneid().longValue());
					if(scene!=null){
						detail.setSceneDescription(scene.getContent());
						detail.setSceneTitle(scene.getTitle()); 
					}
				}
			}
			myproduct.setDetailslist(list);
		}
		return myproduct;
	}
	

	public ReturnModel del_myProductDetail(Long userId, Long dpId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			PMyproductdetails detail = myDetaiMapper.selectByPrimaryKey(dpId);
			if (detail != null) {
				PMyproducts myproduct = myMapper.selectByPrimaryKey(detail.getCartid());
				if (myproduct != null && myproduct.getUserid() != null && myproduct.getUserid().longValue() == userId) {
					detail.setImgurl("");
					detail.setContent("");
					detail.setSceneid(null);
					myDetaiMapper.updateByPrimaryKey(detail);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("删除成功！");
					return rq;
				}
			}
		}
		rq.setStatu(ReturnStatus.ParamError);
		rq.setStatusreson("删除失败");
		return rq;
	}

	public ReturnModel getStyleCoordResult(Long styleId) {
		ReturnModel rq = new ReturnModel();
		List<PStylecoordinate> list = styleCoordMapper.findlistByStyleId(styleId);
		if (list != null && list.size() > 0) {
			List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

			for (PStylecoordinate ss : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				PStylecoordinateitem w_no = styleCoordItemMapper.selectByPrimaryKey(ss.getNocoordid().longValue());
				map.put("number", w_no);
				PStylecoordinateitem w_pic = styleCoordItemMapper.selectByPrimaryKey(ss.getPiccoordid().longValue());
				map.put("pic", w_pic);
				PStylecoordinateitem w_word = styleCoordItemMapper.selectByPrimaryKey(ss.getWordcoordid().longValue());
				map.put("words", w_word);
				map.put("type", ss.getType());

				Map<String, Object> mapWord=new HashMap<String, Object>();
				if(styleId%2==1){
					mapWord.put("size", 33);
					mapWord.put("color", "#595857");
					mapWord.put("lineHeight", 55);
					mapWord.put("letterSpacing", 5);
				}else {
					mapWord.put("size", 29);
					mapWord.put("color", "#595857");
					mapWord.put("lineHeight", 40);
					mapWord.put("letterSpacing", 0);
				}
				map.put("word-mod", mapWord);
				arrayList.add(map);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(arrayList);
		}
		return rq;
	}
}
