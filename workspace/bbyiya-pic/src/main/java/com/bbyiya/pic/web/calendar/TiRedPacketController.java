package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworkredpacketlogsMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.enums.PayOrderStatusEnums;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.RedpacketStatus;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworkredpacketlogs;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/redpacket")
public class TiRedPacketController extends SSOController {

	@Autowired
	private TiMyworkredpacketlogsMapper redlogsMapper;
	@Autowired
	private TiMyworkcustomersMapper redcustomerMapper;
	@Autowired
	private OPayorderMapper payMapper;
	/**
	 * 生成红包订单
	 * @param workId
	 * @param amount
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderInfo")
	public String getWxPayParam(Long workId, String amount) throws MapperException {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiMyworkcustomers actMyworkcustomers= workcustomerMapper.selectByPrimaryKey(workId);
			if(actMyworkcustomers!=null){
				if(actMyworkcustomers.getNeedredpackettotal()!=null&&actMyworkcustomers.getNeedredpackettotal().doubleValue()>0){
					double amountPayed=actMyworkcustomers.getRedpacketamount()==null?0:actMyworkcustomers.getRedpacketamount().doubleValue();				
					if(actMyworkcustomers.getNeedredpackettotal().doubleValue()>amountPayed){
						List<TiMyworkredpacketlogs> amountList=redlogsMapper.findredpacketLogs(null, workId, Integer.parseInt(RedpacketStatus.nopay.toString()));
						//有效的未支付金额
						double amountTemp=0d;
						for (TiMyworkredpacketlogs aa : amountList) {
							// 1  如果2分钟不支付红包 置为过期红包
							if (isInvalid(aa.getCreatetime())) {
								aa.setStatus(Integer.parseInt(RedpacketStatus.invalid.toString()));
								redlogsMapper.updateByPrimaryKeySelective(aa);
							} else {
								//如果之前记录有自己的还在有效期记录，重新生成一条新纪录置之前的过期
								if(aa.getUserid().longValue()==user.getUserId().longValue()){
									aa.setStatus(Integer.parseInt(RedpacketStatus.invalid.toString()));
									redlogsMapper.updateByPrimaryKeySelective(aa);
								}else {
									amountTemp = amountTemp + aa.getAmount();
								}
							}
						}
						if(actMyworkcustomers.getNeedredpackettotal().doubleValue()>(amountPayed+amountTemp)){
							//可以投递的红包金额
							double limitamount=actMyworkcustomers.getNeedredpackettotal().doubleValue()-amountPayed-amountTemp;
							if(limitamount>=ObjectUtil.parseDouble(amount)){
								//有效红包生成
								TiMyworkredpacketlogs log=new TiMyworkredpacketlogs();
								log.setAmount(ObjectUtil.parseDouble(amount));
								log.setUserid(user.getUserId());
								log.setWorkid(workId); 
								log.setCreatetime(new Date());
								log.setHeadimg(user.getHeadImg());
								log.setNickname(user.getNickName());
								log.setStatus(Integer.parseInt(RedpacketStatus.nopay.toString()));
								String payId=GenUtils.getOrderNo(user.getUserId());
								log.setPayid(payId);
								redlogsMapper.insert(log);
								OPayorder payorder=new OPayorder();
								payorder.setPayid(payId);
								payorder.setUserid(user.getUserId());
								payorder.setTotalprice(log.getAmount());
								payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_redpaket.toString()));
								payorder.setCreatetime(new Date());
								payorder.setStatus(Integer.parseInt(PayOrderStatusEnums.noPay.toString()));
								payMapper.insert(payorder);
								rq.setStatu(ReturnStatus.Success);
								rq.setBasemodle(payId); 
							}else {
								rq.setStatu(ReturnStatus.SystemError);
								rq.setStatusreson("整体预付的金额超出，请控制金额在"+(limitamount)+"元以内（包括"+limitamount+"元）！");
								return JsonUtil.objectToJsonStr(rq);
							}
						}
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("来晚一步，红包金额已经筹满了！");
						return JsonUtil.objectToJsonStr(rq);
					}
				}
			}
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 120秒红包单过期验证
	 * @param createTime
	 * @return
	 */
	public boolean isInvalid(Date createTime) {
		Date nowtime=new Date();
		long interval = (nowtime.getTime()-createTime.getTime())/1000;
		if(interval>120){
			return true;
		}
		return false;
	}

	/*-------------------------------------------------------------------------------------------*/
	@Autowired
	private TiMyworkcustomersMapper workcustomerMapper;
	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiMyartsdetailsMapper detailMapper;
	@Autowired
	private TiPromoteradvertinfoMapper advertMapper;
	/**
	 * 代客制作详情
	 * @param workId
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
	@RequestMapping(value = "/workcustomerInfo")
	public String workcustomerInfo(Long workId) throws MapperException {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiMyworkcustomers actMyworkcustomers= workcustomerMapper.selectByPrimaryKey(workId);
			if(actMyworkcustomers!=null){
				Map<String, Object> map=new HashMap<String, Object>();
				//作品详情
				List<TiMyartsdetails> details= detailMapper.findDetailsByWorkId(workId);
				map.put("details", details);
				map.put("actStatus", actMyworkcustomers.getStatus());
				map.put("workInfo", workMapper.selectByPrimaryKey(workId));
				//需要凑多少钱
				if(actMyworkcustomers.getNeedredpackettotal()!=null&&actMyworkcustomers.getNeedredpackettotal().doubleValue()>0){
					double amount=actMyworkcustomers.getRedpacketamount()==null?0:actMyworkcustomers.getRedpacketamount().doubleValue();				
					double amountNeed=actMyworkcustomers.getNeedredpackettotal().doubleValue()-amount;
					map.put("amountNeed", amountNeed);
					if(amount>0){
						map.put("amountLog", redlogsMapper.findredpacketLogs(null, workId, Integer.parseInt(RedpacketStatus.payed.toString())));
					}
					//已完成红包/已经下单
					if(amountNeed<=0||actMyworkcustomers.getStatus()!=null&&actMyworkcustomers.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString())){
						TiPromoteradvertinfo advertMod= advertMapper.getModelByPromoterUserId(actMyworkcustomers.getPromoteruserid());
						if(advertMod!=null){
							map.put("advert", advertMod);
						}
					}
				}else {
					map.put("amountNeed",0); 
					TiPromoteradvertinfo advertMod= advertMapper.getModelByPromoterUserId(actMyworkcustomers.getPromoteruserid());
					if(advertMod!=null){
						map.put("advert", advertMod);
					}
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(map);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("无效的地址");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	
}
