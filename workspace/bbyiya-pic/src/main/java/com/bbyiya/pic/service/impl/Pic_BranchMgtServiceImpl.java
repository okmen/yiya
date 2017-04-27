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

import com.bbyiya.dao.RAreaplansMapper;
import com.bbyiya.dao.RAreaplansagentpriceMapper;
import com.bbyiya.dao.RegionMapper;
import com.bbyiya.dao.SysMessageMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAdminactionlogsMapper;
import com.bbyiya.dao.UAgentapplyMapper;
import com.bbyiya.dao.UAgentsMapper;
import com.bbyiya.dao.UBranchareapriceMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUserresponsesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.AdminActionType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.AgentStatusEnum;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.RAreaplans;
import com.bbyiya.model.RAreaplansagentprice;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.SysMessage;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAdminactionlogs;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UAgents;
import com.bbyiya.model.UBranchareaprice;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUserresponses;
import com.bbyiya.pic.dao.IPic_AgentAreaDao;
import com.bbyiya.pic.dao.IPic_AgentMgtDao;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.pic.vo.agent.AgentSearchParam;
import com.bbyiya.pic.vo.agent.UAgentApplyVo;
import com.bbyiya.pic.vo.agent.UBranchVo;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pic_BranchMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_BranchMgtServiceImpl implements IPic_BranchMgtService{
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	//用户公共模块
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	
	@Autowired
	private UBranchareapriceMapper branchAreaMapper;
	//区域表
	@Autowired
	private RegionMapper regionMapper;
	@Autowired
	private RAreaplansMapper areaplansMapper;
	@Autowired
	private RAreaplansagentpriceMapper areaplansagentpriceMapper;
	@Autowired
	private UAgentapplyMapper agentapplyMapper;	
	@Autowired
	private IPic_AgentAreaDao agentAreaDao;
	@Autowired
	private UAgentsMapper agentsMapper;
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private IPic_AgentMgtDao agentDao;
	
	@Autowired
	private UBranchusersMapper branchuserMapper;
	
	@Autowired
	private UBranchtransaccountsMapper transaccountsMapper;//账户信息
	//------------------------用户信息-------------------
	@Autowired
	private UUsersMapper usersMapper;	
	@Autowired
	private UUserresponsesMapper userresponseMapper;//用户反馈
	@Autowired
	private UAccountsMapper accountsMapper;//账户信息
	@Autowired
	private SysMessageMapper sysMessageMapper;//系统消息
	@Autowired
	private UAdminactionlogsMapper adminlogMapper;//管理员日志信息
	
	
	public ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.Success);
		Map<String, Object> areaMap=new HashMap<String, Object>();
		Double priceTemp=1000d;
		if(province!=null){
			areaMap.put("province", province);
			areaMap.put("provinceName", regionService.getName(province));
			if(priceTemp<=1001d){
				UBranchareaprice mo1= branchAreaMapper.selectByPrimaryKey(province);
				if(mo1!=null){
					priceTemp=mo1.getPrice();
				}
			}
		}
		if(city!=null&&city>0){
			areaMap.put("city", city);
			areaMap.put("cityName", regionService.getName(city));
			if(priceTemp<=1001d){
				UBranchareaprice mo2= branchAreaMapper.selectByPrimaryKey(city);
				if(mo2!=null){
					priceTemp=mo2.getPrice();
				}	
			}
			
		}
		if(district!=null&&district>0){
			areaMap.put("district", city);
			areaMap.put("districtName", regionService.getName(district));
			if(priceTemp<=1001d){
				UBranchareaprice mo3= branchAreaMapper.selectByPrimaryKey(district);
				if(mo3!=null){
					priceTemp=mo3.getPrice();
				}
			}
		}
		areaMap.put("price", priceTemp);
		rqModel.setBasemodle(areaMap); 
		return rqModel;
	}
	
	public ReturnModel findAgentApplyList(AgentSearchParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		List<UAgentApplyVo> list=agentDao.findUAgentapplyVOList(param);
		rq.setBasemodle(list);
		return rq;
	}
	
	public ReturnModel findBranchVoList(AgentSearchParam param,int index, int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<UBranchVo> list=agentDao.findUBranchVoList(param);
		PageInfo<UBranchVo> result=new PageInfo<UBranchVo>(list); 
		for (UBranchVo branchvo : result.getList()) {
			Integer count=usersMapper.getUserCountByUpUserid(param.getUserId());
			branchvo.setUserCount(count==null?0:count);
			branchvo.setProviceName(regionService.getName(branchvo.getProvince())) ;
			branchvo.setCityName(regionService.getName(branchvo.getCity())) ;
			branchvo.setAreaName(regionService.getName(branchvo.getArea())) ;
			branchvo.setAgentArealist(getAgentArealist(branchvo.getArea()));  
		}
		
		rq.setBasemodle(result);
		return rq;
	}
	
	/**
	 * 代理商申请
	 */
	public ReturnModel applyAgent(Long userId,UAgentapply applyInfo){
		ReturnModel rq=new ReturnModel();
		UAgentapply apply= agentapplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			if(apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.ok.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("您已经是代理商了，不能提交申请！");
				return rq;
			}
//			rq.setStatu(ReturnStatus.SystemError);
//			rq.setStatusreson("您已提交过申请，不能重复提交");
//			return rq;
			applyInfo.setAgentuserid(apply.getAgentuserid());
		}
		rq.setStatu(ReturnStatus.SystemError);
		if(applyInfo==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getAgentcompanyname())){
			rq.setStatusreson("公司名称不能为空");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getIdcard())){
			rq.setStatusreson("身份证信息不能为空");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getContactname())){
			rq.setStatusreson("联系人必须填");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getBusinesslicense())){
			rq.setStatusreson("营业执照必须填");
			return rq;
		} 
		if(!ObjectUtil.validSqlStr(applyInfo.getAgentcompanyname())
				||!ObjectUtil.validSqlStr(applyInfo.getContactname())
				||!ObjectUtil.validSqlStr(applyInfo.getStreetdetail())
				||!ObjectUtil.validSqlStr(applyInfo.getIdcard())
				||!ObjectUtil.validSqlStr(applyInfo.getBusinesslicense())
				||!ObjectUtil.validSqlStr(applyInfo.getBusinessscope())
				||!ObjectUtil.validSqlStr(applyInfo.getShopimg())
				||!ObjectUtil.validSqlStr(applyInfo.getTeamimg())
				||!ObjectUtil.validSqlStr(applyInfo.getRemark())
				){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		applyInfo.setAgentuserid(userId);
		applyInfo.setCreatetime(new Date());
		applyInfo.setStatus(Integer.parseInt(AgentStatusEnum.applying.toString()));  
		if(apply!=null&&applyInfo.getAgentuserid()!=null&&applyInfo.getAgentuserid()>0){
			agentapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			agentapplyMapper.insert(applyInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("提交成功，等待审核！"); 
		return rq;
	}
	
	
	public ReturnModel applyBranch(Long userId,UBranches applyInfo){
		ReturnModel rq=new ReturnModel();
		UBranches apply= branchesMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			if(apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("您已经是合作商了，不能再次提交！");
				return rq;
			}
			applyInfo.setBranchuserid(apply.getBranchuserid());
//			rq.setStatu(ReturnStatus.SystemError);
//			rq.setStatusreson("您已提交过申请，不能重复提交");
//			return rq;
		}
		rq.setStatu(ReturnStatus.SystemError);
		if(applyInfo==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getBranchcompanyname())){
			rq.setStatusreson("公司名称不能为空");
			return rq;
		}
		if(applyInfo.getAgentuserid()==null||applyInfo.getAgentuserid()<=0){
			rq.setStatusreson("代理商咿呀号必须填");
			return rq;
		}

		UAgentapply agentapply= agentapplyMapper.selectByPrimaryKey(applyInfo.getAgentuserid());
		if(agentapply==null){
			rq.setStatusreson("找不到相应的代理商信息！");
			return rq;
		}
		if(agentapply.getAgentuserid().longValue()==applyInfo.getBranchuserid()){
			rq.setStatusreson("您已经提交过代理申请，不能再申请分店！");
			return rq;
		}
		//当前用户已经提交过代理申请
		UAgentapply agentBranchApply= agentapplyMapper.selectByPrimaryKey(applyInfo.getBranchuserid());
		if(agentBranchApply!=null){
			rq.setStatusreson("您已经提交过代理申请，不能再申请分店！");
			return rq;
		}
		List<Integer> agentArealist=getAgentAreaCodelist(agentapply.getArea());
		boolean isInArea=false;
		if(agentArealist!=null&&agentArealist.size()>0){
			for (Integer ss : agentArealist) {
				if(ss.intValue()==applyInfo.getArea()){
					isInArea=true;
				}
			}
		}
		if(!isInArea){
			rq.setStatusreson("对不起，门店不在代理区域！");
			return rq; 
		}
		if(ObjectUtil.isEmpty(applyInfo.getUsername())){
			rq.setStatusreson("联系人必须填");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getBusinesslicense())){
			rq.setStatusreson("营业执照必须填");
			return rq;
		} 
		if(!ObjectUtil.validSqlStr(applyInfo.getBranchcompanyname())
				||!ObjectUtil.validSqlStr(applyInfo.getUsername())
				||!ObjectUtil.validSqlStr(applyInfo.getStreetdetail())
				||!ObjectUtil.validSqlStr(applyInfo.getBusinesslicense())
				||!ObjectUtil.validSqlStr(applyInfo.getBusinessscope())
				||!ObjectUtil.validSqlStr(applyInfo.getShopimg())
				||!ObjectUtil.validSqlStr(applyInfo.getTeamimg())
				||!ObjectUtil.validSqlStr(applyInfo.getRemark())
				){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		applyInfo.setBranchuserid(userId);
		applyInfo.setCreatetime(new Date());
		applyInfo.setStatus(Integer.parseInt(BranchStatusEnum.applying.toString()));  
		if(apply!=null&&applyInfo.getBranchuserid()!=null&&applyInfo.getBranchuserid()>0){
			branchesMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			branchesMapper.insert(applyInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("提交成功，等待审核！"); 
		return rq;
	}
	
	
	/**
	 * 代理商审核
	 * @param adminId
	 * @param agentUserId
	 * @param status 1通过并已经交费，2不通过，3通过待交费
	 * @param msg
	 * @return
	 */
	public ReturnModel audit_AgentApply(Long adminId,Long agentUserId,int status,String msg){
		ReturnModel rq=new ReturnModel();
		UAgentapply apply= agentapplyMapper.selectByPrimaryKey(agentUserId); 
		if(apply!=null){
			apply.setStatus(status); 
			agentapplyMapper.updateByPrimaryKeySelective(apply);
			if(status==Integer.parseInt(AgentStatusEnum.ok.toString())){//成为代理
				RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(apply.getArea());
				if(areaplans!=null&&areaplans.getAreaid()!=null){//代理区域表有数据
					if(areaplans.getIsagent()!=null&&areaplans.getIsagent()>0){
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("该区域已经有代理("+areaplans.getAgentuserid()+")");
						return rq;
					}
					List<RAreaplans> arealist= agentAreaDao.findRAreaplansByAreaId(areaplans.getAreaid());
					if(arealist!=null&&arealist.size()>0){
						for (RAreaplans aa : arealist) {
							aa.setAgentuserid(agentUserId); 
							aa.setIsagent(1);
							areaplansMapper.updateByPrimaryKeySelective(aa);
						}
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("系统错误（101）");
					}
				}else {//不在规划内的区域（最低代理）
					RAreas area= regionMapper.getAreaByCode(apply.getArea());
					if(area!=null){
						//代理单元补充
						RAreaplansagentprice areaPlanModel=new RAreaplansagentprice();
						areaPlanModel.setAgentamount(0D);
						areaPlanModel.setStep(4);
						areaPlanModel.setPrepayamount(1000D);
						areaplansagentpriceMapper.insertResultId(areaPlanModel);
						//代理区域更新
						RAreaplans areaMod=new RAreaplans();
						areaMod.setAreacode(apply.getArea());
						areaMod.setAreaid(areaPlanModel.getAreaid());
						areaMod.setAreaname(area.getArea());
						areaMod.setIsagent(1);
						areaMod.setAgentuserid(agentUserId);
						areaplansMapper.insert(areaMod);
					}
				}
			}
			//代理商 申请信息复制到正式代理表
			this.addAgentInfo(apply);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("审核成功");
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请资料");
		} 
		return rq;
	}
	
	
	
	/**
	 * 影楼审核
	 */
	public ReturnModel audit_BranchApply(Long adminId,Long branchUserId,int status,String msg){
		ReturnModel rq=new ReturnModel();
		UBranches apply= branchesMapper.selectByPrimaryKey(branchUserId); 
		if(apply!=null){
			apply.setStatus(status);
			branchesMapper.updateByPrimaryKeySelective(apply);
			if(status==Integer.parseInt(BranchStatusEnum.ok.toString())){
				userBasic.addUserIdentity(branchUserId,UserIdentityEnums.branch); 
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("审核成功");
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请记录");
		} 
		return rq;
	}
	
	/**
	 * 代理商退驻
	 */
	public ReturnModel agentTuiZhu(String adminname,Long adminId,Long agentUserId){
		ReturnModel rq=new ReturnModel();
		UAgents agent=agentsMapper.selectByPrimaryKey(agentUserId);
		if(agent!=null){
			//1.代理商的影楼内部员工身份清除 ,清除身份后删除
			List<UBranchusers>  branchusersList=branchuserMapper.findMemberslistByAgentUserId(agentUserId);
			for (UBranchusers branchuser : branchusersList) {
				userBasic.removeUserIdentity(branchuser.getUserid(), UserIdentityEnums.salesman);
				branchuserMapper.deleteByPrimaryKey(branchuser.getUserid());
			}
			//2.代理区域清理
			List<RAreaplans> areaplansList=agentAreaDao.findRAreaplansByAgentUserId(agentUserId);
			for (RAreaplans areaplan : areaplansList) {
				areaplan.setAgentuserid(null);
				areaplan.setIsagent(null);
				areaplansMapper.updateByPrimaryKey(areaplan);
			}
			//3.影楼信息表清理 (u_branches)
			List<UBranchVo> branchList=agentDao.findUBranchVoListByAgentUserId(agentUserId);
			for (UBranchVo branch : branchList) {
				
				//3.1清理代理商运费账户表
				UBranchtransaccounts branchTransAccount=transaccountsMapper.selectByPrimaryKey(branch.getBranchuserid());
				if(branchTransAccount!=null){
					branchTransAccount.setAvailableamount(0.0);
					transaccountsMapper.updateByPrimaryKey(branchTransAccount);
				}
				//3.2. 清除代理商账户可用余额
				UAccounts count=accountsMapper.selectByPrimaryKey(branch.getBranchuserid());
				if(count!=null){
					count.setAvailableamount(0.0);
					accountsMapper.updateByPrimaryKey(count);
				}	
				//3.3修改影楼状态
				branch.setStatus(Integer.parseInt(BranchStatusEnum.tuizhu.toString()));
				//3.4修改影楼用户身份
				userBasic.removeUserIdentity(branch.getBranchuserid(), UserIdentityEnums.branch);
				branchesMapper.updateByPrimaryKey(branch);
			}
					
			
			//4.修改代理商的状态及用户身份
			userBasic.removeUserIdentity(agentUserId, UserIdentityEnums.agent);			
			agent.setStatus(Integer.parseInt(AgentStatusEnum.tuizhu.toString()));		
			agentsMapper.updateByPrimaryKeySelective(agent);
			
			//5.清理代理申请表
			UAgentapply agentApply=agentapplyMapper.selectByPrimaryKey(agentUserId);
			if(agentApply!=null){
				agentApply.setStatus(Integer.parseInt(AgentStatusEnum.tuizhu.toString()));
				agentapplyMapper.updateByPrimaryKeySelective(agentApply);
			}
			//6.插入cts日志表
			UAdminactionlogs log=new UAdminactionlogs();
			log.setContent("代理商退驻操作，agentUserId:"+agentUserId);
			log.setCreatetime(new Date());
			log.setType(Integer.parseInt(AdminActionType.agent_quit.toString()));
			log.setUserid(adminId);
			log.setUsername(adminname);
			adminlogMapper.insert(log);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("退驻成功");
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到代理商记录");
		} 
		return rq;
	}
	


	/**
	 * 代理商通过审核，录入代理商信息、代理商影楼信息，代理商身份标识
	 * @param apply
	 */
	public void addAgentInfo(UAgentapply apply){
		if(apply!=null){
			//代理商录入
			UAgents agentModel=new UAgents();
			agentModel.setAgentuserid(apply.getAgentuserid());
			agentModel.setAgentcompanyname(apply.getAgentcompanyname());
			agentModel.setContactname(apply.getContactname());
			agentModel.setPhone(apply.getPhone());
			agentModel.setProvince(apply.getProvince());
			agentModel.setCity(apply.getCity());
			agentModel.setArea(apply.getArea());
			agentModel.setStreetdetail(apply.getStreetdetail());
			agentModel.setStatus(Integer.parseInt(AgentStatusEnum.ok.toString()));
			agentModel.setCreatetime(new Date());
			agentModel.setProcesstime(new Date());
			agentsMapper.insertSelective(agentModel);
			//更新代理身份标识
			userBasic.addUserIdentity(apply.getAgentuserid(),UserIdentityEnums.agent); 
			
			//影楼录入
			UBranches branch= branchesMapper.selectByPrimaryKey(apply.getAgentuserid());
			if(branch==null){
				branch=new UBranches();
			}
			branch.setAgentuserid(apply.getAgentuserid());
			branch.setBranchuserid(apply.getAgentuserid());
			branch.setBranchcompanyname(apply.getAgentcompanyname());
			branch.setUsername(apply.getContactname());
			branch.setPhone(apply.getPhone());
			branch.setProvince(apply.getProvince());
			branch.setCity(apply.getCity());
			branch.setArea(apply.getArea());
			branch.setStreetdetail(apply.getStreetdetail());
			branch.setBusinesslicense(apply.getBusinesslicense());
			branch.setBusinessscope(apply.getBusinessscope());
			branch.setTeamimg(apply.getTeamimg());
			branch.setShopimg(apply.getShopimg());
			branch.setStatus(Integer.parseInt(BranchStatusEnum.ok.toString()));
			branch.setRemark(apply.getRemark());
			branch.setCreatetime(new Date());
			branch.setProcesstime(new Date());
			branchesMapper.insertSelective(branch);	
			//更新代理身份标识
			userBasic.addUserIdentity(apply.getAgentuserid(),UserIdentityEnums.branch);  
		}
	}
	
	
	public ReturnModel getAgentApplyStatusModel(Long agentUserId){
		Map<String, Object> map=new HashMap<String, Object>();
//		UAgentapply agentapply= agentapplyMapper.selectByPrimaryKey(agentUserId);
		UAgentApplyVo agentapply=agentDao.getUAgentapplyVOByAgentUserId(agentUserId);
		if(agentapply!=null){
			map.put("isApplyed", 1);
			map.put("status", agentapply.getStatus());
			agentapply.setProviceName(regionService.getName(agentapply.getProvince())) ;
			agentapply.setCityName(regionService.getName(agentapply.getCity())) ;
			agentapply.setAreaName(regionService.getName(agentapply.getArea())) ;
			agentapply.setAgentArealist(getAgentArealist(agentapply.getArea())); 
			map.put("applyInfo", agentapply);
			if(agentapply.getStatus()!=null){
				if(agentapply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.ok.toString())){
					map.put("msg", "已经成为代理商");
				}else if (agentapply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.applying.toString())) {
					map.put("msg", "申请中");
				}else if (agentapply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.no.toString())) {
					map.put("msg", "申请不通过。");
				}
			}else {
				map.put("msg", "申请中");
			}
		}else {
			map.put("isApplyed", 0);
		}
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	public ReturnModel getBranchApplyStatusModel(Long branchUserId){
		Map<String, Object> map=new HashMap<String, Object>();
//		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		UBranchVo branch= agentDao.getUBranchVoByBranchUserId(branchUserId);
		if(branch!=null){
			map.put("isApplyed", 1);
			map.put("status", branch.getStatus());
		
			branch.setProviceName(regionService.getName(branch.getProvince())) ;
			branch.setCityName(regionService.getName(branch.getCity())) ;
			branch.setAreaName(regionService.getName(branch.getArea())) ;
			branch.setAgentArealist(getAgentArealist(branch.getArea()));  
			
			map.put("applyInfo", branch);
			if(branch.getStatus()!=null){
				if(branch.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
					map.put("msg", "已经成为代理商");
				}else if (branch.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.applying.toString())) {
					map.put("msg", "申请中");
				}else if (branch.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.no.toString())) {
					map.put("msg", "申请不通过。");
				}
			}else {
				map.put("msg", "申请中");
			}
		}else {
			map.put("isApplyed", 0);
		}
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	private List<String> getAgentArealist(Integer areaCode){
		RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(areaCode);
		if(areaplans!=null){//区域在规划单元内
			List<RAreaplans> arealist= agentAreaDao.findRAreaplansByAreaId(areaplans.getAreaid());
			if(arealist!=null&&arealist.size()>0){
				List<String> areasList=new ArrayList<String>();
				for (RAreaplans rr : arealist) {
					areasList.add(rr.getAreaname());
				}
				return areasList;
			}
		}
		return null;
	}
	
	private List<String> getAgentArealistByAgentUserID(Long agentUserId){		
		List<RAreaplans> arealist= agentAreaDao.findRAreaplansByAgentUserId(agentUserId);
		if(arealist!=null&&arealist.size()>0){
			List<String> areasList=new ArrayList<String>();
			for (RAreaplans rr : arealist) {
				areasList.add(rr.getAreaname());
			}
			return areasList;
		}
		return null;
		
	}
	/**
	 * 代理区域codelist
	 * @param areaCode
	 * @return
	 */
	private List<Integer> getAgentAreaCodelist(Integer areaCode){
		RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(areaCode);
		if(areaplans!=null){//区域在规划单元内
			List<RAreaplans> arealist= agentAreaDao.findRAreaplansByAreaId(areaplans.getAreaid());
			if(arealist!=null&&arealist.size()>0){
				List<Integer> areasList=new ArrayList<Integer>();
				for (RAreaplans rr : arealist) {
					areasList.add(rr.getAreacode());
				}
				return areasList;
			}
		}
		return null;
	}
	
	public ReturnModel getAgentArea(Integer areaCode){
		ReturnModel rqModel=new ReturnModel();
		Map<String, Object> map=new HashMap<String, Object>();
		RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(areaCode);
		if(areaplans!=null){//区域在规划单元内
			RAreaplansagentprice areaplansagentprice= areaplansagentpriceMapper.selectByPrimaryKey(areaplans.getAreaid());
			List<RAreaplans> arealist= agentAreaDao.findRAreaplansByAreaId(areaplans.getAreaid());
			if(arealist!=null&&arealist.size()>0){
				List<String> areasList=new ArrayList<String>();
				for (RAreaplans rr : arealist) {
					areasList.add(rr.getAreaname());
				}
				if(areaplans.getIsagent()!=null&&areaplans.getIsagent()>0){
					map.put("isAgented", 1);
				}else {
					map.put("isAgented", 0);
					map.put("areas", areasList);
				}
				if(areaplansagentprice!=null){
					map.put("preAmount", areaplansagentprice.getPrepayamount());//预存费用
					map.put("agentAmount", areaplansagentprice.getAgentamount());//代理费
				}
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setBasemodle(map);
			} 
		}else {
			//找到地理区域（区县信息）
			RAreas areas= regionMapper.getAreaByCode(areaCode);
			if(areas!=null){
				//代理费用插入
				RAreaplansagentprice agenPrice=new RAreaplansagentprice();
				agenPrice.setPrepayamount(1000d);
				agenPrice.setStep(4);
				agenPrice.setAgentamount(0d);
				areaplansagentpriceMapper.insertResultId(agenPrice);
				//新增代理区域（4线城市及以下）
				RAreaplans plansArea=new RAreaplans();
				plansArea.setAreacode(areaCode);
				plansArea.setAreaid(agenPrice.getAreaid());
				plansArea.setAreaname(areas.getArea());
				areaplansMapper.insert(plansArea);
				
				map.put("isAgented", 0);
				List<String> areasList=new ArrayList<String>();
				areasList.add(areas.getArea());
				map.put("areas", areasList);
				map.put("preAmount", agenPrice.getPrepayamount());//预存费用
				map.put("agentAmount", agenPrice.getAgentamount());//代理费
				
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setBasemodle(map);
			}else {
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson("不存在的代理区域");
			}
		}
		return rqModel;
	}
	/**
	 * 修改代理商收货地址
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel editBranchAddress(Long branchUserId,String streetdetail){	
		ReturnModel rqModel=new ReturnModel();
		UBranches branch=branchesMapper.selectByPrimaryKey(branchUserId);
		branch.setStreetdetail(streetdetail);
		branchesMapper.updateByPrimaryKeySelective(branch);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("修改收货地址成功！");
		return rqModel;		
	}
	
	
	/**
	 * 获取代理商信息
	 * @param branchUserId
	 * @return
	 */
	public UBranchVo getBranchInfo(Long branchUserId){	
		UBranchVo branch=agentDao.getUBranchVoByBranchUserId(branchUserId);
		if(branch!=null){
			branch.setProviceName(regionService.getName(branch.getProvince())) ;
			branch.setCityName(regionService.getName(branch.getCity())) ;
			branch.setAreaName(regionService.getName(branch.getArea())) ;	
			branch.setAgentArealist(getAgentArealistByAgentUserID(branch.getAgentuserid())); 
		}		
		return branch;		
	}
	/**
	 * 添加意见反馈
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	public ReturnModel addUserResponses(Long branchUserId,String content){	
		ReturnModel rqModel=new ReturnModel();
		UUserresponses response=new UUserresponses();
		response.setUserid(branchUserId);
		response.setContent(content);
		response.setCreatetime(new Date());
		userresponseMapper.insertSelective(response);
		rqModel.setBasemodle(response);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("添加意见反馈成功！");
		return rqModel;		
	}
	
	/**
	 * 获取系统消息通知列表
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	public ReturnModel getSysMessageList(int index,int size,String startTimeStr,String endTimeStr){	
		ReturnModel rqModel=new ReturnModel();
		PageHelper.startPage(index, size);
		List<SysMessage> messagelist=sysMessageMapper.selectSysMessage(startTimeStr, endTimeStr);
		PageInfo<SysMessage> reuslt=new PageInfo<SysMessage>(messagelist); 
		rqModel.setBasemodle(reuslt);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("获取系统消息通知列表成功！");
		return rqModel;		
	}
	
	
}
