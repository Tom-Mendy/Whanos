FROM jenkins/jenkins:lts

WORKDIR /var/jenkins_home

USER root

# Install make, gcc and libcriterion-dev for testing
RUN apt-get update && apt-get install build-essential docker.io -y
RUN usermod -aG docker jenkins

# install kubectl
RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
RUN install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

USER jenkins

# Install Jenkins plugins
COPY ./plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

# setup configuration as code configuration
ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc.yaml
COPY ./casc.yml /var/jenkins_home/casc.yaml

# setup job dsl configuration
COPY job_dsl.groovy /var/jenkins_home/job_dsl.groovy

# build script for whanos image
COPY build.sh /var/jenkins_home/build.sh

EXPOSE 8080
EXPOSE 50000
