folder('Whanos base images') {
    description('Folder for building Whanos base images.')
}

folder('Projects') {
    description('Folder containing linked project jobs.')
}

def baseImages = ['c', 'java', 'javascript', 'python', 'befunge', 'cpp', 'go']

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
                cd /var/jenkins_home/docker_images/${image}/
                docker build -t whanos-${image} - < Dockerfile.base
            """)
        }
    }
}

job("Whanos base images/Build all base images") {
    description("Triggers all base images build jobs.")
    wrappers {
        preBuildCleanup {
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    // Post-build Actions
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
    description("Job to links the specified project in the parameters to the Whanos infrastructure by creating a job")
    parameters {
        stringParam('REPO_URL_SSH', '', 'SSH of the repository to link')
        stringParam('DISPLAY_NAME', '', 'Display name for the job')
    }
    steps {
        dsl {
            scriptText = '''
job("Projects/${DISPLAY_NAME}") {
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    scm {
        git {
            remote {
                url("${REPO_URL_SSH}")
                credentials('admin-ssh-key')
            }
            branch('main')
        }
    }
    steps {
        shell("/var/jenkins_home/build.sh ${DISPLAY_NAME}")
    }
    triggers {
        scm('* * * * *')
    }
}
'''
        }
    }
}
