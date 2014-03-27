package com.fungchoi.sap.entity;

import java.io.Serializable;

public class PassParameter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3185894960099294258L;

	public final static String PP_KEY = "com.fungchoi.sap.pp";
	
	//�ʺź�����
	private String userName;//�ʺ�
	private String password;//����
	
	//��ѯ����
	private String beginDate;//������ʼ����
	private String endDate;//������������
	private String orderCode;//�������ֱ��������������
	private String materialDesc;//���ϲ�������������������
	
	//��������
	private String AUFNR;//����
	private String VORNR;//����
	private float LMNGA;//����
	private String MEINH;//��λ
	private float ISM01;//�˹�
	private float ISM02;//����
	private float ISM03;//����
	private float ISM04;//��ī
	private float ISM05;//ͨ�ò���
	private float ISM06;//����
	private String ILE01;//�˹�-��λ
	private String ILE02;//����-��λ
	private String ILE03;//����-��λ
	private String ILE04;//��ī-��λ
	private String ILE05;//ͨ�ò���-��λ
	private String ILE06;//����-��λ
	private String ISDD;//��ʼִ������
	private String IEDD;//����ִ������
	private String ISDZ;//��ʼִ��ʱ��
	private String IEDZ;//����ִ��ʱ��
	private String BUDAT;//��������
	private String LTXA1;//ȷ���ı�-�����ߵ��ֻ�����
	private String ZZSB;//�豸��������ֹ���
	
	//������
	private String result;//�������ش�����
		
	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the materialDesc
	 */
	public String getMaterialDesc() {
		return materialDesc;
	}
	/**
	 * @param materialDesc the materialDesc to set
	 */
	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}
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
	 * @return the lMNGA
	 */
	public float getLMNGA() {
		return LMNGA;
	}
	/**
	 * @param lMNGA the lMNGA to set
	 */
	public void setLMNGA(float lMNGA) {
		LMNGA = lMNGA;
	}
	/**
	 * @return the iSM01
	 */
	public float getISM01() {
		return ISM01;
	}
	/**
	 * @param iSM01 the iSM01 to set
	 */
	public void setISM01(float iSM01) {
		ISM01 = iSM01;
	}
	/**
	 * @return the iSM02
	 */
	public float getISM02() {
		return ISM02;
	}
	/**
	 * @param iSM02 the iSM02 to set
	 */
	public void setISM02(float iSM02) {
		ISM02 = iSM02;
	}
	/**
	 * @return the iSM03
	 */
	public float getISM03() {
		return ISM03;
	}
	/**
	 * @param iSM03 the iSM03 to set
	 */
	public void setISM03(float iSM03) {
		ISM03 = iSM03;
	}
	/**
	 * @return the iSM04
	 */
	public float getISM04() {
		return ISM04;
	}
	/**
	 * @param iSM04 the iSM04 to set
	 */
	public void setISM04(float iSM04) {
		ISM04 = iSM04;
	}
	/**
	 * @return the iSM05
	 */
	public float getISM05() {
		return ISM05;
	}
	/**
	 * @param iSM05 the iSM05 to set
	 */
	public void setISM05(float iSM05) {
		ISM05 = iSM05;
	}
	/**
	 * @return the iSM06
	 */
	public float getISM06() {
		return ISM06;
	}
	/**
	 * @param iSM06 the iSM06 to set
	 */
	public void setISM06(float iSM06) {
		ISM06 = iSM06;
	}
	/**
	 * @return the iLE01
	 */
	public String getILE01() {
		return ILE01;
	}
	/**
	 * @param iLE01 the iLE01 to set
	 */
	public void setILE01(String iLE01) {
		ILE01 = iLE01;
	}
	/**
	 * @return the iLE02
	 */
	public String getILE02() {
		return ILE02;
	}
	/**
	 * @param iLE02 the iLE02 to set
	 */
	public void setILE02(String iLE02) {
		ILE02 = iLE02;
	}
	/**
	 * @return the iLE03
	 */
	public String getILE03() {
		return ILE03;
	}
	/**
	 * @param iLE03 the iLE03 to set
	 */
	public void setILE03(String iLE03) {
		ILE03 = iLE03;
	}
	/**
	 * @return the iLE04
	 */
	public String getILE04() {
		return ILE04;
	}
	/**
	 * @param iLE04 the iLE04 to set
	 */
	public void setILE04(String iLE04) {
		ILE04 = iLE04;
	}
	/**
	 * @return the iLE05
	 */
	public String getILE05() {
		return ILE05;
	}
	/**
	 * @param iLE05 the iLE05 to set
	 */
	public void setILE05(String iLE05) {
		ILE05 = iLE05;
	}
	/**
	 * @return the iLE06
	 */
	public String getILE06() {
		return ILE06;
	}
	/**
	 * @param iLE06 the iLE06 to set
	 */
	public void setILE06(String iLE06) {
		ILE06 = iLE06;
	}
	/**
	 * @return the iSDD
	 */
	public String getISDD() {
		return ISDD;
	}
	/**
	 * @param iSDD the iSDD to set
	 */
	public void setISDD(String iSDD) {
		ISDD = iSDD;
	}
	/**
	 * @return the iEDD
	 */
	public String getIEDD() {
		return IEDD;
	}
	/**
	 * @param iEDD the iEDD to set
	 */
	public void setIEDD(String iEDD) {
		IEDD = iEDD;
	}
	/**
	 * @return the iSDZ
	 */
	public String getISDZ() {
		return ISDZ;
	}
	/**
	 * @param iSDZ the iSDZ to set
	 */
	public void setISDZ(String iSDZ) {
		ISDZ = iSDZ;
	}
	/**
	 * @return the iEDZ
	 */
	public String getIEDZ() {
		return IEDZ;
	}
	/**
	 * @param iEDZ the iEDZ to set
	 */
	public void setIEDZ(String iEDZ) {
		IEDZ = iEDZ;
	}
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
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}
	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the bUDAT
	 */
	public String getBUDAT() {
		return BUDAT;
	}
	/**
	 * @param bUDAT the bUDAT to set
	 */
	public void setBUDAT(String bUDAT) {
		BUDAT = bUDAT;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
