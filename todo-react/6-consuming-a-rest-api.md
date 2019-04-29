---
title: Consuming a REST API
parent: React
nav_order: 6
---

# Consuming a REST API

As most applications use some kind of online data source we our to-do application will retrieve and persist our to-do items using a REST API.

Create a file named `app-config.json` in your workspace root with the following contents:

```json
{
  "apiBaseUrl": "https://instructure-rails-workshop.herokuapp.com/",
  "apiKey": "such-secret-much-wow"
}
```

This is where we are going to store all externalized configuration for our application. The `apiBaseUrl` will be used to construct REST API paths and the `apiKey` will be sent with all requests as a header in order to authenticate with our credentials.
