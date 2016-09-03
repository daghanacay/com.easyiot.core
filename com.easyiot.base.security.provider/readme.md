# 

${Bundle-Description}

## Example

## Configuration

	Pid: com.easyiot.security
	
	Field					Type				Description
		
	
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
