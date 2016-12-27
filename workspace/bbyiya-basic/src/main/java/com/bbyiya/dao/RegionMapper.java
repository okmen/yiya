package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.RAreas;
import com.bbyiya.model.RCity;
import com.bbyiya.model.RProvince;

public interface RegionMapper {
	/**
	 * 获取省
	 * @param code
	 * @return
	 */
    RProvince getProvinceByCode(Integer code);
    /**
     * 获取所有省
     * @return
     */
    List<RProvince> findProvincelistAll();
    /**
     * 根据省Code 获取市级列表
     * @param provinceCode
     * @return
     */
    List<RCity> findCitylistBy_ProvinceCode(@Param("provinceCode") Integer provinceCode );
    /**
     * 获取区级列表
     * @param cityCode
     * @return
     */
    List<RAreas> findArealistBy_CityCode(@Param("cityCode") Integer cityCode );
    
    
}