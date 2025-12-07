pipeline {
    agent any
    
    environment {
        // Define your environment variables
        GITHUB_REPO = 'https://github.com/MoizZaidi7/AngularCRUD-main.git'
        DOCKER_IMAGE = 'markhobson/maven-chrome:latest'
        APP_URL = 'http://YOUR_EC2_IP:3000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out code from GitHub...'
                    checkout scm
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo 'Building the application...'
                    // If you need to build frontend/backend
                    dir('backend') {
                        sh 'npm install'
                    }
                }
            }
        }
        
        stage('Test') {
            agent {
                docker {
                    image "${DOCKER_IMAGE}"
                    args '-v /var/run/docker.sock:/var/run/docker.sock --network host'
                }
            }
            steps {
                script {
                    echo 'Running Selenium Tests...'
                    dir('selenium-tests') {
                        // Clean and compile
                        sh 'mvn clean compile'
                        
                        // Run tests
                        sh 'mvn test'
                    }
                }
            }
            post {
                always {
                    // Publish test results
                    junit '**/target/surefire-reports/*.xml'
                    
                    // Archive test reports
                    archiveArtifacts artifacts: '**/target/surefire-reports/*', allowEmptyArchive: true
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    echo 'Deploying application...'
                    // Add your deployment steps here
                    // For example: docker-compose up, PM2 restart, etc.
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed.'
        }
        success {
            script {
                // Send email notification on success
                emailext (
                    subject: "SUCCESS: Jenkins Pipeline - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <h2>Build Success</h2>
                        <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Status:</strong> SUCCESS ✓</p>
                        <p><strong>Triggered by:</strong> ${env.CHANGE_AUTHOR}</p>
                        <hr>
                        <p>All tests passed successfully!</p>
                    """,
                    to: "${env.CHANGE_AUTHOR_EMAIL}",
                    from: 'jenkins@yourdomain.com',
                    replyTo: 'jenkins@yourdomain.com',
                    mimeType: 'text/html',
                    attachmentsPattern: '**/target/surefire-reports/*.xml'
                )
            }
        }
        failure {
            script {
                // Send email notification on failure
                emailext (
                    subject: "FAILURE: Jenkins Pipeline - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <h2>Build Failed</h2>
                        <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Status:</strong> FAILURE ✗</p>
                        <p><strong>Triggered by:</strong> ${env.CHANGE_AUTHOR}</p>
                        <hr>
                        <p>Please check the console output for more details.</p>
                        <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
                    """,
                    to: "${env.CHANGE_AUTHOR_EMAIL}",
                    from: 'jenkins@yourdomain.com',
                    replyTo: 'jenkins@yourdomain.com',
                    mimeType: 'text/html',
                    attachmentsPattern: '**/target/surefire-reports/*.xml'
                )
            }
        }
    }
}
