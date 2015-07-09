package com.doorcontrol.switchtest;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
//import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ActivitySwitch extends ActionBarActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private BufferedWriter clientOut;
    private String outMsg;
    private boolean wifiConnected = false;
    private String cmdCode;
    private String serverIpAddress = "192.168.43.4";
    private int port = 5001;
	 
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

         switchStatus = (TextView) findViewById(R.id.switchStatus);
	  
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        //set the switch to OFF
        mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	 
		  @Override
		  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		  {
			  if(isChecked){
				  ledCommand("LED ON");
				  switchStatus.setText("LED ON");
			  }else{
				  ledCommand("LED OFF");
				  switchStatus.setText("LED OFF");
			  }	 
		  }
	  });
	   
	  //check the current state before we display the screen
	  if(mySwitch.isChecked()){
		  switchStatus.setText("LED ON");
	  }
	  else {
		  switchStatus.setText("LED OFF");
	  }

	}

	private void ledCommand(String cmd)
	{
        cmdCode = cmd;
        if(!wifiConnected){
            if(serverIpAddress != null){
                Thread wifiThread = new Thread(new ClientThread());
                wifiThread.start();
            }
        }
	}

    public class ClientThread implements Runnable{
        public void run(){
            try {
                Socket s = new Socket(serverIpAddress, port);
                wifiConnected = true;
                clientOut = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                outMsg = cmdCode;
                clientOut.write(outMsg);
                clientOut.flush();

                clientOut.close();
                s.close();
                wifiConnected = false;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_switch, menu);
        return true;
    }
	 
	}