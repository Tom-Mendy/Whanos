#!/usr/bin/env bash

LANGUAGE=""
REGISTRY="github.com/${GITHUB_DOCKER_REGISTRY_REPO}"
JOB_NAME=$1
IMAGES_DIR="/var/jenkins_home/docker_images"

if [[ $JOB_NAME == "" ]]; then
    echo ""
    exit 1
fi

LanguageNotSupported() {
    echo "Invalid project: too many languages matched."
    exit 1
}

if [[ -f Makefile ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="c"
fi
if [[ -f app/pom.xml ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="java"
fi
if [[ -f package.json ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="javascript"
fi
if [[ -f requirements.txt ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="python"
fi
if [[ -f app/main.bf ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="befunge"
fi

echo "language is ${LANGUAGE}"

if [[ ${LANGUAGE} == "" ]]; then
    echo "Invalid project: no language matched."
    exit 1
fi

# $1: the name of the job
image_name="whanos-${JOB_NAME}-${LANGUAGE}"
image_name_remote_repo="ghcr.io/${GITHUB_DOCKER_REGISTRY}/whanos-${JOB_NAME}-${LANGUAGE}"

if [[ -f Dockerfile ]]; then
    docker build . -t "${image_name}"
    docker tag "${image_name}:latest" "${image_name_remote_repo}:latest"
    echo ${GITHUB_DOCKER_REGISTRY_TOKEN} | docker login ghcr.io -u ${GITHUB_DOCKER_REGISTRY_USERNAME} --password-stdin
    docker push "${image_name_remote_repo}:latest"
else
    docker build . \
        -f "${IMAGES_DIR}/${LANGUAGE}/Dockerfile.standalone" \
        -t "${image_name}-standalone"
    docker tag "${image_name}-standalone:latest" "${image_name_remote_repo}-standalone:latest"
    echo ${GITHUB_DOCKER_REGISTRY_TOKEN} | docker login ghcr.io -u ${GITHUB_DOCKER_REGISTRY_USERNAME} --password-stdin
    docker push "${image_name_remote_repo}-standalone:latest"
fi

if [[ -f "whanos.yml" ]]; then
    echo "launch kubernetes"
fi
