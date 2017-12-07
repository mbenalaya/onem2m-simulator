/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package adnsim;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Actuators {
	 
	static HttpServer server;
	
	public static void start(){
		try {
			server = HttpServer.create(new InetSocketAddress(Constants.aeDevicePort), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		
	
		
		for(int i = 0;i<Constants.numberOfDevice;i++){
			if(i!=41 && i!=53 && i!=85){

			final String aeName = Constants.aeDeviceNamePrefix+i;
			//final String aeid = Constants.aeDeviceIdPrefix+i;	
			final String aeid = Constants.adminAeId;	
			final int index = i;
			Constants.values.add(0);
			new Thread(){
				public void run(){
					
						JSONObject obj = new JSONObject();
					    obj.put("rn", Constants.cntActuatorPrefix);
						obj.put("mni", Constants.containerMaxNumberOfInstances);
						JSONObject resource = new JSONObject();
					    resource.put("m2m:cnt", obj);
						RestHttpClient.post(aeid, Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName, resource.toString(), 3);
						
//						obj = new JSONObject();
//					    obj.put("rn", Constants.cntDesc);
//						obj.put("mni", Constants.containerMaxNumberOfInstances);
//						resource = new JSONObject();
//					    resource.put("m2m:cnt", obj);
//						RestHttpClient.post(aeid, Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName+"/"+Constants.cntActuatorPrefix, resource.toString(), 3);
//						
//						obj = new JSONObject();
//						obj.put("cnf", "application/xml");
//						obj.put("con", "<obj><str name=\"type\" val=\""+Constants.cntActuatorPrefix+"\"/>"
//								+ "<op name=\"SwitchON\" href=\"/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName+"?"+index+"=1\" is=\"execute\"/>"
//								+ "<op name=\"SwitchOFF\" href=\"/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName+"?"+index+"=0\" is=\"execute\"/>"
//										+ "</obj>");
//						resource = new JSONObject();
//						resource.put("m2m:cin", obj);
//						RestHttpClient.post(aeid, Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName+"/"+Constants.cntActuatorPrefix+"/"+Constants.cntDesc, resource.toString(), 4);
//						
						
						obj = new JSONObject();
					    obj.put("rn", Constants.cntData);
						obj.put("mni", Constants.containerMaxNumberOfInstances);
						JSONArray lbl = new JSONArray();
						lbl.put("data");			
						obj.put("lbl", lbl);
						resource = new JSONObject();
					    resource.put("m2m:cnt", obj);
						RestHttpClient.post(aeid,Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName+"/"+Constants.cntActuatorPrefix, resource.toString(), 3);
								
//						obj = new JSONObject();
//						obj.put("cnf", "application/xml");
//						obj.put("con", "<obj><str name=\"type\" val=\""+Constants.cntActuatorPrefix+"\"/><str name=\"id\" val=\""+aeName+"\"/><int name=\"value\" val=\""+Constants.values.get(index)+"\"/></obj>");					
						
						obj = new JSONObject();
						obj.put("cnf", "application/json");
					    JSONObject con = new JSONObject();
					    con.put("myActuatorType", Constants.cntActuatorPrefix);
					    con.put("myActuatorId", aeName);
					    con.put("myActuatorData", Constants.values.get(index));
						obj.put("con", con.toString());
						
						resource = new JSONObject();
						resource.put("m2m:cin", obj);
						RestHttpClient.post(aeid, Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+aeName+"/"+Constants.cntActuatorPrefix+"/"+Constants.cntData, resource.toString(), 4);
				}
			}.start();
			}
		}
	}
	
	static class MyHandler implements HttpHandler {
		JSONObject obj;
		JSONObject resource;
		String index;
		String value;
		public void handle(HttpExchange httpExchange){
			System.out.println("Event Recieved!");
			System.out.println("URI: "+httpExchange.getRequestURI());
			index= httpExchange.getRequestURI().getQuery().split("=")[0];
			System.out.println("Device: "+index);
			value= httpExchange.getRequestURI().getQuery().split("=")[1];
			System.out.println("Value: "+value);
			Constants.values.set(Integer.parseInt(index), Integer.parseInt(value));
			
//			obj = new JSONObject();
//			obj.put("cnf", "application/xml");
//			obj.put("con", "<obj><str name=\"type\" val=\""+Constants.cntActuatorPrefix+"\"/><str name=\"id\" val=\""+Constants.aeDeviceIdPrefix+index+"\"/><int name=\"value\" val=\""+value+"\"/></obj>");
		
			obj = new JSONObject();
			obj.put("cnf", "application/json");
		    JSONObject con = new JSONObject();
		    con.put("myActuatorType", Constants.cntActuatorPrefix);
		    con.put("myActuatorId", Constants.aeDeviceIdPrefix+index);
		    con.put("myActuatorData", value);
			obj.put("con", con.toString());
			
			resource = new JSONObject();
			resource.put("m2m:cin", obj);
			RestHttpClient.post(Constants.aeDeviceIdPrefix+index,Constants.aeDeviceToken,Constants.csePoa+"/~/"+Constants.cseId+"/"+Constants.cseName+"/"+Constants.aeDeviceNamePrefix+index+"/actuator/DATA", resource.toString(), 4);
			
				try {
					String responseBody ="";
					byte[] out = responseBody.getBytes("UTF-8");
					httpExchange.sendResponseHeaders(200, out.length);
					OutputStream os = httpExchange.getResponseBody();
					os.write(out);
					os.close();	
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		}
	}	
}