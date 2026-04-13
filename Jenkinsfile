pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        REGISTRY    = "localhost:5000"
        GITHUB_REPO = "https://github.com/DivineGod00/hospital-management-backend.git"
    }

    stages {

        stage('List Folders') {
            steps {
                bat 'dir'
            }
        }

        stage('Build Eureka Server') {
            steps {
                dir('Hospital_Management_Service_Registry') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build API Gateway') {
            steps {
                dir('Hospital-api-gateway') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Patient Service') {
            steps {
                dir('Patient-Microservices') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Doctor Service') {
            steps {
                dir('Doctor-Microservices') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Docker Build All Images') {
            steps {
                bat "docker build -t %REGISTRY%/hospital-eureka:latest ./Hospital_Management_Service_Registry"
                bat "docker build -t %REGISTRY%/hospital-gateway:latest ./Hospital-api-gateway"
                bat "docker build -t %REGISTRY%/hospital-patient:latest ./Patient-Microservices"
                bat "docker build -t %REGISTRY%/hospital-doctor:latest ./Doctor-Microservices"
            }
        }

        stage('Push to Local Registry') {
            steps {
                bat "docker push %REGISTRY%/hospital-eureka:latest"
                bat "docker push %REGISTRY%/hospital-gateway:latest"
                bat "docker push %REGISTRY%/hospital-patient:latest"
                bat "docker push %REGISTRY%/hospital-doctor:latest"
            }
        }

        stage('Start Database') {
            steps {
                bat "docker stop hospital-db || exit 0"
                bat "docker rm hospital-db || exit 0"
                bat "docker run -d --name hospital-db --network hospital-net -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=dev123 -e POSTGRES_DB=Hospital-Management -p 5432:5432 postgres:15"
                bat "timeout /t 15"
                echo 'Database started successfully'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker-compose down'
                bat 'docker-compose up -d'
                echo 'All services deployed successfully'
            }
        }
    }

    post {
        success {
            echo '========================================='
            echo 'Pipeline SUCCESS — all services running'
            echo '========================================='
        }
        failure {
            echo '========================================='
            echo 'Pipeline FAILED — check logs above'
            echo '========================================='
        }
    }
}