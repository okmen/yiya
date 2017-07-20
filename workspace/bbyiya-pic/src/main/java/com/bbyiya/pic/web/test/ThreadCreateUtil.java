package com.bbyiya.pic.web.test;

import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.ObjectUtil;

public class ThreadCreateUtil implements Runnable {
	
	public String url;
	int index = 1;
	int size = 100;//
	int totalCount=100;
	public ThreadCreateUtil(String urlStr, int pageSize,int count) {
		url = urlStr;
		size = pageSize;
		totalCount=count;
	}
	public void run() {
		synchronized (this) {
			for(int i=0;i<size;i++){
				String result= HttpRequestHelper.sendPost(url, "");
				if(!ObjectUtil.isEmpty(result)){
					System.out.println(result); 
				}
			}
		}
	}
	
	
}
