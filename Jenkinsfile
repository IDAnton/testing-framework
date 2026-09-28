pipeline {
    tools {
        dockerTool 'all-docker'
    }

    agent any

    environment {
        ENV = 'staging'
    }

    stages {
        stage('Run Tests via Docker Compose Network') {
            steps {
                echo "Запуск всей инфраструктуры и тестов в единой Docker Compose сети..."

                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh 'rm -rf target/allure-results && mkdir -p target/allure-results'

                    sh 'docker compose up --build --exit-code-from app-tests'
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo "Сборка результатов и публикация Allure-отчета..."
                allure includeProperties: false,
                       jdk: '',
                       properties: [],
                       reportBuildPolicy: 'ALWAYS',
                       results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            echo "Очистка Docker-контейнеров..."
            sh 'docker compose down -v'
        }
    }
}
