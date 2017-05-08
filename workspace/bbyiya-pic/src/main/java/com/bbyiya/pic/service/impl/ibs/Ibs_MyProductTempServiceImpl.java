package com.bbyiya.pic.service.impl.ibs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.UUserresponses;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.QRCodeUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_MyProductTempService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_MyProductTempServiceImpl implements IIbs_MyProductTempService{
	@Autowired
	private PMyproducttempMapper myproducttempMapper;
	@Autowired
	private PMyproductsMapper myMapper;
	/**
	 * ���ģ��
	 * */
	public ReturnModel addMyProductTemp(Long userid,String title,String remark,Long productid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		
		PMyproducts myproduct = new PMyproducts();	
		myproduct.setUserid(userid);
		myproduct.setProductid(productid);
		myproduct.setCreatetime(new Date());
		myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
		myproduct.setUpdatetime(new Date());	
		myproduct.setIstemp(1);
		myMapper.insertReturnId(myproduct);
			
		PMyproducttemp temp=new PMyproducttemp();
		temp.setBranchuserid(userid);
		temp.setCount(0);
		temp.setCreatetime(new Date());
		temp.setRemark(remark);
		temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));
		temp.setTitle(title);
		temp.setCartid(myproduct.getCartid());	
		myproducttempMapper.insertReturnId(temp);
		
		myproduct.setTempid(temp.getTempid());
		myMapper.updateByPrimaryKey(myproduct);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myProductTempID", temp.getTempid());
		map.put("myProductID", myproduct.getCartid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("���ģ��ɹ���");
		return rq;
	}
	/**
	 * ���û����ģ��
	 * @param type
	 * @param tempid
	 * @return
	 */
	public ReturnModel editMyProductTempStatus(int type,int tempid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);	
		if(temp!=null){
			PMyproducts myproduct =myMapper.selectByPrimaryKey(temp.getCartid());
			//����
			if(type==1){
				temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));			
				myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));				
			}else{//����
				temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.disabled.toString()));			
				myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.disabled.toString()));
			}
			myMapper.updateByPrimaryKey(myproduct);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("�����ɹ�");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("ģ��ID������");
		}
		return rq;
	}
	
	/**
	 * ɾ��ģ��
	 * @param tempid
	 * @return
	 */
	public ReturnModel deleteMyProductTemp(int tempid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);	
		if(temp!=null){
			PMyproducts myproduct =myMapper.selectByPrimaryKey(temp.getCartid());
			temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.del.toString()));			
			myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.deleted.toString()));
			myMapper.updateByPrimaryKey(myproduct);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("ɾ�������ɹ�");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("ģ��ID������");
		}		
		return rq;
	}
	/**
	 * ��ѯģ���б�
	 * @return
	 */
	public ReturnModel findMyProductTempList(int index,int size,Long userid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PageHelper.startPage(index, size);	
		List<PMyproducttemp>  templist=myproducttempMapper.findBranchMyProductTempList(userid);
		PageInfo<PMyproducttemp> reuslt=new PageInfo<PMyproducttemp>(templist); 
		if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){
			for (PMyproducttemp temp : templist) {	
				//String versionString=DateUtil.getTimeStr(new Date(), "yyyyMMddHHMMss"); 
				String redirct_url="currentPage?workId="+temp.getCartid();			
				String urlstr="";
				String url="";
				try {
					urlstr = "https://mpic.bbyiya.com/login/transfer?m=1&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
					url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");				
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				temp.setCodeurl(url);
				
			}
		}
		
		rq.setBasemodle(reuslt);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("��ȡ�б�ɹ���");
		return rq;
	}
	
	/**
	 * ����ģ���ά��ͼƬ
	 * @return
	 * @throws Exception 
	 */
	public ReturnModel saveProductTempRQcode(String url) throws Exception{
		ReturnModel rq=new ReturnModel();
		// ��ȡ�û��ĵ�ǰ������Ŀ¼ 
		String sep=System.getProperty("file.separator");
		String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
		FileUtils.isDirExists(currentWorkDir);
		String filename = "tempRqcode.jpg";
		File file = new File(currentWorkDir + filename);
		BufferedImage bufImg = QRCodeUtil.createImage(url, "", true);
		// ���ɶ�ά��QRCodeͼƬ
		ImageIO.write(bufImg, "jpg", file);
		rq.setBasemodle(file.getPath());
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("��ͼƬ�ɹ���");
		return rq;
	}
	
	
}
