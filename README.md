
# README #

### What is this repository for? ###

* This repository contains the server-side components of the __iGrow Systems__ suite
* Current Version:  0.0.2
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### Architecture ###

![Architecture Diagram](arch.jpg "Architecture Diagram")


### How do I get set up? ###

* Install system packages or rather dependencies that are not managed by Maven.

    Describes system configuration for a Ubuntu 16.04 system

        apt install postgresql postgresql-client postgresql-contrib postgis libpostgis-java git maven openjdk-8-jdk rabbitmq-server protobuf-complier

* RabbitMQ configuration

    As user rabbitmq,

        rabbitmqctl add_user igrow-service-dev igrow-service-dev
        rabbitmqctl set_permissions -p / igrow-service-dev ".*" ".*" ".*"

* Database configuration:

        createuser --no-superuser --no-createdb --no-createrole --pwprompt igrow-service-dev

        createdb --owner igrow-service-dev igrow-service-dev

        psql -d igrow-service-dev

        CREATE EXTENSION postgis;
        CREATE EXTENSION postgis_topology;

    If starting on boot is required, this can be enabled with:

        sudo update-rc.d postgresql enable

* Initialise the database

        cd observation-service
        psql -U igrow-service-dev -h localhost < src/main/resources/initdb.sql

        cd ../device-service
        psql -U igrow-service-dev -h localhost < src/main/resources/initdb.sql


* Build the services

        mvn package install

        mvn clean compile assembly:single


* To run individual services

        mvn -DskipTests=true -Djersey.test.port=9998 -Dcom.igrow.gjl.observice.debug=true -e exec:java


* Configure Eclipse and the dev environment

        mvn eclipse:clean
        mvn eclipse:eclipse

    Set `M2_REPO` classpath variable:

        mvn -Declipse.workspace="igrow-service-eclipse" eclipse:configure-workspace


* Dependencies:
    `mvn depends-tree-or-something`.  When rebuilding with new dependencies and prior to deployment, one should execute `mvn dependency:build-classpath -DincludeScope=runtime` and use that to set the classpath in the relevant start script.  FIXME properly.


* How to run tests:  `mvn test` 

    Code run under JRE 1.8, which is true for these instructions, causes tests to fail due to javax.annotations-version defaulting to 1.2 under this JRE.  Tests are being forked from Maven so a workaround for running tests by forcing test not to fork.

        mvn -DforkCount=0 -Djavax.annotation-version=1.1 test


* Deployment instructions:

        apt-get install tomcat8

        usermod -U myuid --append -G tomcat8

        mvn -DskipTests=true compile war:exploded

    * Run the start script to to start the RabbitMQ instance with 
    * Run the start scripts to start the Device Service `start-device-server.sh`
    * Run the start scripts to start the Observation Service `start-observation-server.sh`
    * Run the start scripts to start the Locator Service `start-locator-server.sh`


### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines


### Who do I talk to? ###

* jeremy.reeve@igrow-systems.com
