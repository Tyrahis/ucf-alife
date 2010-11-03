package com.team5.aLife.Client;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public final class shoutOut{

		static Boolean debug = true;
	
		static public JSONObject build(int type, int Id, String shortTitle, String longTitle){
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
			
			// The return object
			JSONObject jObject;
			
			// Build the JSON string
			String jString = "{" +
			"\"notification\":{" +
				"\"type\":" + type + "," +
				"\"Id\":" + Id + "," +
					"\"shortTitle\":\"" + shortTitle + "\"," +
						"\"longTitle\":\"" + longTitle + "\"}" +
		"}";
		
		// Turn the string into a JSON object and return it
		try {
			jObject = new JSONObject(jString);
			return jObject;
		} catch (JSONException e) {
			Log.e("JSON", e.toString());
			return null;
		} 
			
		}
		
		static JSONObject build(Device device){
/*			
			Example string sequence for a single device
			{
				"deviceUpdate":{
					"type":int,
						"name" : "name",
							"status" : "status"}
			}
*/			
			// The return object
			JSONObject jObject;
			
			// Extract the data out of the device object and
			// build the JSON string
			String jString = "{" +
				"\"deviceUpdate\":{" +
					"\"type\":" + device.getDeviceType() + "," +
						"\"name\":\"" + device.getDeviceName() + "\"," +
							"\"status\":\"" + device.getDeviceStatus() + "\"}" +
			"}";
			
			// Turn the string into a JSON object and return it			
			try {
				jObject = new JSONObject(jString);
				return jObject;
			} catch (JSONException e) {
				Log.e("JSON", e.toString());
				return null;
			} 
			
		}
		
		static JSONObject build(ArrayList<Device> deviceList){
			
			/*
			Example string sequence for a list of devices
			{
				"deviceList":
					[
					{"type":int,
						"name" : "name",
							"status" : "status"},
					{"type":int,
						"name" : "name",
							"status" : "status"}

					]
			}
			*/
			
			// The return object
			JSONObject jObject;
			
			// Create the first part of the JSON string
			String jString = "{\"deviceList\":[";
			
			// Create add the JSON array's
			for (int i = 0 ; i < deviceList.size() ; i++){
			
				int deviceType = deviceList.get(i).getDeviceType();
				String deviceName = deviceList.get(i).getDeviceName();
				int deviceStatus = deviceList.get(i).getDeviceStatus();
				
				String jMeatString = "{" +
						"\"type\":\"" + deviceType + "\"," +
							"\"name\":\"" + deviceName + "\"," +
								"\"status\":\"" + deviceStatus + "\"}";
				
				// add a comma between each object
				jString = jString.concat(jMeatString);
				
				if (i < deviceList.size() - 1)
					jString = jString.concat(",");
				
				// if (debug) Log.d("JSON", "jString: " + jString);
				}
			
			//add final right box
			jString = jString.concat("]}");
			

			if (debug) Log.d("JSON String", jString);
			// Turn the string into a JSON object and return it
			try {
				jObject = new JSONObject(jString);
				if (debug) Log.d("JSON build", "success!");
				return jObject;
			} catch (JSONException e) {
				Log.e("JSON", e.toString());
				return null;
			} 
		}
}
