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
import com.bbyiya.dao.PStylecoordinateMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PProductdetails;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PStylecoordinate;
import com.bbyiya.model.PStylecoordinateitem;
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

	@Autowired
	private PStylecoordinateMapper styleCoordMapper;
	@Autowired
	private PStylecoordinateitemMapper styleCoordItemMapper;

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
			if (param.getCartid() != null && param.getCartid() > 0) {// ����
				cartIdTemp = param.getCartid();
				PMyproducts myproducts = myMapper.selectByPrimaryKey(param.getCartid());
				if (myproducts != null && myproducts.getUserid() != null && myproducts.getUserid().longValue() == userId) {// �޸�
					if (!ObjectUtil.isEmpty(param.getTitle())) {
						myproducts.setTitle(param.getTitle());
					}
					if (!ObjectUtil.isEmpty(param.getAuthor())) {
						myproducts.setAuthor(param.getAuthor());
					}
					// �����û���Ʒ������Ϣ
					myMapper.updateByPrimaryKeySelective(myproducts);
					if (param.getDetails() != null && param.getDetails().size() > 0) {
						myDetaiMapper.deleteByCartId(param.getCartid());
						int maxSort = 0;// myDetaiMapper.getMaxSort(param.getCartid());
						for (PMyproductdetails de : param.getDetails()) {
							de.setCartid(param.getCartid());
							// if (de.getPdid() != null && de.getPdid() > 0) {
							// PMyproductdetails temp =
							// myDetaiMapper.selectByPrimaryKey(de.getPdid());
							// if (temp != null) {
							// de.setCreatetime(new Date());
							// myDetaiMapper.updateByPrimaryKeySelective(de);
							// }
							// } else {
							// if(!ObjectUtil.isEmpty(de.getImgurl())){
							de.setCreatetime(new Date());
							if (de.getSort() == null) {
								de.setSort(maxSort);// ��������
							}
							myDetaiMapper.insert(de);
							// }
							maxSort++;
							// }
						}
					}
				}
			} else {// ����
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
					int sort = 0;
					for (PMyproductdetails de : param.getDetails()) {
						de.setCreatetime(new Date());
						if (de.getSort() == null) {
							de.setSort(sort);// ��������
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

	/**
	 * �ҵ���Ʒ�б�
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
					// ��Ʒ���飨ͼƬ���ϣ�
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
			// �ҵĶ����б�
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

	/**
	 * �ҵ���Ʒ����
	 * 
	 * @return
	 */
	public ReturnModel getMyProductInfo(Long userId, Long cartId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			MyProductResultVo myproduct = myMapper.getMyProductResultVo(cartId);
			if (myproduct != null) {
				PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
				if (product != null) {
					myproduct.setDescription(product.getDescription());
				}
				List<PMyproductdetails> list = myDetaiMapper.findMyProductdetails(cartId);
				myproduct.setDetailslist(list);
				rq.setBasemodle(myproduct);
			}
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	public ReturnModel getMyProductInfo(Long cartId) {
		ReturnModel rq = new ReturnModel();
		MyProductResultVo myproduct = myMapper.getMyProductResultVo(cartId);
		if (myproduct != null) {
			PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
			if (product != null) {
				myproduct.setDescription(product.getDescription());
			}
			List<PMyproductdetails> list = myDetaiMapper.findMyProductdetails(cartId);
			myproduct.setDetailslist(list);
			rq.setBasemodle(myproduct);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	public ReturnModel del_myProductDetail(Long userId, Long dpId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			PMyproductdetails detail = myDetaiMapper.selectByPrimaryKey(dpId);
			if (detail != null) {
				PMyproducts myproduct = myMapper.selectByPrimaryKey(detail.getCartid());
				if (myproduct != null && myproduct.getUserid() != null && myproduct.getUserid().longValue() == userId) {
					myDetaiMapper.deleteByPrimaryKey(dpId);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("ɾ���ɹ���");
					return rq;
				}
			}
		}
		rq.setStatu(ReturnStatus.ParamError);
		rq.setStatusreson("ɾ��ʧ��");
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
				arrayList.add(map);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(arrayList);
		}
		return rq;
	}
}