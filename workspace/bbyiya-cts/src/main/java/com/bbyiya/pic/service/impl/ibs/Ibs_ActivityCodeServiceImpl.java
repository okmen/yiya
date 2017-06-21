package com.bbyiya.pic.service.impl.ibs;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductactivitycodeMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductextMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.ActivityCodeStatusEnum;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductactivitycode;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductext;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_ActivityCodeService;
import com.bbyiya.pic.vo.product.ActivityCodeProductVO;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_ActivityCodeService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_ActivityCodeServiceImpl implements IIbs_ActivityCodeService{
	@Autowired
	private PMyproducttempMapper myproducttempMapper;
	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductactivitycodeMapper activitycodeMapper;
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;

	@Autowired
	private PMyproductchildinfoMapper mychildMapper;
	@Autowired
	private PMyproducttempapplyMapper tempapplyMapper;
	@Autowired
	private PMyproductextMapper myextMapper;
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	/*-------------------�û���Ϣ------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;// Ӱ¥��Ϣ
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private UUseraddressMapper uaddressMapper;

	/**
	 * ��ӻ��
	 * */
	public ReturnModel addActivityCode(Long userid,MyProductTempAddParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	

		PMyproducts myproduct = new PMyproducts();	
		myproduct.setUserid(userid);
		myproduct.setProductid(param.getProductid());
		myproduct.setCreatetime(new Date());
		myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
		myproduct.setUpdatetime(new Date());	
		myproduct.setIstemp(1);
		myMapper.insertReturnId(myproduct);
			
		PMyproducttemp temp=new PMyproducttemp();
		temp.setBranchuserid(userid);
		temp.setCount(0);//��ͨ������
		temp.setCreatetime(new Date());
		temp.setRemark(param.getRemark());
		temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));
		temp.setTitle("���");
		temp.setCartid(myproduct.getCartid());	
		temp.setNeedverifer(0); //����Ҫ���   0����Ҫ��1 ��Ҫ
		temp.setDiscription(param.getDiscription()); //���֪
		if(!ObjectUtil.isEmpty(param.getCodeurl())){
			temp.setTempcodeurl(param.getCodeurl());
		}if(!ObjectUtil.isEmpty(param.getCodesm())){
			temp.setTempcodesm(param.getCodesm());
		}
		temp.setStyleid(param.getStyleId());
		temp.setIsautoorder(1);//Ĭ�϶����Զ��µ�0 �ֹ��µ���1�Զ��µ�
		temp.setOrderhours(0); 
		temp.setApplycount(param.getApplycount()==null?0:param.getApplycount());//��������Ϊ0ʱ������
		temp.setBlesscount(param.getBlesscount()==null?0:param.getBlesscount());//�ռ�ף����
		if(ObjectUtil.isEmpty(param.getIsbranchaddress())){
			param.setIsbranchaddress(0);
		}
		temp.setIsbranchaddress(param.getIsbranchaddress());
		temp.setType(Integer.parseInt(MyProductTempType.code.toString()));//Ĭ��Ϊ��ͨ����
		myproducttempMapper.insertReturnId(temp);
		
		//���ɻ��
		for(int i=0;i<param.getApplycount();i++){
			String idString= GenUtils.generateUuid_Char8();
			PMyproductactivitycode code=new PMyproductactivitycode();
			code.setBranchuserid(userid);
			code.setCodeno(idString);
			code.setCreatetime(new Date());
			code.setStatus(Integer.parseInt(ActivityCodeStatusEnum.notuse.toString()));
			code.setTempid(temp.getTempid());
			activitycodeMapper.insert(code);
		}
		
		myproduct.setTempid(temp.getTempid());
		myMapper.updateByPrimaryKey(myproduct);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myProductTempID", temp.getTempid());
		map.put("myProductID", myproduct.getCartid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("��ӻ��ɹ���");
		return rq;
	}
	
	
	/**
	 * ����ģ��ID�õ���Ʒ�б�
	 * @param branchUserId
	 * @param tempid
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel findMyProductslistForActivityCode(Long branchUserId, Integer tempid,Integer activeStatus,String keywords,int index, int size) {
		ReturnModel rq = new ReturnModel();
		
		PageHelper.startPage(index, size);
		List<PMyproductactivitycode> codelist=activitycodeMapper.findActivitycodelistForTempId(tempid, activeStatus, keywords);
		PageInfo<PMyproductactivitycode> resultPage = new PageInfo<PMyproductactivitycode>(codelist);
		List<ActivityCodeProductVO> codevoList=new ArrayList<ActivityCodeProductVO>();
		
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {	
			for (PMyproductactivitycode code : resultPage.getList()) {
				ActivityCodeProductVO codevo=new ActivityCodeProductVO();
				codevo.setCode(code);
				
				PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(code.getTempid());
				codevo.setStyleid(temp.getStyleid());
				PMyproducts tempproduct=myMapper.selectByPrimaryKey(temp.getCartid());
				codevo.setProductid(tempproduct.getProductid());
				
				// ��ȡ��Ʒ��Ϣ
				PProducts products = productsMapper.selectByPrimaryKey(tempproduct.getProductid());
				PProductstyles styles = styleMapper.selectByPrimaryKey(temp.getStyleid());
				String producttitle=products.getTitle();
				if (products != null && styles != null) {
					if(styles.getStyleid()%2==0){
						producttitle=producttitle+"-���-"+styles.getPrice();
					}else{
						producttitle=producttitle+"-���-"+styles.getPrice();
					}
					codevo.setProductTitle(producttitle);
				}
				//�õ��ͻ��ǳ�
				if(!ObjectUtil.isEmpty(code.getUserid())){
					UUsers user=usersMapper.selectByPrimaryKey(code.getUserid());
					codevo.setInvitedName(user.getNickname());
				}
				//0δʹ�ã�1��ʹ�� 
				codevo.setActiveStatus(code.getStatus());
				if(!ObjectUtil.isEmpty(code.getCartid())){
					PMyproducts myproduct=myMapper.selectByPrimaryKey(code.getCartid());
					if(myproduct!=null){
						codevo.setTitle(myproduct.getTitle());
						codevo.setCreatetimestr(DateUtil.getTimeStr(myproduct.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
						if(ObjectUtil.isEmpty(myproduct.getUpdatetime())){
							codevo.setUpdatetimestr(DateUtil.getTimeStr(myproduct.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
						}else{
							codevo.setUpdatetimestr(DateUtil.getTimeStr(myproduct.getUpdatetime(), "yyyy-MM-dd HH:mm:ss"));
						}
						codevo.setIsDue(0);//Ĭ�ϲ���Ԥ����
						// �õ���������
						PMyproductchildinfo childinfo = mychildMapper.selectByPrimaryKey(myproduct.getCartid());
						if (childinfo != null && childinfo.getBirthday() != null) {
							codevo.setBirthdayStr(DateUtil.getTimeStr(childinfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
							codevo.setIsDue(childinfo.getIsdue()==null?0:childinfo.getIsdue());
						}
						PMyproducttempapply tempapply=tempapplyMapper.getMyProducttempApplyByCartId(myproduct.getCartid());
						if(tempapply!=null){
							codevo.setPhone(tempapply.getMobilephone());
							codevo.setAddress(tempapply.getAdress());
							//1��ʹ�� 3��������� 4��Ʒ��˲�ͨ��5�µ����ͨ��
							codevo.setActiveStatus(tempapply.getStatus());
						}
						// �õ���Ʒ��������
						List<OUserorders> orderList = orderMapper.findOrderListByCartId(myproduct.getCartid());
						List<String> orderNoList = new ArrayList<String>();
						for (OUserorders order : orderList) {
							orderNoList.add(order.getUserorderid());
						}
						if (orderNoList.size() > 0) {
							codevo.setOrderNoList(orderNoList);
						}
						//�õ�������
						codevo.setCommentsCount(0);
						PMyproductext myext=myextMapper.selectByPrimaryKey(myproduct.getCartid());
						if(myext!=null){
							codevo.setCommentsCount(myext.getCommentscount()==null?0:myext.getCommentscount());
						}
					}
				}
				codevoList.add(codevo);
			}
		}
		PageInfoUtil<ActivityCodeProductVO> resultPageList=new PageInfoUtil<ActivityCodeProductVO>(resultPage,codevoList);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPageList);
		return rq;
	}
	
	/**
	 * ɾ��ģ��
	 * @param tempid
	 * @return
	 */
	public ReturnModel deleteActivityCode(String codeno){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PMyproductactivitycode code=activitycodeMapper.selectByPrimaryKey(codeno);	
		if(code!=null){
			if(code.getStatus()!=null&&code.getStatus().intValue()!=Integer.parseInt(ActivityCodeStatusEnum.notuse.toString())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("������˼����ʹ�õĻ�벻��ɾ����");
				return rq;
			}
			code.setStatus(Integer.parseInt(ActivityCodeStatusEnum.deleted.toString()));
			activitycodeMapper.updateByPrimaryKey(code);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("ɾ�������ɹ�");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��벻����");
		}
		return rq;
	}
	
}
