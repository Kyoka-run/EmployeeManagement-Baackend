pipeline {
    agent any

    environment {
        REGISTRY_CREDENTIALS = credentials('docker-hub-credentials')
        IMAGE_NAME = "kyoka74022/employee-management-backend"
        DOCKER_IMAGE_TAG = "latest"
        JAR_FILE = "target/employee-management-0.0.1-SNAPSHOT.jar"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Kyoka-run/EmployeeManagement-Backend.git'
            }
        }

        stage('Build') {
            steps {
                sh 'docker run --rm -v "$PWD":/app -w /app -v /root/.m2:/root/.m2 openjdk:17-jdk mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    docker.build(IMAGE_NAME + ":" + DOCKER_IMAGE_TAG, "--build-arg JAR_FILE=${JAR_FILE} .")
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('', REGISTRY_CREDENTIALS) {
                        docker.image(IMAGE_NAME + ":" + DOCKER_IMAGE_TAG).push()
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