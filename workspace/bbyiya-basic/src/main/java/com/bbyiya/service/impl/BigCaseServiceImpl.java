package com.bbyiya.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.MBigcaseMapper;
import com.bbyiya.dao.MBigcaseclasstagMapper;
import com.bbyiya.dao.MBigcasecollectMapper;
import com.bbyiya.dao.MBigcaseexpMapper;
import com.bbyiya.dao.MBigcasestagesummaryMapper;
import com.bbyiya.dao.MBigcasetagMapper;
import com.bbyiya.dao.MBigcaseuserimgsMapper;
import com.bbyiya.dao.MBigcaseuserreadMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.MBigcase;
import com.bbyiya.model.MBigcaseclasstag;
import com.bbyiya.model.MBigcasecollect;
import com.bbyiya.model.MBigcaseexp;
import com.bbyiya.model.MBigcasestagesummary;
import com.bbyiya.model.MBigcasetag;
import com.bbyiya.model.MBigcaseuserread;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.service.IBigCaseService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.bigcase.BigcaseResult;
import com.bbyiya.vo.bigcase.BigcaseTagResult;
import com.bbyiya.vo.bigcase.BigcasesummaryResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("bigCaseService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BigCaseServiceImpl implements IBigCaseService {
	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private MBigcaseMapper bigcaseMapper;
	@Autowired
	private MBigcaseclasstagMapper tagMapper;
	@Autowired
	private MBigcasetagMapper bigCaseMapper;
	@Autowired
	private MBigcaseuserimgsMapper imgMapper;
	@Autowired
	private MBigcasecollectMapper collectMapper;
	@Autowired
	private MBigcaseuserreadMapper caseReadMapper;
	@Autowired
	private MBigcaseexpMapper caseExpMapper;
	@Autowired
	private MBigcasestagesummaryMapper stageSumaryMapper;

//	public PageInfo<MBigcase> find_MBigcasePage(Long userId, int pageIndex, int pageSize) {
//		UChildreninfo child = childMapper.selectByPrimaryKey(userId);
//		if (child != null) {
//			try {
//				int day = DateUtil.daysBetween(child.getBirthday(), new Date());
//				BigCaseTime timeParam = getStartAndEndDay(day);
//				if (timeParam != null) {
//					PageHelper.startPage(pageIndex, pageSize);
//					List<MBigcase> relist = bigcaseMapper.findMBigcaseList(timeParam.getStart(), timeParam.getEnd());
//					PageInfo<MBigcase> pageInfo = new PageInfo<MBigcase>(relist);
//					return pageInfo;
//				}
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

//	public List<MBigcase> find_MBigcaselist(LoginSuccessResult user, boolean isCurrent, int timeId) {
//		BigCaseTime timeParam = null;
//		if (isCurrent) {
//			if (user.getBabyInfo() != null) {
//				try {
//					int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
//					timeParam = getStartAndEndDay(day);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
//		} else if (timeId > 0) {
//			timeParam = getStartAndEndDayById(timeId);
//		}
//		if (timeParam != null) {
//			String key = "bigcase_start_" + timeParam.getStart() + "end_" + timeParam.getEnd();
//			List<MBigcase> list = (List<MBigcase>) RedisUtil.getObject(key);
//			if (list != null && list.size() > 0) {
//				return list;
//			} else {
//				list = bigcaseMapper.findMBigcaseList(timeParam.getStart(), timeParam.getEnd());
//				if (list != null && list.size() > 0) {
//					RedisUtil.setObject(key, list, 3600);
//					return list;
//				}
//			}
//		}
//		return null;
//	}

//	public PageInfo<BigcaseResult> find_MBigcaseResultPage(LoginSuccessResult user, int pageIndex, int pageSize) {
//		if (user.getBabyInfo() != null) {
//			try {
//				// 宝宝周期
//				int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
//				BigCaseTime timeParam = getStartAndEndDay(day);
//				if (timeParam != null) {
//					PageHelper.startPage(pageIndex, pageSize);
//					List<BigcaseResult> relist = bigcaseMapper.findMBigcaseResultList(timeParam.getStart(), day);
//					PageInfo<BigcaseResult> pageInfo = new PageInfo<BigcaseResult>(relist);
//					for (BigcaseResult mo : pageInfo.getList()) {
//						mo.setTaglist(getBigcaseTagResultlist(mo.getCaseId()));
//					}
//					return pageInfo;
//				}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	public ReturnModel find_MBigcaseResultIndexPage(LoginSuccessResult user) {
		ReturnModel rq = new ReturnModel();
		if (user.getBabyInfo() != null) {
			try {
				// 宝宝周期
				int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
				BigCaseTime timeParam = getStartAndEndDay(day);
				// 最新大事件+当前时期以往的大事件
				List<BigcaseResult> relist = bigcaseMapper.findMBigcaseResultList(timeParam.getStart(), day);
				// 大事件预告
				List<BigcaseResult> relist_front = bigcaseMapper.findMBigcaseResultList(day + 1, timeParam.getEnd());
				// 我的大事件收藏
				List<MBigcasecollect> list = collectMapper.findMBigCaseCollect(user.getUserId());
				int count = 0, currenIndex = 0;
				if (relist != null && relist.size() > 0) {
					count += relist.size();
					currenIndex += relist.size();
					for (BigcaseResult mo : relist) {
						mo.setTaglist(getBigcaseTagResultlist(mo.getCaseId()));
						if(isRead(user.getUserId(), mo.getCaseId())){ 
							mo.setIsRead(1); 
						}
						
						//检验大事件是否已经收藏
						if (list != null && list.size() > 0) {
							for (MBigcasecollect cc : list) {
								if (cc.getCaseid().intValue() == mo.getCaseId().intValue()) {
									mo.setIsCollected(1);
								}
							}
						}
					}
				}
				if (relist_front != null && relist_front.size() > 0) {
					count += relist_front.size();
					for (BigcaseResult mo : relist_front) {
						mo.setTaglist(getBigcaseTagResultlist(mo.getCaseId()));
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("totalCount", count);//当前阶段总共的大事件
				map.put("index", currenIndex);//当前所处的大事件位置
				map.put("bigcase", relist);//大事件列表
				map.put("front", relist_front);//预览
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(map);

			} catch (ParseException e) {
				e.printStackTrace();
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("系统错误");
			}
		}
		return rq;
	}

	public ReturnModel getBigcaseDetails(LoginSuccessResult user, Integer caseId) {
		ReturnModel rqModel = new ReturnModel();
		BigcaseResult caseResult = bigcaseMapper.getMBigcaseResultByCaseId(caseId);
		if (caseResult != null) {
			caseResult.setTaglist(getBigcaseTagResultlist(caseId));
			
			addReads(user.getUserId(), caseId); 
			// 我的大事件收藏---是否收藏了此大事件
			List<MBigcasecollect> list = collectMapper.findMBigCaseCollect(user.getUserId());
			for (MBigcasecollect cc : list) {
				if (cc.getCaseid().intValue() == caseId) {
					caseResult.setIsCollected(1);
				}
			}
			
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(caseResult);
			
		} else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("找不到相应的事件");
		}
		return rqModel;
	}

	
	public ReturnModel addCollection(Long userid, Integer caseId) {
		ReturnModel rqModel = new ReturnModel();
		List<MBigcasecollect> coList = collectMapper.findMBigCaseCollect(userid);
		if (coList != null && coList.size() > 0) {
			for (MBigcasecollect cc : coList) {
				if (cc.getCaseid().intValue() == caseId) {
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("您已经收藏了该条记录！");
					return rqModel;
				}
			}
		}
		MBigcase bigcase = bigcaseMapper.selectByPrimaryKey(caseId);
		if (bigcase != null) {
			MBigcasecollect model = new MBigcasecollect();
			model.setCaseid(caseId);
			model.setUserid(userid);
			model.setCreatetime(new Date());
			try {
				collectMapper.insert(model);
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setStatusreson("收藏成功！");
			} catch (Exception e) {
				// TODO: handle exception
				rqModel.setStatu(ReturnStatus.SystemError);
				rqModel.setStatusreson("收藏失败");
			}
		} else {
			rqModel.setStatu(ReturnStatus.ParamError_1);
			rqModel.setStatusreson("不存在的大事件");
		}
		return rqModel;
	}

	public ReturnModel deleCollection(Long userid, Integer caseId) {
		ReturnModel rqModel = new ReturnModel();
		List<MBigcasecollect> coList = collectMapper.findMBigCaseCollect(userid);
		if (coList != null && coList.size() > 0) {
			for (MBigcasecollect cc : coList) {
				if (cc.getCaseid().intValue() == caseId) {
					collectMapper.deleteByPrimaryKey(cc.getCollectid());
					rqModel.setStatu(ReturnStatus.Success);
					rqModel.setStatusreson("取消成功！");
					return rqModel;
				}
			}
		}
		rqModel.setStatu(ReturnStatus.ParamError_1);
		rqModel.setStatusreson("未找到相应的收藏！");
		return rqModel;
	}

	public List<BigcaseResult> find_MBigcasecollectlist(Long userId) {
		List<BigcaseResult> results = new ArrayList<BigcaseResult>();
		List<MBigcasecollect> list = collectMapper.findMBigCaseCollect(userId);
		if (list != null && list.size() > 0) {
			for (MBigcasecollect mod : list) {
				BigcaseResult caseMod = bigcaseMapper.getMBigcaseResultByCaseId(mod.getCaseid());
				caseMod.setTaglist(getBigcaseTagResultlist(mod.getCaseid()));
				results.add(caseMod);
			}
		}
		return results;
	}

	public List<BigcasesummaryResult> getBigcasesummaryResult(int timeId) {
		// TODO
		List<BigcasesummaryResult> summaryList=stageSumaryMapper.findStageSummaryByStageId(timeId);
		if(summaryList!=null&&summaryList.size()>0){
			for (BigcasesummaryResult su : summaryList) {
				List<MBigcaseclasstag> tags= tagMapper.findTagsByClassId(su.getCasetypeid());
				su.setTags(tags); 
			}
		}
		return summaryList;
	}

	public List<BigcaseTagResult> getBigcaseTagResultlist(Integer caseId) {
		String key = "caseTag_id_" + caseId;
		List<BigcaseTagResult> list = (List<BigcaseTagResult>) RedisUtil.getObject(key);
		if (list != null && list.size() > 0) {
			return list;
		}
		List<MBigcaseclasstag> classtags = tagMapper.findTagsByClassId(caseId);
		List<MBigcasetag> tags = bigCaseMapper.findBigCaseTagByCaseId(caseId);
		if (tags != null && tags.size() > 0) {
			List<BigcaseTagResult> tagResults = new ArrayList<BigcaseTagResult>();
			for (MBigcasetag tag : tags) {
				BigcaseTagResult model = new BigcaseTagResult();
				model.setCaseId(caseId);
				model.setTagId(tag.getId());
				model.setTagContent(tag.getTagcontent());
				for (MBigcaseclasstag ctag : classtags) {
					if (tag.getTagid().intValue() == ctag.getTagid().intValue()) {
						model.setTagName(ctag.getValue());
					}
				}
				tagResults.add(model);
			}
			if (tagResults != null && tagResults.size() > 0) {
				// TODO 缓存信息
				// RedisUtil.setObject(key, tagResults,36000);
			}
			return tagResults;
		}
		return null;
	}

	public BigCaseTime getStartAndEndDay(int theday) {
		BigCaseTime result = new BigCaseTime();
		List<Map<String, String>> timelist = ConfigUtil.getMaplist("timeIntervals");
		for (Map<String, String> map : timelist) {
			int start = ObjectUtil.parseInt(map.get("start"));
			int end = ObjectUtil.parseInt(map.get("end"));
			if (theday >= start && theday <= end) {
				result.setStart(start);
				result.setEnd(end);
				result.setTimeId(ObjectUtil.parseInt(map.get("id")));
				return result;
			}
		}
		return null;
	}

	public BigCaseTime getStartAndEndDayById(int timeId) {
		BigCaseTime result = new BigCaseTime();
		List<Map<String, String>> timelist = ConfigUtil.getMaplist("timeIntervals");
		for (Map<String, String> map : timelist) {
			int start = ObjectUtil.parseInt(map.get("start"));
			int end = ObjectUtil.parseInt(map.get("end"));
			int id = ObjectUtil.parseInt(map.get("id"));
			if (id == timeId) {
				result.setStart(start);
				result.setEnd(end);
				return result;
			}
		}
		return null;
	}

	// -----------------------------------------------------------------------------------------
	// 大事件
	private static String KEY_BIGCASE_READ = "bigcase_read_caseId_";

	public boolean isRead(Long userId, Integer caseId) {
		String key_bigcaseRead = KEY_BIGCASE_READ + userId;
		Map<Integer, Integer> mapUser = (Map<Integer, Integer>) RedisUtil.getObject(key_bigcaseRead);
		if (mapUser != null && !mapUser.isEmpty()) {
			if (mapUser.containsKey(caseId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 加入已读大事件
	 * 
	 * @param userId
	 * @param caseId
	 */
	public void addReads(Long userId, Integer caseId) {
		//增加阅读数============================
		MBigcaseexp exp= caseExpMapper.selectByPrimaryKey(caseId);
		if(exp!=null){
			exp.setReadcount(exp.getReadcount()+1);
			caseExpMapper.updateByPrimaryKeySelective(exp);
		}else {
			exp=new MBigcaseexp();
			exp.setCaseid(caseId); 
			exp.setReadcount(1);
			caseExpMapper.insert(exp);
		}
		String key_bigcaseRead = KEY_BIGCASE_READ + userId;
		Map<Integer, Integer> mapUser = (Map<Integer, Integer>) RedisUtil.getObject(key_bigcaseRead);
		if (mapUser != null && !mapUser.isEmpty()) {
			if (mapUser.containsKey(caseId)) {
				return;
			}
		} else {
			mapUser = new HashMap<Integer, Integer>();
		}
		mapUser.put(caseId, 1);
		//------------------------------------------------------
		MBigcaseuserread record = new MBigcaseuserread();
		record.setCaseid(caseId);
		record.setUserid(userId);
		record.setCreatetime(new Date());
		caseReadMapper.insert(record);
		RedisUtil.setObject(key_bigcaseRead, mapUser);
	}

//	/**
//	 * 
//	 * @param userId
//	 * @param caseId
//	 */
//	public void add_MBigcaseuserread(Long userId, Integer caseId) {
//		// 新增用户阅读记录---------------------------
//		MBigcaseuserread record = new MBigcaseuserread();
//		record.setCaseid(caseId);
//		record.setUserid(userId);
//		record.setCreatetime(new Date());
//		caseReadMapper.insert(record);
//	}
	
	

	/**
	 * 
	 * @author Administrator
	 *
	 */
	public class BigCaseTime {
		private int start;
		private int end;
		private int timeId;

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public int getTimeId() {
			return timeId;
		}

		public void setTimeId(int timeId) {
			this.timeId = timeId;
		}

	}
}
