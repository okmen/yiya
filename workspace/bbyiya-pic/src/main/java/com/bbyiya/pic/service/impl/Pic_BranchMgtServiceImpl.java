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
	//�û�����ģ��
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	
	@Autowired
	private UBranchareapriceMapper branchAreaMapper;
	//�����
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
	private UBranchtransaccountsMapper transaccountsMapper;//�˻���Ϣ
	//------------------------�û���Ϣ-------------------
	@Autowired
	private UUsersMapper usersMapper;	
	@Autowired
	private UUserresponsesMapper userresponseMapper;//�û�����
	@Autowired
	private UAccountsMapper accountsMapper;//�˻���Ϣ
	@Autowired
	private SysMessageMapper sysMessageMapper;//ϵͳ��Ϣ
	@Autowired
	private UAdminactionlogsMapper adminlogMapper;//����Ա��־��Ϣ
	
	
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
	 * ����������
	 */
	public ReturnModel applyAgent(Long userId,UAgentapply applyInfo){
		ReturnModel rq=new ReturnModel();
		UAgentapply apply= agentapplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			if(apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.ok.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���Ѿ��Ǵ������ˣ������ύ���룡");
				return rq;
			}
//			rq.setStatu(ReturnStatus.SystemError);
//			rq.setStatusreson("�����ύ�����룬�����ظ��ύ");
//			return rq;
			applyInfo.setAgentuserid(apply.getAgentuserid());
		}
		rq.setStatu(ReturnStatus.SystemError);
		if(applyInfo==null){
			rq.setStatusreson("��������");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getAgentcompanyname())){
			rq.setStatusreson("��˾���Ʋ���Ϊ��");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getIdcard())){
			rq.setStatusreson("���֤��Ϣ����Ϊ��");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getContactname())){
			rq.setStatusreson("��ϵ�˱�����");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getBusinesslicense())){
			rq.setStatusreson("Ӫҵִ�ձ�����");
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
			rq.setStatusreson("���ڷǷ��ַ�");
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
		rq.setStatusreson("�ύ�ɹ����ȴ���ˣ�"); 
		return rq;
	}
	
	
	public ReturnModel applyBranch(Long userId,UBranches applyInfo){
		ReturnModel rq=new ReturnModel();
		UBranches apply= branchesMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			if(apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���Ѿ��Ǻ������ˣ������ٴ��ύ��");
				return rq;
			}
			applyInfo.setBranchuserid(apply.getBranchuserid());
//			rq.setStatu(ReturnStatus.SystemError);
//			rq.setStatusreson("�����ύ�����룬�����ظ��ύ");
//			return rq;
		}
		rq.setStatu(ReturnStatus.SystemError);
		if(applyInfo==null){
			rq.setStatusreson("��������");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getBranchcompanyname())){
			rq.setStatusreson("��˾���Ʋ���Ϊ��");
			return rq;
		}
		if(applyInfo.getAgentuserid()==null||applyInfo.getAgentuserid()<=0){
			rq.setStatusreson("��������ѽ�ű�����");
			return rq;
		}

		UAgentapply agentapply= agentapplyMapper.selectByPrimaryKey(applyInfo.getAgentuserid());
		if(agentapply==null){
			rq.setStatusreson("�Ҳ�����Ӧ�Ĵ�������Ϣ��");
			return rq;
		}
		if(agentapply.getAgentuserid().longValue()==applyInfo.getBranchuserid()){
			rq.setStatusreson("���Ѿ��ύ���������룬����������ֵ꣡");
			return rq;
		}
		//��ǰ�û��Ѿ��ύ����������
		UAgentapply agentBranchApply= agentapplyMapper.selectByPrimaryKey(applyInfo.getBranchuserid());
		if(agentBranchApply!=null){
			rq.setStatusreson("���Ѿ��ύ���������룬����������ֵ꣡");
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
			rq.setStatusreson("�Բ����ŵ겻�ڴ�������");
			return rq; 
		}
		if(ObjectUtil.isEmpty(applyInfo.getUsername())){
			rq.setStatusreson("��ϵ�˱�����");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getBusinesslicense())){
			rq.setStatusreson("Ӫҵִ�ձ�����");
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
			rq.setStatusreson("���ڷǷ��ַ�");
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
		rq.setStatusreson("�ύ�ɹ����ȴ���ˣ�"); 
		return rq;
	}
	
	
	/**
	 * ���������
	 * @param adminId
	 * @param agentUserId
	 * @param status 1ͨ�����Ѿ����ѣ�2��ͨ����3ͨ��������
	 * @param msg
	 * @return
	 */
	public ReturnModel audit_AgentApply(Long adminId,Long agentUserId,int status,String msg){
		ReturnModel rq=new ReturnModel();
		UAgentapply apply= agentapplyMapper.selectByPrimaryKey(agentUserId); 
		if(apply!=null){
			apply.setStatus(status); 
			agentapplyMapper.updateByPrimaryKeySelective(apply);
			if(status==Integer.parseInt(AgentStatusEnum.ok.toString())){//��Ϊ����
				RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(apply.getArea());
				if(areaplans!=null&&areaplans.getAreaid()!=null){//���������������
					if(areaplans.getIsagent()!=null&&areaplans.getIsagent()>0){
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("�������Ѿ��д���("+areaplans.getAgentuserid()+")");
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
						rq.setStatusreson("ϵͳ����101��");
					}
				}else {//���ڹ滮�ڵ�������ʹ���
					RAreas area= regionMapper.getAreaByCode(apply.getArea());
					if(area!=null){
						//����Ԫ����
						RAreaplansagentprice areaPlanModel=new RAreaplansagentprice();
						areaPlanModel.setAgentamount(0D);
						areaPlanModel.setStep(4);
						areaPlanModel.setPrepayamount(1000D);
						areaplansagentpriceMapper.insertResultId(areaPlanModel);
						//�����������
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
			//������ ������Ϣ���Ƶ���ʽ�����
			this.addAgentInfo(apply);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("��˳ɹ�");
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�Ҳ�����������");
		} 
		return rq;
	}
	
	
	
	/**
	 * Ӱ¥���
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
			rq.setStatusreson("��˳ɹ�");
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�Ҳ��������¼");
		} 
		return rq;
	}
	
	/**
	 * ��������פ
	 */
	public ReturnModel agentTuiZhu(String adminname,Long adminId,Long agentUserId){
		ReturnModel rq=new ReturnModel();
		UAgents agent=agentsMapper.selectByPrimaryKey(agentUserId);
		if(agent!=null){
			//1.�����̵�Ӱ¥�ڲ�Ա�������� ,�����ݺ�ɾ��
			List<UBranchusers>  branchusersList=branchuserMapper.findMemberslistByAgentUserId(agentUserId);
			for (UBranchusers branchuser : branchusersList) {
				userBasic.removeUserIdentity(branchuser.getUserid(), UserIdentityEnums.salesman);
				branchuserMapper.deleteByPrimaryKey(branchuser.getUserid());
			}
			//2.������������
			List<RAreaplans> areaplansList=agentAreaDao.findRAreaplansByAgentUserId(agentUserId);
			for (RAreaplans areaplan : areaplansList) {
				areaplan.setAgentuserid(null);
				areaplan.setIsagent(null);
				areaplansMapper.updateByPrimaryKey(areaplan);
			}
			//3.Ӱ¥��Ϣ������ (u_branches)
			List<UBranchVo> branchList=agentDao.findUBranchVoListByAgentUserId(agentUserId);
			for (UBranchVo branch : branchList) {
				
				//3.1����������˷��˻���
				UBranchtransaccounts branchTransAccount=transaccountsMapper.selectByPrimaryKey(branch.getBranchuserid());
				if(branchTransAccount!=null){
					branchTransAccount.setAvailableamount(0.0);
					transaccountsMapper.updateByPrimaryKey(branchTransAccount);
				}
				//3.2. ����������˻��������
				UAccounts count=accountsMapper.selectByPrimaryKey(branch.getBranchuserid());
				if(count!=null){
					count.setAvailableamount(0.0);
					accountsMapper.updateByPrimaryKey(count);
				}	
				//3.3�޸�Ӱ¥״̬
				branch.setStatus(Integer.parseInt(BranchStatusEnum.tuizhu.toString()));
				//3.4�޸�Ӱ¥�û����
				userBasic.removeUserIdentity(branch.getBranchuserid(), UserIdentityEnums.branch);
				branchesMapper.updateByPrimaryKey(branch);
			}
					
			
			//4.�޸Ĵ����̵�״̬���û����
			userBasic.removeUserIdentity(agentUserId, UserIdentityEnums.agent);			
			agent.setStatus(Integer.parseInt(AgentStatusEnum.tuizhu.toString()));		
			agentsMapper.updateByPrimaryKeySelective(agent);
			
			//5.������������
			UAgentapply agentApply=agentapplyMapper.selectByPrimaryKey(agentUserId);
			if(agentApply!=null){
				agentApply.setStatus(Integer.parseInt(AgentStatusEnum.tuizhu.toString()));
				agentapplyMapper.updateByPrimaryKeySelective(agentApply);
			}
			//6.����cts��־��
			UAdminactionlogs log=new UAdminactionlogs();
			log.setContent("��������פ������agentUserId:"+agentUserId);
			log.setCreatetime(new Date());
			log.setType(Integer.parseInt(AdminActionType.agent_quit.toString()));
			log.setUserid(adminId);
			log.setUsername(adminname);
			adminlogMapper.insert(log);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("��פ�ɹ�");
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�Ҳ��������̼�¼");
		} 
		return rq;
	}
	


	/**
	 * ������ͨ����ˣ�¼���������Ϣ��������Ӱ¥��Ϣ����������ݱ�ʶ
	 * @param apply
	 */
	public void addAgentInfo(UAgentapply apply){
		if(apply!=null){
			//������¼��
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
			//���´�����ݱ�ʶ
			userBasic.addUserIdentity(apply.getAgentuserid(),UserIdentityEnums.agent); 
			
			//Ӱ¥¼��
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
			//���´�����ݱ�ʶ
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
					map.put("msg", "�Ѿ���Ϊ������");
				}else if (agentapply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.applying.toString())) {
					map.put("msg", "������");
				}else if (agentapply.getStatus().intValue()==Integer.parseInt(AgentStatusEnum.no.toString())) {
					map.put("msg", "���벻ͨ����");
				}
			}else {
				map.put("msg", "������");
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
					map.put("msg", "�Ѿ���Ϊ������");
				}else if (branch.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.applying.toString())) {
					map.put("msg", "������");
				}else if (branch.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.no.toString())) {
					map.put("msg", "���벻ͨ����");
				}
			}else {
				map.put("msg", "������");
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
		if(areaplans!=null){//�����ڹ滮��Ԫ��
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
	 * ��������codelist
	 * @param areaCode
	 * @return
	 */
	private List<Integer> getAgentAreaCodelist(Integer areaCode){
		RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(areaCode);
		if(areaplans!=null){//�����ڹ滮��Ԫ��
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
		if(areaplans!=null){//�����ڹ滮��Ԫ��
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
					map.put("preAmount", areaplansagentprice.getPrepayamount());//Ԥ�����
					map.put("agentAmount", areaplansagentprice.getAgentamount());//�����
				}
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setBasemodle(map);
			} 
		}else {
			//�ҵ���������������Ϣ��
			RAreas areas= regionMapper.getAreaByCode(areaCode);
			if(areas!=null){
				//������ò���
				RAreaplansagentprice agenPrice=new RAreaplansagentprice();
				agenPrice.setPrepayamount(1000d);
				agenPrice.setStep(4);
				agenPrice.setAgentamount(0d);
				areaplansagentpriceMapper.insertResultId(agenPrice);
				//������������4�߳��м����£�
				RAreaplans plansArea=new RAreaplans();
				plansArea.setAreacode(areaCode);
				plansArea.setAreaid(agenPrice.getAreaid());
				plansArea.setAreaname(areas.getArea());
				areaplansMapper.insert(plansArea);
				
				map.put("isAgented", 0);
				List<String> areasList=new ArrayList<String>();
				areasList.add(areas.getArea());
				map.put("areas", areasList);
				map.put("preAmount", agenPrice.getPrepayamount());//Ԥ�����
				map.put("agentAmount", agenPrice.getAgentamount());//�����
				
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setBasemodle(map);
			}else {
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson("�����ڵĴ�������");
			}
		}
		return rqModel;
	}
	/**
	 * �޸Ĵ������ջ���ַ
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel editBranchAddress(Long branchUserId,String streetdetail){	
		ReturnModel rqModel=new ReturnModel();
		UBranches branch=branchesMapper.selectByPrimaryKey(branchUserId);
		branch.setStreetdetail(streetdetail);
		branchesMapper.updateByPrimaryKeySelective(branch);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("�޸��ջ���ַ�ɹ���");
		return rqModel;		
	}
	
	
	/**
	 * ��ȡ��������Ϣ
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
	 * ����������
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
		rqModel.setStatusreson("�����������ɹ���");
		return rqModel;		
	}
	
	/**
	 * ��ȡϵͳ��Ϣ֪ͨ�б�
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
		rqModel.setStatusreson("��ȡϵͳ��Ϣ֪ͨ�б�ɹ���");
		return rqModel;		
	}
	
	
}
