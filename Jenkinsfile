pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/Kyoka-run/EmployeeManagement-Backend.git' // GitHub 仓库地址
        IMAGE_NAME = 'kyoka74022/employee-management-backend'                     // Docker 镜像名称
        AWS_EC2_IP = '3.252.231.197'                                             // EC2 实例的 IP 地址
        SSH_KEY = credentials('ec2-ssh-key')                                     // Jenkins 中配置的 SSH 私钥凭证
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')                         // Docker Hub 凭证
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: "${REPO_URL}", credentialsId: 'privatekey'
            }
        }

        stage('Build and Package') {
            steps {
                bat 'mvn clean package -DskipTests' // 使用 Maven 构建
            }
        }

        stage('Build Docker Image') {
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

        stage('Deploy to AWS EC2') {
            steps {
                withCredentials([
                    sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'SSH_KEY'),
                    usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                ]) {
                    bat """
                    powershell -Command '
                    ssh -o StrictHostKeyChecking=no -i %SSH_KEY% ec2-user@${AWS_EC2_IP} "
                    docker login -u %DOCKER_USER% -p %DOCKER_PASS%;
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
            echo 'Cleaning up Docker images locally...'
            bat 'docker rmi ${IMAGE_NAME}:${BUILD_NUMBER} || echo Skipped'
        }
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}


