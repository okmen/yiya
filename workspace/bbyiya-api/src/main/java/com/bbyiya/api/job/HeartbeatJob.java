package com.bbyiya.api.job;

import java.util.Date;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.RedisUtil;

public class HeartbeatJob extends QuartzJobBean {
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		this.work();
	}

	private void work() {
		send_app();
		send_mpic();
		RedisUtil.setObject("timer-send", DateUtil.getTimeStr(new Date(), "yyyy-MM-dd HH:mm:ss"));  
	}

	private String send_app() {
		String url = "https://api.bbyiya.net/login/loginAjax";
		String dataParam = "phone=15012703706&pwd=123456";
		String result = HttpRequestHelper.sendPost(url, dataParam);
		return result;
	}

	private String send_mpic() {
		String url = "https://mpic.bbyiya.com/login/otherLogin";
		String dataParam = "headImg=zhangyao&loginType=2&nickName=111&openId=om_zCwqqNLeG-d39gC5llcDbbaic";
		String result = HttpRequestHelper.sendPost(url, dataParam);
		return result;
	}
}
