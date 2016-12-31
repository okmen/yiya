package com.bbyiya.service.impl.pic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;

@Service("baseUserAddressServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseUserAddressServiceImpl implements IBaseUserAddressService{
	@Autowired
	private UUseraddressMapper addressMapper;
	
	/**
	 * edit/add user's addressInfo
	 * zy 
	 * @return
	 */
	public ReturnModel addOrEdit_UserAddress(UUseraddress address){
		ReturnModel rq=new ReturnModel();
		if(address!=null&&address.getUserid()!=null&&address.getUserid()>0){
			if(!ObjectUtil.validSqlStr(address.getReciver())){
				rq.setStatu(ReturnStatus.ParamError_2);
				rq.setStatusreson("收货人信息存在危险！");
				return rq;
			}
			if(!ObjectUtil.isEmpty(address.getPhone())&&!ObjectUtil.validSqlStr(address.getPhone())){
				rq.setStatu(ReturnStatus.ParamError_2);
				rq.setStatusreson("手机号格式不对！");
				return rq;
			}
			if(!ObjectUtil.isEmpty(address.getStreetdetail())&&!ObjectUtil.validSqlStr(address.getStreetdetail())){
				rq.setStatu(ReturnStatus.ParamError_2);
				rq.setStatusreson("街道详情存在危险");
				return rq;
			}
			if(!ObjectUtil.isEmpty(address.getPostcode())&&!ObjectUtil.validSqlStr(address.getPostcode())){
				rq.setStatu(ReturnStatus.ParamError_2);
				rq.setStatusreson("邮政编码存在危险");
				return rq;
			}
			if(address.getAddrid()!=null&&address.getAddrid()>0){
				UUseraddress temp=addressMapper.get_UUserAddressByKeyId(address.getAddrid());
				if(temp!=null){
					addressMapper.updateByPrimaryKeySelective(address);
					if(!ObjectUtil.isEmpty(address.getReciver()) ){
						temp.setReciver(address.getReciver());
					}
					if(!ObjectUtil.isEmpty(address.getPhone()) ){
						temp.setPhone(address.getPhone());
					}
					if(!ObjectUtil.isEmpty(address.getProvince()) ){
						temp.setProvince(address.getProvince());
					}
					if(!ObjectUtil.isEmpty(address.getCity()) ){
						temp.setCity(address.getCity());
					}
					if(!ObjectUtil.isEmpty(address.getArea()) ){
						temp.setArea(address.getArea());
					}
					if(!ObjectUtil.isEmpty(address.getStreetdetail()) ){
						temp.setStreetdetail(address.getStreetdetail());
					} 
					rq.setBasemodle(temp); 
					rq.setStatu(ReturnStatus.Success);
					return rq;
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("不存在的用户收货地址！"); 
					return rq;
				}
			}else {
				List<UUseraddress> list= addressMapper.find_UUserAddressByUserId(address.getUserid());
				if(list==null||list.size()<=0){
					address.setIsdefault(1);
				}
				addressMapper.insertReturnId(address);
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(address); 
			}
		}else {
			rq.setStatu(ReturnStatus.ParamError_1);
			rq.setStatusreson("参数有误");
		}
		return rq;
	}
}
