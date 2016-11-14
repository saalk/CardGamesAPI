#!/bin/sh

. loadsettings.sh

mvn clean test -Dhost=local -Dproxy=none -Denv=T_stub -Dport=${SERVER_PORT} -Dcucumber.options="--tags @stub --tags @stub" -P
