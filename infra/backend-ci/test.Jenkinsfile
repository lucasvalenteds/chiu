pipeline{
    agent any
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage("Test") {
            steps {
                dir("chiu-backend") {
                    sh "./gradlew test"
                }
            }
        }
    }
}
