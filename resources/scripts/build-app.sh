#!/bin/bash

pushd $(dirname $0) > /dev/null
SCRIPTPATH=$(pwd -P)
popd > /dev/null

set_profile() {
    cp -f /ganyu/resources/properties/application-"$profile".properties \
        /ganyu/src/main/resources/application.properties
}

do_package() {
  cd /ganyu/
  mvn package
}

set_profile
do_package