#!/bin/sh


java -Djersey.test.port=9998 $@ -jar observation-service-1.0-SNAPSHOT-jar-with-dependencies.jar

