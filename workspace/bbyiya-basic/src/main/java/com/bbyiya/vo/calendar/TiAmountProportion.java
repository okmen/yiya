package com.bbyiya.vo.calendar;

/**
 *
 * @author max
 *  **
 *  A 生产商 ；
 *  B 产品授权经销商（代理商） ；
 *  C 活动参与单位（影楼） ；
 *  D 咿呀科技；
 *  E 幻想馆
 */
public class TiAmountProportion {
	/**
	 * 普通用户全款支付 ，分成比例（生产商）
	 */
	public static double full_A=0.33;
	public static double full_B=0.1;
	public static double full_C=0.2;
	public static double full_D=0.27;
	public static double full_E=0.1;
	
	/**
	 * 半价/折扣价购买分成比例
	 */
	public static double half_A=0.43;
	public static double half_B=0.1;
	public static double half_C=0.2;
	public static double half_D=0.17;
	public static double half_E=0.1;
	
	/**
	 * 惊爆价购买分成比例
	 */
	public static double cost_A=0.73;
	public static double cost_B=0.1;
	public static double cost_D=0.07;
	public static double cost_E=0.1;
	
	/**
	 * 运费分成比例（生产商）
	 */
	public static double post_A=0.9;
	public static double post_D=0.05;
	public static double post_E=0.05;
}
