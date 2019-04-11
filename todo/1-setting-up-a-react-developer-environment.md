# Setting up your React project

In order to simplify things we are going to use the Parcel bundler for compiling, bundling our source code. While Webpack, Rollup and others are very popular choices for such tasks Parcel is unique in a way that it barely needs any configuration for getting up and running.

## Getting the basics right

### Start your project

Designate a directory as a workspace for your project. This directory can be anywhere you are comfortable with - no restrictions apply.

In order to be able to properly use npm - which is the package manager for Node - you need to initialize your project as such. Starting by issuing the command ```npm init``` in your workspace directory:

```shell
# npm init
This utility will walk you through creating a package.json file.
It only covers the most common items, and tries to guess sensible defaults.

See `npm help json` for definitive documentation on these fields
and exactly what they do.

Use `npm install <pkg>` afterwards to install a package and
save it as a dependency in the package.json file.

Press ^C at any time to quit.
package name: (todo) todo
version: (1.0.0)
description:
entry point: (app.js)
test command:
git repository:
keywords:
author:
license: (ISC)
About to write to package.json:

{
  "name": "todo",
  "version": "1.0.0",
  "description": "",
  "main": "app.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "",
  "license": "ISC"
}


Is this OK? (yes) y
```

Apart from the package name (which equates to your app name) you do not necessarily need to customize anything else.

### Install dependencies

There are two types of dependencies we are going to install:

* Development dependencies as the name implies are using during our development process. The absolute minimum 
* Application dependencies are what your application uses during runtime.

Our development dependencies include:

* Babel - This is a so called "transpiler" that can transform your React JSX code to its final form that is digestible by your browser - though that is not all that it is capable of - it can also enable you to use newer Javascript versions despite browsers not yet supporting particular language features. 
* SASS - Mature and stable CSS extension language. While not necessarily required it makes using CSS a more comfortable experience.

All of the dependencies can be installed using the following command:

```
npm install --save-dev @babel/core @babel/plugin-proposal-class-properties @babel/preset-env @babel/preset-react sass
```

Most developers will go with one of the Babel presets available. The ```env``` preset will let us to use modern Javascript features (ES2015 and up) while the ```react``` preset contains everything for compiling JSX code. Apart from the presets you can also use additional plugins that enable even more language features - ```plugin-proposal-class-properties``` makes it possible to use static class properties (not required for React to work but we will include it for convenience).

Application dependencies we are going to work with:

* React - World famous component framework by Facebook.
* Instructure UI - our in-house open source React components. Includes a lot of components created by the Canvas folks. 

Application dependencies can be installed with the following command:

```
npm install --save @instructure/ui-elements @instructure/ui-layout @instructure/ui-themes react react-dom
```

Both commands should go smoothly without any errors. Now that you got your the correct dependencies we can begin developing our application.

### Creating a basic HTML page for the application

The following is the HTML code for the entry point for our application:

```html
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Todo application</title>
  </head>
  <body>
    <div id="app-container"></div> <!-- This is the container that React uses to render our application -->

    <script src="app.js" charset="utf-8"></script> <!-- Parcel will automatically pick up and compile this file. This is where most of our work will be done. -->
  </body>
</html>
```

I suggest putting this in a file called ```index.html``` in your workspace. Most of these lines are standard stuff that you might be familiar with - significant lines include the container for the application ```app-container``` where React will render your application and the included JS file ```app.js``` which will contain most our application code. You are free to customize this template however you wish - these are just the absolute bare-bones we need to get the app running.

### Create styles SASS file

SASS is very similar to CSS as it is a superset of the former. We do not need anything fancy for now - just create an empty file named `styles.scss` in your workspace root.

### Create the application entry point JS file

Below is the necessary machinery to get React and Instructure UI running. It does not do anything spectacular yet but it should give as a general idea on how we will build our application. I suggest naming this file `app.js` and putting it in your workspace root.

```javascript
import './styles.scss' // parcel will automatically pick up, compile and include this file

// Import key React components
import React, { Component } from 'react'
import ReactDOM from 'react-dom'

// Apply the base theme shipping with Instructure UI
import theme from '@instructure/ui-themes/lib/canvas/base'
theme.use()

// This is our root component that will get rendered first and bring in all the other components
class App extends Component {
  render() {
    return (
      <div className="app">
      	<h1>Hi!</h1>
      </div>
    )
  }
}

// Find the container...
const $container = document.getElementById('app-container');

// ... and begin rendering our app
ReactDOM.render(React.createElement(App), $container);
```

### Set up the development server

We are going to use development server included in Parcel that compiles and hot-reloads our code. 

Issue the following command:

```shell
# parcel index.html
Server running at http://localhost:1234
✨  Built in 334ms.
```

First time compilation can be somewhat slow but subsequent iterations will be much faster. By default the development server listens at port 1234. You should visit [http://localhost:1234](http://localhost:1234) with your browser and verify that the welcome message is displayed. There also should be zero errors in the developer console (More Tools > Developer Tools in Chrome). 

Any modification in the code will trigger compilation and reload the page in your browser. Your environment is all set for development!