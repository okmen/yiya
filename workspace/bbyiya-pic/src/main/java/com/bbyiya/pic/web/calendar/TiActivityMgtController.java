package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_act")
public class TiActivityMgtController extends SSOController {
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiActivitysMapper actMapper;
	@Autowired
	private TiMyworksMapper myworkMapper;
	@Autowired
	private PMyproductsMapper myproductMapper;
	
	/**
	 * 活动详情
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/actInfo")
	public String workInfo(int actId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiActivitys actInfo=actMapper.selectByPrimaryKey(actId);
			if(actInfo!=null){
				rq.setBasemodle(actInfo); 
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 参与活动
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/inActivity")
	public String inActivity(int actId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			Map<String, Object> map=new HashMap<String, Object>();
			// 1活动信息
			TiActivitys actInfo=actMapper.selectByPrimaryKey(actId);
			if(actInfo!=null){
				TiActivityworks activityworks= activityworksMapper.getActWorkListByActIdAndUserId(actId, user.getUserId());
				if(activityworks!=null){
					TiMyworks mywork= myworkMapper.selectByPrimaryKey(activityworks.getWorkid());
					if(mywork!=null){
						map.put("workId", mywork.getWorkid());
					}
				}else {//参加活动
					// 2 生成 作品id(cartId=workId)
					PMyproducts cart=new PMyproducts();
					cart.setCreatetime(new Date());
					cart.setUserid(0l);
					myproductMapper.insertReturnId(cart);
					
					// 3 新增我的作品
					TiMyworks myworks=new TiMyworks();
					myworks.setWorkid(cart.getCartid());
					myworks.setUserid(user.getUserId());
					myworks.setActid(actId);
					myworks.setCreatetime(new Date());
					myworks.setProductid(actInfo.getProductid());
					myworks.setStyleid(actInfo.getStyleid()==null?actInfo.getProductid():actInfo.getStyleid());
					myworkMapper.insert(myworks);
					// 4 新增活动作品信息
					activityworks=new TiActivityworks();
					activityworks.setWorkid(myworks.getWorkid());
					activityworks.setUserid(user.getUserId());
					activityworks.setCreatetime(new Date());
					activityworks.setActid(actId);
					activityworksMapper.insert(activityworks);
					
					// 5更新参与人数
					actInfo.setApplycount((actInfo.getApplycount()==null?0:actInfo.getApplycount().intValue())+1); 
					actMapper.updateByPrimaryKeySelective(actInfo);
					
					map.put("workId", myworks.getWorkid());
				}
				rq.setBasemodle(map); 
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
}
