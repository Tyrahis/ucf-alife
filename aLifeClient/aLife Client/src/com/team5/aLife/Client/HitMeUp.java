package com.team5.aLife.Client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.util.Log;

public class HitMeUp {
	
	static String returnType(JSONObject jObject){
		if (jObject.has("notification")) return "notification";
		if (jObject.has("deviceUpdate")) return "deviceUpdate";
		if (jObject.has("deviceList")) return "deviceList";
		return "Unknown";
	}
	
	static Notification parseNotification(JSONObject jObject){
		/*
 		Example string sequence for a notification
		{
			"notification": {
				"type":int,
				"Id":int,
					"shortTitle" : "Short Title",
						"longTitle" : "Long Title"}
		}
		*/	
		
	    // Instantiate the Notification:
    	int icon = R.drawable.notificationicon;
    	CharSequence tickerText = null;
    	CharSequence contentTitle = null;
    	CharSequence contentText = null;
    	long when = System.currentTimeMillis();
    	
		try {
			// Extract the data from the JSON Object
			JSONObject n = jObject.getJSONObject("notification");
			tickerText = n.getString("shortTitle");
			contentTitle = n.getString("shortTitle");
			contentText = n.getString("longTitle");

			// TODO Add the context Title and Text to the notification
			// Build and return the notification
	    	return new Notification(icon, tickerText, when);
		} catch (JSONException e) {
			Log.e("JSON", "Unable to extract notification");
			Log.e("JSON", e.toString());
			return null;
		}
	}
	
	static Device parseSingle(JSONObject jObject){
/*			
		// Example string sequence for a single device
		{
			"deviceUpdate":{
				"type":int,
					"name" : "name",
						"status" : "status"}
		}
*/	
		// The object to return
		Device newDevice = new Device();
		
		// Extract the data from the JSON object
		try {
			// Extract the JSON object
			JSONObject device = jObject.getJSONObject("deviceUpdate");
		
			// Extract the data from that object and store it in
			// the device object to return
			newDevice.setDeviceType(device.getInt("type"));
			newDevice.setDeviceName(device.getString("name"));
			newDevice.setDeviceStatus(Integer.parseInt(device.getString("status")));
			return newDevice;
		} catch (JSONException e) {
			Log.e("JSON", "Failed to parse single device");
			Log.e("JSON", e.toString());
			return null;
		}
		
	}
	
	static ArrayList<Device> parseList(JSONObject jObject){
		
		/*
		Example string sequence for a list of devices
		{
			"deviceList":{
				[
				{"type":int,
					"name" : "name",
						"status" : "status"},
				{"type":int,
					"name" : "name",
						"status" : "status"}

				]
		*/
		
		// We'll be parsing multiple aLife devices
		ArrayList<Device> deviceList = new ArrayList<Device>();
		// newDevice is used in the for loop to add to the ArrayList
		Device newDevice;
		
		try {
			// Get the JSON object that contains the JSON Array
			JSONArray deviceArray = jObject.getJSONArray("deviceList");
			JSONObject dObject = null;
			
			// Go through the JSON Array and extract each aLife device
			for (int i = 0; i < deviceArray.length(); i++){
				
				dObject = (JSONObject) deviceArray.get(i);
				
				newDevice = new Device();
			
				newDevice.setDeviceType(dObject.getInt("type"));
				newDevice.setDeviceName(dObject.getString("name"));
				newDevice.setDeviceStatus(Integer.parseInt(dObject.getString("status")));
				
				// Add the extracted device to the ArrayList and return it
				deviceList.add(newDevice);
			}
			return deviceList;
			
		} catch (JSONException e) {
			Log.e("JSON", "Failed to parse multiple device");
			Log.e("JSON", e.toString());
			return null;
		}
	}
	
}
