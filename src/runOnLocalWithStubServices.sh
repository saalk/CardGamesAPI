#!/bin/sh

. ../loadsettings.sh

mvn clean -Dhost=local -Dproxy=none -Denv=T_stub -Dport=$SERVER_PORT -Dcucumber.options="--tags @stub" -P api-test integration-test
