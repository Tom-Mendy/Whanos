#!/usr/bin/env bash

LANGUAGE=""
REGISTRY="ghcr.io/${GITHUB_DOCKER_REGISTRY}"
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
# BONUS LANGUAGES
if [[ -f app/go.mod ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="go"
fi
if [[ -f CMakeLists.txt ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="cpp"
fi
if [[ -f Cargo.toml ]]; then
    if [[ ${LANGUAGE} != "" ]]; then
        LanguageNotSupported
    fi
    LANGUAGE="rust"
fi


if [[ ${LANGUAGE} == "" ]]; then
    echo "Invalid project: no language matched."
    exit 1
fi

echo "language is ${LANGUAGE}"

# $1: the name of the job
image_name="whanos-${JOB_NAME}-${LANGUAGE}"
image_name_remote_repo="${REGISTRY}/whanos-${JOB_NAME}-${LANGUAGE}"

if [[ -f Dockerfile ]]; then
    docker build . -t "${image_name}"
    docker tag "${image_name}:latest" "${image_name_remote_repo}:latest"
    echo ${GITHUB_DOCKER_REGISTRY_TOKEN} | docker login ghcr.io -u ${GITHUB_DOCKER_REGISTRY_USERNAME} --password-stdin
    docker push "${image_name_remote_repo}:latest"
else
    docker build . \
        -f "${IMAGES_DIR}/${LANGUAGE}/Dockerfile.standalone" \
        -t "${image_name}"
    docker tag "${image_name}:latest" "${image_name_remote_repo}:latest"
    echo ${GITHUB_DOCKER_REGISTRY_TOKEN} | docker login ghcr.io -u ${GITHUB_DOCKER_REGISTRY_USERNAME} --password-stdin
    docker push "${image_name_remote_repo}:latest"
fi

if [[ -f "whanos.yml" ]]; then
    echo "launch kubernetes"
    # Authenticate kubectl
    kubectl config set-cluster my-cluster --server=${KUBE_SERVER} --insecure-skip-tls-verify=true
    kubectl config set-credentials my-user --token=${KUBE_TOKEN}
    kubectl config set-context my-context --cluster=my-cluster --user=my-user --namespace=default
    kubectl config use-context my-context

    # Test kubectl connectivity
    kubectl get nodes

    /var/jenkins_home/kubernetes/generate_kubernetes_cluster.py "$image_name_remote_repo"
    kubectl apply -f whanos-deployment.yaml
    kubectl apply -f whanos-service.yaml
fi
