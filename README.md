# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* This repository contains the server-side components of the _GNSS Jammer Locator_ distributed system suite
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration `mvn -D`
* Dependencies `mvn depends-tree-or-something`.  When rebuilding with new dependencies and prior to deployment, one should execute `mvn dependency:build-classpath -DincludeScope=runtime` and use that to set the classpath in the relevant start script.
* Database configuration
* How to run tests:  `mvn test` 
* Deployment instructions:
  * Run the start script to to start the RabbitMQ instance with 
  * Run the start scripts to start the Device Service `start-device-server.sh`
  * Run the start scripts to start the Observation Service `start-observation-server.sh`
  * Run the start scripts to start the Locator Service `start-locator-server.sh`



### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* jeremy.reeve@opengpssignal.com