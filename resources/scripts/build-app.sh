#!/bin/bash

pushd $(dirname $0) > /dev/null
SCRIPTPATH=$(pwd -P)
popd > /dev/null

set_profile() {
    cp -f /ganyu/resources/properties/application-"$profile".yaml \
        /ganyu/src/main/resources/application.yaml
}

do_package() {
  cd /ganyu/
  mvn package
}

set_profile
do_package