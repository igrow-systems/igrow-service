#!/bin/sh

java -Djersey.test.port=9997 $@ -jar device-service-1.0-SNAPSHOT-jar-with-dependencies.jar

