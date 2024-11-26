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
            shell("docker build -t whanos-${image} -f /var/jenkins_home/docker_images/${image}/Dockerfile.base .")
            shell("docker tag whanos-${image}:latest $REGISTRY_HOST:5000/whanos-${image}:latest")
            shell("docker push $REGISTRY_HOST:5000/whanos-${image}:latest")
        }
    }
}


pipelineJob('Whanos base images/Build all base images') {
    definition {
        cps {
            script("""
            pipeline {
                agent any
                stages {
                    stage('Trigger All') {
                        steps {
                            build job: 'Whanos base images/whanos-c'
                            build job: 'Whanos base images/whanos-java'
                            build job: 'Whanos base images/whanos-javascript'
                            build job: 'Whanos base images/whanos-python'
                            // build job: 'Whanos base images/whanos-befunge'
                        }
                    }
                }
            }
            """)
        }
    }
}
