package com.bbyiya.pic.web.user;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PMyproducttempusersMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PMyproducttempusers;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.pic.vo.product.MyProductTempVo;
import com.bbyiya.pic.vo.product.YiyeSubmitParam;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/myProduct/yiye")
public class YiyeMgtController  extends SSOController {
	
	
	@Autowired
	private PMyproducttempMapper tempMapper;
	@Autowired
	private PMyproductsMapper myProductMapper;
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private PMyproducttempapplyMapper tempApplyMapper;
	@Autowired
	private IMyProductsDao myproductDao;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	@Resource(name = "ibs_MyProductTempService")
	private IIbs_MyProductTempService ibs_tempService;
	@Autowired
	private PMyproducttempusersMapper tempUsrMapper;
	
	
	@ResponseBody
	@RequestMapping(value = "/detail")
	public String detail(String workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			MyProductTempVo result=new MyProductTempVo();
			long cartid=ObjectUtil.parseLong(workId);
			if(cartid>0){
				PMyproducts mycart= myProductMapper.selectByPrimaryKey(cartid);
				if(mycart!=null){
					PMyproducttemp temp= tempMapper.selectByPrimaryKey(mycart.getTempid());
					if(temp!=null){
						List<MyProductListVo> myproductList= myproductDao.getMyProductByTempId(temp.getTempid(), user.getUserId());
						if(myproductList!=null&&myproductList.size()>0){
							result.setIsInvited(1);
							result.setApplyStatus(Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString())); 
							result.setCartId(myproductList.get(0).getCartid());
						}else {
							result.setTemp(temp); 
							PMyproducttempapply apply= tempApplyMapper.getMyProducttempApplyByUserId(temp.getTempid(), user.getUserId());
							if(apply!=null){
								result.setApplyStatus(apply.getStatus());
							}else {
								result.setApplyStatus(-1); 
							}
						} 
						rq.setStatu(ReturnStatus.Success);
						rq.setBasemodle(result);
						return JsonUtil.objectToJsonStr(rq);
					}
				}
			}
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误"); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 接受异业模板邀请 、申请模板相册
	 * @param commentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/applyOrAccept")
	public String access(String commentJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			YiyeSubmitParam param = getParam_YiyeSubmitParam(commentJson);
			if (param != null) {
				if(param.getCartId()<=0){
					rq.setStatusreson("作品信息为空！");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(param.getDateTime())){
					rq.setStatusreson("宝宝生日/预产期不能为空！");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(param.getAddressId()<=0){
					rq.setStatusreson("地址信息不能为空！");
					return JsonUtil.objectToJsonStr(rq);
				}
				UUseraddress address=addressMapper.get_UUserAddressByKeyId(param.getAddressId());
				if(address==null){
					rq.setStatusreson("地址信息不存在！");
					return JsonUtil.objectToJsonStr(rq);
				}
				PMyproducts myproducts= myProductMapper.selectByPrimaryKey(param.getCartId());
				if(myproducts!=null&&myproducts.getIstemp()!=null&&myproducts.getTempid()!=null){
					PMyproducttemp temp= tempMapper.selectByPrimaryKey(myproducts.getTempid());
					if(temp!=null){
						/*--------------------已经提交过申请---------------------------------------*/
						PMyproducttempapply applyOld= tempApplyMapper.getMyProducttempApplyByUserId(temp.getTempid(), user.getUserId());
						if(applyOld!=null){
							if(applyOld.getStatus()!=null&&applyOld.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString())){
								List<MyProductListVo>list= myproductDao.getMyProductByTempId(temp.getTempid(), user.getUserId());
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("tempId", myproducts.getTempid());
								map.put("mycartid", list.get(0).getCartid());
								rq.setBasemodle(map); 
							}
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("提交申请成功！");
							return JsonUtil.objectToJsonStr(rq);
						}/*-----------------------------------------------------------------------*/
						
						//提交申请
						PMyproducttempapply apply=new PMyproducttempapply();
						apply.setTempid(myproducts.getTempid());
						apply.setUserid(user.getUserId());
						apply.setMobilephone(address.getPhone());
						apply.setProvince(address.getProvince());
						apply.setCity(address.getCity());
						apply.setStreet(address.getStreetdetail());
						apply.setArea(address.getArea());
						apply.setAdress(regionService.getProvinceName(address.getProvince())+regionService.getCityName(address.getCity())+regionService.getAresName(address.getArea())+address.getStreetdetail());
						if(param.getDateTime().getTime()>(new Date()).getTime()){
							apply.setIsdue(1);
						}
						apply.setBirthday(param.getDateTime());
						apply.setCreatetime(new Date());
						apply.setCompanyuserid(param.getSubUserId());
						
						boolean isNeedVer=false;
						if(temp.getNeedverifer()!=null&&temp.getNeedverifer().intValue()>0){
							//需要审核
							apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
							isNeedVer=true;
						}else {
							//不需要审核
							apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString()));
						}
						tempApplyMapper.insert(apply);
						
						if(!isNeedVer){//TODO 不需要审核 调取 新增作品、客户信息
							rq=ibs_tempService.doAcceptOrAutoTempApplyOpt(apply);
						}else {
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("提交申请成功！");
						}
					}
				}else {
					rq.setStatusreson("此活动已失效！");
					return JsonUtil.objectToJsonStr(rq); 
				}
			} else {
				rq.setStatusreson("参数不全");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 员工 -异业合作 模板 推广列表
	 */
	@ResponseBody
	@RequestMapping(value = "/mytemplist")
	public String templist() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<PMyproducttempusers>list=tempUsrMapper.findTemplistBySunUserId(user.getUserId(), 1);
			if(list!=null&&list.size()>0){
				for (PMyproducttempusers item : list) {
					PMyproducttemp temp= tempMapper.selectByPrimaryKey(item.getTempid());
					if(temp!=null){
						item.setTempStatus(temp.getStatus()); 
						item.setTempName(temp.getTitle()); 
						String redirct_url="apply/form?workId="+URLEncoder.encode(temp.getCartid().toString(),"utf-8")+"&uid="+URLEncoder.encode(user.getUserId().toString(),"utf-8");
						redirct_url=redirct_url+"&sid="+URLEncoder.encode(user.getUserId().toString(),"utf-8");
						String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
						urlstr="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+urlstr;
						item.setqRcodeUrl(urlstr);
					}
					
				}
				rq.setBasemodle(list);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/branchtemplist")
	public String branchtemplist(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			PageHelper.startPage(index, size);
			List<PMyproducttemp>list=tempMapper.findBranchMyProductTempList(user.getUserId());
			PageInfo<PMyproducttemp> resultPage = new PageInfo<PMyproducttemp>(list);
			rq.setBasemodle(resultPage); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 待审核、已通过、未通过 列表
	 * @param tempid
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/applylist")
	public String applylist(int tempid,int status, @RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<PMyproducttempapply>  applylist=tempApplyMapper.findMyProducttempApplyList(tempid, status);
			PageInfo<PMyproducttempapply> reuslt=new PageInfo<PMyproducttempapply>(applylist); 
			if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){
				for (PMyproducttempapply apply : applylist) {
					apply.setCreatetimestr(DateUtil.getTimeStr(apply.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
					if(!ObjectUtil.isEmpty(apply.getVerfiytime())){
						apply.setVerfiytimestr(DateUtil.getTimeStr(apply.getVerfiytime(), "yyyy-MM-dd HH:mm:ss"));
					}
					if(!ObjectUtil.isEmpty(apply.getBirthday())){
						apply.setBirthdaystr(DateUtil.getTimeStr(apply.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
					}
				}
				rq.setBasemodle(reuslt);
			}		
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 我参与的活动列表
	 * @param tempid
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/mytempcartlist")
	public String mytempcartlist(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<PMyproducttempapply>  applylist=tempApplyMapper.findMyProducttempApplyByUserId(user.getUserId());
			PageInfo<PMyproducttempapply> reuslt=new PageInfo<PMyproducttempapply>(applylist); 
			rq.setBasemodle(reuslt);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 消息已读
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateTempRead")
	public String updateTempRead() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<PMyproducttempapply>  applylist=tempApplyMapper.findMyProducttempApplyByUserId(user.getUserId());
			if(applylist!=null&&applylist.size()>0){
				for (PMyproducttempapply ss : applylist) {
					if(ss.getIsread()==null||ss.getIsread().intValue()!=1){
						ss.setIsread(1);
						tempApplyMapper.updateByPrimaryKeySelective(ss);
					}
				}
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	public static YiyeSubmitParam getParam_YiyeSubmitParam(String json) {
		JSONObject model = JSONObject.fromObject(json);
		if (model != null) {
			YiyeSubmitParam param = new YiyeSubmitParam();
			long cartid=ObjectUtil.parseLong(String.valueOf(model.get("cartId")));
			if(cartid>0){
				param.setCartId(cartid);
			}
			String version =String.valueOf(model.get("version"));
			if(!(ObjectUtil.isEmpty(version)||"null".equals(version))){
				param.setVersion(version);
			}
			long addressId=ObjectUtil.parseLong(String.valueOf(model.get("addressId")));
			if(addressId>0){
				param.setAddressId(addressId);
			}
			long subUserId=ObjectUtil.parseLong(String.valueOf(model.get("subUserId")));
			if(subUserId>0){
				param.setSubUserId(subUserId);
			} 
			String dateTimeStr =String.valueOf(model.get("dateTimeStr"));
			long dateVal=ObjectUtil.parseLong(String.valueOf(model.get("dateTimeVal")));
			if(!(ObjectUtil.isEmpty(dateTimeStr)||"null".equals(dateTimeStr))){
				param.setDateTimeStr(dateTimeStr);
				param.setDateTime(DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", dateTimeStr));
			}else if (dateVal>0) {
				param.setDateTime(DateUtil.getDate(dateVal, "yyyy-MM-dd HH:mm:ss")); 
				param.setDateTimeVal(dateVal); 
			}
			return param;
		}
		return null; 
	}
	
}
