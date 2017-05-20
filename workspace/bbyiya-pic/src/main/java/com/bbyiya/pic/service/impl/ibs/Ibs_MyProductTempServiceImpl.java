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
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.utils.ConfigUtil;
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
	@Autowired
	private PMyproducttempapplyMapper myproducttempapplyMapper;

	/**
	 * 添加模板
	 * */
	public ReturnModel addMyProductTemp(Long userid,String title,String remark,Long productid,int needVerifer,String discription){
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
		temp.setNeedverifer(needVerifer);
		temp.setDiscription(discription);
		myproducttempMapper.insertReturnId(temp);
		
		myproduct.setTempid(temp.getTempid());
		myMapper.updateByPrimaryKey(myproduct);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myProductTempID", temp.getTempid());
		map.put("myProductID", myproduct.getCartid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加模板成功！");
		return rq;
	}
	
	/**
	 * 修改模板
	 * */
	public ReturnModel editMyProductTemp(String title,String remark,Integer tempid,int needVerifer,String discription){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);
		if(temp!=null){
			temp.setTitle(title);
			temp.setRemark(remark);
			temp.setNeedverifer(needVerifer);
			temp.setDiscription(discription);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("修改模板成功！");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板不存在！");
		}
		return rq;
	}
	
	/**
	 * 启用或禁用模板
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
			if(myproduct!=null){
				//启用
				if(type==1){
					temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));			
					myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));				
				}else{//禁用
					temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.disabled.toString()));			
					myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.disabled.toString()));
				}
			}
			myMapper.updateByPrimaryKey(myproduct);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("操作成功");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板ID不存在");
		}
		return rq;
	}
	
	/**
	 * 删除模板
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
			rq.setStatusreson("删除操作成功");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板ID不存在");
		}		
		return rq;
	}
	/**
	 * 查询模板列表
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
				String redirct_url="currentPage?workId="+temp.getCartid()+"&uid="+userid;			
				String urlstr="";
				String url="";
				try {
					urlstr = ConfigUtil.getSingleValue("shareulr-base")+"redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
					url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");				
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				temp.setCodeurl(url);
				temp.setCreatetimestr(DateUtil.getTimeStr(temp.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				//得到待审核数量
				Integer checkCount=myproducttempapplyMapper.getNeedCheckApllyCountByTemp(temp.getTempid(),Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
				temp.setNeedCheckCount(checkCount==null?0:checkCount);
			}
		}
		
		rq.setBasemodle(reuslt);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	
	/**
	 * 获取影楼模板待审核用户列表
	 * @return
	 */
	public ReturnModel getMyProductTempApplyCheckList(int index,int size,Long userid,int tempid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PageHelper.startPage(index, size);	
		List<PMyproducttempapply>  applylist=myproducttempapplyMapper.findMyProducttempApplyList(tempid, Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
		PageInfo<PMyproducttempapply> reuslt=new PageInfo<PMyproducttempapply>(applylist); 
		if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){
			for (PMyproducttempapply apply : applylist) {
				apply.setCreatetimestr(DateUtil.getTimeStr(apply.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				if(!ObjectUtil.isEmpty(apply.getVerfiytime())){
					apply.setVerfiytimestr(DateUtil.getTimeStr(apply.getVerfiytime(), "yyyy-MM-dd HH:mm:ss"));
				}
				if(!ObjectUtil.isEmpty(apply.getBirthday())){
					apply.setBirthdaystr(DateUtil.getTimeStr(apply.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
				}
			}
		}
		
		rq.setBasemodle(reuslt);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	/**
	 * 保存模板二维码图片
	 * @return
	 * @throws Exception 
	 */
	public ReturnModel saveProductTempRQcode(String url) throws Exception{
		ReturnModel rq=new ReturnModel();
		// 获取用户的当前工作主目录 
		String sep=System.getProperty("file.separator");
		String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
		FileUtils.isDirExists(currentWorkDir);
		String filename = "tempRqcode.jpg";
		File file = new File(currentWorkDir + filename);
		BufferedImage bufImg = QRCodeUtil.createImage(url, "", true);
		// 生成二维码QRCode图片
		ImageIO.write(bufImg, "jpg", file);
		rq.setBasemodle(file.getPath());
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获图片成功！");
		return rq;
	}
	
	
}
