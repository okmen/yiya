package com.bbyiya.utils.upload;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class FileDownloadUtils {

	public static void download(String urlString, String filename) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

	public static void setDPI(String filePath) throws Exception {
		File infile = new File(filePath);  
        File outfile = new File(filePath);  
 
        ImageReader reader = ImageIO.getImageReadersByFormatName("jpeg").next();  
        reader.setInput(new FileImageInputStream(infile), true, false);  
        IIOMetadata data = reader.getImageMetadata(0);  
//        BufferedImage image = reader.read(0);  
          
//        int w = 600, h = -1;  
//         Image rescaled = image.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);  
         BufferedImage output = reader.read(0); //toBufferedImage(rescaled, BufferedImage.TYPE_INT_RGB);  
           
        Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");  
        Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);  
        for (int i = 0; i < jfif.getAttributes().getLength(); i++) {  
            Node attribute = jfif.getAttributes().item(i);  
            System.out.println(attribute.getNodeName() + "="  
                    + attribute.getNodeValue());  
        }  
        FileOutputStream fos = new FileOutputStream(outfile);  
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fos);  
        JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(output);  
        jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);  
        jpegEncodeParam.setXDensity(300);  
        jpegEncodeParam.setYDensity(300);  
        jpegEncoder.encode(output, jpegEncodeParam);  
        fos.close();  
	}
	
	 public static BufferedImage toBufferedImage(Image image, int type) {
         int w = image.getWidth(null);
         int h = image.getHeight(null);
         BufferedImage result = new BufferedImage(w, h, type);
         Graphics2D g = result.createGraphics();
         g.drawImage(image, 0, 0, null);
         g.dispose();
         return result;
     }
}
