package com.bbyiya.pic.service.impl.pbs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.upload.FileDownloadUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pbs_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pbs_OrderMgtServiceImpl implements IPbs_OrderMgtService{
	//客户信息处理
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	/*------------------订单模块--------------------------------------*/
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
	/*----------------------代理模块--------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	
	
	public PageInfo<PbsUserOrderResultVO> find_pbsOrderList(SearchOrderParam param,int index,int size){
		if(param==null)
			param=new SearchOrderParam();
		PageHelper.startPage(index, size);
		List<PbsUserOrderResultVO> list=orderDao.findPbsUserOrders(param);
		PageInfo<PbsUserOrderResultVO> reuslt=new PageInfo<PbsUserOrderResultVO>(list);
		
		if(list!=null&&list.size()>0){
			for (PbsUserOrderResultVO product : reuslt.getList()) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(product.getUserorderid());
				product.setOrder(order);
				OOrderaddress address= addressMapper.selectByPrimaryKey(order.getOrderaddressid());
				UBranches branch=branchesMapper.selectByPrimaryKey(order.getBranchuserid());
				if(branch!=null){
					product.setBranchesName(branch.getBranchcompanyname());
					product.setBranchesPhone(branch.getPhone());
				}
				int orderType = order.getOrdertype() == null ? 0 : order.getOrdertype();
				//影楼直接下单
				if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
					product.setBranchesAddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getStreetdetail());
				}else{
					//普通用户下单如果是影楼抢单
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
	public ReturnModel editLogistics(String orderId,String expressCom,String expressOrder) throws Exception {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		if(userorders!=null){
			userorders.setExpresscom(expressCom);
			userorders.setExpressorder(expressOrder);
			userOrdersMapper.updateByPrimaryKeySelective(userorders);
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(userorders);
			rq.setStatusreson("修改运单号成功!");
		}else{
			rq.setStatu(ReturnStatus.ParamError);		
			rq.setStatusreson("orderId参数传入有误！");
		}
		
		return rq;
	}
	
	
	public void pbsdownloadImg(List<PbsUserOrderResultVO> orderlist,String basePath){
		try {
			FileUtils.isDirExists(basePath);
		} catch (Exception e) {
			basePath="D:\\orderImgs\\";
			FileUtils.isDirExists(basePath);
		}

		for (PbsUserOrderResultVO order : orderlist) {
			Calendar c1 =  Calendar.getInstance();;
			Date nowtime=new Date();
			c1.setTime(nowtime);
			
			//c1.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY) - 1);
			//c1.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
			//c1.set(Calendar.SECOND, c1.get(Calendar.SECOND));
			String file_temp=DateUtil.getTimeStr(c1.getTime(), "yyyyMMddHHmm");
			
			//创建文件夹
			FileUtils.isDirExists(basePath+"\\"+file_temp);
			FileUtils.isDirExists(basePath+"\\"+file_temp+"\\"+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr()+"×"+order.getCount());;
			int i=1;
			
			List<OOrderproductdetails> detallist=orderDao.findOrderProductDetailsByProductOrderId(order.getOrderproductid());
			int j=detallist.size()+1;
			for (OOrderproductdetails detail : detallist) {
				detail.setImageurl("http://pic.bbyiya.com/"+detail.getImageurl()); 
				String file_dir=basePath+"\\"+file_temp+"\\"+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr()+"×"+order.getCount();
			
				String fileFull_name=file_dir+"\\"+i+".jpg";
				String filebackFull_name=file_dir+"\\"+j+".jpg";
				if(!FileUtils.isFileExists(fileFull_name)){
					try {
						if(!ObjectUtil.isEmpty(detail.getImageurl())){
							FileDownloadUtils.download(detail.getImageurl(),fileFull_name);
							FileDownloadUtils.setDPI(fileFull_name);
						}
						
						if(!ObjectUtil.isEmpty(detail.getBackimageurl())){
							FileDownloadUtils.download(detail.getBackimageurl(),filebackFull_name);
							FileDownloadUtils.setDPI(filebackFull_name);
							j++;
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
				
			}
			
		}
	}
	
	
}
