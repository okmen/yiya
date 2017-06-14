package com.bbyiya.pic.web.test;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.bbyiya.common.enums.UploadTypeEnum;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.upload.FileUploadUtils_qiniu;
import com.bbyiya.vo.order.SubmitOrderProductParam;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

public class ThreadOrderSubmit  extends SSOController implements Runnable {
	@Autowired
	private PMyproductsMapper myproductMapper;
	@Autowired
	private PMyproductdetailsMapper detaiMapper;
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	
	public String filePath;
	String ticket;
	int index = 1;
	int size = 100;// ÿҳ���������
	int totalCount=100;
	public ThreadOrderSubmit(String filePath, int pageSize,int count) {
		this.filePath = filePath;
		size = pageSize;
		totalCount=count;
	}
	
	
	public void run() {
		synchronized (this) {
			for(int i=0;i<size;i++){
				try {
					aa();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MapperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.index++;
			}
		}
	}
	
	public void aa() throws IOException, MapperException{
		   
			PMyproducts myproducts=new PMyproducts();
			myproducts.setTitle("����"+this.index);
			myproducts.setAuthor("ϵͳ"+this.index);
			myproducts.setDescription("���������������ʱ����������һ������������磬����Ҳ������һ�Σ�������������С�����ɶ�");
			myproducts.setInvitestatus(3);
			myproducts.setProductid(1001l);
			myproducts.setStyleid(1001l);
			myproducts.setUserid(157l);
			myproducts.setCreatetime(new Date());
			myproductMapper.insertReturnId(myproducts);
			
		
			
			PMyproductsinvites in=new PMyproductsinvites();
			in.setCartid(myproducts.getCartid());
			in.setUserid(myproducts.getUserid());
			in.setInviteuserid(1876l);
			in.setInvitephone("15012703706"); 
			in.setStatus(3);
			in.setCreatetime(new Date());
			inviteMapper.insert(in);
			for(int j=0;j<12;j++){
				String urlString= FileUploadUtils_qiniu.uploadReturnUrl(this.filePath, UploadTypeEnum.Product);
				PMyproductdetails detail=new PMyproductdetails();
				detail.setCartid(myproducts.getCartid());
				detail.setContent("���쿪�У�����"+this.index+",ͼƬ"+j); 
				detail.setTitle("���쿪�У�����"+this.index+",ͼƬ"+j);
				detail.setSceneid(0);
				detail.setImgurl(urlString);
				detail.setCreatetime(new Date());
				detail.setSort(j); 
				detaiMapper.insert(detail);
			}
			
			
			SubmitOrderProductParam orderPamra=new SubmitOrderProductParam();
			orderPamra.setCartId(myproducts.getCartid());
			orderPamra.setProductId(1001l);
			orderPamra.setStyleId(1001l);
			orderPamra.setPostModelId(1);
			orderPamra.setCount(1);
			String paramString="ticket="+this.ticket+"&productJsonStr="+JsonUtil.objectToJsonStr(orderPamra)+"&orderType=1";
			HttpRequestHelper.sendPost("http://localhost:8082/order/submitOrderNew", paramString);
		
	}
}
