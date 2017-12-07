/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package simdevice;

import java.util.concurrent.CopyOnWriteArrayList;


public class Constants {
	static String adminToken;
	static String adminAeId;
	static String cseProtocol;
	static String cseIp;
	static int csePort;
	static String cseId = "server";
	static String cseName = "server";
	static String aeDeviceIdPrefix;
	static String aeDeviceNamePrefix;
	static int numberOfDevice;
	static int containerMaxNumberOfInstances;
	static String aeDeviceToken;
	static String cntSensorPrefix;
	static int deviceSleepPeriodInMs;
	static String cntActuatorPrefix;
	static String aeDeviceProtocol;
	static String aeDeviceIp;
	static int aeDevicePort;
	static String csePoa;
	static String appPoa;
	public static CopyOnWriteArrayList<Integer> values = new CopyOnWriteArrayList<Integer>();
	static String cntData="DATA";
	static String cntDesc="DESCRIPTOR";
	static boolean reset=false;
}
