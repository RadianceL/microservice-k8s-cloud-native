#!/bin/bash
export M2_HOME="/Users/eddie/Documents/utils/apache-maven-3.8.6"
export PATH="$M2_HOME/bin:$PATH"

cd ..
mvn clean deploy -f pom.xml -N -Dmaven.test.skip=true

mvn clean deploy -f cloud-service-center-domain-service/pom.xml -N -Dmaven.test.skip=true
mvn clean deploy -f cloud-service-dubbo-cloud-provider/pom.xml -N -Dmaven.test.skip=true
mvn clean deploy -f cloud-service-spring-cloud-provider/pom.xml -N -Dmaven.test.skip=true