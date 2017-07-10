package com.bbyiya.pic.web.version_one;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UBranchinfotempMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UAgentapplyareas;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchinfotemp;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.pic.vo.agent.UBranchVo;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/ibs/branch")
public class BranchMgtController extends SSOController {
	@Resource(name = "pic_BranchMgtService")
	private IPic_BranchMgtService branchService;
	@Resource(name = "pic_productService")
	private IPic_ProductService proService;
	@Autowired
	private UBranchinfotempMapper tempMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	@Autowired
	private EErrorsMapper logger;
	@Autowired
	private UUsersMapper userMapper;
	/**
	 * B01 招商报名
	 * @param companyJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/companySubmit")
	public String companySubmit( String companyJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		try {
			
			UBranchinfotemp model= Json2Objects.getParam_UBranchinfotemp(companyJson);// (UBranchinfotemp)JsonUtil.jsonStrToObject(companyJson, UBranchinfotemp.class);
			if(model!=null){
				if(ObjectUtil.isEmpty(model.getPhone())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("手机号不能为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(model.getContactname())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("联系人不能为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(model.getCompanyname())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("公司信息不能为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(!(ObjectUtil.validSqlStr(model.getPhone())&&ObjectUtil.validSqlStr(model.getCompanyname())&&ObjectUtil.validSqlStr(model.getContactname()))){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("存在非法字符");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数错误");
				return JsonUtil.objectToJsonStr(rq);
			}
			model.setCreatetime(new Date()); 
			tempMapper.insertSelective(model);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("提交成功！");
		} catch (Exception e) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误001");
			return JsonUtil.objectToJsonStr(rq);
		} 
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 根据地区获取代理金额
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getBranchAreaPrice")
	public String getBranchAreaPrice(String province,String city,String district) throws Exception {
		ReturnModel rq = branchService.getBranchAreaPrice(ObjectUtil.parseInt(province) , ObjectUtil.parseInt(city), ObjectUtil.parseInt(district));
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 *  代理商申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checkAreaCodeIsApply")
	public String checkAreaCodeIsApply(String areacode) throws Exception {
		ReturnModel rq=new ReturnModel();
		boolean isApply=branchService.checkAreaCodeIsApply(ObjectUtil.parseInt(areacode));
		if(isApply){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("该区域已被代理");
		}else{
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("该区域可以代理");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
		
	/**
	 *  代理商申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/agentApplyNew")
	public String agentApplynew(String agentJson,String areacodeJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					UAgentapply applyInfo=(UAgentapply)JsonUtil.jsonStrToObject(agentJson, UAgentapply.class);
					List<UAgentapplyareas> arealist=Json2Objects.getParam_AgentApplyareas(areacodeJson);
					rq =branchService.applyAgentNew(user.getUserId(), applyInfo,arealist);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("未完成注册");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 *  代理商申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/agentApply")
	public String agentApply(String agentJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					UAgentapply applyInfo=(UAgentapply)JsonUtil.jsonStrToObject(agentJson, UAgentapply.class);
					rq =branchService.applyAgent(user.getUserId(), applyInfo);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("未完成注册");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * cts协助代理商 提交申请
	 * @param agentJson
	 * @param branchUserId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cts_agentApplyNew")
	public String cts_agentApplyNew(String agentJson,String areacodeJson,String branchUserId) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				try {
					UUsers branchUsers= userMapper.getUUsersByUserID(ObjectUtil.parseLong(branchUserId));
					if(branchUsers!=null){
						if(!ObjectUtil.isEmpty(branchUsers.getMobilephone())){
							UAgentapply applyInfo=(UAgentapply)JsonUtil.jsonStrToObject(agentJson, UAgentapply.class);
							List<UAgentapplyareas> arealist=Json2Objects.getParam_AgentApplyareas(areacodeJson);
							
							rq =branchService.applyAgentNew(ObjectUtil.parseLong(branchUserId), applyInfo,arealist);
						}else {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("用户未绑定手机号！");
						}
					}else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("找不到用户！（"+branchUserId+"可能是无效用户）");
					}
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("无此权限");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * cts协助代理商 提交申请
	 * @param agentJson
	 * @param branchUserId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cts_agentApply")
	public String cts_agentApply(String agentJson,long branchUserId) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				try {
					UUsers branchUsers= userMapper.getUUsersByUserID(branchUserId);
					if(branchUsers!=null){
						if(!ObjectUtil.isEmpty(branchUsers.getMobilephone())){
							UAgentapply applyInfo=(UAgentapply)JsonUtil.jsonStrToObject(agentJson, UAgentapply.class);
							rq =branchService.applyAgent(branchUserId, applyInfo);
						}else {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("用户未绑定手机号！");
						}
					}else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("找不到用户！（"+branchUserId+"可能是无效用户）");
					}
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("无此权限");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 *  分店申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/branchApplyNew")
	public String branchApplyNew(String branchJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					UBranches applyInfo=(UBranches)JsonUtil.jsonStrToObject(branchJson, UBranches.class);
					if(applyInfo!=null){
						applyInfo.setBranchuserid(user.getUserId()); 
					}
					rq =branchService.applyBranchNew(user.getUserId(), applyInfo);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					System.out.println(e); 
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("未完成注册");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 *  分店申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/branchApply")
	public String branchApply(String branchJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					UBranches applyInfo=(UBranches)JsonUtil.jsonStrToObject(branchJson, UBranches.class);
					if(applyInfo!=null){
						applyInfo.setBranchuserid(user.getUserId()); 
					}
					rq =branchService.applyBranch(user.getUserId(), applyInfo);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					System.out.println(e); 
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("未完成注册");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取代理单元（根据县区判断代理单元）
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAgentAreas")
	public String getAgentAreas(Integer areaCode) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.getAgentArea(areaCode);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 判断用户代理商申请状态
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getApplyStatus")
	public String getApplyStatus(Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(type!=null&&type==1){
				rq=branchService.getAgentApplyStatusModel(user.getUserId());
			}else {
				rq=branchService.getBranchApplyStatusModel(user.getUserId());
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 登陆后得到代理商信息
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getBranchInfo")
	public String getBranchInfo() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UBranchVo branch=branchService.getBranchInfo(user.getUserId());	
			rq.setBasemodle(branch);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("获取代理商信息成功！");
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 修改代理商收货地址
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editBranchAddress")
	public String editBranchAddress(String streetdetail,String name,String phone ) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.editBranchAddress(user.getUserId(), streetdetail,name,phone);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("修改代理商信息成功！");
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 代理商新增意见反馈
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addUserResponses")
	public String addUserResponses(String content) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.addUserResponses(user.getUserId(), content);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * IBS获取系统消息通知列表
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getSysMessageList")
	public String getSysMessageList(int index,int size,String startTimeStr,String endTimeStr) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.getSysMessageList(index, size, startTimeStr, endTimeStr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
