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
		// 生成的ZIP文件名为Demo.zip
		String tmpFileName = "Demo.zip";
		byte[] buffer = new byte[1024];
		String strZipPath = FilePath + tmpFileName;
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipPath));
			// 需要同时下载的两个文件result.txt ，source.txt
			File[] file1 = { new File(FilePath + "test1.txt"), new File(FilePath + "测试2.docx") };
			for (int i = 0; i < file1.length; i++) {
				FileInputStream fis = new FileInputStream(file1[i]);
				out.putNextEntry(new ZipEntry(file1[i].getName()));
				// 设置压缩文件内的字符编码，不然会变成乱码
				out.setEncoding("GBK");
				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				fis.close();
			}
			out.close();
			this.downFile(getResponse(), tmpFileName);
		} catch (Exception e) {
			Log.error("文件下载出错", e);
		}
		return null;
	}

	/**
	 * 获取Response
	 * 
	 * @return
	 */
	private HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 文件下载
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
				BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
				OutputStream outs = response.getOutputStream();// 获取文件输出IO流
				BufferedOutputStream bouts = new BufferedOutputStream(outs);
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(str, "UTF-8"));// 设置头部信息
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				// 开始向网络传输文件流
				while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
					bouts.write(buffer, 0, bytesRead);
				}
				bouts.flush();// 这里一定要调用flush()方法
				ins.close();
				bins.close();
				outs.close();
				bouts.close();
			} else {
				response.sendRedirect("../error.jsp");
			}
		} catch (IOException e) {
			Log.error("文件下载出错", e);
		}
	}
}
