package com.bbyiya.pic.web.version_one;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAgentcustomersMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.pic.IBaseDiscountService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/myProduct/invite")
public class InviteMgtController  extends SSOController {
	@Resource(name = "pic_myProductService")
	private IPic_myProductService myProductService;
	
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private PMyproducttempapplyMapper tempApplyMapper;
	@Autowired
	private PMyproductsMapper myproductsMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;
	@Autowired
	private UAgentcustomersMapper customerMapper;
	@Autowired
	private UAccountsMapper accountMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	/**
	 * 优惠信息
	 */
	@Resource(name = "baseDiscountServiceImpl")
	private IBaseDiscountService discountService;
	
	/**
	 * 发送 协同编辑 邀请
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sendInvite")
	public String sendInvite(String phone,Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(phone.equals(user.getMobilePhone())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不能邀请自己协同编辑！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=myProductService.sendInvite(user.getUserId(), phone, cartId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 是否活动失败
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checkAct")
	public String processInvite(Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PMyproducttempapply apply= tempApplyMapper.getMyProducttempApplyByCartId(cartId);
			if(apply!=null&&apply.getUserid()!=null&&apply.getUserid().longValue()==user.getUserId().longValue()){
				//异业合作模板
				PMyproducttemp temp=tempMapper.selectByPrimaryKey(apply.getTempid());
				if(temp!=null){
					//如果已达到活动目标完成人数，则自动置为活动失败状态，C端提示活动名额已满，可享受半价优惠
					if(temp.getMaxcompletecount()!=null&&temp.getMaxcompletecount().intValue()>0){
						if(temp.getCompletecount()!=null&&temp.getCompletecount().intValue()>=temp.getMaxcompletecount().intValue()){
							apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.fails.toString()));
							tempApplyMapper.updateByPrimaryKeySelective(apply);
							//活动失败的参与作品 分发优惠
							if(apply.getCartid()!=null&&apply.getCartid().longValue()>0){
								discountService.addTempDiscount(apply.getCartid());
							}
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("对不起，活动名额已满，不要灰心您仍然可以享受优惠购买！");
							return JsonUtil.objectToJsonStr(rq);
						}
					}
				}	
			}
		}
		rq.setStatu(ReturnStatus.Success); 
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 处理我的邀请(已完成活动)
	 * @param phone
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/processInvite")
	public String processInvite(Long cartId,Integer status,String addressId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status!=null&&status==Integer.parseInt(InviteStatus.ok.toString())){
				//更新用户活动收获地址信息
				long userAddressId=ObjectUtil.parseLong(addressId);
				if(userAddressId>0){
					UUseraddress address=addressMapper.get_UUserAddressByKeyId(userAddressId);
					if(address==null){
						rq.setStatusreson("地址信息不存在！");
						return JsonUtil.objectToJsonStr(rq);
					}
					PMyproducttempapply apply= tempApplyMapper.getMyProducttempApplyByCartId(cartId);
					if(apply==null){
						PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
						if(myproducts!=null&&myproducts.getTempid()!=null){
							apply=tempApplyMapper.getMyProducttempApplyByUserId(myproducts.getTempid(), user.getUserId());
						}
					}
					if(apply!=null){
						if(apply.getUserid().longValue()!=user.getUserId().longValue()){
							rq.setStatusreson("非申请本人无法提交！"); 
							return JsonUtil.objectToJsonStr(rq);
						}
						apply.setReceiver(address.getReciver()); 
						apply.setMobilephone(address.getPhone());
						apply.setProvince(address.getProvince());
						apply.setCity(address.getCity());
						apply.setStreet(address.getStreetdetail());
						apply.setArea(address.getArea());
						apply.setAdress(regionService.getProvinceName(address.getProvince())+regionService.getCityName(address.getCity())+regionService.getAresName(address.getArea())+address.getStreetdetail());
												
						//异业合作模板
						PMyproducttemp temp=tempMapper.selectByPrimaryKey(apply.getTempid());
						if(temp!=null) {
							
//							//如果已达到活动目标完成人数，则自动置为活动失败状态，C端提示活动名额已满，可享受半价优惠
//							if(temp.getMaxcompletecount()!=null&&temp.getMaxcompletecount().intValue()>0){
//								if(temp.getCompletecount()!=null&&temp.getCompletecount().intValue()>=temp.getMaxcompletecount().intValue()){
//									apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.fails.toString()));
//									//活动失败的参与作品 分发优惠
//									if(apply.getCartid()!=null&&apply.getCartid().longValue()>0){
//										discountService.addTempDiscount(apply.getCartid());
//									}
//									rq.setStatu(ReturnStatus.ParamError);
//									rq.setStatusreson("对不起，活动名额已满，不要灰心您仍然可以享受优惠购买！");
//									return JsonUtil.objectToJsonStr(rq);
//								}
//							}	
							//冻结众筹金额
							if(temp.getAmountlimit()!=null&&temp.getAmountlimit()>0){
								UAccounts accounts=accountMapper.selectByPrimaryKey(user.getUserId());
								//账户可用金额
								double amounts= accounts==null?0d:(accounts.getAvailableamount()==null?0d:accounts.getAvailableamount().doubleValue());
								double freezecashamount=accounts==null?0d:(accounts.getFreezecashamount()==null?0d:accounts.getFreezecashamount().doubleValue());
								if (amounts < temp.getAmountlimit().doubleValue()) {
									rq.setStatu(ReturnStatus.ParamError);
									rq.setStatusreson("众筹金额不足！");
									return JsonUtil.objectToJsonStr(rq);
								}
								accounts.setFreezecashamount(freezecashamount+temp.getAmountlimit().doubleValue());
								accounts.setAvailableamount(amounts - temp.getAmountlimit().doubleValue());
								accountMapper.updateByPrimaryKeySelective(accounts);
							}
							UAgentcustomers customer= customerMapper.getCustomersByBranchUserId(temp.getBranchuserid(), user.getUserId());
							if(customer!=null){
								if(ObjectUtil.isEmpty(customer.getPhone())){
									customer.setPhone(address.getPhone());
								}
								if(ObjectUtil.isEmpty(customer.getAddress())){
									customer.setAddress(apply.getAdress()); 
									customerMapper.updateByPrimaryKeySelective(customer);
								}
							}
						}
						//更新申请状态
						tempApplyMapper.updateByPrimaryKeySelective(apply);
					}
				}//收获地址信息（完）------------------
				
				rq=myProductService.processInvite(cartId,user.getUserId(), status);
			}else {
				//旧版的状态更新
				rq=myProductService.processInvite(user.getMobilePhone(),cartId, status);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 处理扫码页面的接受邀请
	 * @param phone 被邀请人手机号
	 * @param cartId 作品cartid
	 * @param vcode  验证码
	 * @param needVerfiCode  是否需要验证手机验证码
	 * @param version  二维码版本号 可为空
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/acceptScanQrCodeInvite")
	public String acceptScanQrCodeInvite(String phone,Long cartId,String vcode,Integer needVerfiCode,String version) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PMyproducts myproduct=myProductService.getPMyproducts(cartId);
			if(myproduct==null){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("不存在的作品");
				return JsonUtil.objectToJsonStr(rq);
			}
			//如果是模板作品
			if(myproduct.getIstemp()!=null&&myproduct.getIstemp().toString().equals("1")){
				rq=myProductService.acceptTempScanQrCodeInvite(user.getUserId(), phone, cartId,vcode,needVerfiCode);
			}else{
				rq=myProductService.acceptScanQrCodeInvite(user.getUserId(),phone,cartId,vcode,needVerfiCode,version);	
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 获取用户提示信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myUserInfoExp")
	public String myUserInfoExp() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=myProductService.myUserInfoExp(user.getUserId(), user.getMobilePhone()); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
