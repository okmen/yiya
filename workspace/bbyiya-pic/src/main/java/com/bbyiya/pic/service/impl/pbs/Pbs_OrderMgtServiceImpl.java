package com.bbyiya.pic.service.impl.pbs;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.enums.AmountType;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchtransamountlog;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.utils.FileToZip;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.upload.FileDownloadUtils;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pbs_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pbs_OrderMgtServiceImpl implements IPbs_OrderMgtService{
	//�ͻ���Ϣ����
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	/*------------------����ģ��--------------------------------------*/
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired 
	private IPic_OrderMgtDao orderDao;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	@Autowired
	private OOrderproductdetailsMapper detailMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	
	/*----------------------����ģ��--------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UBranchtransaccountsMapper branchesTransMapper;
	@Autowired
	private UBranchtransamountlogMapper branchesTransLogMapper;
	
	public PageInfo<PbsUserOrderResultVO> find_pbsOrderList(SearchOrderParam param,int index,int size){
		if(param==null)
			param=new SearchOrderParam();
		if(param.getEndTimeStr()!=null&&!param.getEndTimeStr().equals("")){
			param.setEndTimeStr(DateUtil.getEndTime(param.getEndTimeStr()));
		}
		PageHelper.startPage(index, size);
		List<PbsUserOrderResultVO> list=orderDao.findPbsUserOrders(param);
		PageInfo<PbsUserOrderResultVO> reuslt=new PageInfo<PbsUserOrderResultVO>(list);
		
		if(list!=null&&list.size()>0){
			for (PbsUserOrderResultVO product : reuslt.getList()) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(product.getUserorderid());
				product.setOrder(order);
				if(order.getPaytime()!=null)
					product.setPayTimeStr(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss"));
				OOrderaddress address= addressMapper.selectByPrimaryKey(order.getOrderaddressid());
				UBranches branch=branchesMapper.selectByPrimaryKey(order.getBranchuserid());
				if(branch!=null){
					product.setBranchesName(branch.getBranchcompanyname());
					product.setBranchesPhone(branch.getPhone());
				}
				int orderType = order.getOrdertype() == null ? 0 : order.getOrdertype();
				order.setOrdertype(orderType);
				//Ӱ¥ֱ���µ�
				if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
					product.setBranchesAddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getStreetdetail());
				}else{
					//��ͨ�û��µ������Ӱ¥����
					if(order.getIsbranch()!=null&&order.getIsbranch()==1){
						if(branch!=null)
							product.setBranchesAddress(branch.getProvince()+branch.getCity()+branch.getArea()+branch.getStreetdetail());
					}
					product.setReciver(address.getReciver());
					product.setBuyerPhone(address.getPhone());
					product.setBuyerprovince(address.getProvince());
					product.setBuyercity(address.getCity());
					product.setBuyerdistrict(address.getDistrict());
					product.setBuyerstreetdetail(address.getStreetdetail());
				}
				
				
			}
		}
		return reuslt;
	}
	
	/**
	 * �޸��˵���
	 */
	public ReturnModel editLogistics(String orderId,String expressCom,String expressOrder) throws Exception {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		//�����˵�����ͬ�Ķ�����
		if(userorders!=null){
			List<OUserorders> orderList=orderDao.findUserOrderByExpressOrder(userorders.getExpressorder(), userorders.getExpresscom());
			if(orderList!=null&&orderList.size()>0){
				for (OUserorders order : orderList) {
					order.setExpresscom(expressCom);
					order.setExpressorder(expressOrder);
					order.setDeliverytime(new Date()); 
					if(order.getPaytype()==Integer.parseInt(OrderTypeEnum.nomal.toString())){
						//�޸Ķ���״̬Ϊ�ѷ���״̬
						order.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
					}			
					userOrdersMapper.updateByPrimaryKeySelective(order);
				}
			}
			//�޸ı��Ŷ���
			if(userorders.getPaytype()==Integer.parseInt(OrderTypeEnum.nomal.toString())){
				//�޸Ķ���״̬Ϊ�ѷ���״̬
				userorders.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
			}			
			userorders.setExpresscom(expressCom);
			userorders.setExpressorder(expressOrder);
			userOrdersMapper.updateByPrimaryKeySelective(userorders);
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(userorders);
			rq.setStatusreson("�޸��˵��ųɹ�!");
		}else{
			rq.setStatu(ReturnStatus.ParamError);		
			rq.setStatusreson("orderId������������");
			return rq;
		}
		
		return rq;
	}  
	
	public ReturnModel addPostage(String orderId,Double postage) throws Exception {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		if(userorders!=null){
			//�״���д�˵��ţ���ִ���Զ����˷ѿ����
			if(userorders.getPostage()==null||userorders.getPostage().doubleValue()<=0){
				
				//�Զ��۳��������˷��˻���
				UBranchtransaccounts branchTransAccount=branchesTransMapper.selectByPrimaryKey(userorders.getBranchuserid());
				if(branchTransAccount!=null&&branchTransAccount.getAvailableamount()!=null&&branchTransAccount.getAvailableamount().doubleValue()>=postage.doubleValue()){
					UBranchtransamountlog branchTranLog=new UBranchtransamountlog();
					branchTranLog.setAmount(-1*postage);
					branchTranLog.setBranchuserid(branchTransAccount.getBranchuserid());
					branchTranLog.setCreatetime(new Date());
					branchTranLog.setPayid(userorders.getPayid());
					branchTranLog.setType(Integer.parseInt(AmountType.lost.toString()));
					branchesTransLogMapper.insert(branchTranLog);
					branchTransAccount.setAvailableamount(branchTransAccount.getAvailableamount()-postage);
					branchesTransMapper.updateByPrimaryKeySelective(branchTransAccount);
				}else{
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("�˷��˻����㣬���ֵ�������˵�!");
					return rq;
				}
			}else if(userorders.getPostage()!=null&&userorders.getPostage().doubleValue()>0){
				rq.setStatu(ReturnStatus.OrderError);
				rq.setStatusreson("������˷��Զ��ۿ�����������޸ģ�");
				return rq;
			}
			
			//�޸Ķ���״̬Ϊ�ѷ���״̬
			userorders.setPostage(postage);
			userorders.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
			userOrdersMapper.updateByPrimaryKeySelective(userorders);
			
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(userorders);
			rq.setStatusreson("����˷ѳɹ�!");
		}else{
			rq.setStatu(ReturnStatus.ParamError);		
			rq.setStatusreson("orderId������������");
			return rq;
		}
		
		return rq;
	}  
	/**
	 * ��������Ƿ�����˵��ϲ�
	 * ������ 1������ж�������в�ͬ�˵���Ϣ�����ܺϲ�
	 * 		 2��������ͬһ�û��Ķ������ܽ��кϵ�����
	 * 	     2��������ж�����û���˵���Ϣ������Ҫ����¼���˵��ĵ�����
	 *       3�����ֻ��һ���������˵���Ϣ�������˵��ŵĶ�����¼���������˵���Ϣ
	 *       
	 * @param orderIds
	 * @param postage
	 * @return
	 * @throws Exception
	 */
	public ReturnModel isCanMergeOrderLogistic(String orderIds) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(orderIds==null||orderIds.equals("")){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("�����Ų���Ϊ�գ�");
			return rq;
		}
		String orderArr[]=orderIds.split(",");
		HashMap<Long,String> userIdHash=new HashMap<Long,String>();
		HashMap<String,OUserorders> expressHash=new HashMap<String, OUserorders>();
		HashMap<Integer,String> typeHash=new HashMap<Integer,String>();
		HashMap<String,Object> result=new HashMap<String, Object>();

		int ordertypeLast=0;
		if(orderArr!=null&&orderArr.length>0){
			for (String orderid : orderArr) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(orderid);
				if(order!=null){
					int type=order.getOrdertype()==null?0:order.getOrdertype();
					if(!typeHash.containsKey(type)){
						typeHash.put(type,"ordertype");
						ordertypeLast=type;
					}
				}
			}
			if(typeHash.size()>1){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ͬ�û��������ܽ����˵��ϵ�������");
				return rq;
			}
		}
		if(orderArr!=null&&orderArr.length>0){
			for (String orderid : orderArr) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(orderid);
				if(order!=null){
					//����Ǵ����̶����ϵ�
					if(ordertypeLast==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
						if(!userIdHash.containsKey(order.getBranchuserid())){
							userIdHash.put(order.getBranchuserid(), order.getUserorderid());
						}
						
						String express=(order.getExpresscom()==null?"":order.getExpresscom())+(order.getExpressorder()==null?"":order.getExpressorder())+((order.getPostage()==null||order.getPostage()==0)?"":order.getPostage());
						if(express!=null&&express.length()>0){
							expressHash.put(order.getExpressorder(), order);
						}
					}else{
						//���������ͨ�û��ĺϵ�
						if(!userIdHash.containsKey(order.getUserid())){
							userIdHash.put(order.getUserid(), order.getUserorderid());
						}
						String express=(order.getExpresscom()==null?"":order.getExpresscom())+(order.getExpressorder()==null?"":order.getExpressorder());
						if(express!=null&&express.length()>0){
							expressHash.put(order.getExpressorder(), order);
						}
					}
					
					
				}
			}
			if(userIdHash.size()>1){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ͬ�û��Ķ������ܽ����˵��ϵ�������");
				return rq;
			}
			if(expressHash.size()>0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���ں����˵���Ϣ�Ķ��������ܺϵ���");
				return rq;
			}
			
			result.put("orderids", orderIds);
			result.put("ordertype", ordertypeLast);
			rq.setBasemodle(result);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("");
		
		}
		return rq;
	}
	
	/**
	 * 
	 * @param type 1:���ж���û���˵���Ϣ��2��������һ�����˵���Ϣ
	 * @param orderIds ��Ҫ�����˵���Ϣ���˵���
	 * @param expressCom
	 * @param expressOrder
	 * @param postage
	 * @return
	 * @throws Exception
	 */
	public ReturnModel MergeOrderLogistic(int ordertype,String orderIds,String expressCom,String expressOrder,Double postage) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(orderIds==null||orderIds.equals("")){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("�����Ų���Ϊ�գ�");
			return rq;
		}
		//�ٴ�У��һ��
		rq=isCanMergeOrderLogistic(orderIds);
		if(rq.getStatu()!=ReturnStatus.Success){			
			return rq;
		}
		
		String orderArr[]=orderIds.split(",");
		if(orderArr!=null&&orderArr.length>0){
			for(int i=0;i<orderArr.length;i++){
				OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderArr[i]);
				if(userorders!=null){
					userorders.setExpresscom(expressCom);
					userorders.setExpressorder(expressOrder);					
					//ֻ�ܵ�һ�ŵ���ordertype=1�Ż��Զ��ۿ�
					if(i==0&&ordertype==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
						ReturnModel rqmodel=addPostage(orderArr[i], postage);
						if(rqmodel.getStatu()!=ReturnStatus.Success){
							return rqmodel;
						}	
					}else{
						userorders.setPostage(0.0);
					}
					
					//�޸Ķ���״̬Ϊ�ѷ���״̬
					userorders.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
					userOrdersMapper.updateByPrimaryKeySelective(userorders);
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("�ϲ��˵���Ϣ�ɹ���");
		return rq;
		
	}
	public String pbsdownloadImg(List<PbsUserOrderResultVO> orderlist){
		String sep=System.getProperty("file.separator");
		String  basePath = System.getProperty("user.home") +sep + "imagedownloadtemp"+sep+"orderImg";
		FileUtils.isDirExists(basePath);
		Calendar c1 =  Calendar.getInstance();
		Date nowtime=new Date();
		c1.setTime(nowtime); 
		String file_temp=DateUtil.getTimeStr(c1.getTime(), "yyyyMMddHHmm");
		//�����ļ���
		FileUtils.isDirExists(basePath+sep+file_temp);
		for (PbsUserOrderResultVO order : orderlist) {
			FileUtils.isDirExists(basePath+sep+file_temp+sep+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr().replaceAll("/", "-")+"��"+order.getCount()+"("+order.getUserorderid()+")");
			int i=1;			
			List<OOrderproductdetails> detallist=orderDao.findOrderProductDetailsByProductOrderId(order.getOrderproductid());
			
			for (OOrderproductdetails detail : detallist) {
				if(detail.getImageurl()!=null)
					detail.setImageurl("http://pic.bbyiya.com/"+detail.getImageurl());
				if(detail.getBackimageurl()!=null)
					detail.setBackimageurl("http://pic.bbyiya.com/"+detail.getBackimageurl()); 
				
				String file_dir=basePath+sep+file_temp+sep+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr().replaceAll("/", "-")+"��"+order.getCount()+"("+order.getUserorderid()+")";
				
				String fileFull_name=file_dir+sep+i+".jpg";
				i++;
				String filebackFull_name=file_dir+sep+i+".jpg";
				if(!FileUtils.isFileExists(fileFull_name)){
					try {
						if(!ObjectUtil.isEmpty(detail.getImageurl())){
							FileDownloadUtils.download(detail.getImageurl(),fileFull_name);
							FileDownloadUtils.setDPI(fileFull_name);
						}
						
						if(!ObjectUtil.isEmpty(detail.getBackimageurl())){
							FileDownloadUtils.download(detail.getBackimageurl(),filebackFull_name);
							FileDownloadUtils.setDPI(filebackFull_name);
							
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
				
			}
			
		}
		
		//ѹ����zip�ļ�		
		FileToZip z = new FileToZip();  
		z.zip(basePath+sep+file_temp, basePath+sep+file_temp+".zip"); 	
		File file = new File(basePath+sep+file_temp+".zip");
		z.deleteDirectory(basePath+sep+file_temp);
		return file.getPath();
	}
	
	
}
