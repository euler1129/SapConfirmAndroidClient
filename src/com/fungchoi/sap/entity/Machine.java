/**
 * 
 */
package com.fungchoi.sap.entity;

/**
 * @author Administrator
 *
 */
public class Machine {

	/**
	 * 
	 */
	public Machine() {
		// TODO Auto-generated constructor stub
	}
	
	private String ZZSB;//	1 Types	ZZSB	CHAR	10	0	设备编号
	private String ZZSBMS;//	1 Types	ZZSBMS	CHAR	20	0	设备描述
	/**
	 * @return the zZSB
	 */
	public String getZZSB() {
		return ZZSB;
	}
	/**
	 * @param zZSB the zZSB to set
	 */
	public void setZZSB(String zZSB) {
		ZZSB = zZSB;
	}
	/**
	 * @return the zZSBMS
	 */
	public String getZZSBMS() {
		return ZZSBMS;
	}
	/**
	 * @param zZSBMS the zZSBMS to set
	 */
	public void setZZSBMS(String zZSBMS) {
		ZZSBMS = zZSBMS;
	}

}
