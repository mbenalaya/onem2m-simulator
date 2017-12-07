/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package adnsim;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class Start {
	public static void main(String[] args){
		loadParams();
		
		Constants.csePoa = Constants.cseProtocol+"://"+Constants.cseIp+":"+Constants.csePort;
		Constants.appPoa = Constants.aeDeviceProtocol+"://"+Constants.aeDeviceIp+":"+Constants.aeDevicePort;
		
		if(Constants.reset){
			for(int i = 0;i<Constants.numberOfDevice;i++){
				String aeName = Constants.aeDeviceNamePrefix+i;
				RestHttpClient.delete(Constants.adminAeId, Constants.adminToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName);
			}
		}
		
		RestHttpClient.delete(Constants.adminAeId, Constants.adminToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+"asar_sim");
		
		
		JSONObject obj = new JSONObject();
		JSONObject resource = new JSONObject();
		
//	    obj.put("rn", "asar_sim");
//	    JSONArray apci = new JSONArray();
//		apci.put(Constants.aeDeviceToken);
//		obj.put("apci",apci);
//		JSONArray aae = new JSONArray();
//		for(int i=0;i<Constants.numberOfDevice;i++){
//			aae.put(Constants.aeDeviceIdPrefix+i);
//		}
//		aae.put("Cae-monitor");
//		obj.put("aae",aae);

//	    resource.put("m2m:asar", obj);
//		RestHttpClient.post(Constants.adminAeId, Constants.adminToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName, resource.toString(), 19);
//		
		for(int i = 0;i<Constants.numberOfDevice;i++){
				final String aeName = Constants.aeDeviceNamePrefix+i;
				final String aeid = Constants.aeDeviceIdPrefix+i;	
				Constants.values.add(0);
			
							JSONArray array = new JSONArray();
							array.put(Constants.appPoa);
							obj = new JSONObject();
							obj.put("rn", aeName);
							obj.put("api", "company1.com.test");
							obj.put("rr", true);
							obj.put("poa",array);
							resource = new JSONObject();
							resource.put("m2m:ae", obj);
							RestHttpClient.post(aeid, Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName, resource.toString(), 2).getStatusCode();	
		}
						
		Actuators.start();		
		Sensors.start();
	}
	
	public static void loadParams(){
    	System.out.println("[INFO] Get params from config file");
    	Properties prop = new Properties();
    	InputStream input = null;
    	try {
    		input = new FileInputStream("./config.ini");
    		prop.load(input);
    		//Commons
    		//Parameters.originator = prop.getProperty("originator") ;
    		Constants.adminToken = prop.getProperty("adminToken");
    		Constants.adminAeId = prop.getProperty("adminAeId");
    		Constants.cseProtocol = prop.getProperty("cseProtocol") ;
    		Constants.cseIp = prop.getProperty("cseIp");
    		Constants.cseProtocol = prop.getProperty("cseProtocol") ;
    		Constants.cseIp = prop.getProperty("cseIp") ;
    		Constants.csePort = Integer.parseInt(prop.getProperty("csePort"));
    		Constants.cseId = prop.getProperty("cseId") ;
    		Constants.cseName = prop.getProperty("cseName") ;
    		Constants.aeDeviceIdPrefix = prop.getProperty("aeDeviceIdPrefix") ;
    		Constants.aeDeviceNamePrefix = prop.getProperty("aeDeviceNamePrefix") ;
    		Constants.numberOfDevice = Integer.parseInt(prop.getProperty("numberOfDevice"));
    		//Parameters.cntData = prop.getProperty("cntData");
    		//Parameters.cntDesc = prop.getProperty("cntDesc");
    		Constants.containerMaxNumberOfInstances = Integer.parseInt(prop.getProperty("containerMaxNumberOfInstances"));
    		Constants.aeDeviceToken =prop.getProperty("aeDeviceToken");
    		Constants.reset = Boolean.parseBoolean(prop.getProperty("reset")) ;

    		//Sensor
    		Constants.deviceSleepPeriodInMs = Integer.parseInt(prop.getProperty("deviceSleepPeriodInMs")) ;
    		Constants.cntSensorPrefix = prop.getProperty("cntSensorPrefix") ;
    		
    		//Actutaor
    		Constants.cntActuatorPrefix = prop.getProperty("cntActuatorPrefix") ;
    		Constants.aeDeviceProtocol = prop.getProperty("aeDeviceProtocol") ;
    		Constants.aeDeviceIp = prop.getProperty("aeDeviceIp") ;
    		Constants.aeDevicePort = Integer.parseInt(prop.getProperty("aeDevicePort")) ;

    	} catch (IOException io) {
    		io.printStackTrace();
    		return ;
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
	}
}
