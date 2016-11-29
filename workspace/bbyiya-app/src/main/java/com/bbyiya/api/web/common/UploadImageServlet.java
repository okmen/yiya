package com.bbyiya.api.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.oreilly.servlet.MultipartRequest;
import com.sdicons.json.mapper.MapperException;

import sun.misc.BASE64Encoder;

@WebServlet("/uploadimage")
public class UploadImageServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadImageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ReturnModel rq = new ReturnModel();
		// logger.error("UploadImageForChat");
		rq = upLoadImgModel(request);// upload(request);//
		// logger.error("out");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			out.println(JsonUtil.objectToJsonStr(rq));
		} catch (MapperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private ReturnModel upLoadImgModel(HttpServletRequest request) {
		imgReturnBase64(request);
		return null;
	}

	/**
	 * ͼƬ�������м�� Ȼ��תbase64����
	 * 
	 * @param request
	 * @return
	 */
	private void imgReturnBase64(HttpServletRequest request) {
		
		MultipartRequest mr = null;
		// ���������û��ϴ��ļ���С��
		int maxPostSize = 1 * 100 * 1024 * 1024;
		try {
			String imgBasenew = ConfigUtil.getSingleValue("imgUploadBase");// "D://"
			String uploadPath = "Images/" + DateUtil.getTimeString("yyyyMM") + "/";
			File f = new File(imgBasenew + uploadPath);
			if (!f.isDirectory()) {//�ļ�·���Ƿ����
				String bathString = imgBasenew + "Images/";
				File f1 = new File(bathString);
				if (!f1.isDirectory()) {
					f1.mkdir();
				}
				f.mkdir();
			}

			// logger.error("filesPath:"+imgBasenew+uploadPath);
			mr = new MultipartRequest(request, imgBasenew + uploadPath, maxPostSize, "GBK");
			Enumeration files = mr.getFileNames();
			// logger.error("files");
			String filename = "";
			String filePath = "";
			String pathFull = imgBasenew + uploadPath;

			while (files.hasMoreElements()) {
				filename = (String) files.nextElement();
				filePath = mr.getFilesystemName(filename);

				InputStream in = null;
				byte[] data = null;
				// ��ȡͼƬ�ֽ�����
				try {
					in = new FileInputStream(pathFull + filePath);
					data = new byte[in.available()];
					in.read(data);
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// ���ֽ�����Base64����
				BASE64Encoder encoder = new BASE64Encoder();
			
				// logger.error("filePath:"+pathFull+filePath);
				// logger.error(resultMsg.getMsg());
				// ɾ����ʱ�ļ�
				File file = new File(pathFull + filePath);
				if (file.isFile() && file.exists())
					file.delete();

			}

		} catch (Exception e) {
			// TODO: handle exception
			
		}
	}

	/**
	 * ͼƬ�ϴ����汾1��
	 * 
	 * @param request
	 * @return
	 */
	private ReturnModel upload(HttpServletRequest request) {
		ReturnModel rq = new ReturnModel();
		MultipartRequest mr = null;
		// ���������û��ϴ��ļ���С��
		int maxPostSize = 1 * 100 * 1024 * 1024;
		// ��һ������Ϊ������������HttpServletRequest��
		// �ڶ�������Ϊ�ϴ��ļ�Ҫ�洢�ڷ������˵�Ŀ¼����
		// ���������������������û��ϴ��ļ���С
		// ���ĸ����������趨�ú��ֱ��뷽ʽ���ϴ��ļ����ƣ����Խ����������
		try {
			String imgBasenew = ConfigUtil.getSingleValue("imgUploadBase");// "D://"
			String uploadPath = "Images/" + DateUtil.getTimeString("yyyyMM") + "/";
			String newFileName = DateUtil.getTimeString("yyyyMMddhhmmssSSS");// +"_1.jpg";//�����ɵ�ͼƬ
			File f = new File(imgBasenew + uploadPath);
			if (!f.isDirectory()) {
				f.mkdir();
			}
			mr = new MultipartRequest(request, imgBasenew + uploadPath, maxPostSize, "GBK");

			Enumeration files = mr.getFileNames();
			String filename = "";
			String filePath = "";
			String pathFull = imgBasenew + uploadPath;
			while (files.hasMoreElements()) {
				filename = (String) files.nextElement();
				filePath = mr.getFilesystemName(filename);
				// ImageHelper.imgCutSave(pathFull+filePathString,
				// pathFull+newFileName+"_12.jpg", 120, 120);
				// ImageHelper.imgCutSave(pathFull+filePathString,
				// pathFull+newFileName+"_75.jpg", 750, 750);
//				ImageHelper.imgScale(pathFull + filePath, pathFull + newFileName + "_1.jpg", 1);
//				ImageHelper.imgScale(pathFull + filePath, pathFull + newFileName + "_12.jpg", 0.3, 120);

			}

		

		} catch (Exception e) {
			
		}

		return rq;
	}

	// /**
	// * getpart��ʽ
	// * @param request
	// * @return
	// */
	// private ReturnModel uploadImg(HttpServletRequest request)
	// {
	// ReturnModel rq=new ReturnModel();
	// try {
	// request.getPart("file");
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return rq;
	// }
	// private static String getFileName(Part part) {
	// for (String cd : part.getHeader("content-disposition").split(";")) {
	// if (cd.trim().startsWith("filename")) {
	// String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"",
	// "");
	// return fileName.substring(fileName.lastIndexOf('/') +
	// 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	// }
	// }
	// return null;
	// }
}
