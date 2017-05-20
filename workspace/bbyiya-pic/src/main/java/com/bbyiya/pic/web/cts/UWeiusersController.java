package com.bbyiya.pic.web.cts;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UWeiuserapplys;
import com.bbyiya.pic.service.cts.ICts_UWeiUserManageService;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.pic.utils.ExportExcel;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UWeiUserSearchParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/weiuser")
public class UWeiusersController extends SSOController {
	@Resource(name = "cts_UWeiuserService")
	private ICts_UWeiUserManageService weiUserService;
	@Resource(name = "ibs_OrderManageService")
	private IIbs_OrderManageService ibs_OrderManageService;
	/**
	 * W01 ����������
	 * 
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/weiUserApply")
	public String weiUserApply(String weiUserJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {		
					UWeiuserapplys applyInfo=Json2Objects.getParam_UWeiuserapplys(weiUserJson);
					if(applyInfo!=null){
						applyInfo.setMobilephone(user.getMobilePhone());
					}
					//���û��userId˵����ǰ�����������ģ�����userId˵����ctsֱ����ӵ�
					if(applyInfo!=null&&applyInfo.getUserid()==null){
						applyInfo.setUserid(user.getUserId());
					}
					rq =weiUserService.applyWeiUser(applyInfo);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��������101");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("δ���ע��");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * W02 ���������
	 * 
	 * @param agentUserId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_weiUserApply")
	public String audit_weiUserApply(Long weiUserId, int status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {	
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = weiUserService.audit_weiUserApply(user.getUserId(), weiUserId, status);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("��Ȩ��");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * W03 ��ѯ�����������б�
	 * @param weiUserId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findWeiUserApplylist")
	public String findWeiUserApplylist(Integer index,Integer size,String userId,String name,String phone,String status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			UWeiUserSearchParam param=new UWeiUserSearchParam();
			Long userid=ObjectUtil.parseLong(userId);
			if(userid>0)
				param.setUserId(userid);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			if(!ObjectUtil.isEmpty(name)){
				param.setName(name);
			}
			if(!ObjectUtil.isEmpty(phone)){
				param.setMobilephone(phone);
			} 	
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = weiUserService.findWeiUserApplylist(param,index,size);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("��Ȩ��");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * W04 ������ɾ��
	 * 
	 * @param agentUserId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delete_weiUserApply")
	public String delete_weiUserApply(Long weiUserId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {	
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = weiUserService.delete_weiUserApply(user.getUserId(), weiUserId);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("��Ȩ��");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �ж�����������״̬ 
	 * @param type
	 * @return String
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getWeiUserApplyStatus")
	public String getWeiUserApplyStatus() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
				rq=weiUserService.getWeiUserApplyStatus(user.getUserId());
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getWeiUserRecommendUser")
	public String getWeiUserRecommendUser(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "10")int size,String startTime,String endTime,Long userId) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=weiUserService.getRecommendUser(userId, startTime, endTime, index, size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/WeiUserRecommendExport")
	public String WeiUserRecommendExport(String startTime,String endTime,Long userId) throws Exception {
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
			List<OrderCountResultVO> list=ibs_OrderManageService.find_ibsOrderExportExcelbyUpUserid(userId, null, startTime, endTime, 0, 0);
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
				ex.exportExcel("����������ͳ��", headers, fields, list, out, "yyyy-MM-dd");				
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
	
	@ResponseBody
	@RequestMapping(value = "/getWeiUserRecommendOrder")
	public String getWeiUserRecommendOrder(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "10")int size,String startTime,String endTime,Long userId) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=ibs_OrderManageService.find_payorderExtByUpUserid(userId, null, startTime, endTime, index, size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
}
