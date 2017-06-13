package com.bbyiya.cts.job;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bbyiya.utils.HttpRequestHelper;

public class HeartbeatJob extends QuartzJobBean {
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		this.work();
	}

	private void work() {
		
		send_mpic();
	}

	

	private String send_mpic() {
		String url = "https://mpic.bbyiya.com/login/otherLogin";
		String dataParam = "headImg=zhangyao&loginType=2&nickName=111&openId=om_zCwqqNLeG-d39gC5llcDbbaic";
		String result = HttpRequestHelper.sendPost(url, dataParam);
		System.out.println(result); 
		return result;
	}
	
}
