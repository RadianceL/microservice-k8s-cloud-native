#!/bin/bash
export M2_HOME="/Users/eddie/Documents/utils/apache-maven-3.8.6"
export PATH="$M2_HOME/bin:$PATH"

cd ..
mvn clean

mvn clean deploy -B -f micro-service-cloud/micro-service-product/micro-service-product-client/pom.xml -am -Dmaven.test.skip=true

mvn clean deploy -B -f micro-service-cloud/micro-service-sso/micro-service-sso-client/pom.xml -am -Dmaven.test.skip=true

mvn clean deploy -B -f micro-service-dubbo/micro-service-dubbo-order/cloud-service-spring-cloud-client/pom.xml -am -Dmaven.test.skip=true