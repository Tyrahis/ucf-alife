package com.team5.aLife.Client;

public class ALifeNotification {
	
	private int notificationType;
	private int notificationLevel;
	private int notificationID;
	private String shortHeading;
	private String heading;
	private String button1;
	private String button2;
	private String button3;
	
	public ALifeNotification()
	{
		//  Do nothing
	}
	
	public ALifeNotification ( int notificationType,
						  int notificationLevel,
						  int notificationID,
						  String shortHeading,
						  String heading,
						  String button1,
						  String button2,
						  String button3)
	{
		this.notificationType = notificationType;
		this.notificationLevel = notificationLevel;
		this.notificationID = notificationID;
		this.shortHeading = shortHeading;
		this.heading = heading;
		this.button1 = button1;
		this.button2 = button2;
		this.button3 = button3;
	}
	
	public int getNotificationType(){
		return notificationType;
	}
	
	public void setNoficationType(int notificationType){
		this.notificationType = notificationType;
	}

	public int getNotificationLevel(){
		return notificationLevel;
	}
	
	public void setNotificationLevel(int notificationLevel){
		this.notificationLevel = notificationLevel;
	}
	
	public int getNotificationID(){
		return notificationID;
	}
	
	public void setNotificationID(int notificationID){
		this.notificationID = notificationID;
	}
	
	public String getShortHeading(){
		return shortHeading;
	}
	
	public void setShortHeading(String shortHeading){
		this.shortHeading = shortHeading;
	}
	
	public String getHeading(){
		return heading;
	}
	
	public void setHeading(String heading){
		this.heading = heading;
	}
	
	public String getButton1(){
		return button1;
	}
	
	public void setButton1(String button1){
		this.button1 = button1;
	}
	
	public String getButton2(){
		return button2;
	}
	
	public void setButton2(String button2){
		this.button2 = button2;
	}
	
	public String getButton3(){
		return button3;
	}
	
	public void setButton3(String button3){
		this.button3 = button3;
	}
}

