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

        stage('Build Docker Image') {
            steps {
                sh 'mvn clean package -DskipTests'
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
                sh 'docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest'
            }
        }

        stage('Login to Docker Hub') {
            steps {
                sh '''
                echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                sh '''
                docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                docker push ${IMAGE_NAME}:latest
                '''
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ${SSH_USER}@${AWS_EC2_IP} <<EOF
                    echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                    docker pull ${IMAGE_NAME}:${BUILD_NUMBER}
                    docker stop backend || true
                    docker rm backend || true
                    docker run -d --name backend -p 8080:8080 ${IMAGE_NAME}:${BUILD_NUMBER}
                    docker logout
                    EOF
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up Docker images locally...'
            sh 'docker rmi ${IMAGE_NAME}:${BUILD_NUMBER} || true'
        }
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}


