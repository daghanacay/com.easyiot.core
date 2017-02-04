# 

${Bundle-Description}

## Example

## Configuration

	Pid: com.easyiot.security
	
	{
		"service.pid":"com.easyiot.security",
		"authenticationType":"CONFIGURATION",
		"users" : [
			{"name":"daghan","password":"daghan"},
			{"name":"pinar","password":"pinar"}
		],
		"groups": [
			{"name":"admin","user_names":["daghan"]},
			{"name":"authenticated","user_names":["daghan","pinar"]}
		],
		"permissions": [
			{"name":"readDevice","group_names":["admin","authenticated"]},
			{"name":"writeDevice","group_names":["admin"]}
		]
	}
	
	
## Enable HTTPS in all application
 
	Field					Type				Description
	
	[
	  {
	     "service.pid":"org.apache.felix.http",
	     "org.apache.felix.http.enable":"false",
	     "org.osgi.service.http.port":"8080",
	     "org.apache.felix.https.enable":"true",
	     "org.osgi.service.http.port.secure":"8433",
	     "org.apache.felix.https.keystore":"configuration/ssl/keystore.jks",
	     "org.apache.felix.https.keystore.password":"da_iot",
	     "org.apache.felix.https.keystore.key.password":"da_iot",
	     "org.apache.felix.https.truststore":"configuration/ssl/cacerts.jks",
	     "org.apache.felix.https.truststore.type":"jks"
	  }
    ]	
	
## References
Creating ssl certificate and sign it

http://docs.oracle.com/javaee/5/tutorial/doc/bnbxw.html

1- creates a file called keystore.jks
keytool -genkey -alias server-alias -keyalg RSA -keypass da_iot -storepass da_iot -keystore keystore.jks
make sure the user name is the domain e.g.
	What is your first and last name?
	  Unknown:  localhost

2- export keystore.jks to a certiciate and creates server.cer
keytool -export -alias server-alias -storepass da_iot -file server.cer -keystore keystore.jks

3- Puts the certificate server.cer into truststore called cacerts.jks
keytool -import -v -trustcacerts -alias server-alias -file server.cer -keystore cacerts.jks -keypass da_iot -storepass da_iot

#Roadmap

1- Integrate with other role providers e.g. 
a- Text -> 
Reverse tree notation as in AuthenticationGroup is an AdminGroup also is an DeviceGroup
		(AuthenticatedGroup (AdminGroup), (DeviceGroup))
DaghanUser is a member of AdminGroup
        (DaghanUser AdminGroup)

b- DB
c- LDAP, etc.

2- Provide a configuration for HTTPS, see DeviceContextHelper
3- Provide a whiteboard pattern to integrate other filters to be integrated, see DeviceFilter   
4- Create configuration outside the package and read from system folder.