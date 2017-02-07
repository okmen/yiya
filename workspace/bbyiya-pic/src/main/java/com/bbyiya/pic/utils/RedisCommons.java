package com.bbyiya.pic.utils;

import java.util.HashMap;
import java.util.Map;

import com.bbyiya.utils.RedisUtil;

public class RedisCommons {

	/**
	 * 获取制作唯一号
	 * @param userId
	 * @param productId
	 * @return
	 */
	public static String getIndex(Long userId,Long productId){
		int temp=1000;
		long index=userId%temp;
		String key="user_work_index_"+index;
		Map<Long, Map<Long, Integer>> map= (Map<Long, Map<Long, Integer>>)RedisUtil.getObject(key);
		int val=1;
		if(map!=null){
			if(map.containsKey(userId)){
			 	Map<Long, Integer> userMap= map.get(userId);
			 	if(userMap.containsKey(productId)){
			 		val=userMap.get(productId)+1;
			 		userMap.put(productId, val);
			 	}else {
					userMap.put(productId, val);
				}
			 	map.put(userId, userMap);
			}else {
				Map<Long, Integer> userMap= new HashMap<Long, Integer>();
				userMap.put(productId, val);
				map.put(userId, userMap);
			}
		}else {
			map=new HashMap<Long, Map<Long,Integer>>();
			Map<Long, Integer> userMap= new HashMap<Long, Integer>();
			userMap.put(productId, val);
			map.put(userId, userMap);
		}
		RedisUtil.setObject(key, map); 
		return userId+"-"+productId+""+val;
	}
}
