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
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;


//import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.utils.ExportExcel;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.utils.FileUtils;
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
//	private Logger Log = Logger.getLogger(PbsOrderMgtController.class);
	@Resource(name = "pbs_orderMgtService")
	private IPbs_OrderMgtService orderMgtService;
	
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;
	/**
	 * O01 PBS查询订单列表
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
			JSONObject jb = JSONObject.fromObject(myproductJson);
			if(jb.getString("status")==""){
				myproductJson=myproductJson.replaceAll("\"status\":\"\"", "\"status\":null");
			}
			Object object=JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			if(object==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数传入错误！");
				return JsonUtil.objectToJsonStr(rq);
			}

			SearchOrderParam param = (SearchOrderParam)JSONObject.toBean(jb,SearchOrderParam.class);			
			//SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
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
	 * pbs 订单图片列表，作品宝宝信息
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderProductInfoPhotos")
	public String getOrderProductPhotos(@RequestParam(required = false, defaultValue = "0") String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=orderService.getOrderProductInfoByUserOrderId(userOrderId);
		}else {
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
		String[] headers =new String[24];
		headers[0]="订单号";
		headers[1]="用户ID号";
		headers[2]="作品ID号";
		headers[3]="产品编号";
		headers[4]="产品标题";
		headers[5]="产品型号";
		headers[6]="代理商公司名称";
		headers[7]="代理商联系人";
		headers[8]="代理商电话";		
		headers[9]="代理商省份";
		headers[10]="代理商城市";
		headers[11]="代理商区县";
		headers[12]="代理商街道地址";
		headers[13]="收货人姓名";
		headers[14]="收货人电话";
		headers[15]="收货省份";
		headers[16]="收货市";
		headers[17]="收货区域";
		headers[18]="收货地址";		
		headers[19]="订购份数";
		headers[20]="订单实付";
		headers[21]="订单状态";
		headers[22]="物流公司";
		headers[23]="运单号";
		String[] fields = new String[24];
		fields[0]="userorderid";
		fields[1]="order.userid";
		fields[2]="cartid";
		fields[3]="productid";
		fields[4]="producttitle";
		fields[5]="propertystr";
		fields[6]="branchesName";
		fields[7]="branchesUserName";
		fields[8]="branchesPhone";		
		fields[9]="branchesprovince";
		fields[10]="branchesrcity";
		fields[11]="branchesdistrict";	
		fields[12]="branchesAddress";
		fields[13]="reciver";
		fields[14]="buyerPhone";
		fields[15]="buyerprovince";
		fields[16]="buyercity";
		fields[17]="buyerdistrict";
		fields[18]="buyerstreetdetail";
		fields[19]="count";
		fields[20]="order.ordertotalprice";
		fields[21]="order.status";
		fields[22]="order.expresscom";
		fields[23]="order.expressorder";
		
		//导出格式
		String format =".xlsx";
		myproductJson=myproductJson.replaceAll("\"status\":\"\"", "\"status\":null");
		SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
		
		PageInfo<PbsUserOrderResultVO> page = orderMgtService.find_pbsOrderList(param,0,0);
		List<PbsUserOrderResultVO> list=page.getList();
		ExportExcel<PbsUserOrderResultVO> ex = new ExportExcel<PbsUserOrderResultVO>();
		
		Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子
		// 获取用户的当前工作主目录 
		String sep=System.getProperty("file.separator");
		String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
		FileUtils.isDirExists(currentWorkDir);
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
	 * @param expressCom   物流公司名称
	 * @param expressOrder 运单号
	 * @param expressCode  物流编码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editLogistics")
	public String editLogistics(String orderId,String expressCom,String expressOrder,String expressCode) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if(ObjectUtil.isEmpty(expressOrder)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("运单号不有为空，请输入运单号");
				return JsonUtil.objectToJsonStr(rq);
			}
			//校验运单号不能为非数字
			if(!ObjectUtil.isEmpty(expressOrder)&&!ObjectUtil.IsNumber(expressOrder)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("运单号必须输入为数字");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			rq=orderMgtService.editLogistics(orderId, expressCom, expressOrder,expressCode);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}

		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 *B端订单填运费自动扣款
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addPostAge")
	public String addPostAge(String orderId,Double postage) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			//验证邮费不能输入负数
			if(!ObjectUtil.isEmpty(postage)&&postage.longValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邮费不能为负数！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=orderMgtService.addPostage(orderId,postage);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}

		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 *判断是否可以合单运单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/isCanMergeOrderLogistic")
	public String isCanMergeOrderLogistic(String orderIds) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=orderMgtService.isCanMergeOrderLogistic(orderIds);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 *合单运单信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/MergeOrderLogistic")
	public String MergeOrderLogistic(int ordertype,String orderIds,String expressCom,String expressOrder,Double postage,String expressCode) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if(ObjectUtil.isEmpty(expressOrder)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("运单号不有为空，请输入运单号！");
				return JsonUtil.objectToJsonStr(rq);
			}
			//校验运单号不能为非数字
			if(!ObjectUtil.isEmpty(expressOrder)&&!ObjectUtil.IsNumber(expressOrder)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("运单号必须输入为数字");
				return JsonUtil.objectToJsonStr(rq);
			}
			//验证邮费不能输入负数
			if(!ObjectUtil.isEmpty(postage)&&postage.longValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邮费不能为负数！");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			rq=orderMgtService.MergeOrderLogistic(ordertype, orderIds, expressCom, expressOrder, postage,expressCode);
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
	public String batchDownLoadImage(String myproductJson,String isDownload) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			myproductJson=myproductJson.replaceAll("\"status\":\"\"", "\"status\":null");
			SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			//param.setStartTime(c2.getTime());
			//param.setEndTime(c1.getTime()); 
			//param.setStartTimeStr(DateUtil.getTimeStr(param.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			//param.setEndTimeStr(DateUtil.getTimeStr(param.getEndTime(), "yyyy-MM-dd HH:mm:ss")); 
			//System.out.println(param.getStartTimeStr());
			//System.out.println(param.getEndTimeStr() );
			
			
			PageInfo<PbsUserOrderResultVO> page=orderMgtService.find_pbsOrderList(param, 0, 0);
			if(ObjectUtil.parseInt(isDownload)>0){
//				if(ObjectUtil.isEmpty(fileDir)){
//					rq.setStatu(ReturnStatus.ParamError);
//					rq.setStatusreson("请输入要保存到本地的文件路径");
//					return JsonUtil.objectToJsonStr(rq);
//				}
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					String path=orderMgtService.pbsdownloadImg(page.getList());
					rq.setBasemodle(path);
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
	 * 单个订单下载合成图片
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/singleDownLoadImage")
	public String singleDownLoadImage(String orderId,String isDownload) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SearchOrderParam param= new SearchOrderParam();
			param.setOrderNo(orderId);
			PageInfo<PbsUserOrderResultVO> page=orderMgtService.find_pbsOrderList(param, 0, 0);
			//rq=orderService.find_orderList(param);
			if(ObjectUtil.parseInt(isDownload)>0){
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					String path=orderMgtService.pbsdownloadImg(page.getList());
					rq.setBasemodle(path);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下载图片成功");
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("你的登录已过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 单个订单下载原图片
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/singleDownLoadOriginalImage")
	public String singleDownLoadOriginalImage(String orderId,String isDownload) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SearchOrderParam param= new SearchOrderParam();
			param.setOrderNo(orderId);
			PageInfo<PbsUserOrderResultVO> page=orderMgtService.find_pbsOrderList(param, 0, 0);
			if(ObjectUtil.parseInt(isDownload)>0){
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					String path=orderMgtService.pbsdownloadOriginalImage(page.getList());
					rq.setBasemodle(path);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下载图片成功");
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("你的登录已过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
