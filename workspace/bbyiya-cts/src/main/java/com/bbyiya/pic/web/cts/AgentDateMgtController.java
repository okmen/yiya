package com.bbyiya.pic.web.cts;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.PAgentmyproducttempviewMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UAgentsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.PAgentmyproducttempview;
import com.bbyiya.model.UAgents;
import com.bbyiya.pic.dao.IPic_DataTempDao;
import com.bbyiya.pic.vo.AgentDateVO;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/cts/agentdate")
public class AgentDateMgtController {
	@Autowired
	private IPic_DataTempDao temCtsDao;
	@Autowired
	private UBranchesMapper branchMapper;
	@Autowired
	private UAgentsMapper agentMapper;
	@Autowired
	private PMyproducttempapplyMapper tempApplyMapper;
	@Autowired
	private PAgentmyproducttempviewMapper agentTempMapper;
	@Autowired
	private EErrorsMapper errorMapper;

	/**
	 * 数据总览
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/all")
	public String templateMessageSend(@RequestParam(required = false, defaultValue = "0") int type, @RequestParam(required = false, defaultValue = "1") int index, @RequestParam(required = false, defaultValue = "10") int size, String keyWord) throws Exception {
		ReturnModel rq = new ReturnModel();
		updateYesterdayData();
		PageHelper.startPage(index, size);
		// 1 已包含 活动申请数量、完成数量的统计
		List<AgentDateVO> list = temCtsDao.findActslist(type, keyWord);
		PageInfo<AgentDateVO> page = new PageInfo<AgentDateVO>(list);
		if (page != null && page.getList() != null) {
			for (AgentDateVO aa : page.getList()) {
				// 2 代理商所有订单数统计
				List<AgentDateVO> orderlist = temCtsDao.findOrderVO(aa.getAgentUserId(), null, null);
				if (orderlist != null && orderlist.size() > 0) {
					aa.setOrderCount(orderlist.get(0).getOrderCountNew());
				}
				// 3 代理商所有作品统计
				List<AgentDateVO> cartlist1 = temCtsDao.findSelfMycartNewlist(aa.getAgentUserId(), null, null);
				int countCart = 0;
				if (cartlist1 != null && cartlist1.size() > 0) {
					countCart = cartlist1.get(0).getCartCountNew();
				}
				List<AgentDateVO> cartlist2 = temCtsDao.findInviteMycartNewlist(aa.getAgentUserId(), null, null);
				if (cartlist2 != null && cartlist2.size() > 0) {
					countCart = countCart + cartlist2.get(0).getCartCountNew();
				}
				aa.setCartCount(countCart);
			}
		}
		rq.setBasemodle(page);
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 统计昨日数据
	 */
	public void updateYesterdayData() {
		Date endTime = DateUtil.getTimesmorning();
		Date starttime = DateUtil.getYesterdaymorning();
		String keyName = ConfigUtil.getSingleValue("currentRedisKey-Base") + "_yDataNew08263";
		long keyString = ObjectUtil.parseLong(String.valueOf(RedisUtil.getObject(keyName))); 
		if (keyString > 0) {
			if (keyString > endTime.getTime()) {
				// System.out.println(1);
				return;
			}
		}
		try {
			// 所有的影楼（代理商 ） 轮训
			List<UAgents> agentlist = agentMapper.findAgentlistAll();
			if (agentlist != null && agentlist.size() > 0) {
				// 清楚所有昨日数据
				temCtsDao.clearPagentmyproducttempview();
				// 昨日的所有的活动 申请人数
				List<AgentDateVO> datelist = temCtsDao.findMyProductTempVo(null, starttime, endTime);
				// 代理商 昨日活动完成情况
				List<AgentDateVO> dataCompletelist = temCtsDao.findMyProductTempCompleteVo(null, starttime, endTime);
				// 代理商昨日订单情况
				List<AgentDateVO> dataOrderlist = temCtsDao.findOrderVO(null, starttime, endTime);
				// 昨日 用户自己新增作品情况 代理商客户
				List<AgentDateVO> dataCartNew1 = temCtsDao.findSelfMycartNewlist(null, starttime, endTime);
				// 昨日 用户被邀请编辑作品 代理商客户
				List<AgentDateVO> dataCartNew2 = temCtsDao.findInviteMycartNewlist(null, starttime, endTime);
				for (UAgents uu : agentlist) {
					boolean isNew = false;
					PAgentmyproducttempview view = agentTempMapper.selectByPrimaryKey(uu.getAgentuserid());
					if (view == null) {
						isNew = true;
						view = new PAgentmyproducttempview();
						view.setAgentuserid(uu.getAgentuserid());
					}
					// 1、昨日申请人数
					view.setApplycountnew(0);
					if (datelist != null && datelist.size() > 0) {
						for (AgentDateVO ap : datelist) {
							if (ap.getAgentUserId().longValue() == uu.getAgentuserid().longValue()) {
								if (ap.getApplyCountNew() != null && ap.getApplyCountNew().intValue() > 0) {
									view.setApplycountnew(ap.getApplyCountNew());
								}
							}
						}
					}

					// 2、昨日作品完成人数
					view.setCompletecountnew(0);
					if (dataCompletelist != null && dataCompletelist.size() > 0) {
						for (AgentDateVO ap : dataCompletelist) {
							if (ap.getAgentUserId().longValue() == uu.getAgentuserid().longValue()) {
								if (ap.getCompleteCountNew() != null && ap.getCompleteCountNew().intValue() > 0) {
									view.setCompletecountnew(ap.getCompleteCountNew());
								}
							}
						}
					}
					// 3、昨日订单新增
					view.setOrdercountnew(0);
					if (dataOrderlist != null && dataOrderlist.size() > 0) {
						for (AgentDateVO ap : dataOrderlist) {
							if (ap.getAgentUserId().longValue() == uu.getAgentuserid().longValue()) {
								if (ap.getOrderCountNew() != null && ap.getOrderCountNew().intValue() > 0) {
									view.setOrdercountnew(ap.getOrderCountNew());
								}
							}
						}
					}
					// 4、昨天作品新增
					view.setCartcountnew(0);
					if (dataCartNew1 != null && dataCartNew1.size() > 0) {
						for (AgentDateVO ap : dataCartNew1) {
							if (ap.getAgentUserId().longValue() == uu.getAgentuserid().longValue()) {
								if (ap.getCartCountNew() != null && ap.getCartCountNew().intValue() > 0) {
									view.setCartcountnew(ap.getCartCountNew());
								}

							}
						}
					}
					if (dataCartNew2 != null && dataCartNew2.size() > 0) {
						for (AgentDateVO ap : dataCartNew2) {
							if (ap.getAgentUserId().longValue() == uu.getAgentuserid().longValue()) {
								if (ap.getCartCountNew() != null && ap.getCartCountNew().intValue() > 0) {
									view.setCartcountnew(view.getCartcountnew() + ap.getCartCountNew());
								}
							}
						}
					}
					view.setUpdatetime(new Date());
					if (isNew) {
						agentTempMapper.insert(view);
					} else {
						agentTempMapper.updateByPrimaryKey(view);
					}
				}
			}
			RedisUtil.setObject(keyName, (new Date()).getTime());
		} catch (Exception e) {
			addlog("昨日数据更新：" + e);
		}
	}

	/**
	 * 插入错误Log
	 * 
	 * @param msg
	 */
	public void addlog(String msg) {
		try {
			EErrors errors = new EErrors();
			errors.setClassname(this.getClass().getName());
			errors.setMsg(msg);
			errors.setCreatetime(new Date());
			errorMapper.insert(errors);
		} catch (Exception e) {
		}
	}
}
