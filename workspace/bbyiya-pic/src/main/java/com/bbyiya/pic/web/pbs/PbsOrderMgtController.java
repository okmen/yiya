package com.bbyiya.pic.web.pbs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.utils.ExportExcel;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/pbs/order")
public class PbsOrderMgtController extends SSOController {

	@Resource(name = "pbs_orderMgtService")
	private IPbs_OrderMgtService orderMgtService;
	
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;
	/**
	 * O03 我的购买订单
	 *  
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderList")
	public String getOrderList(String myproductJson,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			PageInfo<PbsUserOrderResultVO> result= orderMgtService.find_pbsOrderList(param,index,size);
			rq.setBasemodle(result);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("获取列表成功！");
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 导出Excel
	 * @param request
	 * @return
	 * @throws MapperException 
	 */
	@RequestMapping(value="/orderExportExcel")
	@ResponseBody
	public String orderExportExcel(HttpServletRequest request, HttpServletResponse response,String myproductJson) throws MapperException {
		// 列头
		String[] headers =new String[19];
		headers[0]="订单号";
		headers[1]="用户ID号";
		headers[2]="产品编号";
		headers[3]="产品标题";
		headers[4]="产品型号";
		headers[5]="代理商";
		headers[6]="代理商电话";
		headers[7]="代理商收货地址";
		headers[8]="收货人姓名";
		headers[9]="收货人电话";
		headers[10]="收货省份";
		headers[11]="收货市";
		headers[12]="收货区域";
		headers[13]="收货地址";		
		headers[14]="订购份数";
		headers[15]="订单实付";
		headers[16]="订单状态";
		headers[17]="物流公司";
		headers[18]="运单号";
		String[] fields = new String[19];
		fields[0]="userorderid";
		fields[1]="order.userid";
		fields[2]="productid";
		fields[3]="producttitle";
		fields[4]="propertystr";
		fields[5]="branchesName";
		fields[6]="branchesPhone";
		fields[7]="branchesAddress";
		fields[8]="reciver";
		fields[9]="buyerPhone";
		fields[10]="buyerprovince";
		fields[11]="buyercity";
		fields[12]="buyerdistrict";
		fields[13]="buyerstreetdetail";
		fields[14]="count";
		fields[15]="order.ordertotalprice";
		fields[16]="order.status";
		fields[17]="order.expresscom";
		fields[18]="order.expressorder";
		
		//导出格式
		String format =".xlsx";
		
		SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
		
		PageInfo<PbsUserOrderResultVO> page = orderMgtService.find_pbsOrderList(param,0,0);
		List<PbsUserOrderResultVO> list=page.getList();
		ExportExcel<PbsUserOrderResultVO> ex = new ExportExcel<PbsUserOrderResultVO>();
		
		Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子
		// 获取用户的当前工作主目录 
		String currentWorkDir = System.getProperty("user.home") + "\\";
		String filename =  seed + format; ;
		File file = new File(currentWorkDir + filename);
		try { 
			OutputStream out = new FileOutputStream(file);
			if (format != null && !"".equals(format) && format.indexOf("csv") > -1) {
				ex.exportCSV("订单列表", headers, fields, list, out, "yyyy-MM-dd");
			} else {
				ex.exportExcel("订单列表", headers, fields, list, out, "yyyy-MM-dd");
			}
			out.close();
			ReturnModel rq = new ReturnModel();
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
	}
	
	@RequestMapping(value="/download")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String path = request.getParameter("path");
			// path是指欲下载的文件的路径。
			File file = new File(path);
		    // 路径为文件且不为空则进行删除  
		    if (file.isFile() && file.exists()) {
				// 取得文件名。
				String filename = file.getName();
				//FileDownloadUtils.download(path, filename);
				// 以流的形式下载文件。
				InputStream fis = new BufferedInputStream(new FileInputStream(path));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				// 清空response
				response.reset();
				// 设置response的Header
				response.setCharacterEncoding("utf-8");  
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				toClient.write(buffer);
				toClient.flush();
				toClient.close();  
				// 删除文件
		        file.delete();  
		    }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 *查询订单运单号信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editLogistics")
	public String editLogistics(String orderId,String expressCom,String expressOrder) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=orderMgtService.editLogistics(orderId, expressCom, expressOrder);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}

		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 批量下载订单图片
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/batchDownLoadImage")
	public String batchDownLoadImage(String myproductJson,String isDownload,String fileDir) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			//param.setStartTime(c2.getTime());
			//param.setEndTime(c1.getTime()); 
			//param.setStartTimeStr(DateUtil.getTimeStr(param.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			//param.setEndTimeStr(DateUtil.getTimeStr(param.getEndTime(), "yyyy-MM-dd HH:mm:ss")); 
			//System.out.println(param.getStartTimeStr());
			//System.out.println(param.getEndTimeStr() );
			PageInfo<PbsUserOrderResultVO> page=orderMgtService.find_pbsOrderList(param, 0, 0);
			//rq=orderService.find_orderList(param);
			if(ObjectUtil.parseInt(isDownload)>0){
				if(ObjectUtil.isEmpty(fileDir)){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请输入要保存到本地的文件路径");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					orderMgtService.pbsdownloadImg(page.getList(),fileDir);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下载图片成功"); 
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 单个订单下载图片
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/singleDownLoadImage")
	public String singleDownLoadImage(String orderId,String isDownload,String fileDir) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SearchOrderParam param= new SearchOrderParam();
			param.setOrderNo(orderId);
			PageInfo<PbsUserOrderResultVO> page=orderMgtService.find_pbsOrderList(param, 0, 0);
			//rq=orderService.find_orderList(param);
			if(ObjectUtil.parseInt(isDownload)>0){
				if(ObjectUtil.isEmpty(fileDir)){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请输入要保存到本地的文件路径");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					orderMgtService.pbsdownloadImg(page.getList(),fileDir);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下载图片成功");
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
