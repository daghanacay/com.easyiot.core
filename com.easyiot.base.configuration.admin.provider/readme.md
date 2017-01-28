# 

${Bundle-Description}

# Usage

createDeviceOrProtocolInstance test.factory.pid [id=firstTry]
getRegisteredDevicesOrProtocolsMetaData test.factory.pid
getDeviceOrProtocolInstances test.factory.pid
getDeviceOrProtocolInstancePropertiesMeta test.factory.pid.0c426645-643b-4629-9838-bc2de48c8bd7

## CLI commands

getRegisteredDevicesOrProtocols: returns all the easyiot configurations.
Usage: g! getRegisteredDevicesAndProtocols

getRegisteredDevicesOrProtocolsMetaData: return Metatype of a configuration
Usage: g! getRegisteredDevicesOrProtocolsMetaData test.factory.pid

getDeviceOrProtocolInstances: Get the existing instances that have been created through configuration
Usage: g! getDeviceOrProtocolInstances test.factory.pid

getDeviceOrProtocolInstanceProperties: get the properties of device on protocol that are created through configuration.
Usage: g! getDeviceOrProtocolInstanceProperties test.factory.pid.a9d13487-83da-45d1-a282-aa76515edfa8

getDeviceOrProtocolInstancePropertiesMeta: get the properties of device on protocol with the metadata.
Usage: g! getDeviceOrProtocolInstancePropertiesMeta test.factory.pid.a9d13487-83da-45d1-a282-aa76515edfa8

createDeviceOrProtocolInstance: Creates an instance of a device or protocol
Usage: createDeviceOrProtocolInstance test.factory.pid [prop1=1 prop2=test]

updateDeviceOrProtocolInstanceProperties: updates an existing device or protocol property.
Usage: g! updateDeviceOrProtocolInstanceProperties test.factory.pid [prop1=3 prop2=test pro3=3]

deleteDeviceOrProtocolInstance: Deletes an existing protocol or device.
Usage: g! deleteDeviceOrProtocolInstance test.factory.pid.6c177294-0ed3-41c5-b4a3-1b76844838cc

## Rest Access

getRegisteredDevicesOrProtocols: returns all the easyiot configurations.
Usage: GET http://localhost:8080/rest/RegisteredDevicesOrProtocols

getRegisteredDevicesOrProtocolsMetaData: return Metatype of a configuration
Usage: g! GET http://localhost:8080/rest/RegisteredDevicesOrProtocolsMetaData/{factory.pid}

getDeviceOrProtocolInstances: Get the existing instances that have been created through configuration
Usage: GET http://localhost:8080/rest/DeviceOrProtocolInstances/{factory.pid}

getDeviceOrProtocolInstanceProperties: get the properties of device on protocol that are created through configuration.
Usage: http://localhost:8080/rest/DeviceOrProtocolInstanceProperties/{pid}

getDeviceOrProtocolInstancePropertiesMeta: get the properties of device on protocol with the metadata.
Usage: g! http://localhost:8080/rest/DeviceOrProtocolInstancePropertiesMeta/{pid}

createDeviceOrProtocolInstance: Creates an instance of a device or protocol
Usage: POST http://localhost:8080/rest/DeviceOrProtocolInstance/{factory.pid} 
Body {“prop1”:”1”, “prop2”:”test”}

updateDeviceOrProtocolInstanceProperties: updates an existing device or protocol property.
Usage PUT http://localhost:8080/rest/DeviceOrProtocolInstanceProperties/{pid}
Body {"prop1":"Sam3","prop1":"SAM3"}

deleteDeviceOrProtocolInstance: Deletes an existing protocol or device.
Usage: DELETE http://localhost:8080/rest/DeviceOrProtocolInstance/{pid}

