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
                        sh 'mvn clean compile -Dmaven.repo.local=./.m2/repository'
                        
                        // Run tests
                        sh 'mvn test -Dmaven.repo.local=./.m2/repository'
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
                    subject: "✓ SUCCESS: Jenkins Pipeline - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <h2 style="color: green;">Build Success ✓</h2>
                        <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Status:</strong> <span style="color: green;">SUCCESS ✓</span></p>
                        <p><strong>Triggered by:</strong> ${currentBuild.getBuildCauses()[0].userId ?: 'GitHub Webhook'}</p>
                        <p><strong>Git Author:</strong> ${env.CHANGE_AUTHOR ?: 'N/A'}</p>
                        <hr>
                        <h3>Test Results</h3>
                        <p>All Selenium tests passed successfully!</p>
                        <p>Total test cases executed: 12+</p>
                        <p><a href="${env.BUILD_URL}testReport/">View Test Report</a></p>
                        <hr>
                        <p><em>This email was sent to the user who triggered the pipeline.</em>
                    """,
                    to: "your-email@gmail.com",
                    from: 'jenkins@devops-assignment.com',
                    replyTo: 'noreply@devops-assignment.com',
                    mimeType: 'text/html'
                )
            }
        }
        failure {
            script {
                // Send email notification on failure
                emailext (
                    subject: "✗ FAILURE: Jenkins Pipeline - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <h2 style="color: red;">Build Failed ✗</h2>
                        <p><strong>Job:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                        <p><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>Status:</strong> <span style="color: red;">FAILURE ✗</span></p>
                        <p><strong>Triggered by:</strong> ${currentBuild.getBuildCauses()[0].userId ?: 'GitHub Webhook'}</p>
                        <p><strong>Git Author:</strong> ${env.CHANGE_AUTHOR ?: 'N/A'}</p>
                        <hr>
                        <h3>Failure Details</h3>
                        <p>One or more tests failed. Please review the test reports.</p>
                        <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
                        <p><a href="${env.BUILD_URL}testReport/">View Test Report</a></p>
                        <hr>
                        <p><em>This email was sent to the user who triggered the pipeline.</em>
                    """,
                    to: "your-email@gmail.com",
                    from: 'jenkins@devops-assignment.com',
                    replyTo: 'noreply@devops-assignment.com',
                    mimeType: 'text/html'
                )
            }
        }
    }
}
