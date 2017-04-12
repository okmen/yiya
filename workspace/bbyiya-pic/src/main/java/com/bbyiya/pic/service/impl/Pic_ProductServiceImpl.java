package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PProductdetailsMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.PStylecoordinateMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PProductdetails;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PScenes;
import com.bbyiya.model.PStylecoordinate;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.dao.IMyProductDetailsDao;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.dao.IPic_ProductDao;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.pic.vo.product.MyProductsDetailsResult;
import com.bbyiya.pic.vo.product.MyProductsResult;
import com.bbyiya.pic.vo.product.ProductSampleResultVO;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.product.ProductSampleVo;
import com.bbyiya.vo.user.UChildInfoParam;
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
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;//影楼信息
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private UChildreninfoMapper childMapper;
	
	/*----------------pic-dao----------------------------------*/
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
		String keyName="productsample100_"+productId;
		List<ProductSampleResultVO> listResult=(List<ProductSampleResultVO>)RedisUtil.getObject(keyName);
		if(listResult==null||listResult.size()<=0){ 
			PProducts products= productsMapper.selectByPrimaryKey(productId);
			if(products!=null){
				listResult=productDao.findProductSamplesByProductId(productId);
				if(listResult!=null&&listResult.size()>0){
					for (ProductSampleResultVO sam : listResult) {
						sam.setMyWorks(getMyProductsResult(sam.getCartid()));  
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
				PMyproducts myproduct = null;// myMapper.getMyProductsByProductId(userId, param.getProductid(), Integer.parseInt(MyProductStatusEnum.ok.toString()));
				if (myproduct == null) {
					myproduct = new PMyproducts();
					myproduct.setAuthor(param.getAuthor());
					myproduct.setTitle(param.getTitle());
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
			UUsers user=usersMapper.selectByPrimaryKey(userId);
			if (param.getCartid() != null && param.getCartid() > 0) {// 更新
				cartIdTemp = param.getCartid();
				PMyproducts myproducts = myMapper.selectByPrimaryKey(param.getCartid());
				// A修改作品的宝宝信息
				if(myproducts != null && param.getChildInfo()!=null){
					boolean isnew=false;
					PMyproductchildinfo mychild=mychildMapper.selectByPrimaryKey(param.getCartid());
					if(mychild==null){
						mychild=new PMyproductchildinfo();
						mychild.setCartid(param.getCartid());
						mychild.setCreatetime(new Date());
						isnew=true;
					}
					if(!ObjectUtil.isEmpty(param.getChildInfo().getNickName())){
						mychild.setNickname(param.getChildInfo().getNickName());
					}
					if(!ObjectUtil.isEmpty(param.getChildInfo().getBirthday())){
						mychild.setBirthday(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", param.getChildInfo().getBirthday())) ;
					}
					if(isnew){
						mychildMapper.insert(mychild);
					}else {
						mychildMapper.updateByPrimaryKeySelective(mychild);
					} 
					/*----------------------------更新个人宝宝信息---------------------------------------------------*/
					boolean isHavechild=true;
					UChildreninfo childreninfo=childMapper.selectByPrimaryKey(userId);
					if(childreninfo==null){
						childreninfo=new UChildreninfo();
						childreninfo.setUserid(userId);
						childreninfo.setCreatetime(new Date());
						isHavechild=false;
					} 
					if(!ObjectUtil.isEmpty(mychild.getNickname())){
						childreninfo.setNickname(mychild.getNickname());
					}
					if(!ObjectUtil.isEmpty(mychild.getBirthday()) ){
						childreninfo.setBirthday(mychild.getBirthday());
					}
					if(isHavechild){
						childMapper.updateByPrimaryKey(childreninfo);
					}else {
						childMapper.insert(childreninfo);
					}
				}//----------------------------------------------------
				boolean canModify=false;
				//自己的作品
				if(myproducts != null && myproducts.getUserid() != null && myproducts.getUserid().longValue() == userId){
					canModify=true;
				}else {//受邀请 协同编辑的作品
					List<PMyproductsinvites> invlist= inviteMapper.findListByCartId(param.getCartid());
					if(invlist!=null&&invlist.size()>0){
						for (PMyproductsinvites in : invlist) {
							if(user!=null&&in.getInvitephone().equals(user.getMobilephone()))
								canModify=true;
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
					if (param.getDetails() != null && param.getDetails().size() > 0) {
						//检验 场景是否被选过
						List<PMyproductdetails> details=myDetaiMapper.findMyProductdetails(cartIdTemp);
						if(details!=null&&details.size()>0){//我的作品列表
							for (PMyproductdetails de : param.getDetails()) {
								if(de.getPdid()!=null&&de.getPdid()>0){
									for (PMyproductdetails myde : details) {
										if(de.getPdid().longValue()!=myde.getPdid().longValue()&& myde.getSceneid()!=null&&de.getSceneid()!=null&& myde.getSceneid().intValue()==de.getSceneid().intValue()){
											rq.setStatu(ReturnStatus.InvitError_1);
											rq.setStatusreson("此主题被协同人使用啦，请更换其他主题");
											return rq;
										} 
									}
								}
							}
						}
						for (PMyproductdetails de : param.getDetails()) {
							if(de.getPdid()!=null&&de.getPdid()>0){
								if(!ObjectUtil.isEmpty(de.getImgurl())){
									de.setUserid(userId); 
									myDetaiMapper.updateByPrimaryKeySelective(de);
								}
							}
						}
					}

					myproducts.setUpdatetime(new Date());
					// 更新用户作品基本信息
					myMapper.updateByPrimaryKeySelective(myproducts);
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
			//我的协同编辑作品
			List<MyProductResultVo> mylista=findInvites(user.getMobilephone());
			if(mylista!=null&&mylista.size()>0){
				list.addAll(mylista); 
			}
			
			//我的作品-制作中的
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

	public ReturnModel findMyProductsForBranch(Long branchUserId,Integer status,Integer inviteStatus, int index,int size){
		ReturnModel rq=new ReturnModel();
		List<Long> idsList=new ArrayList<Long>();
		idsList.add(branchUserId);
		//获取影楼的工作人员列表
		List<UBranchusers> userList= branchusersMapper.findMemberslistByBranchUserId(branchUserId);
		if(userList!=null&&userList.size()>0){
			for (UBranchusers uu : userList) {
				idsList.add(uu.getUserid());
			}
		}
		PageHelper.startPage(index, size);
		List<MyProductResultVo> mylist = myMapper.findMyProductslistForBranch(idsList, status, inviteStatus);
		PageInfo<MyProductResultVo> resultPage=new PageInfo<MyProductResultVo>(mylist); 
		if(resultPage.getList()!=null&&resultPage.getList().size()>0){
			for (MyProductResultVo vv : resultPage.getList()) {
				for (UBranchusers uu : userList) {
					if(uu.getUserid().longValue()==vv.getUserid().longValue()){
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
	

	public List<MyProductResultVo> findInvites(String  mobiePhone){
		List<PMyproductsinvites> inviteList=inviteMapper.findListByPhone(mobiePhone);
		if(inviteList!=null&&inviteList.size()>0){
			List<MyProductResultVo> resultList=new ArrayList<MyProductResultVo>();
			for (PMyproductsinvites in : inviteList) {
				MyProductResultVo vo=myMapper.getMyProductResultVo(in.getCartid()); 
				if(vo!=null){
					vo.setIsInvited(1);
					vo.setInvitestatus(in.getStatus()); 
					UUsers users=usersMapper.selectByPrimaryKey(vo.getUserid());
					if(users!=null){
						vo.setUserName(users.getMobilephone());
						if(ObjectUtil.isEmpty(users.getUserimg())){
							vo.setUserImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
						}else {
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
	 * @param mylist
	 * @return
	 */
	private List<MyProductResultVo> getMyProductResultVo(List<MyProductResultVo> mylist){
		if (mylist != null && mylist.size() > 0) {
			for (MyProductResultVo item : mylist) {
				if(!ObjectUtil.isEmpty(item.getUpdatetime())){
					item.setCreatetimestr(DateUtil.getTimeStr(item.getUpdatetime(), "yyyy-MM-dd HH:mm:ss")); 
				}else {
					item.setCreatetimestr(DateUtil.getTimeStr(item.getCreatetime(), "yyyy-MM-dd HH:mm:ss")); 
				}
				if(item.getInvitestatus()!=null&&item.getInvitestatus()>0){//邀请协同编辑
					List<PMyproductsinvites> invites= inviteMapper.findListByCartId(item.getCartid());
					if(invites!=null&&invites.size()>0){
						item.setInviteModel(invites.get(0)); 
					} 
				}
				// 作品详情（图片集合）
				List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(item.getCartid());
				int i = 0;
				if (detailslist != null && detailslist.size() > 0) {
					for (PMyproductdetails dd : detailslist) {
						if (!ObjectUtil.isEmpty(dd.getImgurl())) {
							if(dd.getSort()!=null&&dd.getSort().intValue()==0){
								item.setHeadImg("http://pic.bbyiya.com/"+dd.getImgurl()+"?imageView2/2/w/200");
							}
							i++;
						}
					}
				}
				if(ObjectUtil.isEmpty(item.getHeadImg())){
					item.setHeadImg("http://pic.bbyiya.com/484983733454448354.png"); 
				}
				item.setCount(i);
				if(item.getStatus()!=null&&item.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
					item.setIsOrder(1);
					item.setCount(12);
				} 
			}
		}
		return mylist;
	}

	public ReturnModel deleMyProduct(Long userId, Long cartId){
		ReturnModel rq=new ReturnModel();
		PMyproducts myproducts= myMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null&&myproducts.getUserid()!=null&&myproducts.getUserid().longValue()==userId){
			if(myproducts.getStatus()!=null&&myproducts.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
				if(!ObjectUtil.isEmpty(myproducts.getOrderno())){
					OUserorders order= orderMapper.selectByPrimaryKey(myproducts.getOrderno());
					if(order!=null&&order.getStatus()!=null&&order.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("作品关联的订单未上传成功，请先查看订单并重新上传！");
						return rq;
					}  
				}
			}
			
			if(myproducts.getInvitestatus()!=null&&myproducts.getInvitestatus()>0){
				inviteMapper.deleteByCartId(cartId);
			}
			mydetailDao.deleMyProductDetailsByCartId(cartId); 
			myMapper.deleteByPrimaryKey(cartId);
			
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
			if(myproduct!=null&&myproduct.getStatus()!=null&&myproduct.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
				myproduct.setIsOrder(1);
			} 
			boolean canModify=false;
			if(myproduct != null&&myproduct.getUserid().longValue()==userId){//自己的作品
				canModify=true;	
			}
			else {//是否是邀请的协同编辑人员
				List<PMyproductsinvites> invlist= inviteMapper.findListByCartId(cartId);
				if(invlist!=null&&invlist.size()>0){
					for (PMyproductsinvites in : invlist) {
						if(in.getInvitephone().equals(user.getMobilephone()))
							canModify=true;
					}
				}
			}
			if (canModify) {
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
				PMyproductchildinfo childInfo= mychildMapper.selectByPrimaryKey(cartId);
				if(childInfo!=null){
					UChildInfoParam childInfoParam=new UChildInfoParam();
					if(!ObjectUtil.isEmpty(childInfo.getNickname()) ){
						childInfoParam.setNickName(childInfo.getNickname());
					}
					if(!ObjectUtil.isEmpty(childInfo.getBirthday())){
						childInfoParam.setBirthday(DateUtil.getTimeStr(childInfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));   
					}
					myproduct.setChildInfo(childInfoParam);
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
			if(myproduct.getStatus()!=null&&myproduct.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
				myproduct.setIsOrder(1);
			}
			if(myproduct.getInvitestatus()!=null&&myproduct.getInvitestatus()>0){
				List<PMyproductsinvites> invites= inviteMapper.findListByCartId(cartId);
				if(invites!=null&&invites.size()>0){
					 UUsers  inviteUser= usersMapper.getUUsersByPhone(invites.get(0).getInvitephone()) ;
					 if(inviteUser!=null){
						myproduct.setInviteUserId(inviteUser.getUserid());  
					 }
				}
			}
			
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

				long type=styleId%2;
				List<Map<String, String>> backMaps=ConfigUtil.getMaplist("backcoordinate");
				if(backMaps!=null&&backMaps.size()>0){
					for (Map<String, String> mapBacks : backMaps) {
						if(ObjectUtil.parseLong(mapBacks.get("type"))==type){
							map.put("back-mod", mapBacks);
						}
					}
				}	
				Map<String, Object> mapWord=new HashMap<String, Object>();
				
				if(type==1){ //横版
					mapWord.put("size", 33);
					mapWord.put("color", "#595857");
					mapWord.put("lineHeight", 55);
					mapWord.put("letterSpacing", 5);
					
				}else {//竖版
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
