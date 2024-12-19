pipeline {
    agent any
    environment {
        IMAGE_NAME = "kyoka74022/employee-management-backend"
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
        JAR_FILE = "target/employee-management-0.0.1-SNAPSHOT.jar"
        EC2_HOST = "ec2-user@3.252.231.197"
        RDS_ENDPOINT = "employee-management-db.ctmcuac0g16u.eu-west-1.rds.amazonaws.com"
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

        stage('Docker Build') {
            steps {
                bat """
                    docker build -t ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                    docker tag ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat """
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
                    withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'SSH_KEY')]) {
                        bat """
                            ssh -i "%SSH_KEY%" -o StrictHostKeyChecking=no ${EC2_HOST} "docker login -u %DOCKER_USER% -p %DOCKER_PASS% && docker pull ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} && docker stop backend || true && docker rm backend || true && docker run -d --name backend -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://${RDS_ENDPOINT}:3306/employee_management -e SPRING_DATASOURCE_USERNAME=admin -e SPRING_DATASOURCE_PASSWORD=Cinder1014 ${IMAGE_NAME}:${DOCKER_IMAGE_TAG} && docker logout"
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
        always {
            bat 'docker logout'
        }
    }
}