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
	