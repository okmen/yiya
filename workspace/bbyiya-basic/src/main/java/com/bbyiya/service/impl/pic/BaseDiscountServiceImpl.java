package com.bbyiya.service.impl.pic;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.DMyproductdiscountYiyeMapper;
import com.bbyiya.dao.DMyproductdiscountmodelMapper;
import com.bbyiya.dao.DMyproductdiscountsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.DMyproductdiscountYiye;
import com.bbyiya.model.DMyproductdiscountmodel;
import com.bbyiya.model.DMyproductdiscounts;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.pic.IBaseDiscountService;
import com.bbyiya.utils.JsonUtil;

@Service("baseDiscountServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseDiscountServiceImpl implements IBaseDiscountService {
	private Logger logHelper = Logger.getLogger(BaseDiscountServiceImpl.class);
	@Autowired
	private PMyproducttempapplyMapper applyMapper;
	@Autowired
	private DMyproductdiscountsMapper discountMapper;
	@Autowired
	private DMyproductdiscountYiyeMapper discountDetailMapper;
	@Autowired
	private DMyproductdiscountmodelMapper disModMapper;
	@Autowired
	private PMyproductsMapper mycartMapper;
	@Autowired
	private UUsersMapper userMapper;
	
	@SuppressWarnings("null")
	public void addTempDiscount(Long cartId) {
//		PMyproducts mycart=mycartMapper.selectByPrimaryKey(cartId);
//		if(mycart!=null&&(mycart.getIstemp()==null||mycart.getIstemp().intValue()!=1)&&mycart.getTempid()!=null) {
			try {
				List<DMyproductdiscountmodel> discountlist=disModMapper.findListByProductId(cartId);
				if(discountlist!=null&&discountlist.size()>0){
					DMyproductdiscounts mydis=discountMapper.selectByPrimaryKey(cartId);
					if(mydis!=null){
						return;
					}else {
						//我参加了异业合作
						PMyproducttempapply apply = applyMapper.getMyProducttempApplyByCartId(cartId);
						if(apply!=null){
							UUsers users= userMapper.selectByPrimaryKey(apply.getUserid());
							if(users!=null&&users.getIdentity()!=null&&(ValidateUtils.isIdentity(users.getIdentity(), UserIdentityEnums.branch)||ValidateUtils.isIdentity(users.getIdentity(), UserIdentityEnums.salesman))){
								return ;
							}
							mydis=new DMyproductdiscounts();
							mydis.setCartid(cartId); 
							mydis.setUserid(apply.getUserid());
							mydis.setStatus(0);
							mydis.setCreatetime(new Date());
							mydis.setDiscounttype(1);
							discountMapper.insert(mydis);
							//我参加活动
							DMyproductdiscountYiye dis_yiye= discountDetailMapper.selectByPrimaryKey(cartId);
							if(dis_yiye!=null){
								dis_yiye.setDiscountjson(JsonUtil.objectToJsonStr(discountlist));
								dis_yiye.setCreatetime(new Date());
								discountDetailMapper.updateByPrimaryKeySelective(dis_yiye);
							}else {
								dis_yiye=new DMyproductdiscountYiye();
								dis_yiye.setCartid(cartId);
								dis_yiye.setDiscountjson(JsonUtil.objectToJsonStr(discountlist));
								dis_yiye.setCreatetime(new Date());
								discountDetailMapper.insert(dis_yiye);
							}
						}
					}
				}
			} catch (Exception e) {
				logHelper.error(e); 
//				System.out.println(e);
			}
//		}else {
//			logHelper.error("找不到作品cartId="+cartId); 
//		}
	}
	/**
	 * 获取我作品的优惠
	 */
	public List<DMyproductdiscountmodel> findMycartDiscount(long userId,Long cartId){
		DMyproductdiscounts mydiscount= discountMapper.selectByPrimaryKey(cartId);
		if(mydiscount!=null&&mydiscount.getUserid()!=null&&mydiscount.getUserid().longValue()==userId){
			if(mydiscount.getStatus()!=null&&mydiscount.getStatus().intValue()==0){
				DMyproductdiscountYiye dis_yiye= discountDetailMapper.selectByPrimaryKey(cartId);
				if(dis_yiye!=null){
					@SuppressWarnings("unchecked")
					List<DMyproductdiscountmodel> list=(List<DMyproductdiscountmodel>)JsonUtil.jsonStrToObject(dis_yiye.getDiscountjson(), DMyproductdiscountmodel.class) ;
    				if(list!=null&&list.size()>0){
    					return list;
    				}
				}
			}
		}
		return null;
	}
	
	/**
	 * 使用作品优惠购买
	 * 
	 */
	public boolean useMycartDiscount(long userId,Long cartId){
		DMyproductdiscounts mydiscount= discountMapper.selectByPrimaryKey(cartId);
		if(mydiscount!=null&&mydiscount.getUserid()!=null&&mydiscount.getUserid().longValue()==userId){
			if(mydiscount.getStatus()!=null&&mydiscount.getStatus().intValue()==0){
				mydiscount.setStatus(1);
				mydiscount.setUsedtime(new Date());
				discountMapper.updateByPrimaryKeySelective(mydiscount);
				return true;
			}
		}
		return false;
	}
}
