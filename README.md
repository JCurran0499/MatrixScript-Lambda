## MatrixScript Backend
#### MatrixScript is a simple and straightforward interpreted command language for calculating linear algebra problems

This version of MatrixScript is written in Java (here) and serves as an API, fronted by a [Node.js React web layer](https://github.com/JCurran0499/MatrixScript-Frontend) found in another repository.<br/>
Each command is sent to the API with its corresponding token (assigned to users), and the API responds with the output.
This allows for continuous communication with the frontend.
<br/>
<br/>
**Note:** While MatrixScript is designed to run as an API with a separate frontend, this version can also be run as a **Command Line tool**.
This way, you can run MatrixScript commands directly on your command line.
<br/>
<br/>
⭐ The **official MatrixScript documentation** can be found [here](https://github.com/JCurran0499/MatrixScript-Docs/blob/main/docs.md).

### Frontend and Backend Servers

This project is designed to be run with a separate frontend and backend. This repository represents the **backend**, and below are instructions for setting up and running the backend server. 
The frontend can be found [here](https://github.com/JCurran0499/MatrixScript-Frontend).<br/>

### Download and Installation

The tools provided to you in this repository are based on Linux OS distributions. 
This can include Amazon Linux, Red Hat, Debian, and more. 
For other servers and virtual machines, the commands may be slightly different. <br/>
<br/>

### Backend Setup
#### Requirements
- OS: ``Linux`` (preferred) or ```MacOS```
- Java: ```OpenJDK 17```
- Maven: ```Maven 3.9```
<br/><br/><br/>

#### Download Project Repo
```
git clone https://github.com/JCurran0499/MatrixScript-Backend.git
cd MatrixScript-Backend
```
<br/>

#### Frontend CORS Permissions
If desired, edit the ```.env``` file, and fill in your frontend server's IP address or domain in the `FRONTEND` value. 
This way, your web app backend will be able send CORS permissions to your frontend. 
By default, this value is set to ```http://localhost``` and therefore allows your local frontend on HTTP port 80 to access the backend.
<br/><br/>
This step is optional, and is only used for security purposes to restrict backend access to your frontend domain alone.
<br/>
This is not needed if you are running MatrixScript from the command line, as there is no corresponding frontend.
<br/>
<br/>

#### Build the app
This is an optional manual step, as it can be covered by the run script below.
```
mvn clean package
```
<br/>

#### Run the app
This script will offer you multiple options, simply select the one that is best for you.
```
./run.sh
```

From here, the MatrixScript API can be accessed on port 4567, at `<server_domain_or_ip>:4567`.
<br/><br/>

### Commands for Windows
Using the ```run.sh``` script will likely be difficult if running on Windows.
Below are similar commands to be used to get around this limitation.
<br/>
All of these commands are to be run from the root folder.
<br/>

#### Build app
```mvn clean package```
<br/>
<br/>

#### Run API Locally
```java -jar target/MatrixScript-1.0-jar-with-dependencies.jar```
<br/>
<br/>

#### Run Command Line Locally
```java -jar target/MatrixScript-1.0-jar-with-dependencies.jar run```
<br/>
<br/>

#### Run Full Test Suite
```mvn test```
<br/>
<br/>