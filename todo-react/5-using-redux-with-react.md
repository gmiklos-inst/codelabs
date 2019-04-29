---
title: Using Redux with React
parent: React
nav_order: 5
---

# Using Redux with React

Now that we have our actions, reducer and store in place we can begin integrating our components with Redux. Start by installing the React bindings for Redux:

```
$ npm i --save react-redux
```

# Connecting the application to the store

In order to make the Redux store available to all our components the following two changes need to be made in `index.tsx`:

* Import the application store and the `<Provider/>` tag: 
```typescript jsx
import {createAppStore} from "./store";
import { Provider } from 'react-redux';
```

* Wrap the application component with the provider tag:
```typescript jsx
ReactDOM.render(<Provider store={createAppStore()}><App /></Provider>, container);
```

The `<Provider />` tag makes the store available to all the components that had been wrapped using the `connect()` function - connecting components is how we make them React-aware.

While you can connect and wrap an arbitrary number of components for the purposes of our simple application we are only going to wrap the main `<App/>` component which will then propagate any changes through properties.