# CloudMunch-SDK-Java
CloudMunch SDK for Java provides helper classes for CloudMunch plugin development.

###Download SDK
We recommend using [Maven](https://maven.apache.org "Maven") as package manager. All you need to install the sdk is to have the following entry in your pom.xml file.

```xml
<dependency>
	<groupId>com.cloudmunch</groupId>
	<artifactId>CloudMunch-Java-SDK</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```
Above SDK is in the following repository, so add the following entry under repositories. 
```xml
	<!-- Repository to Download Cloudmunch SDK Jar -->
	<repository>
		<id>ossrh</id>
		<url>https://oss.sonatype.org/content/repositories/snapshots</url>
	</repository>
```
Under build section, Set the maven project output directory to "temp" instead of "target" as later will be used for CM Plugin Structure

```xml
	<directory>temp</directory>
```

Also add, 
- Maven Clean Plugin to clear the previous data of "temp" and "target" folders
- Maven assembly plugin to package plugin along with SDK dependencies. 
- Maven Resources plugin to copy plugin outputs to "temp" directory


```xml
	<plugins>
		<plugin>
				<artifactId>maven-clean-plugin</artifactId>
				<configuration>
					<filesets>
						<fileset>
							<directory>target</directory>
						</fileset>
						<fileset>
							<directory>temp</directory>
						</fileset>
					</filesets>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-resources-plugin</artifactId>
				<version>2.6</version>
				<executions>
					<execution>
						<id>copy-resources1</id>
						<phase>package</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/target/${artifactId}/bin</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}/temp</directory>
									<includes>
										<include>*.jar</include>
									</includes>
								</resource>
							</resources>
						</configuration>
					</execution>
					<execution>
						<id>copy-plugin-json</id>
						<phase>initialize</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/target/${artifactId}/</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}</directory>
									<includes>
										<include>plugin.json</include>
									</includes>
								</resource>
							</resources>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>single</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<descriptorRefs>
						<descriptorRef>jar-with-dependencies</descriptorRef>
					</descriptorRefs>
				</configuration>
			</plugin>

		</plugins>

```

##Cloudmunch SDK Details
###PluginAbstract class
This is a base abstract class that should be extended by any plugin. 
This class, provides methods to read parameters, variables and integrations. 

This class has the following lifecycle methods that the plugin need to invoke,

#####initialize(String[] args) - 
This method handles the input parameters, variables and integrations  and make these available for plugin.

#####getInputParams() - 
This method returns the list of inputs to the plugin.

#####getVariables() - 
This method returns the list of variables available to the plugin. 

#####getIntegrationDetails() - 
This method returns the details of integration to the plugin. 

#####process() - 
This is an abstract method to be implemented by every plugin.

#####performAppcompletion() - 
This method handles the completion of a plugin execution.

#####outputPipelineVariables(variablename, variablevalue) - 
Plugin uses this method to output the variables to pipeline to use in the following steps/plugins. Syntax to use that variable in further steps/plugins is `{variablename}`. 

Ex: `outputPipelineVariables(“commit_comment”, “Fixed issue with logging”);`

Usage of this variable in pipeline is `{commit_comment}`
 
####Here is  the list of helper methods that can be used by plugin,
#####getPluginContext()
This method returns PluginContext object for this runtime.




###PluginContext class
Plugins can get the context or environment information from this class.
Here is the list of methods available,
#####getWorkSpaceLocation() - 
This methods returns the absolute path to the workspace of the job.

#####getApplication() - 
This method returns the Application name in which the plugin is being executed.
#####getPipeline() - 
This method returns the Pipeline name in which the plugin is being executed.
#####getDomain() - 
This method returns the domain in which plugin is being executed.
 

##Sample Plugin     
 
Let us look at a sample plugin that prints "Hello "+ "string passed at runtime" on execution.
Plugin name : Hello
Input: helloname: This need to be printed out with Hello.
 
Step1: Create a Maven Project "Hello"
Step2: Create a file plugin.json with the following contents, "id" should be always same as Maven Artifact ID

```
{
  "id": "Hello",
  "name": "Hello,
  "author": "rosmi@cloudmunch.com",
  "version": "1",
  "status": "enabled",
  "tags": ["Hello","Sample"],
  "inputs": {
    "name": {
      "type": "text",
        "mandatory": "yes",
      "display": "yes", 
      "label": "Name",
      "hint": "Give the name to be printed"    
    }
  },
  "outputs": {
    "param1": {
      "name": "",
      "format": ""
    }
  },
  "execute": {
    "main": "com.cloudmunch.HelloDisplay",
    "language": "JAVA",
    "options": "./bin/Hello-0.0.1-jar-with-dependencies.jar"
  },
  "documentation": {
    "description": "Prints Hello"
  },
  "_created_by": "rosmi@cloudmunch.com",
  "_create_time": "2015-10-01 06:23:25.0474",
  "_updated_by": "rosmi@cloudmunch.com",
  "_update_time": ""
}
 ```
 
Step 3:
Create pom.xml for the plugin with the artifactId as "id" given in plugin.json. Refer the following sample pom.xml 
```xml
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>Cloudmunch</groupId>
	<artifactId>HelloDisplay</artifactId>
	<version>0.0.1</version>

	<dependencies>
		<dependency>
			<groupId>com.cloudmunch</groupId>
			<artifactId>CloudMunch-Java-SDK</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
	</dependencies>
	<build>
		<directory>temp</directory>
		<plugins>
			<plugin>
				<artifactId>maven-clean-plugin</artifactId>
				<configuration>
					<filesets>
						<fileset>
							<directory>target</directory>
						</fileset>
						<fileset>
							<directory>temp</directory>
						</fileset>
					</filesets>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-resources-plugin</artifactId>
				<version>2.6</version>
				<executions>
					<execution>
						<id>copy-resources1</id>
						<phase>package</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/target/${artifactId}/bin</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}/temp</directory>
									<includes>
										<include>*.jar</include>
									</includes>
								</resource>
							</resources>
						</configuration>
					</execution>
					<execution>
						<id>copy-plugin-json</id>
						<phase>initialize</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/target/${artifactId}/</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}</directory>
									<includes>
										<include>plugin.json</include>
									</includes>
								</resource>
							</resources>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>single</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<descriptorRefs>
						<descriptorRef>jar-with-dependencies</descriptorRef>
					</descriptorRefs>
				</configuration>
			</plugin>
	</build>
	<repositories>
		<!-- Repository to Download Cloudmunch SDK Jar -->
		<repository>
			<id>ossrh</id>
			<url>https://oss.sonatype.org/content/repositories/snapshots</url>
		</repository>
	</repositories>
</project>
``` 



Step 4:
Create a folder 'src/main/java'. and package as com.cloudmunch. 
Create a java class file , HelloDisplay.java.
This file will have all the life cycle methods to control the plugin execution. Add main method as follows.
```
  public static void main(String[] args) {
    HelloDisplayClass helloDisplay = new HelloDisplayClass();
    helloDisplay.initialize(args);
    helloDisplay.process();
    helloDisplay.performAppcompletion();
  }
```

Step 5:
Create a file HelloDisplayClass.java

```
public class HelloDisplayClass extends PluginAbstract {

    @Override
    public void process() {
    PluginLogHandler pluginLogger = getLogHandler();
	pluginLogger.logHandler(LogModes.INFO.toString(),
		"Inside Plugin Process "
		);
	pluginLogger.logHandler(LogModes.DEBUG.toString(),
		"Recieved Parameters as " + getInputParams());
	pluginLogger.logHandler(LogModes.DEBUG.toString(),
		"Recieved Variables as " + getVariables());
	pluginLogger.logHandler(LogModes.DEBUG.toString(),
		"Recieved Integrations as " + getIntegrationDetails());
		
	//Getting environment related information from Plugin Context 
	String masterUrl = this.pc.getMasterUrl();
	pluginLogger.logHandler(LogModes.INFO.toString(),
		"URL is , " + masterUrl);
		
	JSONObject integrations = getIntegrationDetails();
	String url = integrations.getString("jenkins_url");
	pluginLogger.logHandler(LogModes.INFO.toString(), url);
    }
     
     //Getting input parameters   
    String name = getInputParams().getString("name");
	PluginLogHandler.logHandler(LogModes.INFO.toString(),
		"HELLO, " + name);
  
}

```


##Using Integrations

Cloudmunch has an integration framework using which plugins can interact with any external service providers.
For example if the plugin need to interact with Amazon web services, the account can be registered in cloudmunch integrations page.
This integration will now be available for the plugin at runtime. The details can be retrieved in the plugin with the following code sample,

 ```
  @Override
  public void process() {
      JSONObject integrations = getIntegrationDetails();
      String jenkinsUrl = integrations.getString("jenkins_url");
  }
```

##Logging framework

Any event in the plugin has to be logged so that the plugin report will give enough information to the end user. There is a logging framework in the SDK and this has to be used.
Following types of messages are supported,
DEBUG, INFO, WARN and ERROR.
Here is the sample code to output log messages,

```
PluginLogHandler pluginLogger = getLogHandler();
pluginLogger.logHandler("INFO", “Info message”);
pluginLogger.logHandler("DEBUG", “Debug message”);
```

##Handling failure or warning scenarios
 The plugin should exit with error on any failure scenarios.To enable this SDK provides a method to exit with error. The format to invoke the event is as below,
 
```
$message = "Error message”;
PluginLogHandler pluginLogger = getLogHandler();
pluginLogger.logHandler("ERROR", $message);
pluginLogger.logHandler("WARN", $message);
```

## Creating Plugin Deliverable
Run goal `clean package` on this maven project to create deliverables. On successful completion of running the package goal, following deliverables will be created. 
 - target
 	- bin 
		 - HelloDisplay-0.0.1.jar
		 - HelloDisplay-0.0.1-jar-with-dependencies.jar
- 	plugin.json
		 
HelloDisplay-0.0.1-jar-with-dependencies.jar is the one with dependencies, and this target folder will be your Plugin Deliverable.  
 
