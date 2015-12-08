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

##Cloudmunch SDK Details
###PluginAbstract class
This is a base abstract class that should be extended by any plugin. 
This class, provides methods to read parameters, variables and integrations. 
This class has the following lifecycle methods that the plugin need to invoke,

#####initialize() - 
This method handles the input parameters, variables and integrations  and make these available for plugin.

#####getInputParams() - 
This method returns the list of inputs to the plugin.

#####getVariables() - 
This method returns the list of variables available to the plugin. 

#####getIntegrations() - 
This method returns the list of integrations to the plugin. 

#####process() - 
This is an abstract method to be implemented by every plugin.

#####performAppcompletion() - 
This method handles the completion of a plugin execution.
 
####Here is  the list of helper methods that can be used by plugin,
#####getPc()
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
Step2: Create a file plugin.json with the following contents,

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
Add the following dependency details in pom.xml to download Cloudmunch Java  SDK 
```
  <dependency>
  	<groupId>com.cloudmunch</groupId>
  	<artifactId>CloudMunch-Java-SDK</artifactId>
  	<version>1.0-SNAPSHOT</version>
  </dependency>
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
	PluginLogHandler.logHandler(LogModes.INFO.toString(),
		"Inside Plugin Process "
		);
	PluginLogHandler.logHandler(LogModes.DEBUG.toString(),
		"Recieved Parameters as " + getInputParams());
	PluginLogHandler.logHandler(LogModes.DEBUG.toString(),
		"Recieved Variables as " + getVariables());
	PluginLogHandler.logHandler(LogModes.DEBUG.toString(),
		"Recieved Integrations as " + getIntegrations());
		
	//Getting environment related information from Plugin Context 
	String masterUrl = this.pc.getMasterUrl();
	PluginLogHandler.logHandler(LogModes.INFO.toString(),
		"URL is , " + masterUrl);
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
      JSONObject integrations = getIntegrations();
      String accessKey = integrations.getString("accesskey");
      String processKey = integrations.getString("processkey");
  }
```

##Logging framework

Any event in the plugin has to be logged so that the plugin report will give enough information to the end user. There is a logging framework in the SDK and this has to be used.
Following types of messages are supported,
DEBUG, INFO, WARN and ERROR.
Here is the sample code to output log messages,

```
PluginLogHandler.logHandler("INFO", “Info message”);
PluginLogHandler.logHandler("DEBUG", “Debug message”);
```

##Handling failure or warning scenarios
 The plugin should exit with error on any failure scenarios.To enable this SDK provides a method to exit with error. The format to invoke the event is as below,
 
```
$message = "Error message”;
PluginLogHandler.logHandler("ERROR", $message);
PluginLogHandler.logHandler("WARN", $message);
```

