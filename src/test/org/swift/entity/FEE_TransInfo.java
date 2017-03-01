package test.org.swift.entity;

import java.util.Date;

/**
 * 通话记录表
 * @author Administrator
 *
 */
public class FEE_TransInfo {
	private long  	uniID;
	private String  	accountID;
	private int  	transType;
	private int  	productID;
	private String  	callerUID;
	private String  	callerPhone;
	private String  	calledUID;
	private String  	calledPhone;
	private String  	feeType;
	private int  	feeKind;
	private int  	ruleID;
	private Date  	startTime;
	private Date  	endTime;
	private int  	feeMinute;
	private float  	feeRate;
	private float  	feeMoney;
	private String  	feeCurrency;
	private Date  	operateTime;
	private String  	operator;
	private String  	remark;
	
	
	public long getUniID() {
		return uniID;
	}
	public void setUniID(long uniID) {
		this.uniID = uniID;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public int getTransType() {
		return transType;
	}
	public void setTransType(int transType) {
		this.transType = transType;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getCallerUID() {
		return callerUID;
	}
	public void setCallerUID(String callerUID) {
		this.callerUID = callerUID;
	}
	public String getCallerPhone() {
		return callerPhone;
	}
	public void setCallerPhone(String callerPhone) {
		this.callerPhone = callerPhone;
	}
	public String getCalledUID() {
		return calledUID;
	}
	public void setCalledUID(String calledUID) {
		this.calledUID = calledUID;
	}
	public String getCalledPhone() {
		return calledPhone;
	}
	public void setCalledPhone(String calledPhone) {
		this.calledPhone = calledPhone;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public int getFeeKind() {
		return feeKind;
	}
	public void setFeeKind(int feeKind) {
		this.feeKind = feeKind;
	}
	public int getRuleID() {
		return ruleID;
	}
	public void setRuleID(int ruleID) {
		this.ruleID = ruleID;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getFeeMinute() {
		return feeMinute;
	}
	public void setFeeMinute(int feeMinute) {
		this.feeMinute = feeMinute;
	}
	public float getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(float feeRate) {
		this.feeRate = feeRate;
	}
	public float getFeeMoney() {
		return feeMoney;
	}
	public void setFeeMoney(float feeMoney) {
		this.feeMoney = feeMoney;
	}
	public String getFeeCurrency() {
		return feeCurrency;
	}
	public void setFeeCurrency(String feeCurrency) {
		this.feeCurrency = feeCurrency;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
