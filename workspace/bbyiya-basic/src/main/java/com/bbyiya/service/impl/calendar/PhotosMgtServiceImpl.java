package com.bbyiya.service.impl.calendar;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PFetchlogsMapper;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.PFetchlogs;
import com.bbyiya.service.calendar.IPhotosMgtService;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.upload.FileUploadUtils_qiniu;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.storage.model.FetchRet;

@Service("photosMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class PhotosMgtServiceImpl implements IPhotosMgtService{
	@Autowired
	private OUserordersMapper userOrderMapper;
	@Autowired
	private OOrderproductphotosMapper ophotoMapper;
	@Autowired
	private PFetchlogsMapper fetchMapper;
	/**
	 * 不合格的图片处理
	 */
	public void orderPhotosLimitReplace(){
		PageHelper.startPage(1, 100);
		List<OOrderproductphotos> photolist= ophotoMapper.findImgNotGood();
		PageInfo<OOrderproductphotos> page=new PageInfo<OOrderproductphotos>(photolist);
		if(page!=null&&page.getList()!=null&&page.getList().size()>0){
			for (OOrderproductphotos photo : page.getList()) {
				try {
					String resultJson=HttpRequestHelper.sendGet(photo.getImgurl(), "imageInfo");
					JSONObject model = JSONObject.fromObject(resultJson);
					if (model != null) {
						FetchRet result=null;
						int fsize=Integer.parseInt(String.valueOf(model.get("size"))); 
						if(fsize/(1024*1024)>4){
							result=FileUploadUtils_qiniu.fetchBigIMG(photo.getImgurl());
						}else{
							result= FileUploadUtils_qiniu.fetch(photo.getImgurl());
						}
						if(result!=null){  
							String urlNew=ImgDomainUtil.getImageUrl_Full(result.key);
							//插入抓取日志
							PFetchlogs log=new PFetchlogs();
							log.setUrl(urlNew);
							log.setRemoteurl(photo.getImgurl()); 
							log.setStatus(0);
							log.setCreatetime(new Date()); 
							fetchMapper.insert(log);
							//替换图片
							photo.setImgurl(urlNew);
							ophotoMapper.updateByPrimaryKeySelective(photo);
							System.out.println(urlNew); 
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
		}
	}
}
