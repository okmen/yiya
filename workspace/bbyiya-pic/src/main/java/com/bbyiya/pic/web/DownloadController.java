package com.bbyiya.pic.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@RequestMapping(value = "/login")
public class DownloadController extends ActionSupport {

	private Logger Log = Logger.getLogger(DownloadController.class);
	private static final String FilePath = "D:\\";

	private static final long serialVersionUID = -8694640030455344419L;

	public String execute() {
		// ���ɵ�ZIP�ļ���ΪDemo.zip
		String tmpFileName = "Demo.zip";
		byte[] buffer = new byte[1024];
		String strZipPath = FilePath + tmpFileName;
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipPath));
			// ��Ҫͬʱ���ص������ļ�result.txt ��source.txt
			File[] file1 = { new File(FilePath + "test1.txt"), new File(FilePath + "����2.docx") };
			for (int i = 0; i < file1.length; i++) {
				FileInputStream fis = new FileInputStream(file1[i]);
				out.putNextEntry(new ZipEntry(file1[i].getName()));
				// ����ѹ���ļ��ڵ��ַ����룬��Ȼ��������
				out.setEncoding("GBK");
				int len;
				// ������Ҫ���ص��ļ������ݣ������zip�ļ�
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				fis.close();
			}
			out.close();
			this.downFile(getResponse(), tmpFileName);
		} catch (Exception e) {
			Log.error("�ļ����س���", e);
		}
		return null;
	}

	/**
	 * ��ȡResponse
	 * 
	 * @return
	 */
	private HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * �ļ�����
	 * 
	 * @param response
	 * @param str
	 */
	private void downFile(HttpServletResponse response, String str) {
		try {
			String path = FilePath + str;
			File file = new File(path);
			if (file.exists()) {
				InputStream ins = new FileInputStream(path);
				BufferedInputStream bins = new BufferedInputStream(ins);// �ŵ�����������
				OutputStream outs = response.getOutputStream();// ��ȡ�ļ����IO��
				BufferedOutputStream bouts = new BufferedOutputStream(outs);
				response.setContentType("application/x-download");// ����response���ݵ�����
				response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(str, "UTF-8"));// ����ͷ����Ϣ
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				// ��ʼ�����紫���ļ���
				while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
					bouts.write(buffer, 0, bytesRead);
				}
				bouts.flush();// ����һ��Ҫ����flush()����
				ins.close();
				bins.close();
				outs.close();
				bouts.close();
			} else {
				response.sendRedirect("../error.jsp");
			}
		} catch (IOException e) {
			Log.error("�ļ����س���", e);
		}
	}
}
