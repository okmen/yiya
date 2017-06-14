package com.bbyiya.cts.job;



import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bbyiya.cts.service.ITempAutoOrderSumbitService;
import com.bbyiya.cts.vo.job.JobTime;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;

public class HeartbeatJob extends QuartzJobBean {
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		this.JobRun();
	}
	
	/**
	 * ����job
	 */
	private void JobRun(){
		//��ȡ����job�ķ����б�
		List<Map<String, String>> joblist=ConfigUtil.getMaplist("jobs");
		if(joblist!=null&&joblist.size()>0){
			String keyBase=ConfigUtil.getSingleValue("currentRedisKey-Base")+"_job";
			Date nowtime =new Date();
			for (Map<String, String> job : joblist) {
				if(ObjectUtil.parseInt(job.get("seton"))==1 ){
					//����� keyid
					String key=keyBase+"_"+job.get("id");
					JobTime timeMod=  (JobTime)RedisUtil.getObject(key);
					if(timeMod!=null&&timeMod.getLastTime()!=null){
						long timeSpanLong= nowtime.getTime()-timeMod.getLastTime().getTime();
						long timeSpan=timeSpanLong/1000;
						if(timeSpan>ObjectUtil.parseLong(job.get("timespan"))){
							//�ﵽjob��ִ��ʱ��
							if(ObjectUtil.parseInt(job.get("ispost"))==1){
								HttpRequestHelper.sendPost(job.get("posturl"),""); 
							}else{//��ȥ����
								doLocalServiceMothod(job.get("id"));
							}
							//�ﵽjob����������ִ��job����
							timeMod.setLastTime(nowtime);
							RedisUtil.setObject(key, timeMod);
						}
					}else {//�ս���ʱ��ִ��job
						timeMod=new JobTime();
						timeMod.setLastTime(nowtime);
						RedisUtil.setObject(key, timeMod);
					}
				}
			}
		}
	}

	@Resource(name = "tempAutoOrderSumbitService")
	private ITempAutoOrderSumbitService autoOrderService;
	
	public void doLocalServiceMothod(String serviceId){
		
		try {
			if(serviceId.equalsIgnoreCase("dotempAutoOrderSumbit")){
				ReturnModel rq=autoOrderService.dotempAutoOrderSumbit();
				if(!rq.getStatu().equals(ReturnStatus.Success))//δͨ��������֤
				{
					System.out.println("�Զ��µ�ʧ�ܣ�"+rq.getStatusreson());
				}else{
					System.out.println("�Զ��µ��ɹ�ִ�У�");
				}
			}else{
				System.out.println("�޷���ִ�У�");
			}
		} catch (Exception e) {
			System.out.println(serviceId+"����ִ�г���");
			// TODO: handle exception
		}
		
		
	}
	
	
}
