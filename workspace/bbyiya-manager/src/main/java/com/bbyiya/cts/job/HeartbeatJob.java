package com.bbyiya.cts.job;



import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.bbyiya.cts.service.ITempAutoOrderSumbitService;
import com.bbyiya.cts.vo.job.JobTime;
import com.bbyiya.dao.SysLogsMapper;
import com.bbyiya.model.SysLogs;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;

public class HeartbeatJob extends QuartzJobBean {
	
	private Logger Log = Logger.getLogger(HeartbeatJob.class);
	@Resource(name = "tempAutoOrderSumbitService")
	private ITempAutoOrderSumbitService autoOrderService;
	@Autowired
	private SysLogsMapper syslogMapper;
	
	
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		this.JobRun();
	}
	
	/**
	 * 公用job
	 */
	private void JobRun(){
		
		try {
			autoOrderService.dotempAutoOrderSumbit();
			Log.info("dotempAutoOrderSumbit执行自动下单操作完成！");
		} catch (Exception e) {
			Log.error("dotempAutoOrderSumbit方法执行出错！");
		}
			
		
//		//获取公用job的服务列表
//		List<Map<String, String>> joblist=ConfigUtil.getMaplist("jobs");
//		if(joblist!=null&&joblist.size()>0){
//			String keyBase=ConfigUtil.getSingleValue("currentRedisKey-Base")+"_job";
//			Date nowtime =new Date();
//			for (Map<String, String> job : joblist) {
//				if(ObjectUtil.parseInt(job.get("seton"))==1 ){
//					//服务的 keyid
//					String key=keyBase+"_"+job.get("id");
//					JobTime timeMod=  (JobTime)RedisUtil.getObject(key);
//					if(timeMod!=null&&timeMod.getLastTime()!=null){
//						long timeSpanLong= nowtime.getTime()-timeMod.getLastTime().getTime();
//						long timeSpan=timeSpanLong/1000;
//						if(timeSpan>ObjectUtil.parseLong(job.get("timespan"))){
//							//达到job的执行时间
//							if(ObjectUtil.parseInt(job.get("ispost"))==1){
//								HttpRequestHelper.sendPost(job.get("posturl"),""); 
//							}else{
//								//调去本地
//								doLocalServiceMothod(job.get("id"));
//							}
//							//达到job启动条件，执行job任务
//							timeMod.setLastTime(nowtime);
//							RedisUtil.setObject(key, timeMod);
//						}
//					}else {//刚进来时不执行job
//						timeMod=new JobTime();
//						timeMod.setLastTime(nowtime);
//						RedisUtil.setObject(key, timeMod);
//					}
//				}
//			}
//		}
	}

	
//	public void doLocalServiceMothod(String serviceId){	
//		try {
//			if(serviceId.equalsIgnoreCase("dotempAutoOrderSumbit")){
//				autoOrderService.dotempAutoOrderSumbit();	
//				Log.info(serviceId+"执行自动下单操作完成！");
//			}else{
//				//System.out.println("无方法执行！");
//			}
//		} catch (Exception e) {
//			Log.error(serviceId+"方法执行出错！");
//			addSysLog(serviceId+"方法执行出错！",serviceId,"自动下单");
//		}
//		
		
//	}
//	
//	public void addSysLog(String msg,String jobid,String jobname){
//		SysLogs log=new SysLogs();
//		log.setContent(msg);
//		log.setJobid(jobid);
//		log.setJobname(jobname);
//		log.setCreatetime(new Date());
//		syslogMapper.insert(log);
//	}
	
}
