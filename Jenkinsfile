pipeline {
    agent any

    environment {
        DOCKER_USER = credentials('dockerhub')
        SSH_KEY = credentials('ec2-ssh-key')
        SSH_USER = 'ec2-user'
        EC2_HOST = '3.252.231.197'
        IMAGE_NAME = 'kyoka74022/employee-management-backend'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Kyoka-run/EmployeeManagement-Backend.git', credentialsId: 'privatekey'
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
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                    docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest
                    docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    docker push ${IMAGE_NAME}:latest
                    docker logout
                    """
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'SSH_KEY')]) {
                    bat """
                    powershell -Command '
                    ssh -o StrictHostKeyChecking=no -i %SSH_KEY% %SSH_USER%@${EC2_HOST} "
                    docker login -u ${DOCKER_USER} -p ${DOCKER_PASS};
                    docker pull ${IMAGE_NAME}:${BUILD_NUMBER};
                    docker stop backend || echo. >nul;
                    docker rm backend || echo. >nul;
                    docker run -d --name backend -p 8080:8080 ${IMAGE_NAME}:${BUILD_NUMBER};
                    docker logout"
                    '
                    """
                }
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}


