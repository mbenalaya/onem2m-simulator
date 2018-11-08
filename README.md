# oneM2M Devices Simulator

## Getting Started

### Requirements
* Apache Maven 3 to build the project
* JAVA 1.8 to run the project 


### Clone and build from source
Clone the project and go to the folder onem2m-simulator
```sh
$ git clone https://github.com/mbenalaya/onem2m-simulator.git
```
Build the project using the following command
```sh
$ mvn clean install
```
The binary, config, and starting script will be generated under the folder "target"

### Configure the interworking
You can configure the interworking using the file Config/config.ini

#### Target CSE params
```sh
adminAeId = Cae-admin
adminToken=changeme
cseProtocol=https
cseIp=127.0.0.1
csePort = 8443
cseId = server
cseName = server
reset=false
```

#### Device params
```sh
aeDeviceIdPrefix = CAE-device
aeDeviceNamePrefix = ae_device
aeDeviceToken = password
numberOfDevice = 1
deviceSleepPeriodInMs = 1000
containerMaxNumberOfInstances = 10000
```

#### Sensor params
```sh
cntSensorPrefix = sensor
```

#### Actuator params
```sh
cntActuatorPrefix = actuator
aeDeviceProtocol=http
aeDeviceIp = 127.0.0.1
aeDevicePort = 1401
```

### Start the simulator
Execute the following script to start the interworking
```sh
$ ./start.sh
```
