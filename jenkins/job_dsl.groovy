folder('Whanos base images') {
    description('Folder for building Whanos base images.')
}

folder('Projects') {
    description('Folder containing linked project jobs.')
}

// def baseImages = ['c', 'java', 'javascript', 'python', 'befunge']
def baseImages = ['c', 'java', 'javascript', 'python']

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
            // shell("docker build -t whanos-${image} -f Dockerfile.base --build-arg REPO=${GITHUB_DOCKER_REGISTRY_REPO}")
            // shell("docker tag whanos-${image}:latest ghcr.io/${GITHUB_DOCKER_REGISTRY}/whanos-${image}:latest")
            // shell("echo ${GITHUB_DOCKER_REGISTRY_TOKEN} | docker login ghcr.io -u ${GITHUB_DOCKER_REGISTRY_USERNAME} --password-stdin")
            // shell("docker push ghcr.io/${GITHUB_DOCKER_REGISTRY}/whanos-${image}:latest")
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


// job("link-project") {
//     description("Job to links the specified project in the parameters to the Whanos infrastructure by creating a job")
//     wrappers {
//         preBuildCleanup {
//             includePattern('**/target/**')
//             deleteDirectories()
//             cleanupParameter('CLEANUP')
//         }
//     }
//     parameters {
//         stringParam('REPO_URL', '', 'SSH of the repository to link')
//         stringParam('DISPLAY_NAME', '', 'Display name for the job')
//     }
//     steps {
//         dsl {
//             scriptText = '''
// job(Projects/DISPLAY_NAME) {
//     wrappers {
//         preBuildCleanup { // Clean before build
//             includePattern('**/target/**')
//             deleteDirectories()
//             cleanupParameter('CLEANUP')
//         }
//     }
//     steps {
//         git branch: 'main',
//         credentialsId: 'admin-ssh-key',
//         url: 'REPO_URL'
//         shell('echo "Hello World"')
//     }
//     triggers {
//         scm('* * * * *')
//     }
// }
// '''
//         }
//     }
// }
