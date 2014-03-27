/**
 * 
 */
package com.fungchoi.sap.entity;

/**
 * @author Administrator
 *
 */
public class Step {

	/**
	 * 
	 */
	public Step() {
		// TODO Auto-generated constructor stub
	}
	
	private String VORNR;//	1 Types	VORNR	CHAR	4	0	操作/活动编号
	private String STEUS;//	1 Types	STEUS	CHAR	4	0	控制码 
	private String LTXA1;//	1 Types	LTXA1	CHAR	40	0	工序短文本 
	private float MGVRG;//	1 Types	MGVRG	QUAN	13	3	工序数量
	private String MEINH;//	1 Types	VORME	UNIT	3	0	作业/工序的计量单位 
	private String MSEHT;//	1 Types	MSEHT	CHAR	10	0	度量单位文本(最多10个字符)
	private String RUECK;//确认号
	/**
	 * @return the vORNR
	 */
	public String getVORNR() {
		return VORNR;
	}
	/**
	 * @param vORNR the vORNR to set
	 */
	public void setVORNR(String vORNR) {
		VORNR = vORNR;
	}
	/**
	 * @return the sTEUS
	 */
	public String getSTEUS() {
		return STEUS;
	}
	/**
	 * @param sTEUS the sTEUS to set
	 */
	public void setSTEUS(String sTEUS) {
		STEUS = sTEUS;
	}
	/**
	 * @return the lTXA1
	 */
	public String getLTXA1() {
		return LTXA1;
	}
	/**
	 * @param lTXA1 the lTXA1 to set
	 */
	public void setLTXA1(String lTXA1) {
		LTXA1 = lTXA1;
	}
	/**
	 * @return the mGVRG
	 */
	public float getMGVRG() {
		return MGVRG;
	}
	/**
	 * @param mGVRG the mGVRG to set
	 */
	public void setMGVRG(float mGVRG) {
		MGVRG = mGVRG;
	}
	/**
	 * @return the mEINH
	 */
	public String getMEINH() {
		return MEINH;
	}
	/**
	 * @param mEINH the mEINH to set
	 */
	public void setMEINH(String mEINH) {
		MEINH = mEINH;
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
	/**
	 * @return the rUECK
	 */
	public String getRUECK() {
		return RUECK;
	}
	/**
	 * @param rUECK the rUECK to set
	 */
	public void setRUECK(String rUECK) {
		RUECK = rUECK;
	}
	
	

}
