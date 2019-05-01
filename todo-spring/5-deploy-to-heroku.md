---
title: Deploy to Heroku
parent: Spring Boot & Kotlin
nav_order: 6
---

# Deploy to Heroku

## Steps based on [Heroku documentation](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku#preparing-a-spring-boot-app-for-heroku)
Follow the steps below:
```bash
$ cd {root-of-spring-project}
$ heroku login
$ heroku create
$ heroku addons:create heroku-postgresql --app {your-app-name}
```

Create an `application-prod.properties` file next to the `application.properties` file with the following content:
```bash
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

Create a Procfile in the root:
```bash
web: java -Dserver.port=$PORT $JAVA_OPTS -jar build/libs/codelabs-0.0.1-SNAPSHOT.jar --spring.profiles=prod
```

And finally:
```bash
$ git push heroku master
```