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

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.pic.utils.ExportExcel;
import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/ibs/order")
public class OrderIBSController extends SSOController {
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;

	@Resource(name = "ibs_OrderManageService")
	private IIbs_OrderManageService ibs_OrderManageService;

	/**
	 * ������Ķ����б�
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/waitinglist")
	public String waitinglist() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderService.findAgentOrders(user.getUserId());
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * ���궩���б�
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myOrderlist")
	public String myOrderlist(@RequestParam(required = false, defaultValue = "2") int status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)) {
				rq = orderService.findMyOrderlist(user.getUserId(), status);
			} else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("�������Ǵ����̣�û��Ȩ��");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderDetail")
	public String getOrderDetail(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderService.getOrderDetail(userOrderId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ��ȡ����ԭͼ���
	 * 
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderPhotos")
	public String getOrderPhotos(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderService.getOrderPhotos(userOrderId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * ��Ҫ����ͻ�
	 * 
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyOrder")
	public String getMyOrder(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)) {
				rq = orderService.addCustomer(user.getUserId(), userOrderId);
			} else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("�������Ǵ����̣�û��Ȩ��");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * �����Ƽ���userid��ȡ�����б�
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPayOrderByUpUserId")
	public String getPayOrderByUpUserId(Integer status, int index, int size,String startTime,String endTime) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = ibs_OrderManageService.find_payorderExtByUpUserid(user.getUserId(), status,startTime,endTime, index, size);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * Ӱ¥�ڲ��µ�ǰ�õ���Ʒ����ص�ַ ��Ӱ¥��ַ�������û��ĵ�ַ��
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyProductAddressList")
	public String getMyProductAddressList(Long cartid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=ibs_OrderManageService.getMyProductAddressList(user.getUserId(), cartid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * IBS����ͳ�Ƶ���Excel
	 * @param request
	 * @return
	 * @throws MapperException 
	 */
	@RequestMapping(value="/ibsCountExportExcel")
	@ResponseBody
	public String ibsCountExportExcel(HttpServletRequest request, HttpServletResponse response,Integer status, String startTime,String endTime) throws MapperException {
		// ��ͷ
		String[] headers =new String[9];
		headers[0]="��ѯ��ʼʱ��";
		headers[1]="��ѯ����ʱ��";
		headers[2]="ע���û��ǳ�";
		headers[3]="�û�ע��ʱ��";
		headers[4]="�������";
		headers[5]="����֧��ʱ��";
		headers[6]="��Ʒ����";
		headers[7]="��������";
		headers[8]="�������";		
		String[] fields = new String[9];
		fields[0]="starttime";
		fields[1]="endtime";
		fields[2]="username";
		fields[3]="usercreatetime";
		fields[4]="userorderid";
		fields[5]="orderpaytime";
		fields[6]="Producttitle";
		fields[7]="count";
		fields[8]="totalprice";
		//������ʽ
		String format =".xlsx";
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<OrderCountResultVO> list=ibs_OrderManageService.find_ibsOrderExportExcelbyUpUserid(user.getUserId(), status, startTime, endTime, 0, 0);
			Long seed = System.currentTimeMillis();// ���ϵͳʱ�䣬��Ϊ���������������
			// ��ȡ�û��ĵ�ǰ������Ŀ¼ 
			String sep=System.getProperty("file.separator");
			String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
			FileUtils.isDirExists(currentWorkDir);
			ExportExcel<OrderCountResultVO> ex = new ExportExcel<OrderCountResultVO>();
			String filename =  seed + format; ;
			File file = new File(currentWorkDir + filename);	
			try { 
				OutputStream out = new FileOutputStream(file);				
				ex.exportExcel("IBS����������ͳ��", headers, fields, list, out, "yyyy-MM-dd");				
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
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
