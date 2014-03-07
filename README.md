jvfsftpd
========

JVFSFTPd is highly pluggable FTP server based on jakarta VFS project and JAAS writen on JAVA language . JVFSFTPd may be configured whatever you wish.

Copy of SF.NET project. Updates for this projects will be hosted only on GitHub

Example of ftpd.xml configuration file:

```xml
<ftpd>
	<!-- Starts FTP server on all interfaces-->
	<bind>127.0.0.1</bind>
	<!-- Starts FTP server on 21 tcp port-->
	<port>21</port>
	<!-- Uncomment if you wish to use JAAS as autorisation service-->
	<!--<security-domain>other</security-domain>-->
	<!-- True - if set of 'user' elements defines userspace. In this case new JAAS configuration defined.-->
	<use-users>true</use-users>
	<!-- Let's define default configuration for FTP server-->
	<default>
		<!-- Base dir in this example is C:/ -->
		<basedir>c:/</basedir>
		<!-- Initialy user will be placed in C:/windows directory-->
		<initialdir>c:/windows</initialdir>
		<!-- Bandwidth configuration -->
		<bandwidth>
		      <!-- Limits input rate by 10000 bytes per second-->
			<input>10000</input>
			<!-- Limits output rate by 10000 bytes per second-->
			<output>10000</output>
		</bandwidth>
	</default>
	<!-- Defines user with name 'jvfsftpd' and password '123'-->
	<user name="jvfsftpd" password="123">
		<!-- Base dir for user jvfsftpd will be c:/ -->
		<basedir>c:/</basedir>
		<!-- Initial dir for user jvfsftpd will be c:/Program Files-->
		<initialdir>c:/Program Files</initialdir>
		<!-- Bandwidth configuration for user jvfsftpd -->
		<bandwidth>
			<!-- Limits input rate for user jvfsftpd by 20000 bytes per second-->
			<input>100000</input>
			<!-- Limits output rate for user jvfsftpd by 20000 bytes per second-->
			<output>100000</output>
		</bandwidth>
	</user>
</ftpd>
```
