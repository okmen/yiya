package com.bbyiya.pic.web.user;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.pic.vo.product.MyProductTempVo;
import com.bbyiya.pic.vo.product.YiyeSubmitParam;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

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
						MyProductListVo myproduct= myproductDao.getMyProductByTempId(temp.getTempid(), user.getUserId());
						if(myproduct!=null){
							result.setIsInvited(1);
							result.setApplyStatus(Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString())); 
							result.setCartId(myproduct.getCartid());
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
