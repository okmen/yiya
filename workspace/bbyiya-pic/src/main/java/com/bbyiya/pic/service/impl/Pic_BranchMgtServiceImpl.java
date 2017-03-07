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
import com.bbyiya.dao.UAgentapplyMapper;
import com.bbyiya.dao.UAgentsMapper;
import com.bbyiya.dao.UBranchareapriceMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.AgentStatusEnum;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.RAreaplans;
import com.bbyiya.model.RAreaplansagentprice;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UAgents;
import com.bbyiya.model.UBranchareaprice;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.dao.IPic_AgentAreaDao;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;

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
	private UAgentapplyMapper agentapplyMapper;
	@Autowired
	private RAreaplansMapper areaplansMapper;
	@Autowired
	private RAreaplansagentpriceMapper areaplansagentpriceMapper;
	@Autowired
	private IPic_AgentAreaDao agentAreaDao;
	@Autowired
	private UAgentsMapper agentsMapper;
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UUsersMapper usersMapper;
	
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
	
	/**
	 * ����������
	 */
	public ReturnModel applyAgent(Long userId,UAgentapply applyInfo){
		ReturnModel rq=new ReturnModel();
		UAgentapply apply= agentapplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�����ύ�����룬�����ظ��ύ");
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
		if(ObjectUtil.isEmpty(apply.getIdcard())){
			rq.setStatusreson("���֤��Ϣ����Ϊ��");
			return rq;
		} 
		if(ObjectUtil.isEmpty(apply.getContactname())){
			rq.setStatusreson("��ϵ�˱�����");
			return rq;
		} 
		if(ObjectUtil.isEmpty(apply.getBusinesslicense())){
			rq.setStatusreson("Ӫҵִ�ձ�����");
			return rq;
		} 
		if(ObjectUtil.validSqlStr(applyInfo.getAgentcompanyname())
				||ObjectUtil.validSqlStr(applyInfo.getContactname())
				||ObjectUtil.validSqlStr(applyInfo.getStreetdetail())
				||ObjectUtil.validSqlStr(applyInfo.getIdcard())
				||ObjectUtil.validSqlStr(applyInfo.getBusinesslicense())
				||ObjectUtil.validSqlStr(applyInfo.getBusinessscope())
				||ObjectUtil.validSqlStr(applyInfo.getShopimg())
				||ObjectUtil.validSqlStr(applyInfo.getTeamimg())
				||ObjectUtil.validSqlStr(applyInfo.getRemark())
				){
			rq.setStatusreson("���ڷǷ��ַ�");
			return rq;
		}
		applyInfo.setAgentuserid(userId);
		applyInfo.setCreatetime(new Date());
		agentapplyMapper.insert(applyInfo);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("�ύ�ɹ����ȴ���ˣ�"); 
		return rq;
	}
	
	
	public ReturnModel applyBranch(Long userId,UBranches applyInfo){
		ReturnModel rq=new ReturnModel();
		UBranches apply= branchesMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�����ύ�����룬�����ظ��ύ");
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
		if(ObjectUtil.isEmpty(apply.getUsername())){
			rq.setStatusreson("��ϵ�˱�����");
			return rq;
		} 
		if(ObjectUtil.isEmpty(apply.getBusinesslicense())){
			rq.setStatusreson("Ӫҵִ�ձ�����");
			return rq;
		} 
		if(ObjectUtil.validSqlStr(applyInfo.getBranchcompanyname())
				||ObjectUtil.validSqlStr(applyInfo.getUsername())
				||ObjectUtil.validSqlStr(applyInfo.getStreetdetail())
				||ObjectUtil.validSqlStr(applyInfo.getBusinesslicense())
				||ObjectUtil.validSqlStr(applyInfo.getBusinessscope())
				||ObjectUtil.validSqlStr(applyInfo.getShopimg())
				||ObjectUtil.validSqlStr(applyInfo.getTeamimg())
				||ObjectUtil.validSqlStr(applyInfo.getRemark())
				){
			rq.setStatusreson("���ڷǷ��ַ�");
			return rq;
		}
		applyInfo.setBranchuserid(userId);
		applyInfo.setCreatetime(new Date());
		branchesMapper.insert(applyInfo);
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
			entryAgentInfo(apply);
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
	 * ������ͨ����ˣ�¼���������Ϣ��������Ӱ¥��Ϣ����������ݱ�ʶ
	 * @param apply
	 */
	public void entryAgentInfo(UAgentapply apply){
		if(apply!=null){
			//������¼��
			UAgents agentInfo=new UAgents();
			agentInfo.setAgentuserid(apply.getAgentuserid());
			agentInfo.setAgentcompanyname(apply.getAgentcompanyname());
			agentInfo.setContactname(apply.getContactname());
			agentInfo.setPhone(apply.getPhone());
			agentInfo.setProvince(apply.getProvince());
			agentInfo.setCity(apply.getCity());
			agentInfo.setArea(apply.getArea());
			agentInfo.setStreetdetail(apply.getStreetdetail());
			agentInfo.setStatus(Integer.parseInt(AgentStatusEnum.ok.toString()));
			agentInfo.setCreatetime(new Date());
			agentInfo.setProcesstime(new Date());
			agentsMapper.insert(agentInfo);
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
			branchesMapper.insert(branch);	
			//���´�����ݱ�ʶ
			userBasic.addUserIdentity(apply.getAgentuserid(),UserIdentityEnums.branch);  
		}
	}
	
	public ReturnModel getAgentApplyStatusModel(Long agentUserId){
		Map<String, Object> map=new HashMap<String, Object>();
		UAgentapply agentapply= agentapplyMapper.selectByPrimaryKey(agentUserId);
		if(agentapply!=null){
			map.put("isApplyed", 1);
			map.put("status", agentapply.getStatus());
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
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch!=null){
			map.put("isApplyed", 1);
			map.put("status", branch.getStatus());
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
	
	
}
