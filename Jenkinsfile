pipeline {
    agent any

    environment {
        REGISTRY_CREDENTIALS = credentials('dockerhub')
        IMAGE_NAME = "kyoka74022/employee-management-backend"
        DOCKER_IMAGE_TAG = "latest"
        JAR_FILE = "target/employee-management-0.0.1-SNAPSHOT.jar"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Kyoka-run/EmployeeManagement-Backend.git', credentialsId: 'privatekey', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    docker.build("${IMAGE_NAME}:${BUILD_NUMBER}", "--build-arg JAR_FILE=${JAR_FILE} .")
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('', REGISTRY_CREDENTIALS) {
                        docker.image("${IMAGE_NAME}:${BUILD_NUMBER}").push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Docker Push completed successfully!'
        }
        failure {
            echo 'Build failed. Please check the logs.'
        }
    }
}

