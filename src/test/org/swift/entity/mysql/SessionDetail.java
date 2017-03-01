package test.org.swift.entity.mysql;

import org.swift.serenebao.orm.anotation.*;
@Table(name = "session_detail", auto_increment = false)
public class SessionDetail{
	private String instanceId;
	private String id;
	//��Ҫ
	private Integer setupTime;
	private Integer alertTime;
	private Integer connectTime;
	private Integer releaseTime;
	private String releaseParty;
	private String releaseReason;
	private Integer record;
	private String codec;
	private Integer rtpRelay;
	private String callerIp;
	//·��
	private String apId;
	private String apName;
	private String gwId;
	private String gwName;
	private String routeShapedNumber;
	private String pooledCaller;
	private String rrId;
	private String rrName;
	private String spareGateway;
	//������Ϣ
	private String called;
	private String preShapedCalled;
	private String postShapedCalled;
	private String calledVendor;
	private String callerRtptype;
	//������Ϣ
	private String caller;
	private String preShapedCaller;
	private String postShapedCaller;
	private String callerVendor;
	private String calledRtptype;
	//�Ʒ���Ϣ
	private String aptRateId;
	private String aptRateName;
	private Integer aptCost;
	private String gwtRateId;
	private String gwtRateName;
	private Integer gwtCost;

	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getSetupTime() {
		return setupTime;
	}
	public void setSetupTime(Integer setupTime) {
		this.setupTime = setupTime;
	}
	public Integer getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(Integer alertTime) {
		this.alertTime = alertTime;
	}
	public Integer getConnectTime() {
		return connectTime;
	}
	public void setConnectTime(Integer connectTime) {
		this.connectTime = connectTime;
	}
	public Integer getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Integer releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getReleaseParty() {
		return releaseParty;
	}
	public void setReleaseParty(String releaseParty) {
		this.releaseParty = releaseParty;
	}
	public String getReleaseReason() {
		return releaseReason;
	}
	public void setReleaseReason(String releaseReason) {
		this.releaseReason = releaseReason;
	}
	public Integer getRecord() {
		return record;
	}
	public void setRecord(Integer record) {
		this.record = record;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public Integer getRtpRelay() {
		return rtpRelay;
	}
	public void setRtpRelay(Integer rtpRelay) {
		this.rtpRelay = rtpRelay;
	}
	public String getCallerIp() {
		return callerIp;
	}
	public void setCallerIp(String callerIp) {
		this.callerIp = callerIp;
	}
	public String getApId() {
		return apId;
	}
	public void setApId(String apId) {
		this.apId = apId;
	}
	public String getApName() {
		return apName;
	}
	public void setApName(String apName) {
		this.apName = apName;
	}
	public String getGwId() {
		return gwId;
	}
	public void setGwId(String gwId) {
		this.gwId = gwId;
	}
	public String getGwName() {
		return gwName;
	}
	public void setGwName(String gwName) {
		this.gwName = gwName;
	}
	public String getRouteShapedNumber() {
		return routeShapedNumber;
	}
	public void setRouteShapedNumber(String routeShapedNumber) {
		this.routeShapedNumber = routeShapedNumber;
	}
	public String getPooledCaller() {
		return pooledCaller;
	}
	public void setPooledCaller(String pooledCaller) {
		this.pooledCaller = pooledCaller;
	}
	public String getRrId() {
		return rrId;
	}
	public void setRrId(String rrId) {
		this.rrId = rrId;
	}
	public String getRrName() {
		return rrName;
	}
	public void setRrName(String rrName) {
		this.rrName = rrName;
	}
	public String getSpareGateway() {
		return spareGateway;
	}
	public void setSpareGateway(String spareGateway) {
		this.spareGateway = spareGateway;
	}
	public String getCalled() {
		return called;
	}
	public void setCalled(String called) {
		this.called = called;
	}
	public String getPreShapedCalled() {
		return preShapedCalled;
	}
	public void setPreShapedCalled(String preShapedCalled) {
		this.preShapedCalled = preShapedCalled;
	}
	public String getPostShapedCalled() {
		return postShapedCalled;
	}
	public void setPostShapedCalled(String postShapedCalled) {
		this.postShapedCalled = postShapedCalled;
	}
	public String getCalledVendor() {
		return calledVendor;
	}
	public void setCalledVendor(String calledVendor) {
		this.calledVendor = calledVendor;
	}
	public String getCallerRtptype() {
		return callerRtptype;
	}
	public void setCallerRtptype(String callerRtptype) {
		this.callerRtptype = callerRtptype;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getPreShapedCaller() {
		return preShapedCaller;
	}
	public void setPreShapedCaller(String preShapedCaller) {
		this.preShapedCaller = preShapedCaller;
	}
	public String getPostShapedCaller() {
		return postShapedCaller;
	}
	public void setPostShapedCaller(String postShapedCaller) {
		this.postShapedCaller = postShapedCaller;
	}
	public String getCallerVendor() {
		return callerVendor;
	}
	public void setCallerVendor(String callerVendor) {
		this.callerVendor = callerVendor;
	}
	public String getCalledRtptype() {
		return calledRtptype;
	}
	public void setCalledRtptype(String calledRtptype) {
		this.calledRtptype = calledRtptype;
	}
	public String getAptRateId() {
		return aptRateId;
	}
	public void setAptRateId(String aptRateId) {
		this.aptRateId = aptRateId;
	}
	public String getAptRateName() {
		return aptRateName;
	}
	public void setAptRateName(String aptRateName) {
		this.aptRateName = aptRateName;
	}
	public Integer getAptCost() {
		return aptCost;
	}
	public void setAptCost(Integer aptCost) {
		this.aptCost = aptCost;
	}
	public String getGwtRateId() {
		return gwtRateId;
	}
	public void setGwtRateId(String gwtRateId) {
		this.gwtRateId = gwtRateId;
	}
	public String getGwtRateName() {
		return gwtRateName;
	}
	public void setGwtRateName(String gwtRateName) {
		this.gwtRateName = gwtRateName;
	}
	public Integer getGwtCost() {
		return gwtCost;
	}
	public void setGwtCost(Integer gwtCost) {
		this.gwtCost = gwtCost;
	}

}
