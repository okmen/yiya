package com.bbyiya.pic.web.ibs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.utils.ExportExcel;
import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/ibs/branch")
public class MyProductsController extends SSOController {
	
	@Resource(name = "pic_productService")
	private IPic_ProductService proService;
	
	
	
	/**
	 * IBS客户一对一作品列表
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyProductsForBranch")
	public String findMyProductsForBranch(Integer inviteStatus,@RequestParam(required = false, defaultValue = "")String keywords,
			@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=proService.findMyProductsForBranch(user.getUserId(),null,inviteStatus,keywords,index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 得到模板下的作品列表
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyProductslistForTempId")
	public String findMyProductslistForTempId(Integer tempid,Integer activeStatus,
			@RequestParam(required = false, defaultValue = "")String keywords,
			@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=proService.findMyProductslistForTempId(user.getUserId(), tempid,activeStatus,keywords, index, size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * IBS数据统计导出Excel
	 * @param request
	 * @return
	 * @throws MapperException 
	 */
	@RequestMapping(value="/tempProductExportExcel")
	@ResponseBody
	public String tempProductExportExcel(Integer tempid,Integer activeStatus,
			@RequestParam(required = false, defaultValue = "")String keywords) throws Exception {
		// 列头
		String[] headers =new String[11];
		headers[0]="作品ID";
		headers[1]="作品名称";
		headers[2]="本店员工";
		headers[3]="客户昵称";
		headers[4]="宝宝生日";
		headers[5]="活动状态";
		headers[6]="制作类型";
		headers[7]="作品进度";
		headers[8]="评论数";	
		headers[9]="客户手机";	
		headers[10]="收货地址";	
		String[] fields = new String[11];
		fields[0]="cartid";
		fields[1]="title";
		fields[2]="userName";
		fields[3]="invitedName";
		fields[4]="birthdayStr";
		fields[5]="activeStatus";
		fields[6]="productTitle";
		fields[7]="count";
		fields[8]="commentsCount";
		fields[9]="inviteModel.invitephone";
		fields[10]="address";
		
		//导出格式
		String format =".xlsx";
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=proService.findMyProductslistForTempId(user.getUserId(), tempid,activeStatus,keywords, 0, 0);
			if(rq.getStatu()!=ReturnStatus.Success){
				return JsonUtil.objectToJsonStr(rq);
			}
			
			PageInfo<MyProductResultVo> resultPage =(PageInfo<MyProductResultVo>) rq.getBasemodle();
			
			List<MyProductResultVo> list=resultPage.getList();
			Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子
			// 获取用户的当前工作主目录 
			String sep=System.getProperty("file.separator");
			String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
			FileUtils.isDirExists(currentWorkDir);
			ExportExcel<MyProductResultVo> ex = new ExportExcel<MyProductResultVo>();
			String filename =  seed + format; ;
			File file = new File(currentWorkDir + filename);	
			try { 
				OutputStream out = new FileOutputStream(file);				
				ex.exportExcel("客户作品", headers, fields, list, out, "yyyy-MM-dd");				
				out.close();				
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(file.getPath());
				return JsonUtil.objectToJsonStr(rq);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
