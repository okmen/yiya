package com.bbyiya.pic.service.impl.cts;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UAgentsMapper;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.UAdmin;
import com.bbyiya.model.UAgents;
import com.bbyiya.pic.service.cts.IActivityStatisticsService;
import com.bbyiya.pic.vo.activity.AllActivityCountResultVO;
import com.bbyiya.pic.vo.report.ReportJsonData;
import com.bbyiya.pic.vo.report.ReportLineDataVO;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ctsActivityStatisticsService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class ActivityStatisticsServiceImpl implements IActivityStatisticsService{
	
	@Autowired
	private PMyproducttempMapper mytempMapper;
	@Autowired
	private PMyproductsMapper myproductMapper;
	@Autowired
	private PMyproducttempapplyMapper tempapplyMapper;
	@Autowired
	private UAgentsMapper agentMapper;
	/**
	 * 活动统计页面
	 * @param userid
	 * @return
	 */
	public ReturnModel getActivityStaticsticsPage(Long agentuserid,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		AllActivityCountResultVO countvo=new AllActivityCountResultVO();
		//未开启的活动数
		Integer nostartNum=mytempMapper.getAgentActivityCountByStatus(agentuserid, Integer.parseInt(MyProductTempStatusEnum.disabled.toString()));
		//进行中的活动数
		Integer goingNum=mytempMapper.getAgentActivityCountByStatus(agentuserid, Integer.parseInt(MyProductTempStatusEnum.enable.toString()));
		//已结束的活动数
		Integer endingNum=mytempMapper.getAgentActivityCountByStatus(agentuserid, Integer.parseInt(MyProductTempStatusEnum.over.toString()));
		//所有状态的活动数
		Integer allNum=mytempMapper.getAgentActivityCountByStatus(agentuserid, null);
		countvo.setNostartNum(nostartNum==null?0:nostartNum);
		countvo.setGoingNum(goingNum==null?0:goingNum);
		countvo.setEndingNum(endingNum==null?0:endingNum);
		countvo.setAllNum(allNum==null?0:allNum);
		
		//新客资活动总报名人数
		Integer xkzapplycount=mytempMapper.getAgentActivityApplyCountByType(agentuserid, Integer.parseInt(MyProductTempType.normal.toString()));
		//兑换码活动总报名人数
		Integer codeapplycount=mytempMapper.getAgentActivityApplyCountByType(agentuserid, Integer.parseInt(MyProductTempType.code.toString()));
//		//影楼一对一总报名人数
//		Integer ylydyApplycount=myproductMapper.getAgentOneToOneApplyCount(agentuserid);
//		if(ylydyApplycount==null)ylydyApplycount=0;
//		//影楼一对一作品总下单人数
//		Integer ylydyCompletecount=myproductMapper.getAgentOneToOneCompleteCount(agentuserid);
//		if(ylydyCompletecount==null)ylydyCompletecount=0;
//		
//		PMyproducttemp oneTone=new PMyproducttemp();
//		oneTone.setTitle("影楼一对一");
//		oneTone.setStatus(1);
//		oneTone.setType(1);
//		oneTone.setApplycount(ylydyApplycount);
//		oneTone.setCompletecount(ylydyCompletecount);
		
		//活动报名人数及完成人数统计
		HashMap<String, Object> countmap=mytempMapper.getAgentApplyCompleteMap(agentuserid);	
		if(countmap!=null){
			countvo.setApplyNum(Integer.parseInt(countmap.get("applyCount").toString()));
			countvo.setCompleteNum(Integer.parseInt(countmap.get("completeCount").toString()));
		}
		
		countvo.setXkzhdApplyNum(xkzapplycount);
		countvo.setCodeApplyNum(codeapplycount);

		PageHelper.startPage(index, size);
		List<PMyproducttemp> templist=mytempMapper.findAllTempListByAgentUserId(agentuserid);
		PageInfo<PMyproducttemp> resultPage = new PageInfo<PMyproducttemp>(templist);
		HashMap<String, Object> mapresult=new HashMap<String, Object>();
		mapresult.put("countvo", countvo);
		mapresult.put("resultPage", resultPage);
		rq.setBasemodle(mapresult);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	
	/**
	 * 单个活动的统计页
	 * @param tempid
	 * @param starttime
	 * @param endtime
	 * @param type 0 天，1小时，默认为0
	 * @return
	 * @throws ParseException 
	 */
	public ReturnModel activityDetailsCountPage(Integer tempid,String starttime,String endtime,Integer type) throws ParseException{
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		HashMap<String, Object> resultmap=new HashMap<String, Object>();
		
		//得到单个活动信息
		if(type==null) type=0;
		if(tempid==null||tempid==0){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("tempid参数为空！");
			return rq;
		}else{
			//属于活的报表
			PMyproducttemp temp=mytempMapper.selectByPrimaryKey(tempid);
			resultmap.put("tempinfo", temp);
			//选择天，天数不能大于24天
			int days=DateUtil.daysBetween(starttime, endtime);
			if(type.intValue()==1){
				//精确到小时
				if(days>1){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("日期不能大于1天！");
					return rq;
				}
				days=24;
			}else{//精确到天
				if(days>24){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("日期不能大于24天！");
					return rq;
				}
			}
			String[]xcontent=new String[days];
			List<Integer> dataapply=new ArrayList<Integer>();
			List<Integer> datacomplete=new ArrayList<Integer>();
			String startTimeStr="";
			String endTimeStr="";
			for(int i=0;i<days;i++){
				
				if(type.intValue()==1){//精确到小时
					if(endTimeStr=="") endTimeStr=DateUtil.getTimeStr(DateUtil.getDateByString("yyyy-MM-dd", starttime),"yyyy-MM-dd")+ " 00:00:00";
					startTimeStr=endTimeStr;
					endTimeStr=DateUtil.addDateHour(startTimeStr, 1);
					xcontent[i]=(i+1)+"";
				}else{
					startTimeStr=DateUtil.getSpecifiedDayAfter(starttime, i)+ " 00:00:00";
					endTimeStr=DateUtil.getEndTime(startTimeStr);
					
					xcontent[i]=DateUtil.getTimeStr(DateUtil.getDateByString("yyyy-MM-dd", startTimeStr), "MM月dd");
				}
				Integer applycount=tempapplyMapper.countTempApplyByDay(tempid, startTimeStr, endTimeStr);
				Integer completecount=tempapplyMapper.countTempCompleteByDay(tempid, startTimeStr, endTimeStr);
				dataapply.add(applycount);
				datacomplete.add(completecount);
			}
			List<ReportJsonData> reportjsondata=new ArrayList<ReportJsonData>();
			ReportJsonData jsonDataApply=new ReportJsonData();
			jsonDataApply.setName("新增报名");
			jsonDataApply.setData(dataapply);
			ReportJsonData jsonDataComplete=new ReportJsonData();
			jsonDataComplete.setName("新增完成");
			jsonDataComplete.setData(datacomplete);
			
			reportjsondata.add(jsonDataApply);
			reportjsondata.add(jsonDataComplete);
			ReportLineDataVO tld=new ReportLineDataVO();
			tld.setXcontent(xcontent);
			tld.setData(reportjsondata);
			
			resultmap.put("jsons", tld);
			rq.setStatu(ReturnStatus.Success);	
			rq.setBasemodle(resultmap);
			
			
		}
		
		
		return rq;
	}
	
	/**
	 * 得到所有代理商列表
	 * @return
	 * @throws ParseException
	 */
	public ReturnModel getAgentList(){
		ReturnModel rq=new ReturnModel();
		List<UAgents> agentlist= agentMapper.findAgentlistAll();
		rq.setStatu(ReturnStatus.Success);	
		rq.setBasemodle(agentlist);
		return rq;
	}
	
	
}
