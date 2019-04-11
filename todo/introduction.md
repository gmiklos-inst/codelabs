# ToDo application code lab

Hello everyone! Today we are going to build a ToDo application with the following technology stack:

* React + Instructure UI
* Kotlin + Spring

Our intention is to guide you through all the steps a full-stack developer would go through  from development to operations:

* Creating a small and simple ToDo application using React and JSX.
* Design a REST API for this application.
* Develop a suitable backend for persistence.
* Package and deploy the application to Heroku.

# Pre-flight

Make sure you have the correct tools installed. We would like you to complete these before the lab but if you need assistance we are very happy to help you.

## Front-end

We suggest you to install nvm in order to have a recent version of nodejs. nvm mostly supports Linux and Mac OS. Windows folks can install the binary distribution from the official site and follow instructions after step 2.

There is no hard requirement on the editor you are using so you can pick anything you are comfortable with. 

When in doubt, pick one from this list:

* Atom
* Sublime Text
* WebStorm *
* VS Code *

Choices marked with a star include support for code assistance.

### Installation on Linux/Mac OS

Follow through these steps to have a NodeJS environment ready. We will also use parcel to build our application bundles. 

1. Run the installation script:

	```shell
	curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.34.0/install.sh | bash
	```
	
2. Install the latest stable version of nodejs and make sure it is active.
	```shell
	nvm i stable
	nvm use
	```
	
3. Install the parcel bundler.
	
	```shell
	npm install -g parcel-bundler
	```

### Backend

Any modern Java Development Kit will do that is at least version 8. 

There are plenty of places where you can get one, including:

* [Oracle JDK download page](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [AdoptOpenJDK](https://adoptopenjdk.net/)

If you are on Linux you usually have it packaged already. Debian/Ubuntu distributions usually have a package named ```openjdk-8-jdk``` which should work great.

As for the development environment we have the following suggestions for you:

* IntelliJ IDEA Ultimate (Paid, trial available) or Community (Free, lacks Spring support but works)
* Eclipse STS (Free)

### Heroku

You can register a developer account for free which includes 750 hours for running your application. 	Credit card not required. Free account applications get automatically suspended but that only means they will enter a hibernation-like state until you visit the application again. Thawing from this state usually takes a couple of seconds.