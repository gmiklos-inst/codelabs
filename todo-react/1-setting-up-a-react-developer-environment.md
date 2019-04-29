---
title: Setting up your React project
parent: React
nav_order: 1
---

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
  "main": "app.tsx",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "",
  "license": "ISC"
}


Is this OK? (yes) y
```

Apart from the package name (which equates to your app name) you do not necessarily need to customize anything else.

### Set up access for Instructure NPM registry

Some of the dependencies are available only through the Instructure NPM repository which requires authentication. Visit [https://instructure.jfrog.io](https://instructure.jfrog.io) then click on your username in the upper right corner. Under "Authentication Settings" click "Generate API Key" and copy it to your clipboard as you are going to need for the following command:

```shell
$ npm login --registry=https://instructure.jfrog.io/instructure/api/npm/internal-npm/ --scope=@inst --always-auth
Username: <enter your usernanme>
Password: <enter the API key you have just copied>
Email: (this IS public) <enter your email>
Logged in as gmiklos on https://instructure.jfrog.io/instructure/api/npm/npm-cache.
```

### Install dependencies

There are two types of dependencies we are going to install:

* Development dependencies as the name implies are used during the development process.
* Application dependencies are what your application uses during runtime.

Our development dependencies include:

* Babel - This is a so called "transpiler" that can transform your React JSX/TSX code to its final form that is digestible by your browser - though that is not all that it is capable of - it can also enable you to use newer Javascript versions despite browsers not yet supporting particular language features.
* Typescript Compiler and Types - We are using Typescript in this tutorial which is a superset of Javascript that provides static typing for extra safety. The `@babel/preset-typescript` enables Typescript language support and the `@types/react` contains type definitions for the React library. These definitions are quite useful to have as the vast majority of libraries are written in Javascript without any type definitions. Installing the definitions enables the compiler to very our React usage.
* Jest - Test runner for running our tests. Simple to use, easy to integrate.

All of the dependencies can be installed using the following command:

```
npm install --save-dev @babel/core @babel/plugin-proposal-class-properties @babel/preset-env @babel/preset-react @babel/preset-typescript @types/react @types/react-dom @types/jest typescript jest jest-dom react-testing-library
```

Most developers will use the presets Babel provides which are collections of sane defaults for frequent use cases. The ```env``` preset will let us to use modern Javascript features (ES2015 and up) while the ```react``` preset contains everything for compiling JSX code. `preset-typescript` is for Typescript support as discussed previously. Apart from the presets you can also use additional plugins that enable even more language features - ```plugin-proposal-class-properties``` makes it possible to use static class properties (not required for React to work but we will include it for convenience).

Application dependencies we are going to work with:

* React - World famous component framework by Facebook.
* Bridge UI Components - our in-house open source React components. Includes a lot of ready to use components.

Application dependencies can be installed with the following command:

```
npm install --save react react-dom @inst/bridge-ui-components @inst/bridge-ui-components.buc-utils @inst/bridge-ui-components.format-message @inst/bridge-ui-components.icon @inst/bridge-ui-components.svg-props @inst/bridge-ui-components.text-input
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

    <script src="index.tsx" charset="utf-8"></script> <!-- Parcel will automatically pick up and compile this file. This is where most of our work will be done. -->
  </body>
</html>
```

I suggest putting this in a file called `index.html` in your workspace. Most of these lines are standard stuff that you might be familiar with - significant lines include the container for the application `app-container` where React will render your application and the included JS file `app.tsx` which will contain most our application code. The .tsx extension in this case denotes that it is a Typescript file with React extensions. You are free to customize this template however you wish - these are just the absolute bare-bones we need to get the app running.

### Setting up Babel

Babel needs some additional setup so that it correctly transpiles our source files. Create a file named `.babelrc` in your workspace root with the following contents:

```json
{
  "presets": [
    "@babel/react",
    "@babel/env",
    "@babel/typescript"
  ],
  "plugins": ["@babel/plugin-proposal-class-properties"]
}
```

The `@babel/react` preset makes it possible to use the JS extensions (JSX) provided by React for convenience while developing applications. While its use is not mandatory including it makes it considerably easier to create React elements inline in your code like this:

```jsx
const element = <h1>Hello, world!</h1>;
```

The `@babel/env` preset contains a set of transformations that enable you to use more modern Javascript while developing your appication.

The `@babel/typescript` preset has sane defaults for compiling Typescript code. It also supports React-specific .tsx files.

As mentioned previously, the `class-properties` plugin enables the use of static member methods and fields in classes like this:

```js
class SomeClass {

	static somevar = 1

	static somefun() {}

}
```

This is purely for convenience sake and is not a hard requirement.

### Tweak the Typescript compiler

Just drop a file named `tsconfig.json` in your workspace root:

```json
{
  "compilerOptions": {
    "jsx": "react",
    "noImplicitAny": false,
    "esModuleInterop": true,
    "lib": ["es6", "dom"],
    "outDir": "dist"
  },
  "exclude": [
    "node_modules"
  ]
}
```

Parcel and Babel should pick it up automatically. These settings are here to ensure that we can use the React extensions, do not allow implicit any in  your source code - meaning that you must define types everywhere you are using a variable. The `esModuleInterop` is included for Babel compatibility.

### Create the application component file

The simplest possible component looks like this:

```tsx
import React, { Component } from 'react';

export class App extends Component {
  render() {
    return (
      <div className="app">
        <h1>Hi!</h1>
      </div>
    )
  }
}
```

This file is going to serve as the root component encompassing our entire application.

### Create the application entry point TSX file

Below is the necessary machinery to get React and Bridge UI running. It does not do anything spectacular yet but it should give as a general idea on how we will build our application. I suggest naming this file `index.tsx` and putting it in your workspace root.

```tsx
import ReactDOM from 'react-dom';
import React from 'react';

import { App } from "./app";

const container = document.getElementById('app-container');

ReactDOM.render(<App />, container);
```

### Set up tests

Create a file named `app.test.tsx` in your workspace root:

```tsx
import React from 'react'
import { render } from 'react-testing-library'
import { App } from './App'

describe('App', () => {
  it('renders the greeting', () => {
    const { getByText } = render(<App />);
    getByText('Hi!');
  });
});
```

This is the simplest possible test to ensure that the application loads. If the test framework fails to find the specified text ("Hi!") the test case will fail.

In order to make it easier to run tests we add the following lines to package.json:

```json
{
  "scripts": {
    "test": "jest"
  }
}
```

This enables us to just type `npm test` to run tests.

```
# npm test
> todo@1.0.0 test /Users/gmiklos/Desktop/codelabs/todo-react/chkpoint1-setting-up-a-react-dev-environment
> jest

 PASS  ./app.test.tsx
  App
    ✓ renders the greeting (14ms)

Test Suites: 1 passed, 1 total
Tests:       1 passed, 1 total
Snapshots:   0 total
Time:        1.036s, estimated 2s
Ran all test suites.
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
