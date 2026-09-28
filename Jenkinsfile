pipeline {
    agent any

    environment {
        ENV = 'staging'
    }

    stages {
        stage('Run Tests in Docker Container') {
            steps {
                echo "Запуск автотестов"
                sh 'chmod +x run_tests.sh'
                sh './run_tests.sh'
            }
        }
    }

    post {
        always {
            echo "Публикация Allure-отчета..."
            allure includeProperties: false,
                   jdk: '',
                   properties: [],
                   reportBuildPolicy: 'ALWAYS',
                   results: [[path: 'target/allure-results']]
        }
    }
}
