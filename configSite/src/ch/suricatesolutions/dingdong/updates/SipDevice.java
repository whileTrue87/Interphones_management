package ch.suricatesolutions.dingdong.updates;

import java.io.Serializable;

public class SipDevice implements Serializable{
	private static final long serialVersionUID = -634609127146633314L;
	private String deviceId;
	private String deviceType;
	private DeviceStatus deviceStatus;
	
	
	public SipDevice(String deviceId, String deviceType, DeviceStatus deviceStatus) {
		super();
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.deviceStatus = deviceStatus;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	
	
}
