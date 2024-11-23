folder('Whanos base images') {
    description('Folder for building Whanos base images.')
}

folder('Projects') {
    description('Folder containing linked project jobs.')
}

def baseImages = ['whanos-c', 'whanos-java', 'whanos-javascript', 'whanos-python', 'whanos-befunge']

baseImages.each { image ->
    job("Whanos base images/${image}") {
        description("Build the ${image} base image")
        wrappers {
            preBuildCleanup {
                includePattern('**/target/**')
                deleteDirectories()
                cleanupParameter('CLEANUP')
            }
        }
        steps {
            shell("docker build -t ${image} .")
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
                            build job: 'Whanos base images/whanos-befunge'
                        }
                    }
                }
            }
            """)
        }
    }
}
