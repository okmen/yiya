package com.bbyiya.vo.calendar;


import java.util.List;

import com.bbyiya.model.TiProductareas;

public class TiMachineproductVo {
	
	 	private Integer id;

	    private Integer machineid;

	    private Long productid;
	    private String producttitle;
	    
	    private List<TiProductareas> canotSetareas;
	    private List<TiProductareas> setedareas;

		public Integer getId() {
			return id;
		}

		public Integer getMachineid() {
			return machineid;
		}

		public Long getProductid() {
			return productid;
		}

		
		public void setId(Integer id) {
			this.id = id;
		}

		public void setMachineid(Integer machineid) {
			this.machineid = machineid;
		}

		public void setProductid(Long productid) {
			this.productid = productid;
		}

		public List<TiProductareas> getCanotSetareas() {
			return canotSetareas;
		}

		public void setCanotSetareas(List<TiProductareas> canotSetareas) {
			this.canotSetareas = canotSetareas;
		}

		public List<TiProductareas> getSetedareas() {
			return setedareas;
		}

		public void setSetedareas(List<TiProductareas> setedareas) {
			this.setedareas = setedareas;
		}

		public String getProducttitle() {
			return producttitle;
		}

		public void setProducttitle(String producttitle) {
			this.producttitle = producttitle;
		}

		
	    
	
}
