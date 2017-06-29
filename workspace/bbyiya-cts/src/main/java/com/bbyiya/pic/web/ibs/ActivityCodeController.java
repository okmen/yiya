package com.bbyiya.pic.web.ibs;

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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ExportExcel;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.ibs.IIbs_ActivityCodeService;
import com.bbyiya.pic.vo.product.ActivityCodeProductVO;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/ibs/temp")
public class ActivityCodeController extends SSOController {
	
	@Resource(name = "ibs_ActivityCodeService")
	private IIbs_ActivityCodeService activitycodeService;
	
	
	
	/**
	 * ���ģ��
	 * @param title ģ�����
	 * @param remark ��ע
	 * @param productid ��Ʒ��ʾ
	 * @param needverifer �Ƿ���Ҫ���
	 * @param discription ���֪
	 * @param codeurl  ��ά��ͼƬ
	 * @returnp
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addActivityCode")
	public String addActivityCode(String myproductTempJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			MyProductTempAddParam param = (MyProductTempAddParam)JsonUtil.jsonStrToObject(myproductTempJson,MyProductTempAddParam.class);			
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("������ȫ");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("����Ʋ���Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��Ӧ��Ʒ����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getStyleId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���Ʒ����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getApplycount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("������������Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getApplycount()!=null&&param.getApplycount()<=0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("�����������������!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("����ƴ���Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getCodesm())&&!ObjectUtil.validSqlStr(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ά������˵����Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			rq=activitycodeService.addActivityCode(user.getUserId(), param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �õ�������εĵĻ����Ʒ�б�
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyProductslistForActivityCode")
	public String findMyProductslistForActivityCode(Integer tempid,Integer activeStatus,
			@RequestParam(required = false, defaultValue = "")String keywords,
			@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=activitycodeService.findMyProductslistForActivityCode(user.getUserId(), tempid,activeStatus,keywords, index, size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	/**
	 * ɾ�����
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteActivityCode")
	public String deleteActivityCode(String codeno) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=activitycodeService.deleteActivityCode(codeno);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getActivityCodeDetail")
	public String getActivityCodeDetail(Integer tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){		
			rq=activitycodeService.getActivityCodeDetail(tempid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * ���û�µ��ѱ��������
	 * @param cartId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/reSetTempApplySort")
	public String resetAllTempApplySort(Integer tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq=activitycodeService.resetAllTempApplySort(tempid);
		return JsonUtil.objectToJsonStr(rq);
		
	}
	
	/**
	 * ������excel����
	 * @param request
	 * @return
	 * @throws MapperException 
	 */
	@RequestMapping(value="/activityCodeProductExportExcel")
	@ResponseBody
	public String activityCodeProductExportExcel(Integer tempid,Integer activeStatus,
			@RequestParam(required = false, defaultValue = "")String keywords) throws Exception {
		// ��ͷ
		String[] headers =new String[11];
		headers[0]="���";
		headers[1]="��Ӧ��Ʒ";
		headers[2]="�״̬";
		headers[3]="�ͻ��ǳ�";
		headers[4]="��������";
		headers[5]="��Ʒ����";
		headers[6]="������";
		headers[7]="�ͻ��ֻ�";
		headers[8]="�ջ���ַ";
		headers[9]="��ʼ����ʱ��";
		headers[10]="�������ʱ��";	
		String[] fields = new String[11];
		fields[0]="code.codeno";
		fields[1]="productTitle";
		fields[2]="activeStatus";
		fields[3]="invitedName";
		fields[4]="birthdayStr";
		fields[5]="count";
		fields[6]="commentsCount";
		fields[7]="phone";
		fields[8]="address";
		fields[9]="createtimestr";
		fields[10]="updatetimestr";
		
		
		//������ʽ
		String format =".xlsx";
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=activitycodeService.findMyProductslistForActivityCode(user.getUserId(), tempid,activeStatus,keywords, 0, 0);
			
			if(rq.getStatu()!=ReturnStatus.Success){
				return JsonUtil.objectToJsonStr(rq);
			}
			
			PageInfo<ActivityCodeProductVO> resultPage =(PageInfo<ActivityCodeProductVO>) rq.getBasemodle();
			
			List<ActivityCodeProductVO> list=resultPage.getList();
			Long seed = System.currentTimeMillis();// ���ϵͳʱ�䣬��Ϊ���������������
			// ��ȡ�û��ĵ�ǰ������Ŀ¼ 
			String sep=System.getProperty("file.separator");
			String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
			FileUtils.isDirExists(currentWorkDir);
			ExportExcel<ActivityCodeProductVO> ex = new ExportExcel<ActivityCodeProductVO>();
			String filename =  seed + format; ;
			File file = new File(currentWorkDir + filename);	
			try { 
				OutputStream out = new FileOutputStream(file);				
				ex.exportExcel("���", headers, fields, list, out, "yyyy-MM-dd");				
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
