package com.team5.aLife.Client;

public class Device {

	private int deviceID;
	private int	deviceType;
    private String deviceName;
    private String deviceAddress;
    private int zigbeeStatus;
    private int deviceStatus;
    ALifeNotification notification;
    
    public Device()
    {
    	this.deviceType = 0;
    	this.deviceName = "";
    	this.deviceAddress = "";
    	this.zigbeeStatus = 0;
    	this.deviceStatus = 0;
    }
    
    
    public Device (int deviceType, String deviceName, String deviceAddress,
    			   int zigbeeStatus, int deviceStatus)
    {
    	this.deviceType = deviceType;
    	this.deviceName = deviceName;
    	this.deviceAddress = deviceAddress;
    	this.zigbeeStatus = zigbeeStatus;
    	this.deviceStatus = deviceStatus;
    }
    
    public Device (int deviceType, String deviceName, String deviceAddress,
			   int zigbeeStatus, int deviceStatus, ALifeNotification notification)
	{
		this.deviceType = deviceType;
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
		this.zigbeeStatus = zigbeeStatus;
		this.deviceStatus = deviceStatus;
		this.notification = notification;
	}
    
    public int getDeviceID(){
    	return deviceID;
    }
    
    public void setDeviceID(int id){
    	deviceID = id;
    }
    
    public int getDeviceType() {
    	return deviceType;
    }
    
    public void setDeviceType(int deviceType){
    	this.deviceType = deviceType;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public String getDeviceAddress(){
    	return deviceAddress;
    }
    
    public void setDeviceAddress(String address){
    	deviceAddress = address;
    }
    
    public int getZigbeeStatus(){
    	return zigbeeStatus;
    }
    
    public void setZigbeeStatus(int zigStatus){
    	zigbeeStatus = zigStatus;
    }
    
    public int getDeviceStatus() {
        return deviceStatus;
    }
    
    public String getDeviceStatusReadable(){
    	switch (deviceStatus){
    	case 0: return "Off";
		case 1: return "On";
		default: return "Unknown";		
    	}	
    		
    }
    
    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
    
    public ALifeNotification getNotification(){
    	return notification;
    }
    
    public void setNotification(ALifeNotification notification){
    	this.notification = notification;
    }
    
    public void turnON()
    {
    	//  Yet to be implemented.  Turns Zigbee device on
    }
    
    public void turnOff()
    {
    	//  Yet to be implemented.  Turns Zigbee device off
    }
    
    public int getDeviceIcon() {
		switch (deviceType){
		case 1: return R.drawable.light;		// Light
		case 2: return R.drawable.flash;		// Relay
		case 3: return R.drawable.phone;		// Indoor Temperature Sensor
		case 4: return R.drawable.settings;		// Thermostat
		case 5: return R.drawable.exit;			// Garage Door Sensor";
		case 6: return R.drawable.chart;		// Water sensor";
		case 7: return R.drawable.equalizer;	// Outdoor Temperature Sensor";
		};
		
		return R.drawable.database;
    }
    
    public String toString(){
    	return "Type: " + deviceType + "\n" +
    			"Device ID: " + deviceID + "\n" +
    			"Name: " + deviceName + "\n" +
    			"Zigbee Address: " + deviceAddress + "\n" +
    			"Zigbee Status: " + zigbeeStatus + "\n" +
    			"Status: " + this.getDeviceStatusReadable() + "\n";    	
    }
}
