#!/bin/bash

set -e

pushd $(dirname $0) > /dev/null
SCRIPTPATH=$(pwd -P)
popd > /dev/null

build_image() {
    local img_name=$1
    local img_ver=$2
    local docker_context_path=$3
    local profile=$4
    local docker_file=$5

    docker build -t "${img_name}:${img_ver}" \
           -f "${docker_file}" \
           --build-arg profile=${profile} \
           "${docker_context_path}"
}

show_usage() {
   printf "usage: build_image.sh [-i IMAGE_NAME] [-v IMAGE_VERSION] -d ROOT_RESPOSITORY_DIR [-f DOCKER_FILE] -m\n"
   printf "\t-i IMAGE_NAME the name of building image, default is ksewen/ganyu\n"
   printf "\t-v IMAGE_VERSION the version of building image, default is 1.0.0.RELEASE\n"
   printf "\t-d ROOT_RESPOSITORY_DIR the root directory of repository\n"
   printf "\t-f DOCKER_FILE the location of Dockerfile, if it's ommited, default location is ROOT_RESPOSITORY_DIR/resources/rootfs/Dockerfile\n"
   printf "\t-p PROFILE the environment of program, default is test"
   printf ""
}

IMAGE_NAME=ksewen/ganyu
IMAGE_VERSION=1.0.1.RELEASE
PROFILE=test



while getopts 'hpi:v:d:f:' flag; do
  case "${flag}" in
    h) show_usage; exit ;;
    i) IMAGE_NAME=${OPTARG};;
    v) IMAGE_VERSION=${OPTARG};;
    d) REPOSITORY_DIR=${OPTARG};;
    f) DOCKER_FILE=${OPTARG};;
    p) PROFILE=production;;
    *) show_usage; exit ;;
  esac
done

if [ -z "${REPOSITORY_DIR}" ]; then
   show_usage
   exit 1
fi

if [ -z "${DOCKER_FILE}" ];
then
    DOCKER_FILE=${REPOSITORY_DIR}/ganyu/resources/rootfs/Dockerfile
fi


build_image "${IMAGE_NAME}" \
	"${IMAGE_VERSION}" \
	"${REPOSITORY_DIR}" \
        "${PROFILE}" \
        "${DOCKER_FILE}"
