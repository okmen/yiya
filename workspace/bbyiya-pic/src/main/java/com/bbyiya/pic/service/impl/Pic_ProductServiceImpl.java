package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderwalletdetailsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductextMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PProductdetailsMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.PStylecoordinateMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.DMyproductdiscountmodel;
import com.bbyiya.model.OPayorderwalletdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproductext;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PProductdetails;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.PScenes;
import com.bbyiya.model.PStylecoordinate;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.dao.IMyProductDetailsDao;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.dao.IPic_ProductDao;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.pic.vo.product.MyProductsDetailsResult;
import com.bbyiya.pic.vo.product.MyProductsResult;
import com.bbyiya.pic.vo.product.MyProductsTempVo;
import com.bbyiya.pic.vo.product.ProductSampleResultVO;
import com.bbyiya.pic.vo.product.PublicFinacingMyPro;
import com.bbyiya.service.pic.IBaseDiscountService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.product.ProductSampleVo;
import com.bbyiya.vo.user.UChildInfoParam;
import com.bbyiya.vo.user.UUserAddressResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pic_productService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_ProductServiceImpl implements IPic_ProductService {

	/*------------------Product---------------------------*/
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductdetailsMapper detailMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	/*---------------------坐标模板---------------------------------*/
	@Autowired
	private PStylecoordinateMapper styleCoordMapper;
	@Autowired
	private PStylecoordinateitemMapper styleCoordItemMapper;
	/*--------------------我的作品----------------------------------*/
	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	@Autowired
	private PScenesMapper sceneMapper;
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private PMyproductchildinfoMapper mychildMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;
	
	
	@Autowired
	private PMyproducttempapplyMapper tempapplyMapper;
	@Autowired
	private PMyproductextMapper myextMapper;
	
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;// 影楼信息
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private UUseraddressMapper uaddressMapper;

	/*----------------pic-dao----------------------------------*/
	@Autowired
	private IMyProductsDao myProductsDao;
	@Autowired
	private IMyProductDetailsDao mydetailDao;
	@Autowired
	private IPic_ProductDao productDao;
	@Autowired
	private IPic_OrderMgtDao orderDao;
	
	@Autowired
	private OPayorderwalletdetailsMapper walletdetailsMapper;
	
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService baseAddressService;
	//优惠信息
	@Resource(name = "baseDiscountServiceImpl")
	private IBaseDiscountService discountService;
	
	
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
		String keyName = ConfigUtil.getSingleValue("currentRedisKey-Base") + "_productsample100_" + productId;
		List<ProductSampleResultVO> listResult = (List<ProductSampleResultVO>) RedisUtil.getObject(keyName);
		if (listResult == null || listResult.size() <= 0) {
			PProducts products = productsMapper.selectByPrimaryKey(productId);
			if (products != null) {
				listResult = productDao.findProductSamplesByProductId(productId);
				if(listResult != null && listResult.size() > 0) {
					for (ProductSampleResultVO sam : listResult) {
						sam.setMyWorks(getMyProductsResultSamp(sam.getCartid()));
					}
					RedisUtil.setObject(keyName, listResult, 7200);
				}
			}
		}
		rq.setBasemodle(listResult);
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
					if (!ObjectUtil.isEmpty(param.getDescription())) {
						myproducts.setDescription(param.getDescription());
					}
					myproducts.setUpdatetime(new Date());
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
				} else {
					rq.setStatu(ReturnStatus.SystemError_1);
					rq.setStatusreson("没有权限编辑别人的作品");
					return rq;
				}
			} else {// 新增
				if (param.getProductid() == null || param.getProductid() <= 0) {
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("");
					return rq;
				}
				PMyproducts myproduct = null;// myMapper.getMyProductsByProductId(userId,
												// param.getProductid(),
												// Integer.parseInt(MyProductStatusEnum.ok.toString()));
				if (myproduct == null) {
					myproduct = new PMyproducts();
					myproduct.setAuthor(param.getAuthor());
					myproduct.setTitle(param.getTitle());
					if (!ObjectUtil.isEmpty(param.getDescription())) {
						myproduct.setDescription(param.getDescription());
					}
					myproduct.setUserid(userId);
					myproduct.setProductid(param.getProductid());
					myproduct.setCreatetime(new Date());
					myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
					myproduct.setUpdatetime(new Date());
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
				// 插入宝宝生日信息
				if (param.getChildInfo() != null) {
					boolean isnew = false;
					PMyproductchildinfo mychild = mychildMapper.selectByPrimaryKey(myproduct.getCartid());
					if (mychild == null) {
						mychild = new PMyproductchildinfo();
						mychild.setCartid(myproduct.getCartid());
						mychild.setCreatetime(new Date());
						isnew = true;
					}
					if (!ObjectUtil.isEmpty(param.getChildInfo().getNickName())) {
						mychild.setNickname(param.getChildInfo().getNickName());
					}
					if (!ObjectUtil.isEmpty(param.getChildInfo().getBirthday())) {
						mychild.setBirthday(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
						Date now=new Date();
						int compare=now.compareTo(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
						//如果在今天之后，说明是预产期
						if(compare<0){
							mychild.setIsdue(1);
						}else{
							mychild.setIsdue(0);
						}
					}
					if (isnew) {
						mychildMapper.insert(mychild);
					} else {
						mychildMapper.updateByPrimaryKeySelective(mychild);
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

	public ReturnModel Modify_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		if (param != null) {
			if (param.getCartid() != null && param.getCartid() > 0) {// 修改
				return edit_MyProducts(userId, param);
			} else {// 新增
				return add_MyProducts(userId, param);
			}
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
		}
		return rq;
	}

	/**
	 * 我的作品新增
	 * 
	 * @param userId
	 * @param param
	 * @return
	 */
	public ReturnModel add_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		Long cartIdTemp = 0l;
		if (param != null) {
			if (param.getCartid() != null && param.getCartid() > 0) {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请调去修改接口");
				return rq;
			}
			if (param.getProductid() == null || param.getProductid() <= 0) {
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("");
				return rq;
			}
			PMyproducts myproduct = new PMyproducts();
			myproduct.setAuthor(param.getAuthor());
			myproduct.setTitle(param.getTitle());
			if (!ObjectUtil.isEmpty(param.getDescription())) {
				myproduct.setDescription(param.getDescription());
			}
			myproduct.setIstemp(0);
			myproduct.setUserid(userId);
			myproduct.setProductid(param.getProductid());
			myproduct.setCreatetime(new Date());
			myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
			myproduct.setUpdatetime(new Date());
			myMapper.insertReturnId(myproduct);

			cartIdTemp = myproduct.getCartid();
			if (param.getDetails() != null && param.getDetails().size() > 0) {
				int sort = 0;
				for (PMyproductdetails de : param.getDetails()) {
					if (!ObjectUtil.isEmpty(de.getImgurl())) {
						de.setCreatetime(new Date());
						if (de.getSort() == null) {
							de.setSort(sort);// 设置排序
						}
						de.setUserid(userId);
						de.setCartid(myproduct.getCartid());
						myDetaiMapper.insert(de);
						sort++;
					}
				}
			}
			// 插入宝宝生日信息
			if (param.getChildInfo() != null) {
				boolean isnew = false;
				PMyproductchildinfo mychild = mychildMapper.selectByPrimaryKey(myproduct.getCartid());
				if (mychild == null) {
					mychild = new PMyproductchildinfo();
					mychild.setCartid(myproduct.getCartid());
					mychild.setCreatetime(new Date());
					isnew = true;
				}
				if (!ObjectUtil.isEmpty(param.getChildInfo().getNickName())) {
					mychild.setNickname(param.getChildInfo().getNickName());
				}
				if (!ObjectUtil.isEmpty(param.getChildInfo().getBirthday())) {
					mychild.setBirthday(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
					Date now=new Date();
					int compare=now.compareTo(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
					//如果在今天之后，说明是预产期
					if(compare<0){
						mychild.setIsdue(1);
					}else{
						mychild.setIsdue(0);
					}
				}
				
				if (isnew) {
					mychildMapper.insert(mychild);
				} else {
					mychildMapper.updateByPrimaryKeySelective(mychild);
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
	 * 作品的修改（包括图片的修改、新增）
	 * 
	 * @param userId
	 * @param param
	 * @return
	 */
	public ReturnModel edit_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		if (param != null) {
			if ((param.getCartid() == null || param.getCartid() <= 0)) {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("作品id不能为空");
				return rq;
			}
			PMyproducts myproducts = myMapper.selectByPrimaryKey(param.getCartid());
			// A修改作品的宝宝信息
			if (myproducts != null) {
				if (param.getChildInfo() != null) {
					boolean isnew = false;
					boolean isNeedUp = false;
					PMyproductchildinfo mychild = mychildMapper.selectByPrimaryKey(param.getCartid());
					if (mychild == null) {
						mychild = new PMyproductchildinfo();
						mychild.setCartid(param.getCartid());
						mychild.setCreatetime(new Date());
						isnew = true;
					}
					if (!ObjectUtil.isEmpty(param.getChildInfo().getNickName())) {
						mychild.setNickname(param.getChildInfo().getNickName());
						isNeedUp = true;
					}
					if (!ObjectUtil.isEmpty(param.getChildInfo().getBirthday())) {
						mychild.setBirthday(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
						Date now=new Date();
						int compare=now.compareTo(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
						//如果在今天之后，说明是预产期
						if(compare<0){
							mychild.setIsdue(1);
						}else{
							mychild.setIsdue(0);
						}
						isNeedUp = true;
					}
					if (isNeedUp) {
						if (isnew) {
							mychildMapper.insert(mychild);
						} else {
							mychildMapper.updateByPrimaryKeySelective(mychild);
						}
					}
					/*----------------------------更新个人宝宝信息---------------------------------------------------*/
					boolean isHavechild = true;
					UChildreninfo childreninfo = childMapper.selectByPrimaryKey(userId);
					if (childreninfo == null) {
						childreninfo = new UChildreninfo();
						childreninfo.setUserid(userId);
						childreninfo.setCreatetime(new Date());
						isHavechild = false;
					}
					if (!ObjectUtil.isEmpty(mychild.getNickname())) {
						childreninfo.setNickname(mychild.getNickname());
					}
					if (!ObjectUtil.isEmpty(mychild.getBirthday())) {
						childreninfo.setBirthday(mychild.getBirthday());
					}
					if (isHavechild) {
						childMapper.updateByPrimaryKey(childreninfo);
					} else {
						childMapper.insert(childreninfo);
					}
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("找不到相应的作品！");
				return rq;
			}

			// ****---------------- 作品图片修改 、新增 ----------------------------
			// *****
			if (param.getDetails() != null && param.getDetails().size() > 0) {
				// c1 检验 场景是否被选过
				List<PMyproductdetails> details = myDetaiMapper.findMyProductdetails(param.getCartid());
				int count = details.size();
				int sort = 0;
				if (count > 0) {
					sort = myDetaiMapper.getMaxSort(param.getCartid());
				}
				for (PMyproductdetails de : param.getDetails()) {
					de.setUserid(userId);
					if (de.getPdid() != null && de.getPdid() > 0) {
						if (!ObjectUtil.isEmpty(de.getImgurl())) {
							de.setUserid(userId);
							myDetaiMapper.updateByPrimaryKeySelective(de);
						} else if (de.getSort() != null && de.getSort() > 0) {// 排序
							myDetaiMapper.updateByPrimaryKeySelective(de);
						}
					} else if (count < 12) {
						if (!ObjectUtil.isEmpty(de.getImgurl())) {
							de.setCartid(param.getCartid());
							de.setSort(count == 0 ? 0 : (sort + 1));
							myDetaiMapper.insert(de);
							count++;
							sort++;
						}
					}
				}
			}
			/*--------------------作品表修改 -----------------------------------*/
			if (!ObjectUtil.isEmpty(param.getTitle())) {
				myproducts.setTitle(param.getTitle());
			}
			if (!ObjectUtil.isEmpty(param.getAuthor())) {
				myproducts.setAuthor(param.getAuthor());
			}
			if (!ObjectUtil.isEmpty(param.getDescription())) {
				myproducts.setDescription(param.getDescription());
			}
			myproducts.setUpdatetime(new Date());
			// 更新用户作品基本信息
			myMapper.updateByPrimaryKeySelective(myproducts);
			/*-------------------------------------------------*/

			rq.setStatu(ReturnStatus.Success);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cartId", param.getCartid());
			rq.setBasemodle(map);
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
		}
		return rq;
	}

	/*------------------------------------------------------------------------------------*/

	/**
	 * 我的作品修改
	 */
	public ReturnModel Edit_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		Long cartIdTemp = 0l;
		if (param != null) {
			if ((param.getCartid() == null || param.getCartid() <= 0) && (param.getProductid() == null || param.getProductid() <= 0)) {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("没有选择款式");
				return rq;
			}
			UUsers user = usersMapper.selectByPrimaryKey(userId);
			if (param.getCartid() != null && param.getCartid() > 0) {// 更新
				cartIdTemp = param.getCartid();
				PMyproducts myproducts = myMapper.selectByPrimaryKey(param.getCartid());
				// A修改作品的宝宝信息
				if (myproducts != null && param.getChildInfo() != null) {
					boolean isnew = false;
					PMyproductchildinfo mychild = mychildMapper.selectByPrimaryKey(param.getCartid());
					if (mychild == null) {
						mychild = new PMyproductchildinfo();
						mychild.setCartid(param.getCartid());
						mychild.setCreatetime(new Date());
						isnew = true;
					}
					if (!ObjectUtil.isEmpty(param.getChildInfo().getNickName())) {
						mychild.setNickname(param.getChildInfo().getNickName());
					}
					if (!ObjectUtil.isEmpty(param.getChildInfo().getBirthday())) {
						mychild.setBirthday(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
						Date now=new Date();
						int compare=now.compareTo(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday()));
						//如果在今天之后，说明是预产期
						if(compare<0){
							mychild.setIsdue(1);
						}else{
							mychild.setIsdue(0);
						}
					}
					if (isnew) {
						mychildMapper.insert(mychild);
					} else {
						mychildMapper.updateByPrimaryKeySelective(mychild);
					}
					/*----------------------------更新个人宝宝信息---------------------------------------------------*/
					boolean isHavechild = true;
					UChildreninfo childreninfo = childMapper.selectByPrimaryKey(userId);
					if (childreninfo == null) {
						childreninfo = new UChildreninfo();
						childreninfo.setUserid(userId);
						childreninfo.setCreatetime(new Date());
						isHavechild = false;
					}
					if (!ObjectUtil.isEmpty(mychild.getNickname())) {
						childreninfo.setNickname(mychild.getNickname());
					}
					if (!ObjectUtil.isEmpty(mychild.getBirthday())) {
						childreninfo.setBirthday(mychild.getBirthday());
					}
					if (isHavechild) {
						childMapper.updateByPrimaryKey(childreninfo);
					} else {
						childMapper.insert(childreninfo);
					}
				}// ----------------------------------------------------
				boolean canModify = false;
				// 自己的作品
				if (myproducts != null && myproducts.getUserid() != null && myproducts.getUserid().longValue() == userId) {
					canModify = true;
				} else {// 受邀请 协同编辑的作品
					List<PMyproductsinvites> invlist = inviteMapper.findListByCartId(param.getCartid());
					if (invlist != null && invlist.size() > 0) {
						for (PMyproductsinvites in : invlist) {
							if (in.getInviteuserid() != null && in.getInviteuserid().longValue() == userId) {
								canModify = true;
							} else if (user != null && in.getInvitephone().equals(user.getMobilephone()))
								canModify = true;
						}
					}
				}

				if (canModify) {// 修改
					if (!ObjectUtil.isEmpty(param.getTitle())) {
						myproducts.setTitle(param.getTitle());
					}
					if (!ObjectUtil.isEmpty(param.getAuthor())) {
						myproducts.setAuthor(param.getAuthor());
					}
					if (!ObjectUtil.isEmpty(param.getDescription())) {
						myproducts.setDescription(param.getDescription());
					}

					if (param.getDetails() != null && param.getDetails().size() > 0) {
						// 检验 场景是否被选过
						List<PMyproductdetails> details = myDetaiMapper.findMyProductdetails(cartIdTemp);
						if (details != null && details.size() > 0) {// 我的作品列表
							for (PMyproductdetails de : param.getDetails()) {
								if (de.getPdid() != null && de.getPdid() > 0) {
									for (PMyproductdetails myde : details) {
										if (de.getPdid().longValue() != myde.getPdid().longValue() && myde.getSceneid() != null && de.getSceneid() != null && myde.getSceneid().intValue() == de.getSceneid().intValue() && de.getSceneid() > 0) {
											rq.setStatu(ReturnStatus.InvitError_1);
											rq.setStatusreson("此主题被协同人使用啦，请更换其他主题");
											return rq;
										}
									}
								}
							}
						}
						for (PMyproductdetails de : param.getDetails()) {
							if (de.getPdid() != null && de.getPdid() > 0) {
								if (!ObjectUtil.isEmpty(de.getImgurl())) {
									de.setUserid(userId);
									myDetaiMapper.updateByPrimaryKeySelective(de);
								} else if (de.getSort() != null && de.getSort() > 0) {
									myDetaiMapper.updateByPrimaryKeySelective(de);
								}
							}
						}
					}
					myproducts.setUpdatetime(new Date());
					// 更新用户作品基本信息
					myMapper.updateByPrimaryKeySelective(myproducts);
				} else {
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
			// 我的协同编辑作品
			List<MyProductResultVo> mylista = findInvites(user.getMobilephone());
			if (mylista != null && mylista.size() > 0) {
				list.addAll(mylista);
			}

			// 我的作品-制作中的
			List<MyProductResultVo> mylist = myMapper.findMyProductslist(userId, Integer.parseInt(MyProductStatusEnum.ok.toString()));
			list.addAll(getMyProductResultVo(mylist));
			// 我的作品-已经下单的列表
			List<MyProductResultVo> myOrderlist = myMapper.findMyProductslist(userId, Integer.parseInt(MyProductStatusEnum.ordered.toString()));
			list.addAll(getMyProductResultVo(myOrderlist));
			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	public ReturnModel findMyProductsForBranch(Long branchUserId, Integer status, Integer inviteStatus,String keywords, int index, int size) {
		ReturnModel rq = new ReturnModel();
		List<Long> idsList = new ArrayList<Long>();
		idsList.add(branchUserId);
		// 获取影楼的工作人员列表
		List<UBranchusers> userList = branchusersMapper.findMemberslistByBranchUserId(branchUserId);
		if (userList != null && userList.size() > 0) {
			for (UBranchusers uu : userList) {
				idsList.add(uu.getUserid());
			}
		}
		PageHelper.startPage(index, size);
		List<MyProductResultVo> mylist = myMapper.findMyProductslistForBranch(idsList, status, inviteStatus,keywords);
		PageInfo<MyProductResultVo> resultPage = new PageInfo<MyProductResultVo>(mylist);
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {
			for (MyProductResultVo vv : resultPage.getList()) {
				for (UBranchusers uu : userList) {
					if (uu.getUserid().longValue() == vv.getUserid().longValue()) {
						vv.setUserName(uu.getName());
					}
				}
			}
			resultPage.setList(getMyProductResultVo(resultPage.getList()));
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	/**
	 * 根据模板ID得到作品列表
	 * @param branchUserId
	 * @param tempid
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel findMyProductslistForTempId(Long branchUserId, Integer tempid,Integer activeStatus,String keywords,int index, int size) {
		ReturnModel rq = new ReturnModel();
		List<Long> idsList = new ArrayList<Long>();
		idsList.add(branchUserId);
		// 获取影楼的工作人员列表
		List<UBranchusers> userList = branchusersMapper.findMemberslistByBranchUserId(branchUserId);
		if (userList != null && userList.size() > 0) {
			for (UBranchusers uu : userList) {
				idsList.add(uu.getUserid());
			}
		}
		PageHelper.startPage(index, size);
		List<MyProductResultVo> mylist = myMapper.findMyProductslistForTempId(idsList,tempid,activeStatus,keywords);
		PageInfo<MyProductResultVo> resultPage = new PageInfo<MyProductResultVo>(mylist);
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {
			for (MyProductResultVo vv : resultPage.getList()) {
				for (UBranchusers uu : userList) {
					if (uu.getUserid().longValue() == vv.getUserid().longValue()) {
						vv.setUserName(uu.getName());//员工昵称
					}
				}
			}
			resultPage.setList(getMyProductResultVo(resultPage.getList()));
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}

	public List<MyProductResultVo> findInvites(String mobiePhone) {
		List<PMyproductsinvites> inviteList = inviteMapper.findListByPhone(mobiePhone);
		if (inviteList != null && inviteList.size() > 0) {
			List<MyProductResultVo> resultList = new ArrayList<MyProductResultVo>();
			for (PMyproductsinvites in : inviteList) {
				MyProductResultVo vo = myMapper.getMyProductResultVo(in.getCartid());
				if (vo != null) {
					vo.setIsInvited(1);
					vo.setInvitestatus(in.getStatus());
					UUsers users = usersMapper.selectByPrimaryKey(vo.getUserid());
					if (users != null) {
						vo.setUserName(users.getMobilephone());
						if (ObjectUtil.isEmpty(users.getUserimg())) {
							vo.setUserImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
						} else {
							vo.setUserImg(users.getUserimg());
						}
					}
					resultList.add(vo);
				}
			}
			return getMyProductResultVo(resultList);
		}
		return null;
	}

	/**
	 * 我的作品model转换
	 * 
	 * @param mylist
	 * @return
	 */
	private List<MyProductResultVo> getMyProductResultVo(List<MyProductResultVo> mylist) {
		if (mylist != null && mylist.size() > 0) {
			for (MyProductResultVo item : mylist) {
				if (!ObjectUtil.isEmpty(item.getUpdatetime())) {
					item.setCreatetimestr(DateUtil.getTimeStr(item.getUpdatetime(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					item.setCreatetimestr(DateUtil.getTimeStr(item.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				}
				if (item.getInvitestatus() != null && item.getInvitestatus() > 0) {// 邀请协同编辑
					List<PMyproductsinvites> invites = inviteMapper.findListByCartId(item.getCartid());
					if (invites != null && invites.size() > 0) {
						item.setInviteModel(invites.get(0));
						item.setInvitestatus(invites.get(0).getStatus());
						UUsers inviteusers = null;
						if (!ObjectUtil.isEmpty(invites.get(0).getInvitephone())) {
							inviteusers = usersMapper.getUUsersByPhone(invites.get(0).getInvitephone());
						}
						if (inviteusers == null && !ObjectUtil.isEmpty(invites.get(0).getInviteuserid())) {
							inviteusers = usersMapper.selectByPrimaryKey(invites.get(0).getInviteuserid());
						}
						if (inviteusers != null) {
							item.setInvitedName(inviteusers.getNickname());
							if (item.getInviteModel() != null) {
								if (ObjectUtil.isEmpty(item.getInviteModel().getInvitephone())) {
									item.getInviteModel().setInvitephone(inviteusers.getMobilephone());
								}
							}

						}
						
						// 得到来源，即模板名称
						if (item.getTempid() != null) {
							PMyproducttemp temp = tempMapper.selectByPrimaryKey(item.getTempid());
							if (temp != null && temp.getTitle() != null) {
								item.setTempTitle(temp.getTitle());
								item.setStyleid(temp.getStyleid());
							}
						}else{
							if(inviteusers!=null){
								//来源于客户一对一
								UUserAddressResult address=baseAddressService.getUserAddressResult(inviteusers.getUserid(), null);
								if(address!=null){
									item.setAddress(address.getProvinceName()+address.getCityName()+address.getCityName()+address.getStreetdetail());
								}
							}
						}
						
					}
				}
				
				// 得到制作类型
				PProducts product = productsMapper.selectByPrimaryKey(item.getProductid());
				if (product != null && product.getTitle() != null) {
					item.setProductTitle(product.getTitle());
				}
				// 作品详情（图片集合）
				List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(item.getCartid());
				int i = 0;
				if (detailslist != null && detailslist.size() > 0) {
					for (PMyproductdetails dd : detailslist) {
						if (!ObjectUtil.isEmpty(dd.getImgurl())) {
							if (dd.getSort() != null && dd.getSort().intValue() == 0) {
								item.setHeadImg(ImgDomainUtil.getImageUrl_Full(dd.getImgurl()));
//								item.setHeadImg("http://pic.bbyiya.com/" + dd.getImgurl() + "?imageView2/2/w/200");
							}
							i++;
						}
					}
				}
				if (ObjectUtil.isEmpty(item.getHeadImg())) {
					item.setHeadImg("http://pic.bbyiya.com/484983733454448354.png");
				}
				item.setCount(i);
				if (item.getStatus() != null && item.getStatus().intValue() == Integer.parseInt(MyProductStatusEnum.ordered.toString())) {
					item.setIsOrder(1);
					item.setCount(12);
				}
				item.setIsDue(0);//默认不是预产期
				// 得到宝宝生日
				PMyproductchildinfo childinfo = mychildMapper.selectByPrimaryKey(item.getCartid());
				if (childinfo != null && childinfo.getBirthday() != null) {
					item.setBirthdayStr(DateUtil.getTimeStr(childinfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
					item.setIsDue(childinfo.getIsdue()==null?0:childinfo.getIsdue());
				}
				
				
				List<OUserorders> orderList = orderDao.findOrderListByCartIdAndBranchUserID(item.getCartid(),item.getUserid());
				List<String> orderNoList = new ArrayList<String>();
				for (OUserorders order : orderList) {
					orderNoList.add(order.getUserorderid());
				}
				if (orderNoList.size() > 0) {
					item.setOrderNoList(orderNoList);
				}
				//得到评论数
				item.setCommentsCount(0);
				PMyproductext myext=myextMapper.selectByPrimaryKey(item.getCartid());
				if(myext!=null){
					item.setCommentsCount(myext.getCommentscount()==null?0:myext.getCommentscount());
				}
				
				//得到活动状态
				PMyproducttempapply apply= tempapplyMapper.getMyProducttempApplyByCartId(item.getCartid());
				if(apply==null){
					apply=tempapplyMapper.getMyProducttempApplyByUserId(item.getTempid(), item.getInviteModel().getInviteuserid());
				}
				if(apply!=null){
					item.setActiveStatus(apply.getStatus());
					item.setAddress(apply.getAdress());
					item.setSort(apply.getSort());
					if(apply.getStyleid()!=null&&apply.getStyleid()>0){
						item.setStyleid(apply.getStyleid());
					}
				}else{
					if(item.getCount()<12){
						//制作中
						item.setActiveStatus(Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString()));
					}else{
						//制作完成
						item.setActiveStatus(Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString()));
					}
				}

			}
		}
		return mylist;
	}

	public ReturnModel deleMyProduct(Long userId, Long cartId) {
		ReturnModel rq = new ReturnModel();
		PMyproducts myproducts = myMapper.selectByPrimaryKey(cartId);
		if (myproducts != null && myproducts.getUserid() != null && myproducts.getUserid().longValue() == userId) {
			myproducts.setStatus(Integer.parseInt(MyProductStatusEnum.deleted.toString()));
			myMapper.updateByPrimaryKeySelective(myproducts);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("删除成功");
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("作品不存在（或者无法删除）");
		}
		return rq;
	}


	/**
	 * 我的作品详情 （用户操作页 ） 需要登录
	 * 
	 * @return
	 */
	public ReturnModel getMyProductInfo(Long userId, Long cartId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			MyProductsResult myproduct = myProductsDao.getMyProductResultVo(cartId);
			if (myproduct == null) {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("作品不存在");
				return rq;
			}
			if (myproduct != null && myproduct.getStatus() != null && myproduct.getStatus().intValue() == Integer.parseInt(MyProductStatusEnum.ordered.toString())) {
				myproduct.setIsOrder(1);
			}
			//获取协同编辑者的userId------------------------------------
			if (myproduct.getInvitestatus() != null && myproduct.getInvitestatus() > 0) {
				List<PMyproductsinvites> invites = inviteMapper.findListByCartId(cartId);
				if (invites != null && invites.size() > 0) {
					if(invites.get(0).getInviteuserid()!=null){
						myproduct.setInviteUserId(invites.get(0).getInviteuserid());
					}else if (!ObjectUtil.isEmpty(invites.get(0).getInvitephone())) {
						UUsers inviteUser=usersMapper.getUUsersByPhone(invites.get(0).getInvitephone());
						if(inviteUser!=null){
							myproduct.setInviteUserId(inviteUser.getUserid());
						}
					}
				}
			}/*---------------------------------*/
			
			// 如果是否是模板作品------------------------
			if (myproduct.getIstemp() != null && myproduct.getIstemp() > 0 && myproduct.getTempid() != null && myproduct.getTempid() > 0) {
				PMyproducttemp mtemp = tempMapper.selectByPrimaryKey(myproduct.getTempid());
				if (mtemp != null) {
					myproduct.setTempStatus(mtemp.getStatus() == null ? 0 : mtemp.getStatus());
					// 判断用户是否接受模板邀请---------------
					if (userId != myproduct.getUserid().longValue()) {
						List<MyProductListVo> myprolist = myProductsDao.getMyProductResultByTempId(myproduct.getTempid());
						if (myprolist != null && myprolist.size() > 0) {
							for (MyProductListVo ll : myprolist) {
								List<PMyproductsinvites> invlist = inviteMapper.findListByCartId(ll.getCartid());
								if (invlist != null && invlist.size() > 0) {
									for (PMyproductsinvites iv : invlist) {
										if (iv.getInviteuserid() != null && iv.getInviteuserid().longValue() == userId) {
											myproduct.setTempCartId(ll.getCartid());
										}
									}
								}
							}
						}
					}// 判断用户是否接受模板邀请(over)---------------
				}
			}else if (myproduct.getTempid()!=null&&myproduct.getTempid().intValue()>0&&myproduct.getInviteUserId()!=null&&(myproduct.getInviteUserId().longValue()==userId||myproduct.getUserid().longValue()==userId)) {
				//----------用户此作品参与了 异业合作 活动----------------------------------------------------------------
				myproduct.setTempVo(getMyProductsTempVo(userId,myproduct));  
			}

			UUsers myUser = userId == myproduct.getUserid().longValue() ? user : usersMapper.selectByPrimaryKey(myproduct.getUserid());
			if (myUser != null && !ObjectUtil.isEmpty(myUser.getNickname())) {
				myproduct.setMyNickName(myUser.getNickname());
				myproduct.setUserIdentity(myUser.getIdentity());
			}
			if (ObjectUtil.isEmpty(myproduct.getDescription())) {
				PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
				if (product != null) {
					myproduct.setDescription(product.getDescription());
				}
			}
			// --作品图片信息-------
			myproduct.setDetailslist(getMyProductsDetailsResultList(userId, cartId));

			// 作品宝宝信息----------------------------------------------------------------------------
			PMyproductchildinfo childInfo = mychildMapper.selectByPrimaryKey(cartId);
			if (childInfo != null) {
				UChildInfoParam childInfoParam = new UChildInfoParam();
				if (!ObjectUtil.isEmpty(childInfo.getNickname())) {
					childInfoParam.setNickName(childInfo.getNickname());
				}
				if (!ObjectUtil.isEmpty(childInfo.getBirthday())) {
					childInfoParam.setBirthday(DateUtil.getTimeStr(childInfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
				}
				myproduct.setChildInfo(childInfoParam);
			}
			rq.setBasemodle(myproduct);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	
	public ReturnModel getMyProductInfoNew(long userId,long cartId){
		ReturnModel rq=new ReturnModel();
		MyProductsResult myproductRedis=getCartDetailsRedis(userId,cartId);
		if(myproductRedis!=null){
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(myproductRedis);
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("作品不存在");
		}
		return rq;
	}
	

	
	public ReturnModel getPublicFincingInfo(long cartId){
		
		ReturnModel rq=new ReturnModel();
		Map<String, Object> map=new HashMap<String, Object>();
		//返回结果类
		PublicFinacingMyPro pro=new PublicFinacingMyPro();
		//作品图片
		String headImgKey= ConfigUtil.getSingleValue("currentRedisKey-Base")+"_HEADIMG_KEY_" +cartId;
		String ImgDefault=RedisUtil.getString(headImgKey);
		if(!ObjectUtil.isEmpty(ImgDefault)){
			pro.setHeadImg(ImgDefault);
		}else {
			List<MyProductsDetailsResult> detailsList = mydetailDao.findMyProductDetailsResult(cartId);
			if(detailsList!=null&&detailsList.size()>0){
				pro.setHeadImg(detailsList.get(0).getImgurl()) ;
				RedisUtil.setString(headImgKey, detailsList.get(0).getImgurl(), 600); 
			}else {//默认封面
				pro.setHeadImg("http://pic.bbyiya.com/484983733454448354.png");
			}
		}
		PMyproducttempapply apply=tempapplyMapper.getMyProducttempApplyByCartId(cartId);
		if(apply!=null&&apply.getStyleid()!=null&&apply.getProductid()!=null){
			PMyproducttemp temp=tempMapper.selectByPrimaryKey(apply.getTempid());
			if(temp!=null){
				pro.setAmountLimit(temp.getAmountlimit()) ;
			}
			//款式
			PProductstyles style = styleMapper.selectByPrimaryKey(apply.getStyleid()); 
			if(style!=null){
				pro.setPrice( style.getPrice()) ;
			}
			//相册 -阶段
			PProducts products=productsMapper.selectByPrimaryKey(apply.getProductid());
			if(products!=null){
				pro.setTitle( products.getTitle());
			}
			if (apply.getStatus()!=null&&(apply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString())||
					apply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.pass.toString()))) {
				pro.setPublicAmountNeed(0d);
			}else {
				UAccounts accounts=accountsMapper.selectByPrimaryKey(apply.getUserid());
				if(accounts!=null){
					pro.setPublicAmountNeed(temp.getAmountlimit().doubleValue()-(accounts.getAvailableamount()==null?0d:accounts.getAvailableamount().doubleValue()));
				}else {
					pro.setPublicAmountNeed(temp.getAmountlimit());
				} 
			}
		}
		map.put("pro", pro);
		map.put("amountlist", walletdetailsMapper.findWalletsByCartId(cartId));
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success); 
		return rq;
	}
	
	private MyProductsResult getCartDetailsRedis(long userId,Long cartId){
		String keycart=ConfigUtil.getSingleValue("currentRedisKey-Base")+"_cartIdMy_"+cartId;
		MyProductsResult myproductRedis=(MyProductsResult)RedisUtil.getObject(keycart);
		if(myproductRedis!=null){
			//非本人作品  直接调取缓存
			if(!((myproductRedis.getUserid()!=null&&myproductRedis.getUserid().longValue()==userId)||(myproductRedis.getInviteUserId()!=null&&myproductRedis.getInviteUserId().longValue()==userId))){
				return myproductRedis;
			}
		}
		MyProductsResult result=getCartDetails(userId,cartId);
		if(result!=null&&result.getDetailslist()!=null&&result.getDetailslist().size()>=12){
			RedisUtil.setObject(keycart, result, 600);
			if(result.getTempVo()!=null&&result.getInviteUserId()!=null&&result.getInviteUserId().longValue()==userId){
				List<DMyproductdiscountmodel> disList= discountService.findMycartDiscount(userId, cartId);
				if(disList!=null&&disList.size()>0){
					for (DMyproductdiscountmodel dd : disList) {
						dd.setPrice(styleMapper.selectByPrimaryKey(dd.getStyleid()).getPrice()); 
					}
					result.getTempVo().setDiscountList(disList); 
				}
			}
		}
		return result;
	}
	/**
	 * 获取作品信息
	 * @param userId
	 * @param cartId
	 * @return
	 */
	private MyProductsResult getCartDetails(long userId,Long cartId){
		MyProductsResult myproduct = myProductsDao.getMyProductResultVo(cartId);
		if (myproduct == null) {
			return null;
		}
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (myproduct != null && myproduct.getStatus() != null && myproduct.getStatus().intValue() == Integer.parseInt(MyProductStatusEnum.ordered.toString())) {
			myproduct.setIsOrder(1);
		}
		//获取协同编辑者的userId------------------------------------
		if (myproduct.getInvitestatus() != null && myproduct.getInvitestatus() > 0) {
			List<PMyproductsinvites> invites = inviteMapper.findListByCartId(cartId);
			if (invites != null && invites.size() > 0) {
				if(invites.get(0).getInviteuserid()!=null){
					myproduct.setInviteUserId(invites.get(0).getInviteuserid());
				}else if (!ObjectUtil.isEmpty(invites.get(0).getInvitephone())) {
					UUsers inviteUser=usersMapper.getUUsersByPhone(invites.get(0).getInvitephone());
					if(inviteUser!=null){
						myproduct.setInviteUserId(inviteUser.getUserid());
					}
				}
			}
		}/*---------------------------------*/
		
		// 如果是否是模板作品------------------------
		if (myproduct.getIstemp() != null && myproduct.getIstemp() > 0 && myproduct.getTempid() != null && myproduct.getTempid() > 0) {
			PMyproducttemp mtemp = tempMapper.selectByPrimaryKey(myproduct.getTempid());
			if (mtemp != null) {
				myproduct.setTempStatus(mtemp.getStatus() == null ? 0 : mtemp.getStatus());
				// 判断用户是否接受模板邀请---------------
				if (userId != myproduct.getUserid().longValue()) {
					List<MyProductListVo> myprolist = myProductsDao.getMyProductResultByTempId(myproduct.getTempid());
					if (myprolist != null && myprolist.size() > 0) {
						for (MyProductListVo ll : myprolist) {
							List<PMyproductsinvites> invlist = inviteMapper.findListByCartId(ll.getCartid());
							if (invlist != null && invlist.size() > 0) {
								for (PMyproductsinvites iv : invlist) {
									if (iv.getInviteuserid() != null && iv.getInviteuserid().longValue() == userId) {
										myproduct.setTempCartId(ll.getCartid());
									}
								}
							}
						}
					}
				}// 判断用户是否接受模板邀请(over)---------------
			}
		}else if (myproduct.getTempid()!=null&&myproduct.getTempid().intValue()>0&&myproduct.getInviteUserId()!=null&&(myproduct.getInviteUserId().longValue()==userId||myproduct.getUserid().longValue()==userId)) {
			//----------用户此作品参与了 异业合作 活动----------------------------------------------------------------
			myproduct.setTempVo(getMyProductsTempVo(userId,myproduct));  
		}

		UUsers myUser = userId == myproduct.getUserid().longValue() ? user : usersMapper.selectByPrimaryKey(myproduct.getUserid());
		if (myUser != null && !ObjectUtil.isEmpty(myUser.getNickname())) {
			myproduct.setMyNickName(myUser.getNickname());
			myproduct.setUserIdentity(myUser.getIdentity());
		}
		if (ObjectUtil.isEmpty(myproduct.getDescription())) {
			PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
			if (product != null) {
				myproduct.setDescription(product.getDescription());
			}
		}
		// --作品图片列表 信息-------
		myproduct.setDetailslist(getMyProductsDetailsResultList(userId, cartId));

		// 作品宝宝信息----------------------------------------------------------------------------
		PMyproductchildinfo childInfo = mychildMapper.selectByPrimaryKey(cartId);
		if (childInfo != null) {
			UChildInfoParam childInfoParam = new UChildInfoParam();
			if (!ObjectUtil.isEmpty(childInfo.getNickname())) {
				childInfoParam.setNickName(childInfo.getNickname());
			}
			if (!ObjectUtil.isEmpty(childInfo.getBirthday())) {
				childInfoParam.setBirthday(DateUtil.getTimeStr(childInfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
			}
			myproduct.setChildInfo(childInfoParam);
		}
		
		return myproduct;
	}
	
	@Autowired
	private UAccountsMapper accountsMapper;
	/**
	 * 异业活动 作品参与活动
	 * @param userId
	 * @param myproduct
	 * @return
	 */
	private MyProductsTempVo getMyProductsTempVo(Long userId, MyProductsResult myproduct){
		PMyproducttemp mtemp = tempMapper.selectByPrimaryKey(myproduct.getTempid());
		if(mtemp!=null){
			MyProductsTempVo vo=new MyProductsTempVo();
			if(!ObjectUtil.isEmpty(mtemp.getCodeurl())){
				vo.setQRCodeUrl(mtemp.getCodeurl()); 
			}
			//是否限定完成人数
			if(mtemp.getMaxcompletecount()!=null&&mtemp.getMaxcompletecount()>0){
				vo.setIsLimitQuotas(1); 
				vo.setRemainingCount(mtemp.getMaxcompletecount()-(mtemp.getCompletecount()==null?0:mtemp.getCompletecount()));
			}
			if(mtemp.getBlesscount()!=null&&mtemp.getBlesscount().intValue()>0){
				vo.setIsLimitCommentsCount(1);
				//规定评论数量
				vo.setMaxcommentCount(mtemp.getBlesscount()); 
				//作品评论数量
				PMyproductext myproductext= myextMapper.selectByPrimaryKey(myproduct.getCartid());
				if(myproductext!=null){
					vo.setCommentCount(myproductext.getCommentscount()==null?0:myproductext.getCommentscount());
				}else { 
					vo.setCommentCount(0); 
				}
			}
			//我的申请信息
			PMyproducttempapply apply= tempapplyMapper.getMyProducttempApplyByCartId(myproduct.getCartid());
			if(apply==null){
				apply=tempapplyMapper.getMyProducttempApplyByUserId(myproduct.getTempid(), userId);
			}
			if(apply!=null){
				vo.setMytempStatus(apply.getStatus());
			}
			if(apply!=null&&!ObjectUtil.isEmpty(apply.getStyleid())){
				vo.setStyleId(apply.getStyleid());
			}else if(mtemp.getStyleid()!=null){
				vo.setStyleId(mtemp.getStyleid());
			}else {
				vo.setStyleId(apply.getProductid()); 
			}
			//---------是否可以众筹--------------
			if(mtemp.getAmountlimit()!=null&&mtemp.getAmountlimit().doubleValue()>0){
				vo.setPublicAmountLimit(mtemp.getAmountlimit());
				if(apply.getStatus()!=null&&(apply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString())||
						apply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.pass.toString()))){
					vo.setPublicAmountNeedMore(0d);
				}else {
					UAccounts accounts= accountsMapper.selectByPrimaryKey(userId);
					if(accounts!=null){
						vo.setPublicAmountNeedMore(mtemp.getAmountlimit().doubleValue()-(accounts.getAvailableamount()==null?0d:accounts.getAvailableamount().doubleValue()));
					}else {
						vo.setPublicAmountNeedMore(mtemp.getAmountlimit());
					}
				}
			}
			return vo;
		}
		return null;
	}
	
	/**
	 * 得到 作品详情图片列表
	 * @param userId
	 * @param cartId
	 * @return
	 */
	public List<MyProductsDetailsResult> getMyProductsDetailsResultList(Long userId, Long cartId){
		List<MyProductsDetailsResult> arrayList = mydetailDao.findMyProductDetailsResult(cartId);
		if (arrayList != null && arrayList.size() > 0) {
			String base_code = userId + "-" + cartId;
			int i = 1;
			for (MyProductsDetailsResult dd : arrayList) {
				dd.setImgurl(ImgDomainUtil.getImageUrl_Full(dd.getImgurl())); 
				dd.setPrintcode(base_code + "-" + String.format("%02d", i)); 
				if (dd.getSceneid() != null && dd.getSceneid() >= 0) {
					// 打印编号
					if (ObjectUtil.isEmpty(dd.getDescription())) {
						PScenes scene = sceneMapper.selectByPrimaryKey(dd.getSceneid().longValue());
						if (scene != null) {
							dd.setSceneDescription(scene.getContent());
							dd.setSceneTitle(scene.getTitle());
						}
					} else {
						dd.setSceneDescription(dd.getDescription());
						dd.setSceneTitle(dd.getTitle());
					}
				}
				i++;
			}
		}
		return arrayList;
	}

	private MyProductsResult getMyProductsResult(MyProductsResult myproduct, Long userId, Long productId) {
		if (myproduct == null) {
			myproduct = new MyProductsResult();
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
			// 我的作品
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
						if (dd.getSceneid() != null && dd.getSceneid() > 0) {
							dd.setPrintcode(base_code + "-" + String.format("%02d", dd.getSceneid()) + "-" + String.format("%02d", i)); // 打印编号
						}
						i++;
					}
					myproduct.setDetailslist(arrayList);
				} else {
					myproduct = getMyProductsResult(myproduct, userId, productId);
				}
				rq.setBasemodle(myproduct);
			} else {
				myproduct = getMyProductsResult(myproduct, userId, productId);
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
//		ReturnModel rq = new ReturnModel();
//		MyProductsResult myproduct = getMyProductsResult(cartId);
//		if (myproduct != null) {
//			rq.setBasemodle(myproduct);
//		}
//		rq.setStatu(ReturnStatus.Success);
//		return rq;
		return getMyProductInfoNew(0, cartId);
	}

	/**
	 * 通过作品Id获取作品详细（非登录）
	 * 分享
	 * 
	 * @param cartId
	 * @return
	 */
	public MyProductsResult getMyProductsResultSamp(Long cartId) {
		MyProductsResult myproduct = myProductsDao.getMyProductResultVo(cartId);
		if (myproduct != null) {
			if (myproduct.getStatus() != null && myproduct.getStatus().intValue() == Integer.parseInt(MyProductStatusEnum.ordered.toString())) {
				myproduct.setIsOrder(1);
			}
			//获取协同编辑者的userId------------------------------------
			if (myproduct.getInvitestatus() != null && myproduct.getInvitestatus() > 0) {
				List<PMyproductsinvites> invites = inviteMapper.findListByCartId(cartId);
				if (invites != null && invites.size() > 0) {
					if(invites.get(0).getInviteuserid()!=null){
						myproduct.setInviteUserId(invites.get(0).getInviteuserid());
					}else if (!ObjectUtil.isEmpty(invites.get(0).getInvitephone())) {
						UUsers inviteUser=usersMapper.getUUsersByPhone(invites.get(0).getInvitephone());
						if(inviteUser!=null){
							myproduct.setInviteUserId(inviteUser.getUserid());
						}
					}
				}
			}/*---------------------------------*/
			if (ObjectUtil.isEmpty(myproduct.getDescription())) {
				PProducts product = productsMapper.selectByPrimaryKey(myproduct.getProductid());
				if (product != null) {
					myproduct.setDescription(product.getDescription());
				}
			}
			myproduct.setDetailslist(getMyProductsDetailsResultList(myproduct.getUserid(), cartId));
		}
		return myproduct;
	}

	/**
	 * 删除作品单个图片信息
	 */
	public ReturnModel del_myProductDetail(Long userId, Long dpId) {
		ReturnModel rq = new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if (user != null) {
			PMyproductdetails detail = myDetaiMapper.selectByPrimaryKey(dpId);
			if (detail != null) {
				myDetaiMapper.deleteByPrimaryKey(dpId);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("删除成功！");
				return rq;
			}
		}
		rq.setStatu(ReturnStatus.ParamError);
		rq.setStatusreson("删除失败");
		return rq;
	}

	/**
	 * 获取款式坐标
	 */
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

				long type = styleId % 2;
				List<Map<String, String>> backMaps = ConfigUtil.getMaplist("backcoordinate");
				if (backMaps != null && backMaps.size() > 0) {
					for (Map<String, String> mapBacks : backMaps) {
						if (ObjectUtil.parseLong(mapBacks.get("type")) == type) {
							map.put("back-mod", mapBacks);
						}
					}
				}
				Map<String, Object> mapWord = new HashMap<String, Object>();
				
				//相册正面文字大小、行高、间距，字体颜色
				List<Map<String, String>> mapcoordlist=ConfigUtil.getMaplist("frontcoordinate");
				for (Map<String, String> frontMap : mapcoordlist) {
					if (type == ObjectUtil.parseLong(frontMap.get("type"))) {
						mapWord.put("size", frontMap.get("contentSize"));
						mapWord.put("color", frontMap.get("color"));
						mapWord.put("lineHeight", frontMap.get("lineHeight"));
						mapWord.put("letterSpacing", frontMap.get("letterSpacing"));
					}
				}
//				if (type == 1) { // 横版
//					mapWord.put("size", 33);
//					mapWord.put("color", "#595857");
//					mapWord.put("lineHeight", 55);
//					mapWord.put("letterSpacing", 5);
//				} else {// 竖版
//					mapWord.put("size", 30);
//					mapWord.put("color", "#595857");
//					mapWord.put("lineHeight", 48);
//					mapWord.put("letterSpacing", 0);
//				}
				map.put("word-mod", mapWord);
				arrayList.add(map);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(arrayList);
		}
		return rq;
	}
}
