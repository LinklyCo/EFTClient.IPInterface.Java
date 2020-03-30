# Java IP Interface Set-Up Guide

## Setting Up TestPOS in IntelliJ

Download [IntelliJ Idea](https://www.jetbrains.com/idea/) from Jetbrain's website.

Download [JDK 13](https://www.oracle.com/java/technologies/javase-jdk13-downloads.html)

The project is already set-up to run inside IntelliJ:
- File -> Open...
- SDK.EFTClient.Java
- Open Module Settings

![Modules](README_PICS\intellij.PNG)

- Make sure there is an attached Project SDK
- Build and clean project
- Run with "AsyncMainWindow" as the main class

![AddConfig](README_PICS\configuration.PNG)

Add AsyncMainWindow as a Build Configuration

----

## Building The Project (Demo POS)
https://www.jetbrains.com/help/idea/packaging-a-module-into-a-jar-file.html

```
File -> Project Structure
```

![Building](README_PICS\artifacts.PNG)

Add a JAR with Build modules from dependencies selected. Select the class (the class must contain a Main[] function)
to build. In this case the AsyncMainWindow.class

```
Build -> Build Artifacts...
```

![Building](README_PICS\buildartifact.PNG)

From here the project can be built into an executable .jar file, outputed into out/artifacts/[project-name]/[project-name].jar

## Building The Library (sdk.eftclient.java.jar)

## Problems & Fixes

#### JSON Package Not Recognised

Two external JSON libraries are included in the package:
 - JSON.jar
 - json-simple-1.1.jar
 
JSON Simple is only used by the Test POS to store and retrieve settings such as the cloud token, whilst JSON.jar is used by both the
interface (for basket data) & the Test POS.

![Add External Library](README_PICS\addextlib.PNG)

In project structure, make sure the Libraries are added to the project, if not then add them by selecting the '+' icon in the top left
and manually select the .jar to add. 

## Setting Up IP.Interface.Java in Netbeans

Whilst the demo POS is set up to run inside of IntelliJ IDEA, a lot of Java developers use Netbeans (There's a lot of form differences
that stop the demo forms from being accessible inside Netbeans).

- Download & Install Netbrains 8.2
- JDK download https://www.oracle.com/technetwork/java/javase/downloads/index.html
- JDK 8 or Higher is required to install Netbrains

-----

![netbeans](README_PICS\netbrains.PNG)

- Copy src files (NOT GUI FILES) as package into TestPos project
- Alternatively add the SDK.EFTClient.Java.jar file as an external library
- Make sure TESTPOS project is using JSON.jar (included in .lib folder in repo)
-----