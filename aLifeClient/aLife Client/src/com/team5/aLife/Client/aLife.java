package com.team5.aLife.Client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.*;

public class aLife extends Activity {
	
    private EditText te_serverIP, te_port, te_userName, te_password;
	private Button connectButton;
	
	private static InetAddress serverAddress;
	private static int serverPort;
	
	private String userName, password;
	private Boolean connected = false;
	private Boolean startConnection = false;
	
	private Timer timer = new Timer();
	
	private Gson gsonIn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        // Get the EditText id's
        te_serverIP = (EditText) findViewById(R.id.ServerIP);
        te_port = (EditText) findViewById(R.id.ServerPORT);
        te_userName = (EditText) findViewById(R.id.UserID);
        te_password = (EditText) findViewById(R.id.Password);
        connectButton = (Button) findViewById(R.id.Login);
        connectButton.setOnClickListener(loginListener);
        
        te_serverIP.setText("10.0.2.2"); 
        te_port.setText("8640");
        te_userName.setText("amos");
        // te_password.setHint("password");
        // TODO Make password input hidden
        te_password.setText("pass");
        
        // TODO Make the IP address and Port number a history input
        
        // TODO Make "login" button change to connected


               
    } // end onCreate
   @Override
 protected void onStart(){
	   super.onStart();
	   
	   
   	    	
} // end onStart()
   
   @Override
 protected void onDestroy(){
	 super.onDestroy();
	 // TODO aLife:onDestroy
	 stopListener();
	 
 } // end onDestroy
  
    public void startListener() {
    	Log.d("aLife:startListener", "Starting aLifeListener");
    	startService(new Intent(this, aLifeListener.class));
    }
    
    public void stopListener() {
    	if (timer != null)
    		timer.cancel();
    	Log.d("aLife:stopListener", "Timer Stopped");
    }
    
    private View.OnClickListener loginListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			// These 4 booleans are used to verify all the TextEdit
			// fields have an valid entry before attempting a connection.
			// When all 4 are true then a connection attempt will be made
			// and the username/password combo will be checked at the
			// server
			Boolean ipValid = false;
			Boolean portValid = false;
			Boolean passwordValid = false;
			Boolean usernameValid = false;
			
			// make sure a we don't allow and old IP address to sneak through
			serverAddress = null;
			
			if (!connected){
				
				// Verify valid port number
				// if (te_port.hasSelection()) // this didn't respond to a filled TextEdit
					// TODO See what happens if Integer.parseInt fails w/ an invalid Port #
					serverPort = Integer.parseInt(te_port.getText().toString());
				// TODO Narrow down valid port number range
				if (serverPort > 1024 && serverPort < 65565)
					portValid = true;			
				
				// Verify valid IP address
				// if (te_serverIP.hasSelection()){  // try/catch sufficient replacement for if?
				try {
					String s = te_serverIP.getText().toString();
					// Log.d("aLife", "to_serverIP.toString() = " + s);					
					if (s != null)
						serverAddress = InetAddress.getByName(s);		// requires try/catch
					if (serverAddress != null)
						ipValid = true;
				} catch (UnknownHostException e) {
					makeToast("Enter a valid IP address");
					Log.e("aLife", "te_serverIP.toString failed",e);
				}
				// } end if
				
				// Verify username field
				userName = te_userName.getText().toString();
				if (userName == null){
					makeToast("Enter a username");
				} else {
					usernameValid = true;
				}
					
				// Verify password field
				password = te_password.getText().toString();
				if (password == null){
					makeToast("Enter a password");
				} else {
					passwordValid = true;
				}
				
				// If all data is complete and valid then start the 
				// listener thread!
				if (portValid && ipValid && usernameValid && passwordValid){
					// Attempt a connection
					makeShortToast("Connecting...");
					Log.d("aLife", "Login pressed: portValid:" + portValid +
							" ipValid:" + ipValid +
							" userNameValid:" + usernameValid +
							" passwordValid:" + passwordValid);
					startConnection = true;
//					startService(new Intent(this, aLifeListener.class));
					
			    	timer.schedule(new TimerTask(){
			    		public void run(){
			    			Log.d("aLife:ButtonListener", "timer.schedule(new TimerTask()");
			    			startListener();
			    			}
			    		}, 1000);
					
					
				// this.getClass().getClassLoader()	
					
				} else {
					makeToast("Enter valid data for all fields");
					Log.d("aLife", "Login pressed but portValid:" + portValid +
							" ipValid:" + ipValid +
							" userNameValid:" + usernameValid +
							" passwordValid:" + passwordValid);
				}
						
			} else { // end if(!connected)
				makeToast("You're Already Connected");
			}
			
		} // end onClick()
	}; // end loginListener
	
    public void makeToast(String string) {
    	Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    public void makeShortToast(String string) {
    	Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
    
    // Required to pass data to aLifeListener service
    // TODO Tim says this data should be packaged in a "bundle" and pasted with the Intent 
    public static InetSocketAddress getServerAddress (){
		return new InetSocketAddress(serverAddress, serverPort);
    	
    }
    
}