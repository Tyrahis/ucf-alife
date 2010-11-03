package com.team5.aLife.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class aLifeListener extends Service{
	
	// default ip to listen to
    // public static String SERVERIP = "";
			
	// Thread Handler
	private Handler handler = new Handler();
	
	// TCP/IP Stuff
	private ServerSocket serverSocket;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	String nextLine = null;
	
	ObjectInputStream in_test;
	
	// TCP/IP I/O
    private String line;
	private String toSend;
	private Gson gsonIn;
	InetAddress serverIP;
	int serverPort = 4444;

	// Nofication Service variables
	// String ns;
	NotificationManager mNM;
	Context context;
	CharSequence contentTitle = "null";
	CharSequence contentText = "null";
	Notification notification;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        Log.d("aLifeListener:onCreate()", "Hello!");
        
        context = getApplicationContext();
        
        // Get notification variables setup
        // ns = Context.NOTIFICATION_SERVICE;
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
    }

    public void onStart(Intent intent, int startId){
    	
    	//Set startup notification
    	// ALifeNotification startupN = new ALifeNotification(1,1,1,"aLife Awesomeness","aLife running",
		//				  "button1",
		//				  "button2",
		//				  "button3");
    	
    	// sendNotification(startupN);
    	
        super.onStart(intent, startId);
        
        InetSocketAddress socketAddress = aLife.getServerAddress();
     
        // SERVERIP = getLocalIpAddress();
        
        serverIP = socketAddress.getAddress();
        serverPort = socketAddress.getPort();
                
        // Runnable serverThread = new ServerThread();
        // new Thread(serverThread).start();
        
        Runnable callServer = new callServer();
        // new Thread(callServer).start();
        Thread t = new Thread(callServer);
        t.start();
        
        if (t.isAlive())
        	Toast.makeText(this,"Service created ..." + serverIP, Toast.LENGTH_LONG).show();  
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // make sure you close the socket upon exiting
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = null;
        Log.d("aLifeListener:onDestroy()", "Goodbye!");
    }
    
    public Boolean openSocket(){
		Log.d("aLifeListener:callServer", "Attempting connection with " + serverIP + " on port " + serverPort);
		
		try {
			socket = new Socket (serverIP, serverPort);
			socket.setSoTimeout(2000);
			socket.setKeepAlive(true);
			return true;
		} catch (IOException e) {
			Log.e("aLifeListener:openSocket", "Failed to connect", e);
			makeToast("I/O connection fail");
			return false;
		}

    	
    }
    
    public void closeSocket(){
    	try {
			socket.close();
		} catch (IOException e) {
			Log.e("aLifeListener:closeSocket", "Socket Closed?", e);
		}
    }
    
    public class callServer implements Runnable {

		@Override
		public void run() {
			
			// Looper.prepare();
			
			// Reset for a new thread
			socket = null;
			out = null;
			in = null;
			nextLine = null;
			
			if (openSocket()){
				try{	
					out = new PrintWriter(socket.getOutputStream(), true);
					//in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
					in_test = new ObjectInputStream(socket.getInputStream());
					
					Log.d("aLife callServer", "Connected to port " + socket.getPort());
					
					// This line caused a crash. Something about Handler.looper(); required.
					// makeToast("Connected to port:" + socket.getPort());
					
				// Catches for the initial connection	
				} catch (IOException e) {
					Log.e("aLifeListener:callServer", "Could not getOutputStream()", e);
					closeSocket();
				}  
			} // end if(openSocket())
			

			// Wait for data to arrive from the server
			// or for data waiting to be sent
			while(socket.isConnected()){
				
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e1) {
					Log.d("aLifeListener:callServer","sleep(1000) failed");
					e1.printStackTrace();
				}
				
				out.println("Send me something bro!");
				
				// while(true) {
					try {
						
						// if data has been received...
							// if ((nextLine = in.readLine()) != null){
							   if((nextLine = (String) in_test.readObject()) != null){
								// send the data to be processed
								parseReceived(nextLine);
								// clear the input
								nextLine = null;
							
						} // end if(in.ready())
						
					} catch (IOException e) {
						Log.e("aLife callServer", "Tried in.readLine(), but timed out");
						} catch (ClassNotFoundException ce){
							Log.d("aLifeListener:run()", "Read Object Failed", ce);
						}
					
					
					// if there is any data to be sent...
					if (toSend != null){
						makeToast("Sent:" + toSend.substring(0, 10));
						Log.d("aLife callServer", "Sent: " + toSend.toString());
						out.println(toSend);
						toSend = null;
					}
					
				//} // end while(true);
				
			} // end while socket.isConnected()
			
			makeToast("Connection lost");
			Log.d("aLife callServer", "Connection lost");
			
			// close the socket output
			try {
				socket.shutdownOutput();
			} catch (IOException e) {
				Log.e("aLife callServer", "socket.shutdownOutput()", e);
			}
			
			// close the input buffer
			try {
				in.close();
			} catch (IOException e) {
				Log.e("aLife callServer", "in.close()", e);
			}
			
			closeSocket();
			
			// Close the buffered output stream
			out.close();
			
			// TODO Stop service
			
			
		} // end run()
} // end callServer
    
    // gets the ip address of your phone's network
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }
    
    
    public void parseReceived(String nextLine) {
		// TODO figure out what to do with the incoming data
		// makeToast("Received data");
		Log.d("aLifeListener:parseReceived", "Received: " + nextLine);
		
		Gson g = new Gson();
		
		ALifeNotification a = g.fromJson(nextLine, com.team5.aLife.Client.ALifeNotification.class);
		
		sendNotification(a);
		
		
		
		// The first character defines what type of data is
		// coming in. Get that first byte, convert to an int,
		// and strip it out of the data stream
		
		/*
		int inType = Integer.parseInt(nextLine.substring(0, 0));
		if (inType != 0){
			Log.d("aLifeListener:parseReceived", "inType=" + inType);
			nextLine = nextLine.substring(1, nextLine.length());
			
			switch(inType){
			case 1:	// Notification
				break;
			case 2:	// Complete device list
				break;
			default:
				Log.d("aLifeListener:parseReceived", "unknown inType");
		
		
			
			}
			*/
		
			// reset for next time
		//	inType = 0;
			
		} // end if(inType != 0)
	    	
	// } // end parseReceived()

	// ServerThread for listening...
    /*
    public class ServerThread implements Runnable {

        public void run() {
           try {
            	
            	// Check to see if we even have a valid IP address to be reached at
            	// TODO: What if this service starts before an IP address is acquired.
                if (SERVERIP != null) {
                    // InetAddress localAddr = InetAddress.getByName(serverIP);
                    serverSocket = new ServerSocket(serverPort, 5, localAddr);
                    
                    while (true) {
                        // listen for incoming clients. Holds here until a connection is made.
                        Socket client = serverSocket.accept();
                        
                        // Report the remote connection ipaddress
                        Log.i("REMOTE_IP", client.getRemoteSocketAddress().toString());
                        
                        //  Connected client
                        try {
                        	
                        	// Receive data 
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            
                            line = null;
                            
                            while ((line = in.readLine()) != null) {
                                Log.d("aLife Receiver: ", line);
                                
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                    	
		                                //  New Message Received
                                    	
                                    	// TODO: Check to see if input is a notification.
                                    	// Check to see whats in line:
                                    	Log.d("aLife receiver: ", "Received a notification");
                                    	makeToast("Received a notification");
                                    	
                                    	
        
                                    } // end Run()
                                }); // end Runnable()
                            } // end while(line != null)
                            break;  // break from while(true)
                        } catch (Exception e) {
                            
                        	// TODO: Reconnect when connection lost
                        	Log.d("aLife Receiver", "Connection Lost", e);
                              
                        }
                    }
                } else 
                {
                 Log.d("aLife Receiver", "Didn't detect a local ip address so we didn't open a socket");       
                }
            // Catches Socket errors    
            } catch (Exception e) {
                //  Error
            	Log.e("aLife Receiver", "Socket Error?", e);
            }
        } // end Run()
    } // end Runnable
    */
    
    public int sendNotification(ALifeNotification an){
    	
    	// Set the Notification text
    	contentTitle = an.getHeading();
    	contentText = an.getShortHeading();

    	// Redundent?
        // context = getApplicationContext();

    	
    	// Setup the Intent
    	Intent notificationIntent = new Intent(this, aLifeListener.class);
    	//PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    	
    	// TODO Set Notification Sound
    	notification = new Notification(R.drawable.notificationicon, contentText, System.currentTimeMillis());
    	PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
    	    	
    	
    	// Not sure what this does
    	Log.d("aLifeListener:sendNotification","context:" + context + 
    				"; contentTitle:" + contentTitle +
    				"; contentText:" + contentText + 
    				"; pendingIntent:" + pendingIntent);
    	
    	notification.setLatestEventInfo(this, contentTitle, contentText, pendingIntent);	
    	
    	// Pass the notification to the NotificationManager
    	mNM.notify(an.getNotificationID(), notification);
    	
    	return an.getNotificationID();
    }
    
    
    public void makeToast(String string) {
    	Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }


}
