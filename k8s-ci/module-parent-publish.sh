#!/bin/bash
cd ..
mvn clean deploy -f pom.xml -N -Dmaven.test.skip=true

## jar
mvn clean deploy -f micro-service-basic/pom.xml -N -Dmaven.test.skip=true
## pom
mvn clean deploy -f micro-service-cloud/micro-service-sso/pom.xml -N -Dmaven.test.skip=true
mvn clean deploy -f micro-service-cloud/micro-service-product/pom.xml -N -Dmaven.test.skip=true