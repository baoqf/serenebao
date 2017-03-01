package test.org.swift.entity;

import java.util.Date;

import org.swift.serenebao.orm.anotation.Table;
public class UserInfo {
    /// 用户ID
	private int		userId;
    ///  UID类型（0.普通卡；1.刮刮卡；2.贵宾卡；3.企业总机）
	private String		uid;
    ///  用户类型（1．个人用户；2．企业用户）
	private int		userType;
    /// 企业信息ID
	private int		enterpriseID;
    /// 用户密码
	private String		password;
    /// 用户姓名
	private String		userName;
    /// 用户年龄
	private int		userAge;
    /// 用户性别（0.女；1.男）
	private int		userSex;
    /// 身份证
	private String		userIdCard;
    /// 电子邮箱
	private String		userEmail;
    /// 联系方式
	private String		userContact;
    /// 电话号码
	private String		userTel;
    /// 手机
	private String		userMobile;
    /// 地址
	private String		address;
    /// 产品ID
	private int		productId;
    /// 用户状态
	private int		state;
    // 过期时间
	private Date		expireDate;
    /// 注册时间
	private Date		regTime;
    /// 注册方式
	private int		regModule;
    /// 注册人
	private String	regOperator;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getEnterpriseID() {
		return enterpriseID;
	}
	public void setEnterpriseID(int enterpriseID) {
		this.enterpriseID = enterpriseID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public int getUserSex() {
		return userSex;
	}
	public void setUserSex(int userSex) {
		this.userSex = userSex;
	}
	public String getUserIdCard() {
		return userIdCard;
	}
	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public int getRegModule() {
		return regModule;
	}
	public void setRegModule(int regModule) {
		this.regModule = regModule;
	}
	public String getRegOperator() {
		return regOperator;
	}
	public void setRegOperator(String regOperator) {
		this.regOperator = regOperator;
	}
	
	
}
