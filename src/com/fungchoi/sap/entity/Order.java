/**
 * 
 */
package com.fungchoi.sap.entity;

/**
 * @author Administrator
 *
 */
public class Order {
	private String AUFNR;//	1 Types	AUFNR	CHAR	12	0	订单号 
	private String MAKTX;//1 Types	MAKTX	CHAR	40	0	物料描述（短文本） 
	private int GAMNG;//1 Types	GAMNG	QUAN	13	3	订单数量总计
	private int IGMNG;//1 Types	CO_IGMNG	QUAN	13	3	确认订单中的产量确认
	private String MEINS;//1 Types	MEINS	UNIT	3	0	基本计量单位
	private String MSEHT;//1 Types	MSEHT	CHAR	10	0	度量单位文本(最多10个字符)
	/**
	 * @return the aUFNR
	 */
	public String getAUFNR() {
		return AUFNR;
	}
	/**
	 * @param aUFNR the aUFNR to set
	 */
	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}
	/**
	 * @return the mAKTX
	 */
	public String getMAKTX() {
		return MAKTX;
	}
	/**
	 * @param mAKTX the mAKTX to set
	 */
	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}
	/**
	 * @return the gAMNG
	 */
	public int getGAMNG() {
		return GAMNG;
	}
	/**
	 * @param gAMNG the gAMNG to set
	 */
	public void setGAMNG(int gAMNG) {
		GAMNG = gAMNG;
	}
	/**
	 * @return the iGMNG
	 */
	public int getIGMNG() {
		return IGMNG;
	}
	/**
	 * @param iGMNG the iGMNG to set
	 */
	public void setIGMNG(int iGMNG) {
		IGMNG = iGMNG;
	}
	/**
	 * @return the mEINS
	 */
	public String getMEINS() {
		return MEINS;
	}
	/**
	 * @param mEINS the mEINS to set
	 */
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	/**
	 * @return the mSEHT
	 */
	public String getMSEHT() {
		return MSEHT;
	}
	/**
	 * @param mSEHT the mSEHT to set
	 */
	public void setMSEHT(String mSEHT) {
		MSEHT = mSEHT;
	}

}
