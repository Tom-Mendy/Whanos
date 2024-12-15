# Jenkins Documentation

## Overview

Jenkins is an open-source automation server designed to orchestrate and manage various aspects of CI/CD pipelines. This documentation provides details on configuring a Jenkins instance using Configuration as Code (CasC) and Job DSL for Whanos, including job creation, plugin management, and security configurations.

---

## Files and Configuration

### 1. `casc.yml`

This is the Jenkins Configuration as Code (CasC) file, defining the primary configuration of the Jenkins instance.

#### Highlights

- **User Configuration**: Creates an admin user with credentials.
- **System Message**: Adds a welcome message.
- **Role-Based Authorization**: Defines admin roles and permissions.
- **Security**:
  - Job DSL script security disabled for flexibility.
  - SSH host key verification strategy set to "AcceptFirstConnectionStrategy".
- **Credentials Management**: Adds global SSH credentials for admin.

```yaml
jenkins:
  securityRealm:
    local:
      allowsSignup: false
      enableCaptcha: false
      users:
        - id: admin
          name: Admin
          password: ${USER_ADMIN_PASSWORD}
  systemMessage: "Welcome to the Whanos Jenkins instance."
  authorizationStrategy:
    roleBased:
      roles:
        global:
          - name: "admin"
            description: "Whanos master"
            permissions:
              - "Overall/Administer"
            entries:
              - user: "admin"
jobs:
  - file: /var/jenkins_home/job_dsl.groovy
security:
  globalJobDslSecurityConfiguration:
    useScriptSecurity: false
  gitHostKeyVerificationConfiguration:
    sshHostKeyVerificationStrategy: "AcceptFirstConnectionStrategy"
credentials:
  system:
    domainCredentials:
    - credentials:
      - basicSSHUserPrivateKey:
          id: "admin-ssh-key"
          privateKeySource:
            directEntry:
              privateKey: ${SSH_PRIVATE_KEY}
          scope: GLOBAL
          username: "admin"
```

---

### 2. `job_dsl.groovy`

This file defines Jenkins jobs and folders using the Job DSL plugin.

#### Key Jobs

1. **Folder Creation**:
   - Organizes Whanos base images and project jobs into folders.
2. **Base Image Build Jobs**:
   - Builds Docker images for supported programming languages (e.g., `whanos-java`, `whanos-python`).
   - Includes cleanup tasks and Docker build commands.
3. **Trigger Job**:
   - Triggers all base image build jobs sequentially.
4. **Project Linking Job**:
   - Links a specified Git repository to Jenkins.
   - Configures a Jenkins job for the project with Git SCM.

```groovy
folder('Whanos base images') {
    description('Folder for building Whanos base images.')
}

folder('Projects') {
    description('Folder containing linked project jobs.')
}

def baseImages = ['c', 'java', 'javascript', 'python', 'befunge', 'cpp', 'go', 'rust']

baseImages.each { image ->
    job("Whanos base images/whanos-${image}") {
        description("Build the whanos-${image} base image")
        wrappers {
            preBuildCleanup {
                includePattern('**/target/**')
                deleteDirectories()
                cleanupParameter('CLEANUP')
            }
        }
        steps {
            shell("""
echo "Building ${image} base image"
                cd /var/jenkins_home/docker_images/${image}/
                docker build -t whanos-${image} - < Dockerfile.base
            """)
        }
    }
}

job("Whanos base images/Build all base images") {
    description("Triggers all base images build jobs.")
    publishers {
        downstreamParameterized {
            baseImages.each { image ->
                trigger("Whanos base images/whanos-${image}") {
                    condition('SUCCESS')
                    triggerWithNoParameters()
                }
            }
        }
    }
}

job("link-project") {
    parameters {
        stringParam('REPO_URL_SSH', '', 'SSH of the repository to link')
        stringParam('DISPLAY_NAME', '', 'Display name for the job')
    }
    steps {
        dsl {
            scriptText = '''
job("Projects/${DISPLAY_NAME}") {
    scm {
        git {
            remote {
                url("${REPO_URL_SSH}")
                credentials('admin-ssh-key')
            }
            branch('main')
        }
    }
    triggers {
        cron('* * * * *')
    }
}'''
        }
    }
}
```

---

### 3. `plugins.txt`

Defines required Jenkins plugins for Whanos.

#### Example Plugins

- **Folder Management**: `cloudbees-folder`
- **Configuration Management**: `configuration-as-code`
- **Job DSL**: `job-dsl`
- **Security**: `credentials`, `role-strategy`
- **Git and Kubernetes Support**: `git`, `kubernetes`

```plaintext
cloudbees-folder
configuration-as-code
credentials
github
instance-identity
job-dsl
script-security
role-strategy
pipeline-utility-steps
kubernetes
kubernetes-cli
```

---

## Usage

### 1. Setting Up Jenkins

1. Deploy Jenkins and ensure the required plugins from `plugins.txt` are installed.
2. Place the `casc.yml` file in the configured CasC path.
3. Restart Jenkins to apply the configuration.

### 2. Running Jobs

- Use the "Whanos base images" folder to build Docker images.
- Use the "link-project" job to link new projects to Jenkins.

### 3. Manage Credentials

- Update environment variables (`${USER_ADMIN_PASSWORD}` and `${SSH_PRIVATE_KEY}`) with secure values.
