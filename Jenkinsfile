pipeline {
    agent any
    environment {
        IMAGE_NAME = "kyoka74022/employee-management-backend"
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Kyoka-run/EmployeeManagement-Backend.git', 
                    credentialsId: 'privatekey', 
                    branch: 'main'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build and Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat """
                        docker build -t ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                        docker tag ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${IMAGE_NAME}:latest
                        docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                        docker push ${IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                        docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat """
                        ssh -i "C:\\codes\\EmployeeManagement\\employee-management-key.pem" -o StrictHostKeyChecking=no ec2-user@3.252.231.197 "docker login -u %DOCKER_USER% -p %DOCKER_PASS% && docker pull ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} && docker stop backend || true && docker rm backend || true && docker run -d --name backend -p 8080:8080 ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} && docker logout"
                    """
                }
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }
    }
}