package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.UAgentcustomersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.CustomerSourceTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.agent.UAgentcustomersVo;

@Service("pic_memberMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_MemberMgtServiceImpl implements IPic_MemberMgtService{
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	
	@Autowired
	private UBranchusersMapper branchusersMapper;
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UAgentcustomersMapper customerMapper;
	@Autowired
	private UChildreninfoMapper childMapper;
	
	@Autowired
	private OOrderproductsMapper orderproductMapper;
	
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private IMyProductsDao myProductsDao;
	@Autowired
	private PMyproducttempMapper tempMapper;
	
	public ReturnModel findBranchUserslistByBranchUserId(Long branchUserId){
		ReturnModel rqModel=new ReturnModel();
		List<UBranchusers> list= branchusersMapper.findMemberslistByBranchUserId(branchUserId);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setBasemodle(list);
		return rqModel;
	}
	
	
	
	public ReturnModel addBranchUser(Long branchUserId,UBranchusers param){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		if(param==null){
			rqModel.setStatusreson("��������");
			return rqModel;
		}
		if(ObjectUtil.isEmpty(param.getPhone())){ 
			rqModel.setStatusreson("�ֻ��Ų���Ϊ��");
			return rqModel;
		}
		//���Ӱ¥���
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null||branch.getStatus()==null||branch.getStatus().intValue()!=Integer.parseInt(BranchStatusEnum.ok.toString())){
			rqModel.setStatusreson("������Ӱ¥����Ա����ʱû��Ȩ�ޣ�");
			return rqModel;
		}
		//��ⱻ��ӵ��û���ݣ�������ֻ���
		UUsers member= usersMapper.getUUsersByPhone(param.getPhone());
		if(member!=null){
			//�鿴�û��Ƿ��Ѿ��� ������Ա
			UBranchusers usBranchusers= branchusersMapper.selectByPrimaryKey(member.getUserid()); 
			if(usBranchusers!=null){
				//������Ǳ�Ӱ¥�ķ�����Ա����Ȼ���Ա����
				if(usBranchusers.getBranchuserid()!=null&&usBranchusers.getBranchuserid().intValue()==branchUserId){
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("���û��Ѿ�����Ա���������ظ���ӣ�");
					return rqModel;
				}else{
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("���û��Ѿ�������Ӱ¥��Ա������������ӣ�");
					return rqModel;
				}
			}
			param.setBranchuserid(branchUserId);
			param.setAgentuserid(branch.getAgentuserid());
			param.setUserid(member.getUserid());
			param.setStatus(1);
			param.setCreatetime(new Date());
			branchusersMapper.insert(param);
			
			//����ӵ��û�����ݱ�ʾ
			userBasic.addUserIdentity(member.getUserid(),UserIdentityEnums.salesman); 
			
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("��ӳɹ���");
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("�û������ڣ����߸��ֻ�δ�󶨣���");
		}
		return rqModel;
	}
	
	public ReturnModel delBranchUser(Long branchUserId,Long userId){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		//���Ӱ¥���
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null||branch.getStatus()==null||branch.getStatus().intValue()!=Integer.parseInt(BranchStatusEnum.ok.toString())){
			rqModel.setStatusreson("������Ӱ¥����Ա����ʱû��Ȩ�ޣ�");
			return rqModel;
		}
		UBranchusers usBranchusers= branchusersMapper.selectByPrimaryKey(userId); 
		if(usBranchusers!=null){
			if(usBranchusers.getBranchuserid()!=null&&usBranchusers.getBranchuserid().longValue()==branchUserId){
				branchusersMapper.deleteByPrimaryKey(userId);
				//�Ƴ��û���ʶ
				userBasic.removeUserIdentity(userId,UserIdentityEnums.salesman); 
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setStatusreson("ɾ���ɹ���"); 
			}
		}
		return rqModel;
	}
	
	
	
	/**
	 * ����AgentUserId�õ�Uagentcustomers��Ӫ���ͻ��б�
	 * @param branchUserId
	 *   @return  
	 */
	public ReturnModel findCustomerslistByAgentUserId(Long branchUserId){
		ReturnModel rqModel=new ReturnModel();
		UBranches branch=branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null){
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(null);
			return rqModel;
		}
		//��ȡ��Ӫ���ͻ��б�
		List<UAgentcustomersVo> list= customerMapper.findCustomersByAgentUserId(branch.getAgentuserid(),0);
		
		for (UAgentcustomersVo cus : list) {
			if(cus.getCreatetime()!=null) cus.setCreatetimeStr(DateUtil.getTimeStr(cus.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			UChildreninfo child=childMapper.selectByPrimaryKey(cus.getUserid());
			if(child!=null){
				if(child.getBirthday()!=null)
					cus.setBabyBirthday(DateUtil.getTimeStr(child.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
				else
					cus.setBabyBirthday("");
				cus.setBabyNickName(child.getNickname());
			}
			
			OUserorders order=orderMapper.findLatelyOrderByUserId(cus.getUserid());
			if(order!=null){
				cus.setLastBuyDateStr(DateUtil.getTimeStr(order.getOrdertime(), "yyyy-MM-dd HH:mm:ss"));
			}
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setBasemodle(list);
		return rqModel;
	}
	
	/**
	 * ����BranchUserId�õ��ѻ�ȡ�ͻ��б�
	 * @param branchUserId
	 *   @return  
	 */
	public ReturnModel findMarketCustomerslistByBranchUserId(Long branchUserId){
		ReturnModel rqModel=new ReturnModel();
		UBranches branch=branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null){
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(null);
			return rqModel;
		}
		//��ȡ�ѻ�ȡ�ͻ��б�
		List<UAgentcustomersVo> list= customerMapper.findCustomersByBranchUserId(branch.getAgentuserid(),1);
		for (UAgentcustomersVo cus : list) {
			if(cus.getCreatetime()!=null) cus.setCreatetimeStr(DateUtil.getTimeStr(cus.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			UChildreninfo child=childMapper.selectByPrimaryKey(cus.getUserid());
			if(child!=null){
				if(child.getBirthday()!=null)
					cus.setBabyBirthday(DateUtil.getTimeStr(child.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
				else
					cus.setBabyBirthday("");
				cus.setBabyNickName(child.getNickname());
			}	
			OUserorders order=orderMapper.findLatelyOrderByUserId(cus.getUserid());
			if(order!=null){
				cus.setLastBuyDateStr(DateUtil.getTimeStr(order.getOrdertime(), "yyyy-MM-dd HH:mm:ss"));
			}
			//�õ��ͻ���Դ
			if(cus.getSourcetype()==null){
				cus.setSourcename("�ͻ�һ��һ");
			}
			else if(cus.getSourcetype()!=null&&cus.getSourcetype()==Integer.parseInt(CustomerSourceTypeEnum.oneinvite.toString())){
				cus.setSourcename("�ͻ�һ��һ");
			}else if(cus.getSourcetype()!=null&&cus.getSourcetype()==Integer.parseInt(CustomerSourceTypeEnum.temp.toString())){
				PMyproducttemp temp=tempMapper.selectByPrimaryKey(cus.getExtid());
				if(temp!=null){
					cus.setSourcename(temp.getTitle());
				}else{
					cus.setSourcename("��ҵ����");
				}
			}
			cus.setCartCount(0);
			//�õ������е���Ʒ����
			List<MyProductListVo> mylist=myProductsDao.findMyProductList(cus.getUserid(),cus.getPhone());
			if(mylist!=null){
				List<String> cartIdList=new ArrayList<String>();
				cus.setCartCount(mylist.size());
				for (MyProductListVo pvo : mylist) {
					cartIdList.add(pvo.getCartid().toString());
				}
				cus.setCartIdList(cartIdList);
			}
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setBasemodle(list);
		return rqModel;
	}
	
	/**
	 * ����UserId�õ��ͻ��Ĺ����б�
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel findCustomersBuylistByUserId(Long userId){
		ReturnModel rqModel=new ReturnModel();
		List<OOrderproducts> productlist=orderproductMapper.findOProductsByBuyerUserId(userId);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("�޸ĳɹ�");
		rqModel.setBasemodle(productlist);
		return rqModel;
	}
	
	public ReturnModel editCustomer(Long branchUserId,UAgentcustomers param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param==null||param.getCustomerid()==null||param.getCustomerid()<=0){
			rq.setStatusreson("��������");
			return rq;
		}
		UAgentcustomers model= customerMapper.selectByPrimaryKey(param.getCustomerid());
		if(model!=null){
			if(!ObjectUtil.isEmpty(param.getName())){
				model.setName(param.getName());
			}
			if(param.getStatus()!=null){
				model.setStatus(param.getStatus());
			}
			if(!ObjectUtil.isEmpty(param.getRemark())){
				model.setRemark(param.getRemark());
			}
			if(!ObjectUtil.isEmpty(param.getPhone())){
				model.setPhone(param.getPhone());
				UUsers customerUsers=usersMapper.getUUsersByPhone(param.getPhone());
				if(customerUsers!=null){
					model.setUserid(customerUsers.getUserid()); 
				}
			}
			customerMapper.updateByPrimaryKeySelective(model);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("�޸ĳɹ�");
		}else {
			rq.setStatusreson("�Ҳ�����Ӧ�Ŀͻ���"); 
		} 
		return rq;
	}
	
	public ReturnModel deleteCustomer(Long branchUserId,Long customerId){
		ReturnModel rq=new ReturnModel();
		UAgentcustomers agentcustomers= customerMapper.selectByPrimaryKey(customerId);
		if(agentcustomers!=null){
			if(agentcustomers.getBranchuserid()!=null&&agentcustomers.getBranchuserid().longValue()==branchUserId){
				customerMapper.deleteByPrimaryKey(customerId);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("ɾ���ɹ�");
				return rq;
			}
			else if (branchUserId.longValue()==agentcustomers.getAgentuserid().longValue()) {
				customerMapper.deleteByPrimaryKey(customerId);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("ɾ���ɹ�");
				return rq;
			}
			else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("�����ǹ���Ա���˿ͻ�û��Ȩ��ɾ����");
				return rq;
			}
		}
		rq.setStatu(ReturnStatus.SystemError);
		rq.setStatusreson("�Ҳ�����Ӧ�Ŀͻ���");
		return rq; 
	}
	
	public ReturnModel addCustomer(Long branchUserId,UAgentcustomers param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param==null|| ObjectUtil.isEmpty(param.getPhone())|| !ObjectUtil.isMobile(param.getPhone()) ){
			rq.setStatusreson("��������");
			return rq;
		}
		UUsers branchUsers=usersMapper.selectByPrimaryKey(branchUserId);
		if(branchUsers!=null&& ValidateUtils.isIdentity(branchUsers.getIdentity(), UserIdentityEnums.branch)){
			UBranches branches=branchesMapper.selectByPrimaryKey(branchUserId);
			if(branches!=null){
				param.setBranchuserid(branchUserId);
				param.setAgentuserid(branches.getAgentuserid());
				param.setCreatetime(new Date());
				UUsers customerUsers=usersMapper.getUUsersByPhone(param.getPhone());
				if(customerUsers!=null){
					param.setUserid(customerUsers.getUserid()); 
				}
				
				UAgentcustomers customer= customerMapper.getCustomersByAgentUserId(branches.getAgentuserid(), param.getUserid());
				if(customer!=null){
					param.setCustomerid(customer.getCustomerid());
					customerMapper.updateByPrimaryKey(param);
				}else{
					customerMapper.insert(param);
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("�����ɹ�");
				return rq;
			} 
		}
		rq.setStatusreson("����������ѽ�ĺ�����飬�˹��ܵ�Ȩ�޲��㣡");  
		return rq;
	}
	
	

}
