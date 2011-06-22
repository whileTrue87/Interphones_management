package ch.suricatesolutions.dingdong.updates;

public interface SipDevice {
	String getDeviceId();
	String getDeviceType();
	DeviceStatus getDeviceStatus();
}
