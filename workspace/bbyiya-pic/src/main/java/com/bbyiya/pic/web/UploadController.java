package com.bbyiya.pic.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PFetchlogsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PFetchlogs;
import com.bbyiya.utils.AccessTokenUtils;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.upload.FileUploadUtils_qiniu;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.FetchRet;

@Controller
@RequestMapping(value = "/upload")
public class UploadController extends SSOController {

	@Autowired
	private PFetchlogsMapper fetchMapper;
	/**
	 * 获取图片上传uploadToken
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getUploadToken")
	public String loginAjax() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String token=FileUploadUtils_qiniu.getUpToken();
			rq.setStatu(ReturnStatus.Success);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("upToken", token);
			rq.setBasemodle(map);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取图片上传uploadToken （优化版）
	 * 2017-07-06
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getUploadTokenNew")
	public String getUploadTokenNew() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			Map<String, String> tokenMap=FileUploadUtils_qiniu.getUpTokenNew();
			if(tokenMap!=null){
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(tokenMap);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 图片下载
	 * @param media_id
	 * @param remoteUrl
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/filedownload")
	public String downWxImgToQiniu(String media_id,String remoteUrl) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			try {
				FetchRet result=null;
				if(!ObjectUtil.isEmpty(remoteUrl)){
					result= FileUploadUtils_qiniu.fetch(remoteUrl);
				}else if (!ObjectUtil.isEmpty(media_id)) {
					remoteUrl="https://api.weixin.qq.com/cgi-bin/media/get?access_token="+AccessTokenUtils.getAccessToken()+"&media_id="+media_id;
					result= FileUploadUtils_qiniu.fetch(remoteUrl);
				}
				if(result!=null){
					rq.setStatu(ReturnStatus.Success);
					Map<String, Object> map=new HashMap<String, Object>();
					String urlString=ImgDomainUtil.getImageUrl_Full(result.key);
					map.put("url", urlString);
					map.put("fsize", result.fsize);
					/*-----------------插入log记录-----------------------*/
					PFetchlogs log=new PFetchlogs();
					log.setUrl(urlString);
					log.setRemoteurl(remoteUrl); 
					log.setStatus(0);
					log.setCreatetime(new Date()); 
					fetchMapper.insert(log);
					rq.setBasemodle(map); 
				}
			} catch (QiniuException e) {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson(e.getMessage());
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("Ticket失效！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
