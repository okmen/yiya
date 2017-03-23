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
	 * O03 �ҵĹ��򶩵�
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
			rq.setStatusreson("��ȡ�б�ɹ���");
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ����Excel
	 * @param request
	 * @return
	 * @throws MapperException 
	 */
	@RequestMapping(value="/orderExportExcel")
	@ResponseBody
	public String orderExportExcel(HttpServletRequest request, HttpServletResponse response,String myproductJson) throws MapperException {
		// ��ͷ
		String[] headers =new String[19];
		headers[0]="������";
		headers[1]="�û�ID��";
		headers[2]="��Ʒ���";
		headers[3]="��Ʒ����";
		headers[4]="��Ʒ�ͺ�";
		headers[5]="������";
		headers[6]="�����̵绰";
		headers[7]="�������ջ���ַ";
		headers[8]="�ջ�������";
		headers[9]="�ջ��˵绰";
		headers[10]="�ջ�ʡ��";
		headers[11]="�ջ���";
		headers[12]="�ջ�����";
		headers[13]="�ջ���ַ";		
		headers[14]="��������";
		headers[15]="����ʵ��";
		headers[16]="����״̬";
		headers[17]="������˾";
		headers[18]="�˵���";
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
		
		//������ʽ
		String format =".xlsx";
		
		SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
		
		PageInfo<PbsUserOrderResultVO> page = orderMgtService.find_pbsOrderList(param,0,0);
		List<PbsUserOrderResultVO> list=page.getList();
		ExportExcel<PbsUserOrderResultVO> ex = new ExportExcel<PbsUserOrderResultVO>();
		
		Long seed = System.currentTimeMillis();// ���ϵͳʱ�䣬��Ϊ���������������
		// ��ȡ�û��ĵ�ǰ������Ŀ¼ 
		String currentWorkDir = System.getProperty("user.home") + "\\";
		String filename =  seed + format; ;
		File file = new File(currentWorkDir + filename);
		try { 
			OutputStream out = new FileOutputStream(file);
			if (format != null && !"".equals(format) && format.indexOf("csv") > -1) {
				ex.exportCSV("�����б�", headers, fields, list, out, "yyyy-MM-dd");
			} else {
				ex.exportExcel("�����б�", headers, fields, list, out, "yyyy-MM-dd");
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
			// path��ָ�����ص��ļ���·����
			File file = new File(path);
		    // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��  
		    if (file.isFile() && file.exists()) {
				// ȡ���ļ�����
				String filename = file.getName();
				//FileDownloadUtils.download(path, filename);
				// ��������ʽ�����ļ���
				InputStream fis = new BufferedInputStream(new FileInputStream(path));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				// ���response
				response.reset();
				// ����response��Header
				response.setCharacterEncoding("utf-8");  
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				toClient.write(buffer);
				toClient.flush();
				toClient.close();  
				// ɾ���ļ�
		        file.delete();  
		    }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 *��ѯ�����˵�����Ϣ
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
			rq.setStatusreson("��¼����");
		}

		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �������ض���ͼƬ
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
					rq.setStatusreson("������Ҫ���浽���ص��ļ�·��");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					orderMgtService.pbsdownloadImg(page.getList(),fileDir);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("����ͼƬ�ɹ�"); 
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * ������������ͼƬ
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
					rq.setStatusreson("������Ҫ���浽���ص��ļ�·��");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(page!=null&&page.getList()!=null&&page.getList().size()>0){
					orderMgtService.pbsdownloadImg(page.getList(),fileDir);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("����ͼƬ�ɹ�");
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
