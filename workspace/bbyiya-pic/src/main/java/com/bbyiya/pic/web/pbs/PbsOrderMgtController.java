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
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.utils.ExportExcel;
import com.bbyiya.pic.vo.order.PbsSearchOrderParam;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.upload.FileDownloadUtils;
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
	
	
	/**
	 * O03 我的购买订单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderList")
	public String getOrderList(PbsSearchOrderParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			PageInfo<PbsUserOrderResultVO> result= orderMgtService.find_pbsOrderList(param);
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
	public String orderExportExcel(HttpServletRequest request, HttpServletResponse response,PbsSearchOrderParam param) throws MapperException {
		// 列头
		String[] headers =new String[18];
		headers[0]="订单号";
		headers[1]="产品编号";
		headers[2]="产品标题";
		headers[3]="产品型号";
		headers[4]="代理商";
		headers[5]="代理商电话";
		headers[6]="代理商收货地址";
		headers[7]="收货人姓名";
		headers[8]="收货人电话";
		headers[9]="收货省份";
		headers[10]="收货市";
		headers[11]="收货区域";
		headers[12]="收货地址";		
		headers[13]="订购份数";
		headers[14]="订单实付";
		headers[15]="订单状态";
		headers[16]="物流公司";
		headers[17]="运单号";
		String[] fields = new String[18];
		fields[0]="userorderid";
		fields[1]="productid";
		fields[2]="producttitle";
		fields[3]="propertystr";
		fields[4]="branchesName";
		fields[5]="branchesPhone";
		fields[6]="branchesAddress";
		fields[7]="reciver";
		fields[8]="buyerPhone";
		fields[9]="buyerprovince";
		fields[10]="buyercity";
		fields[11]="buyerdistrict";
		fields[12]="buyerstreetdetail";
		fields[13]="count";
		fields[14]="order.ordertotalprice";
		fields[15]="order.status";
		fields[16]="order.expresscom";
		fields[17]="order.expressorder";
		
		//导出格式
		String format =".xlsx";
		
		
		PageInfo<PbsUserOrderResultVO> page = orderMgtService.find_pbsOrderList(param);
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

	
}
