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
	 * 代分配的订单列表
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
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 本店订单列表
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
				rq.setStatusreson("您还不是代理商，没有权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 获取订单详情
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
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取订单原图相册
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
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 我要这个客户
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
				rq.setStatusreson("您还不是代理商，没有权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 根据推荐人userid获取订单列表
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
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 影楼内部下单前得到作品的相关地址 （影楼地址和申请用户的地址）
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
	@RequestMapping(value="/ibsCountExportExcel")
	@ResponseBody
	public String ibsCountExportExcel(HttpServletRequest request, HttpServletResponse response,Integer status, String startTime,String endTime) throws MapperException {
		// 列头
		String[] headers =new String[9];
		headers[0]="查询开始时间";
		headers[1]="查询结束时间";
		headers[2]="注册用户昵称";
		headers[3]="用户注册时间";
		headers[4]="订单编号";
		headers[5]="订单支付时间";
		headers[6]="产品种类";
		headers[7]="购买数量";
		headers[8]="订单金额";		
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
		//导出格式
		String format =".xlsx";
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<OrderCountResultVO> list=ibs_OrderManageService.find_ibsOrderExportExcelbyUpUserid(user.getUserId(), status, startTime, endTime, 0, 0);
			Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子
			// 获取用户的当前工作主目录 
			String sep=System.getProperty("file.separator");
			String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
			FileUtils.isDirExists(currentWorkDir);
			ExportExcel<OrderCountResultVO> ex = new ExportExcel<OrderCountResultVO>();
			String filename =  seed + format; ;
			File file = new File(currentWorkDir + filename);	
			try { 
				OutputStream out = new FileOutputStream(file);				
				ex.exportExcel("IBS流量主数据统计", headers, fields, list, out, "yyyy-MM-dd");				
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
