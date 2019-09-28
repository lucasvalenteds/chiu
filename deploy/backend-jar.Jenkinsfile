pipeline{
    agent any
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage("Package") {
            steps {
                dir("chiu-backend") {
                    sh "./gradlew shadowJar"
                }
            }
        }
    }
}
