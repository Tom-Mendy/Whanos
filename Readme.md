# Whanos main documentation

## Table of Contents

- [Whanos main documentation](#whanos-main-documentation)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
    - [Description](#description)
    - [Features](#features)
  - [How to launch the project](#how-to-launch-the-project)
  - [Needed Files](#needed-files)

## Overview

### Description

Whanos is a comprehensive DevOps solution designed to automatically containerize and deploy applications to a Kubernetes cluster with ease. By combining the power of Docker, Jenkins, Ansible, and Kubernetes, Whanos offers a seamless infrastructure for developers to deploy their applications directly from a Git repository with minimal manual effort.

### Features

The goal of Whanos is to integrate various DevOps technologies into a unified system that performs the following tasks:

- Fetch the application source code from a Git repository.
- Detect the application's technology and dependencies.
- Automatically containerize the application into a Docker image.
- Push the Docker image to a private registry.
- Deploy the containerized application to a Kubernetes cluster if a whanos.yml configuration file exists.
- With Whanos, developers can benefit from a robust and automated pipeline that significantly simplifies the process of deploying applications.

## How to launch the project

First, you need to run the `run.sh` script.

```bash
./run.sh
```

When the script finishes, you can access the Jenkins instance by going to the following port: **30100**

## Needed Files

To launch this project you need to have the following files at the root of the repository:

- `linode_token.json`: This file should contain your access token to Linode API. It should be a JSON file with the following structure:

```json
{
  "linode_token": "YOUR_TOKEN"
}
```

To get this token you need to create a Linode account and generate a token [here](https://cloud.linode.com/profile/tokens), by clicking on the **"Create A Personal Access Token"** button.

- `private_key`: This file should contain your private key to access the Linode instances. [Here](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) is a guide on how to generate a private key. After, generating the key, take your public one, and add it [here](https://cloud.linode.com/profile/keys), and click on the **"Add An SSH Key"**. Do not forget to add a blank line at the end of the file that contains the private key.

- `id_rsa_single_line.pub`: This file should contain your public key to access the Linode instances. It should be a single line file.

- `.env`: This file should contain the following variables:
  - `GITHUB_DOCKER_REGISTRY`: Name of the registry on github (name of your github account but in lowercase).
  - `GITHUB_DOCKER_REGISTRY_USERNAME`: Name of your github account.
  - `GITHUB_DOCKER_REGISTRY_TOKEN`: The token to access the Docker registry refer to [this](./docs/registry.md) for more information.
  - `SSH_PRIVATE_KEY`: your private key to access the Linode instances. On a single line with `\n` to separate each line.
  - `USER_ADMIN_PASSWORD`: The name of the admin user of the jenkins instance.

All other variables that are used in the project are set automatically by some ansible jobs.
