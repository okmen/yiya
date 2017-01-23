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
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PProductdetails;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.vo.product.MyProductParam;
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
						int maxSort = myDetaiMapper.getMaxSort(param.getCartid());
						for (PMyproductdetails de : param.getDetails()) {
							if (de.getPdid() != null && de.getPdid() > 0) {
								PMyproductdetails temp = myDetaiMapper.selectByPrimaryKey(de.getPdid());
								if (temp != null) {
									de.setCreatetime(new Date());
									myDetaiMapper.updateByPrimaryKeySelective(de);
								}
							} else {
								maxSort++;
								de.setCreatetime(new Date());
								de.setSort(maxSort);// 设置排序
								myDetaiMapper.insert(de);
							}
						}
					}
				}
			} else {// 新增
				PMyproducts myproduct = new PMyproducts();
				myproduct.setAuthor(param.getAuthor());
				myproduct.setTitle(param.getTitle());
				myproduct.setUserid(userId);
				myproduct.setProductid(param.getProductid());
				myproduct.setCreatetime(new Date());
				myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
				myMapper.insertReturnId(myproduct);
				cartIdTemp = myproduct.getCartid();
				if (param.getDetails() != null && param.getDetails().size() > 0) {
					int sort = 1;
					for (PMyproductdetails de : param.getDetails()) {
						de.setCreatetime(new Date());
						de.setSort(sort);// 设置排序
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
					item.setCount(1);// TODO 缺少产品图片数量
				}
				list.addAll(mylist);
			}
			// 我的订单列表
			List<MyProductResultVo> myOrderlist = myMapper.findMyProductslist(userId, Integer.parseInt(MyProductStatusEnum.ordered.toString()));
			if (myOrderlist != null && myOrderlist.size() > 0) {
				for (MyProductResultVo oo : myOrderlist) {
					oo.setIsOrder(1);
					oo.setCount(12);
				}
				list.addAll(myOrderlist);
			}

			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	/**
	 * 我的作品详情
	 * 
	 * @return
	 */
	public ReturnModel getMyProductInfo(Long userId, Long cartId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			MyProductResultVo myproduct= myMapper.getMyProductResultVo(cartId);
			if(myproduct!=null){
				List<PMyproductdetails> list=myDetaiMapper.findMyProductdetails(cartId);
				myproduct.setDetailslist(list);
				rq.setBasemodle(myproduct);
			}
		}
		rq.setStatu(ReturnStatus.Success); 
		return rq;
	}
}
