#!/bin/sh

. loadsettings.sh

echo "--- Using port ${SERVER_PORT}"
echo "--- Using port ${SERVER_PORT}"

mvn clean test -Dhost=local -Dproxy=none -Denv=T_stub -Dport=${SERVER_PORT} -Dcucumber.options="--tags @api" -P run-tomcat api-test
echo "--- Finished, press ENTER"
read
#echo "Done"
#sleep 6