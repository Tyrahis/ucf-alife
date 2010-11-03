package com.team5.aLife.Client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class customListView extends ListActivity {
    /** Called when the activity is first created. */

	private static final int LIGHT_SWITCH = 1;
	private static final int RELAY = 2;
	private static final int INDOOR_TEMP_SENSOR = 3;
	private static final int THERMOSTAT = 4;
	private static final int GARAGE_DOOR_SENSOR = 5;
	private static final int WATER_SENSOR = 6;
	private static final int OUTDOOR_TEMP_SENSOR = 7;
    
    private ProgressDialog m_ProgressDialog = null; 	// progress dialog
    private ArrayList<Device> deviceList = null;		// holds our device list
    private DeviceAdapter dAdapter;						// custom class the extends ArrayAdapter
    private Runnable viewDevices;						// downloads data from the net
    
    private String deviceListData;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        deviceList = new ArrayList<Device>();			// create a new array of devices
        this.dAdapter = new DeviceAdapter(this, R.layout.row, deviceList); 
        setListAdapter(this.dAdapter);
        
        viewDevices = new Runnable(){
            @Override
            public void run() {
                // getDevices();
                downloadDeviceData();
                parseDeviceData();
            }
        };
        Thread thread =  new Thread(null, viewDevices, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(customListView.this,    
              "Please wait...", "Retrieving data ...", true);
    } // end onCreate
    
    public void showList(){
    	Intent myIntent = new Intent();
    	myIntent.setClassName("com.team5.aLife.Client", "customListView.class");
    	startActivity(myIntent);
    }
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(deviceList != null && deviceList.size() > 0){
                dAdapter.notifyDataSetChanged();
                for(int i=0;i<deviceList.size();i++)
                dAdapter.add(deviceList.get(i));
            }
            m_ProgressDialog.dismiss();
            dAdapter.notifyDataSetChanged();
        }
    };
    
    private void parseDeviceData(){
        try{        	
            deviceList = new ArrayList<Device>();
            
            String[] arr = deviceListData.split("\n");
           for (String s : arr)
        	   Log.i("ARRAY", s);
           
            
           
            String[] arrC;
            
            for (int i = 0; i < arr.length; i++ ){
            	Device tempD = new Device();
            	arrC = arr[i].split(",");
            	tempD.setDeviceType(Integer.parseInt(arrC[0]));
            	tempD.setDeviceName(arrC[1]);
            	tempD.setDeviceStatus(Integer.parseInt(arrC[2]));
            	deviceList.add(tempD);
            }
            
           
            /*  Could use tokenizer
             * String str = "tim,kerry,timmy,Camden";
				StringTokenizer st = new StringTokenizer(str, ",");
				while (st.hasMoreTokens()) {
     			System.out.println(st.nextToken());
				}
             */
           
            Thread.sleep(3000);
            Log.i("ARRAY", "" + deviceList.size() + "Devices");
          } catch (Exception e) { 
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
    }
    
	private void downloadDeviceData(){        
        try {
            HttpClient hc = new DefaultHttpClient();
            // HttpResponse hs = hc.execute(new HttpGet("http://eustis.eecs.ucf.edu/~am834740/deviceList.txt"));
            HttpResponse hs = hc.execute(new HttpGet("http://dl.dropbox.com/u/2435953/deviceList.txt"));
            
			int status = hs.getStatusLine().getStatusCode();
			
			// we assume that the response body contains the error message
			if (status != HttpStatus.SC_OK) {
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				hs.getEntity().writeTo(ostream);
				Log.e("HTTP CLIENT", ostream.toString());
			} else {
				InputStream is = hs.getEntity().getContent();
				
				if (is != null) {
		            StringBuilder sb = new StringBuilder();
		            String line;

		            try {
		                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		                while ((line = reader.readLine()) != null) {
		                    sb.append(line).append("\n");
		                }
		            } finally {
		                is.close();
		            }
		            deviceListData = sb.toString();
		            Log.i("DEVICE LIST", deviceListData);
		        } else {        
		            deviceListData = "";
		            Log.i("DEVICE LIST", "empty");
		        }
				is.close();
			}

            
            
        } catch (IOException e1) {
        	Log.e("HTTP IOException", e1.toString());
        }

	}
    
    private void getDevices(){
/*
          try{
              deviceList = new ArrayList<Device>();
              Device o1 = new Device();
              o1.setDeviceType(LIGHT_SWITCH);
              o1.setDeviceName("Light Switch");
              o1.setDeviceStatus(1);
              Device o2 = new Device();
              o2.setDeviceType(THERMOSTAT);
              o2.setDeviceName("Thermostat");
              o2.setDeviceStatus(1);
              Device o3 = new Device();
              o3.setDeviceType(RELAY);
              o3.setDeviceName("Relay");
              o3.setDeviceStatus(0);
              Device o4 = new Device();
              o4.setDeviceType(INDOOR_TEMP_SENSOR);
              o4.setDeviceName("Temp Sensor");
              o4.setDeviceStatus(1);
              deviceList.add(o1);
              deviceList.add(o2);
              deviceList.add(o3);
              deviceList.add(o4);
              deviceList.add(o2);
              deviceList.add(o2);
              deviceList.add(o1);
              deviceList.add(o4);
              deviceList.add(o4);
              deviceList.add(o3);
              deviceList.add(o2);
              deviceList.add(o1);
              Thread.sleep(3000);
              Log.i("ARRAY", ""+ deviceList.size());
            } catch (Exception e) { 
              Log.e("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes); */
        }
        
    class DeviceAdapter extends ArrayAdapter<Device> {

        private ArrayList<Device> items;

        public DeviceAdapter(Context context, int textViewResourceId, ArrayList<Device> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Device o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        ImageView icon = (ImageView) v.findViewById(R.id.icon);
                        if (tt != null) {
                              tt.setText("Name: "+o.getDeviceName());                            }
                        if(bt != null){
                              bt.setText("Status: "+ o.getDeviceStatus());
                        }
                        if (icon != null) {
                        	icon.setImageResource(o.getDeviceIcon());
                        }
                }
                return v;
        }
        

    } // End Device Adapter
} // end customListView
