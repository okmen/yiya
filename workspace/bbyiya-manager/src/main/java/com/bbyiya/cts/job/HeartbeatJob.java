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
	 * ����job
	 */
	private void JobRun(){
		
		try {
			autoOrderService.dotempAutoOrderSumbit();
			Log.info("dotempAutoOrderSumbitִ���Զ��µ�������ɣ�");
		} catch (Exception e) {
			Log.error("dotempAutoOrderSumbit����ִ�г���");
		}
			
		
//		//��ȡ����job�ķ����б�
//		List<Map<String, String>> joblist=ConfigUtil.getMaplist("jobs");
//		if(joblist!=null&&joblist.size()>0){
//			String keyBase=ConfigUtil.getSingleValue("currentRedisKey-Base")+"_job";
//			Date nowtime =new Date();
//			for (Map<String, String> job : joblist) {
//				if(ObjectUtil.parseInt(job.get("seton"))==1 ){
//					//����� keyid
//					String key=keyBase+"_"+job.get("id");
//					JobTime timeMod=  (JobTime)RedisUtil.getObject(key);
//					if(timeMod!=null&&timeMod.getLastTime()!=null){
//						long timeSpanLong= nowtime.getTime()-timeMod.getLastTime().getTime();
//						long timeSpan=timeSpanLong/1000;
//						if(timeSpan>ObjectUtil.parseLong(job.get("timespan"))){
//							//�ﵽjob��ִ��ʱ��
//							if(ObjectUtil.parseInt(job.get("ispost"))==1){
//								HttpRequestHelper.sendPost(job.get("posturl"),""); 
//							}else{
//								//��ȥ����
//								doLocalServiceMothod(job.get("id"));
//							}
//							//�ﵽjob����������ִ��job����
//							timeMod.setLastTime(nowtime);
//							RedisUtil.setObject(key, timeMod);
//						}
//					}else {//�ս���ʱ��ִ��job
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
//				Log.info(serviceId+"ִ���Զ��µ�������ɣ�");
//			}else{
//				//System.out.println("�޷���ִ�У�");
//			}
//		} catch (Exception e) {
//			Log.error(serviceId+"����ִ�г���");
//			addSysLog(serviceId+"����ִ�г���",serviceId,"�Զ��µ�");
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
