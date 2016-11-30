package com.bbyiya.api.web.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.qiniu.Uploader;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.UserValidate;
import com.sdicons.json.mapper.MapperException;

@WebServlet("/common/uploadfile")
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadFileServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult userResult=UserValidate.getLoginUser(request);
		if(userResult!=null){
			rq = upload(request);
			// �ɹ��ϴ������أ���ʱ�ļ���
			if (rq.getStatu().equals(ReturnStatus.Success)) {
				String imgurl = Uploader.uploadReturnUrl(rq.getStatusreson(), 0);
				if (!ObjectUtil.isEmpty(imgurl)) {
					ImageInfo img = new ImageInfo();
					img.setUrl(imgurl);
					ImageInfo imgSmall = new ImageInfo();
					imgSmall.setUrl(imgurl + "?imageView2/2/h/240/h/240");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("small", imgSmall);
					map.put("original", img);
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(map);
					File file = new File(rq.getStatusreson());
					if (file.isFile() && file.exists())
						file.delete();
					rq.setStatusreson("�ϴ��ɹ���");
				} else {
					rq.setStatu(ReturnStatus.SystemError);
				}
			}	
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("û��Ȩ��");
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			out.println(JsonUtil.objectToJsonStr(rq));
		} catch (MapperException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * �ļ��ϴ�����
	 * 
	 * @param request
	 * @return
	 */
	private ReturnModel upload(HttpServletRequest request) {
		ReturnModel rq = new ReturnModel();
		//
		String savePath =  ConfigUtil.getSingleValue("imgPathTemp");
		File file = new File(savePath);
		//
		if (!file.exists() && !file.isDirectory()) {
			//
			file.mkdir();
		}
		//
		String message = "";
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			if (!ServletFileUpload.isMultipartContent(request)) {
				return rq;
			}
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {

				// ���fileitem�з�װ������ͨ�����������
				if (item.isFormField()) {
					String name = item.getFieldName();
					// �����ͨ����������ݵ�������������
					String value = item.getString("UTF-8");
					//
					System.out.println(name + "=" + value);
				} else {// ���fileitem�з�װ�����ϴ��ļ�
					// �õ��ϴ����ļ����ƣ�
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺
					// c:\a\b\1.txt������Щֻ�ǵ������ļ���
					// �����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
					filename = filename
							.substring(filename.lastIndexOf("\\") + 1);
					// ��ȡitem�е��ϴ��ļ���������
					InputStream in = item.getInputStream();
					// ����һ���ļ������
					FileOutputStream out = new FileOutputStream(savePath + "\\"
							+ filename);
					// ����һ��������
					byte buffer[] = new byte[1024];
					// �ж��������е������Ƿ��Ѿ�����ı�ʶ
					int len = 0;
					// ѭ�������������뵽���������У�(len=in.read(buffer))>0�ͱ�ʾin���滹������
					while ((len = in.read(buffer)) > 0) {
						// ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath + "\\"
						// + filename)����
						out.write(buffer, 0, len);
					}
					// �ر�������
					in.close();
					// �ر������
					out.close();
					// ɾ�������ļ��ϴ�ʱ���ɵ���ʱ�ļ�
					item.delete();
					message = savePath + filename;
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson(message);
				}
			}
		} catch (Exception e) {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�ϴ�ʧ��");
			rq.setBasemodle(e);
		}
		return rq;
	}


}
