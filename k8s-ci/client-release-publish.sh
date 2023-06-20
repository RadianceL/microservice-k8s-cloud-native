#!/bin/bash
export M2_HOME="/Users/eddie/Documents/utils/apache-maven-3.8.6"
export PATH="$M2_HOME/bin:$PATH"

cd ..
mvn clean

mvn clean deploy -B -f cloud-service-center-domain-service/cloud-service-center-service-client/pom.xml -am -Dmaven.test.skip=true

mvn clean deploy -B -f cloud-service-dubbo-cloud-provider/cloud-service-dubbo-cloud-client/pom.xml -am -Dmaven.test.skip=true

mvn clean deploy -B -f cloud-service-spring-cloud-provider/cloud-service-spring-cloud-client/pom.xml -am -Dmaven.test.skip=true