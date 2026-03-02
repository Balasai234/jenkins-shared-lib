/*
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
*/
def call(Map config) {

    pipeline {
        agent { label 'AGENT-1' }

        parameters {
            choice(name: 'ENV', choices: config.environments, description: 'Select environment')
        }

        stages {

            stage('Build') {
                steps {
                    sh 'npm install'
                }
            }

            stage('Docker Build') {
                steps {
                    sh "docker build -t ${config.appName}:${params.ENV} ."
                }
            }

            stage('Deploy') {
                steps {
                    script {

                        // Dynamically construct credential ID
                        def dbCredentialId = "db-url-${params.ENV}"

                        withCredentials([string(credentialsId: dbCredentialId, variable: 'DB_URL')]) {

                            sh '''
                                echo "Deploying to ${ENV}"
                                echo "DB_URL is injected securely"
                                node app.js
                            '''
                        }
                    }
                }
            }

        }
    }
}
