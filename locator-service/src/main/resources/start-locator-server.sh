#!/bin/sh


java -Djersey.test.port=9996 $@ -jar locator-service-1.0-SNAPSHOT-jar-with-dependencies.jar

