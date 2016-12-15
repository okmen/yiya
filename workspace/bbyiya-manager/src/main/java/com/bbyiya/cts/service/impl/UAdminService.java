package com.bbyiya.cts.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.cts.service.IUAdminService;
import com.bbyiya.dao.UAdminMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UAdmin;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;


@Service("userAdminService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UAdminService implements IUAdminService{
	
	@Autowired 
	private UAdminMapper adminMapper;
	
	
	public ReturnModel loginProcess(String username,String pwd){
		ReturnModel rqModel=new ReturnModel();
		String pwdMd5=MD5Encrypt.encrypt(pwd);
		//admin�û��������ݿ�
		List<Map<String, String>> users= ConfigUtil.getMaplist("users");
		if(users!=null&&users.size()>0){
			for (Map<String, String> map : users) {
				if(map.get("username").equals(username)&&map.get("pwd").equals(pwdMd5)){
					UAdmin user=new UAdmin();
					user.setAdminid(ObjectUtil.parseInt(map.get("adminId")));
					user.setUsername((map.get("username")));
					rqModel.setStatu(ReturnStatus.Success);
					rqModel.setBasemodle(user); 
					rqModel.setStatusreson("��¼�ɹ�");
					return rqModel;
				}
			}
		}
		rqModel.setStatu(ReturnStatus.LoginError_1);
		rqModel.setStatusreson("�û������������");
		return rqModel;
		// �������ݿ�
//		UAdmin admin= adminMapper.getUAdminByUsername(username);
//		if(admin!=null){
//			if(!admin.getPassword().equals(MD5Encrypt.encrypt(pwd))){
//				rqModel.setStatu(ReturnStatus.LoginError_1);
//				rqModel.setStatusreson("��¼�������");
//				return rqModel;
//			}else {
//				rqModel.setStatu(ReturnStatus.Success);
//				rqModel.setBasemodle(admin); 
//				rqModel.setStatusreson("��¼�ɹ�");
//			}
//		}else {
//			rqModel.setStatu(ReturnStatus.LoginError);
//			rqModel.setStatusreson("�û���������");
//		}
//		return rqModel;
	}
}