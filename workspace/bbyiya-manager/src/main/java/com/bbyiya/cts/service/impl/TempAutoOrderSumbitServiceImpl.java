package com.bbyiya.cts.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.cts.job.HeartbeatJob;
import com.bbyiya.cts.service.ITempAutoOrderSumbitService;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.SysLogsMapper;
import com.bbyiya.dao.UAdminactionlogsMapper;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.SysLogs;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressParam;
import com.bbyiya.vo.order.SubmitOrderProductParam;
import com.bbyiya.vo.order.UserOrderSubmitParam;

@Service("tempAutoOrderSumbitService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class TempAutoOrderSumbitServiceImpl implements ITempAutoOrderSumbitService{
	
	private Logger Log = Logger.getLogger(TempAutoOrderSumbitServiceImpl.class);
	
	@Autowired
	private PMyproductsMapper myproductMapper;
	
	@Autowired
	private PMyproducttempMapper tempMapper;
	
	@Autowired
	private PMyproducttempapplyMapper applyMapper;
	
	@Autowired
	private SysLogsMapper syslogMapper;

	
	@Resource(name = "baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderMgtService;
	
	public ReturnModel dotempAutoOrderSumbit(){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		//�õ������ѿ����Ļ�б�
		List<PMyproducttemp> templist=tempMapper.findAllAutoOrderTempByStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));
		if(templist!=null&&templist.size()>0){
			for (PMyproducttemp temp : templist) {
				//���orderhoursΪ����Ĭ��Ϊ48Сʱ
				if(temp.getOrderhours()==null) temp.setOrderhours(48);
				//�õ�������п��µ�����Ʒ�б�
				List<PMyproducts> productlist=myproductMapper.findCanOrderMyProducts(temp.getTempid(), temp.getOrderhours());
				for (PMyproducts myproduct : productlist) {
					//�����µ��ӿ�
					SubmitOrderProductParam productParam=new SubmitOrderProductParam();
					productParam.setProductId(myproduct.getProductid());
					Long styleId=temp.getStyleid();
					//���Ϊ�գ�Ĭ��Ϊ����
					if(ObjectUtil.isEmpty(styleId)) styleId=myproduct.getProductid();
					productParam.setStyleId(styleId);
					productParam.setCount(1);
					productParam.setCartId(myproduct.getCartid());
					
					PMyproducttempapply tempapply=applyMapper.getMyProducttempApplyByCartId(myproduct.getCartid());
					OrderaddressParam addressParam=new OrderaddressParam();
					addressParam.setUserid(tempapply.getUserid());
					addressParam.setCity(tempapply.getCity());
					addressParam.setDistrict(tempapply.getArea());
					addressParam.setPhone(tempapply.getMobilephone());
					addressParam.setProvince(tempapply.getProvince());
					addressParam.setReciver(tempapply.getReceiver());
					addressParam.setStreetdetail(tempapply.getStreet());
					if (productParam != null&&addressParam!=null) {
						OOrderproducts product = new OOrderproducts();
						product.setProductid(productParam.getProductId());
						product.setStyleid(productParam.getStyleId());
						product.setCount(productParam.getCount());
						
						// �µ�����
						UserOrderSubmitParam param = new UserOrderSubmitParam();
						
						param.setUserId(myproduct.getUserid());
						param.setRemark("ϵͳ�Զ��µ�");
						
						if (productParam.getCartId() != null && productParam.getCartId() > 0) {
							param.setCartId(productParam.getCartId());
						}
						//ΪӰ¥����
						param.setOrderType(1);
						if(productParam.getPostModelId()!=null){
							param.setPostModelId(productParam.getPostModelId()); 
						} 
						param.setOrderproducts(product);
						if(addressParam.getCity()==null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("��ַ��������cityΪ��");
							return rq;
						}
						if(addressParam.getProvince()==null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("��ַ��������provinceΪ��");
							return rq;
						}
						if(addressParam.getDistrict()==null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("��ַ��������districtΪ��");
							return rq;
						}
						if(addressParam.getStreetdetail()==null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("��ַ��������streetdetailΪ��");
							return rq;
						}
						if(addressParam.getPhone()==null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("��������,�ֻ���Ϊ��");
							return rq;
						}
						if(!ObjectUtil.isEmpty(addressParam.getPhone())&&!ObjectUtil.isMobile(addressParam.getPhone())){
							rq.setStatu(ReturnStatus.ParamError_2);
							rq.setStatusreson("�ֻ��Ÿ�ʽ���ԣ�");
							return rq;
						}
						if(addressParam.getReciver()==null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("��������,��ϵ��Ϊ��");
							return rq;
						}
						param.setAddressparam(addressParam);
						rq = orderMgtService.submitOrder_IBS(param);
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("��������");
					}
					if(!rq.getStatu().equals(ReturnStatus.Success))//δͨ��������֤
					{
						addSysLog("��Ʒ"+productParam.getCartId()+"�Զ��µ�ʧ�ܣ�ԭ��"+rq.getStatusreson(),"dotempAutoOrderSumbit","�µ�ʧ��");
						Log.error("��Ʒ"+productParam.getCartId()+"�Զ��µ�ʧ�ܣ�ԭ��"+rq.getStatusreson());
					}else{
						Log.info("��Ʒ"+productParam.getCartId()+"�Զ��µ��ɹ���");
						addSysLog("��Ʒ"+productParam.getCartId()+"�Զ��µ��ɹ���","dotempAutoOrderSumbit","�µ��ɹ�");
					}
					
				}//end For 1
				
			}//end For 2
			
		}
		
		return rq;
	}
	
	public void addSysLog(String msg,String jobid,String jobname){
		SysLogs log=new SysLogs();
		log.setContent(msg);
		log.setJobid(jobid);
		log.setJobname(jobname);
		log.setCreatetime(new Date());
		syslogMapper.insert(log);
	}
}
