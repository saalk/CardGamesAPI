#! /bin/sh

echo server_port=$((RANDOM+10000))

server_port=8383
container_port=$((server_port+1))

echo "--- Using port ${server_port}"

export MAVEN_OPTS="$MAVEN_OPTS -Xmx512m -XX:MaxPermSize=256m"
mvn clean -Dhost=local -Dproxy=none -Denv=T_stub \
    -Dport=${server_port} -Dtomcat.port=${server_port} -Djersey.config.test.container.port=${container_port} \
    -Dcucumber.options="--tags @stub" -P run-tomcat,api-test, verify

echo "--- Finished, press ENTER"
read
#echo "Done"
#sleep 6
