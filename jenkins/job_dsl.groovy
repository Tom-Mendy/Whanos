// def folderName = "Tools"
folder('Whanos base images') {
    description('Folder for miscellaneous tools.')
}



job('Whanos base images/whanos-c') {
    description('build the c base image')
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('docker build -t whanos-c .')
    }
}

job('Whanos base images/whanos-java') {
    description('build the java base image')
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('docker build -t whanos-java .')
    }
}





job('Whanos base images/whanos-javascript') {
    description('build the javascript base image')
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('docker build -t whanos-javascript .')
    }
}

job('Whanos base images/whanos-python') {
    description('build the python base image')
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('docker build -t whanos-python .')
    }
}

job('Whanos base images/whanos-befunge') {
    description('build the befunge base image')
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('docker build -t whanos-befunge .')
    }
}


job('Whanos base images/Build all base images') {
    description('Build all base images')
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
}


folder('Projects') {
    description('Folder for miscellaneous tools.')
}





folder('Tools') {
    description('Folder for miscellaneous tools.')
}

job('Tools/clone-repository') {
    description('Job to clone a repository')
    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('git clone $GIT_REPOSITORY_URL')
    }
}

job('Tools/SEED') {
    description('Seed job to create other jobs')
    parameters {
        stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "EpitechIT31000/chocolatine")')
        stringParam('DISPLAY_NAME', '', 'Display name for the job')
    }
    steps {
        dsl {
            scriptText = '''
job(DISPLAY_NAME) {
    scm {
        git {
            remote {
                github(GITHUB_NAME)
            }
        }
    }
    wrappers {
        preBuildCleanup { // Clean before build
            includePattern('**/target/**')
            deleteDirectories()
            cleanupParameter('CLEANUP')
        }
    }
    steps {
        shell('make fclean')
        shell('make')
        shell('make tests_run')
        shell('make clean')
    }
    triggers {
        scm('* * * * *')
    }
}
'''
        }
    }
}
