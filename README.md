# PROCESSUS DE DEVELOPPEMENT LOGICIEL AUTOMATISE / CONDUITE DE PROJET

## Overview
This project implements a Benevole App. This app allows:

- Requesting missions and post them on you user page.
- Giving your help by accpeting missions requested by others, on you benevole friendly page.
- Moderate missions requested by users.
- Give your review of the app :)

The project is built using Maven, making it easy to compile and run on any machine.

## Prerequisites
- **Java**: Ensure you have Java 8 or later installed.
- **Maven**: Ensure Maven is installed and configured.

## Compilation and Running Instructions
### Step 1: Clone the Repository

```
https://github.com/toumanydby/PDLA-CP.git
cd PDLA-CP/
```

### Step 2: Package the Project
Create an executable JAR file with dependencies:

`mvn clean package`

The packaged JAR file will be located in the **target/** directory, named ***pdla-cp-1.0-SNAPSHOT-jar-with-dependencies.jar***

### Step 3: Run the Demonstration Application

Run the demonstration application from the command line:

`java -jar target/pdla-cp-1.0-SNAPSHOT-jar-with-dependencies.jar`

Make sure to run the jar with dependencies, or the app will not run !!
