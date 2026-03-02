def call(Map config) {

    pipeline {
        agent {label 'AGENT-1'}

        stages {

            stage('Build') {
                steps {
                    sh 'npm install'
                }
            }

            stage('Test') {
                steps {
                    sh 'echo Running tests...'
                }
            }

            stage('Docker Build') {
                steps {
                    sh "docker build -t ${config.appName}:latest ."
                }
            }

        }
    }
}