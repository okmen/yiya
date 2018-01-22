package com.bbyiya.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.service.IBaseMyworkDao;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;

@Service("baseMyworkDao")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseMyworkDaoImpl implements IBaseMyworkDao{

	@Autowired
	private PMyproductsMapper myproductMapper;
	private static String KEY_MYWORKID="lastworkid_20180116";
	
	public synchronized long getNewWorkId(){
		long newworkId=0l;
		long max_workId =ObjectUtil.parseLong(RedisUtil.getString(KEY_MYWORKID));
		if(max_workId>0){
			newworkId=max_workId+1;
		}
		try {
			// 2 生成 作品id(cartId=workId)
			PMyproducts cart=new PMyproducts();
			cart.setCreatetime(new Date());
			cart.setUserid(0l);
			if(newworkId>0){
				cart.setCartid(newworkId);
				myproductMapper.insert(cart);
			}else{
				myproductMapper.insertReturnId(cart);
				newworkId=cart.getCartid();
			}
		} catch (Exception e) {
			// TODO: handle exception
			PMyproducts cart=new PMyproducts();
			cart.setCreatetime(new Date());
			cart.setUserid(0l);
			myproductMapper.insertReturnId(cart);
			if(cart.getCartid()!=null&&cart.getCartid().longValue()>0){
				newworkId=cart.getCartid();
			}
		}
		if(newworkId>0){
			RedisUtil.setString(KEY_MYWORKID, String.valueOf(newworkId));
		}
		return newworkId; 
	}
}
