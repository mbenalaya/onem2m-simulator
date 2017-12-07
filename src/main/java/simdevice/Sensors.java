/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package simdevice;

import org.json.JSONArray;
import org.json.JSONObject;

public class Sensors {
	
	public static int sensorValue;
	
	private static String csePoa = Constants.cseProtocol+"://"+Constants.cseIp+":"+Constants.csePort;
	
	public static void start(){
		for(int i = 0;i<Constants.numberOfDevice;i++){
	
				String aeName = Constants.aeDeviceNamePrefix+i;
				//String aeid = Constants.aeDeviceIdPrefix+i;
				String aeid = Constants.adminAeId;	
				Sensors.start(aeid, aeName);
		}
	}
	
	public static void start(final String aeid, final String name) {


//		for(int i=1;i<2;i++){
			final String cntName= Constants.cntSensorPrefix;
			new Thread(){
				JSONObject obj;
				JSONObject resource;
					public void run(){
						int status = 0;

						obj = new JSONObject();
						obj.put("rn", cntName);
						obj.put("mni", Constants.containerMaxNumberOfInstances);
						resource = new JSONObject();
					    resource.put("m2m:cnt", obj);
						RestHttpClient.post(aeid, Constants.aeDeviceToken,csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+name, resource.toString(), 3);
						
//						obj = new JSONObject();
//						obj.put("rn", Constants.cntDesc);
//						obj.put("mni", Constants.containerMaxNumberOfInstances);
//						resource = new JSONObject();
//						resource.put("m2m:cnt", obj);
//						RestHttpClient.post(aeid, Constants.aeDeviceToken,csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+name+"/"+cntName, resource.toString(), 3);
//						
//						obj = new JSONObject();
//						obj.put("cnf", "application/xml");
//						obj.put("con", "<obj><str name=\"type\" val=\""+cntName+"\"/></obj>");
//						resource = new JSONObject();
//						resource.put("m2m:cin", obj);
//						RestHttpClient.post(aeid,Constants.aeDeviceToken, csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+name+"/"+cntName+"/"+Constants.cntDesc, resource.toString(), 4);
						
						obj = new JSONObject();
						obj.put("rn", Constants.cntData);
						obj.put("mni", Constants.containerMaxNumberOfInstances);
						JSONArray lbl = new JSONArray();
						lbl.put("data");			
						obj.put("lbl", lbl);
						resource = new JSONObject();
						resource.put("m2m:cnt", obj);
						RestHttpClient.post(aeid, Constants.aeDeviceToken,csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+name+"/"+cntName, resource.toString(), 3);
						
						boolean loop=true;
						while (loop){
					
//							obj = new JSONObject();
//							obj.put("cnf", "application/xml");
//							obj.put("con", "<obj><str name=\"type\" val=\""+cntName+"\"/><str name=\"id\" val=\""+name+"\"/><int name=\"value\" val=\""+getSensorValue()+"\"/></obj>");
							obj = new JSONObject();
							obj.put("cnf", "application/json");
						    JSONObject con = new JSONObject();
						    con.put("mySensorType", cntName);
						    con.put("mySensorId", name+'_'+cntName);
						    con.put("mySensorData", getSensorValue());
							obj.put("con", con.toString());
							
							resource = new JSONObject();
							resource.put("m2m:cin", obj);
							status = RestHttpClient.post(aeid,Constants.aeDeviceToken, csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+name+"/"+cntName+"/"+Constants.cntData, resource.toString(), 4).getStatusCode();
							if(status==404){
								loop=false;
							}
							try {
								Thread.sleep(Constants.deviceSleepPeriodInMs);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						run();
						
					}
			}.start();
	}
	
	public static int getSensorValue(){
		sensorValue = (int)(Math.random()*100);
		System.out.println("Sensor value = "+sensorValue);
		return sensorValue;
	}
}