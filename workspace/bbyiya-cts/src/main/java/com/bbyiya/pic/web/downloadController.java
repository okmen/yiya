package com.bbyiya.pic.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/pbs/order")
public class downloadController extends SSOController {

	@RequestMapping(value="/download")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String path = request.getParameter("path");
			// path是指欲下载的文件的路径。
			File file = new File(path);
		    // 路径为文件且不为空则进行删除  
		    if (file.isFile() && file.exists()) {
				// 取得文件名。
				String filename = file.getName();
				//FileDownloadUtils.download(path, filename);
				// 以流的形式下载文件。
				InputStream fis = new BufferedInputStream(new FileInputStream(path));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				// 清空response
				response.reset();
				// 设置response的Header
				response.setCharacterEncoding("utf-8");  
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				toClient.write(buffer);
				toClient.flush();
				toClient.close();  
				// 删除文件
		        file.delete();  
		    }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
		
}
