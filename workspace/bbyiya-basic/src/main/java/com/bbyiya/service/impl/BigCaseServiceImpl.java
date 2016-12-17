package com.bbyiya.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.MBigcaseMapper;
import com.bbyiya.dao.MBigcaseclasstagMapper;
import com.bbyiya.dao.MBigcasetagMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.model.MBigcase;
import com.bbyiya.model.MBigcaseclasstag;
import com.bbyiya.model.MBigcasetag;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.service.IBigCaseService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.bigcase.BigcaseResult;
import com.bbyiya.vo.bigcase.BigcaseTagResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
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

	/**
	 * 大事件列表
	 * 
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	// TODO
	public PageInfo<MBigcase> find_MBigcasePage(Long userId, int pageIndex, int pageSize) {
		UChildreninfo child = childMapper.selectByPrimaryKey(userId);
		if (child != null) {
			try {
				int day = DateUtil.daysBetween(child.getBirthday(), new Date());
				BigCaseTime timeParam = getStartAndEndDay(day);
				if (timeParam != null) {
					// TODO 进行缓存处理
					PageHelper.startPage(pageIndex, pageSize);
					List<MBigcase> relist = bigcaseMapper.findMBigcaseList(timeParam.getStart(), timeParam.getEnd());
					PageInfo<MBigcase> pageInfo = new PageInfo<MBigcase>(relist);
					return pageInfo;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取大事件列表(PS：根据阶段获取列表)
	 * 
	 * @param userId
	 * @param isCurrent
	 * @param timeId
	 * @return
	 */
	public List<MBigcase> find_MBigcaselist(LoginSuccessResult user, boolean isCurrent, int timeId) {
		BigCaseTime timeParam = null;
		if (isCurrent) {
			if (user.getBabyInfo() != null) {
				try {
					int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
					timeParam = getStartAndEndDay(day);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else if (timeId > 0) {
			timeParam = getStartAndEndDayById(timeId);
		}
		if (timeParam != null) {
			String key = "bigcase_start_" + timeParam.getStart() + "end_" + timeParam.getEnd();
			List<MBigcase> list = (List<MBigcase>) RedisUtil.getObject(key);
			if (list != null && list.size() > 0) {
				return list;
			} else {
				list = bigcaseMapper.findMBigcaseList(timeParam.getStart(), timeParam.getEnd());
				if (list != null && list.size() > 0) {
					RedisUtil.setObject(key, list, 3600);
					return list;
				}
			}
		}
		return null;
	}

	// public PageInfo<BigcaseResult> find_MBigcaseResultPage(Long userId, int
	// pageIndex, int pageSize) {
	// UChildreninfo child = childMapper.selectByPrimaryKey(userId);
	// if (child != null) {
	// try {
	// int day = DateUtil.daysBetween(child.getBirthday(), new Date());
	// BigCaseTime timeParam = getStartAndEndDay(day);
	// if (timeParam != null) {
	// // TODO 进行缓存处理
	// PageHelper.startPage(pageIndex, pageSize);
	// List<BigcaseResult> relist =
	// bigcaseMapper.findMBigcaseResultList(timeParam.getStart(),
	// timeParam.getEnd());
	// PageInfo<BigcaseResult> pageInfo = new PageInfo<BigcaseResult>(relist);
	// for (BigcaseResult mo : pageInfo.getList()) {
	// List<MBigcaseclasstag> classtags =
	// tagMapper.findTagsByClassId(mo.getTypeid());
	// List<MBigcasetag> tags =
	// bigCaseMapper.findBigCaseTagByCaseId(mo.getCaseid());
	// if (tags != null && tags.size() > 0) {
	// List<BigcaseTagResult> tagResults = new ArrayList<BigcaseTagResult>();
	// for (MBigcasetag tag : tags) {
	// BigcaseTagResult model = new BigcaseTagResult();
	// model.setCaseId(mo.getCaseid());
	// model.setTagId(tag.getId());
	// model.setTagContent(tag.getTagcontent());
	// for (MBigcaseclasstag ctag : classtags) {
	// if (tag.getTagid().intValue() == ctag.getTagid().intValue()) {
	// model.setTagName(ctag.getValue());
	// }
	// }
	// tagResults.add(model);
	// }
	// mo.setTaglist(tagResults);
	// }
	// }
	// return pageInfo;
	// }
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	public PageInfo<BigcaseResult> find_MBigcaseResultPage(LoginSuccessResult user, int pageIndex, int pageSize) {
		if (user.getBabyInfo() != null) {
			try {
				int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
				BigCaseTime timeParam = getStartAndEndDay(day);
				if (timeParam != null) {
					// TODO 进行缓存处理
					PageHelper.startPage(pageIndex, pageSize);
					List<BigcaseResult> relist = bigcaseMapper.findMBigcaseResultList(timeParam.getStart(), day);
					PageInfo<BigcaseResult> pageInfo = new PageInfo<BigcaseResult>(relist);
					for (BigcaseResult mo : pageInfo.getList()) {
						mo.setTaglist(getBigcaseTagResultlist(mo.getCaseid()));
					}
					return pageInfo;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 大事件标签列表（列表及详情）
	 * 
	 * @param caseId
	 * @return
	 */
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
			if(tagResults!=null&&tagResults.size()>0){
				RedisUtil.setObject(key, tagResults,36000); 
			}
			return tagResults;
		}
		return null;
	}

	/**
	 * 获取宝宝当前属于哪个时间段
	 * 
	 * @param theday
	 * @return
	 */
	public BigCaseTime getStartAndEndDay(int theday) {
		BigCaseTime result = new BigCaseTime();
		List<Map<String, String>> timelist = ConfigUtil.getMaplist("timeIntervals");
		for (Map<String, String> map : timelist) {
			int start = ObjectUtil.parseInt(map.get("start"));
			int end = ObjectUtil.parseInt(map.get("end"));
			if (theday >= start && theday <= end) {// 找到这个时期
				result.setStart(start);
				result.setEnd(end);
				result.setTimeId(ObjectUtil.parseInt(map.get("id")));
				return result;
			}
		}
		return null;
	}

	/**
	 * 通过时期Id获取起止时间
	 * 
	 * @param timeId
	 * @return
	 */
	public BigCaseTime getStartAndEndDayById(int timeId) {
		BigCaseTime result = new BigCaseTime();
		List<Map<String, String>> timelist = ConfigUtil.getMaplist("timeIntervals");
		for (Map<String, String> map : timelist) {
			int start = ObjectUtil.parseInt(map.get("start"));
			int end = ObjectUtil.parseInt(map.get("end"));
			int id = ObjectUtil.parseInt(map.get("id"));
			if (id == timeId) {// 找到这个时期
				result.setStart(start);
				result.setEnd(end);
				return result;
			}
		}
		return null;
	}

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
