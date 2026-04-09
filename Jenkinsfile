pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    environment {
        REGISTRY    = "localhost:5000"
        GITHUB_REPO = "https://github.com/DivineGod00/hospital-management-backendt.git"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: "${GITHUB_REPO}"
                echo 'Code pulled from GitHub successfully'
            }
        }

        // ── Maven builds ───────────────────────────────────
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
                dir('Doctor-Microservices') {     // ← fixed folder name
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        // ── Docker builds ──────────────────────────────────
        stage('Docker Build All Images') {
            steps {
                bat "docker build -t %REGISTRY%/hospital-eureka:latest   ./Hospital_Management_Service_Registry"
                bat "docker build -t %REGISTRY%/hospital-gateway:latest  ./Hospital-api-gateway"
                bat "docker build -t %REGISTRY%/hospital-patient:latest  ./Patient-Microservices"
                bat "docker build -t %REGISTRY%/hospital-doctor:latest   ./Doctor-Microservices"  // ← fixed
            }
        }

        // ── Push to registry ───────────────────────────────
        stage('Push to Local Registry') {
            steps {
                bat "docker push %REGISTRY%/hospital-eureka:latest"
                bat "docker push %REGISTRY%/hospital-gateway:latest"
                bat "docker push %REGISTRY%/hospital-patient:latest"
                bat "docker push %REGISTRY%/hospital-doctor:latest"
            }
        }

        // ── Start DB first then all services ───────────────
        stage('Start Database') {
            steps {
                // stop and remove old DB container if exists
                bat "docker stop hospital-db || exit 0"
                bat "docker rm hospital-db || exit 0"

                // start fresh PostgreSQL container
                bat "docker run -d --name hospital-db --network hospital-net ^
                    -e POSTGRES_USER=postgres ^
                    -e POSTGRES_PASSWORD=dev123 ^
                    -e POSTGRES_DB=Hospital-Management ^
                    -p 5432:5432 postgres:15"

                // wait 15 seconds for DB to be ready
                bat "timeout /t 15"
                echo 'Database started successfully'
            }
        }

        // ── Deploy all services ────────────────────────────
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