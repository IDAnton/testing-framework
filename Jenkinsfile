pipeline {
    tools {
        dockerTool 'all-docker'
    }

    agent any

    environment {
        ENV = 'staging'
    }

    stages {
        stage('Initialize and Environment Check') {
            agent {
                dockerContainer {
                    image 'eclipse-temurin:25-jdk-noble'
                }
            }
            steps {
                echo "Проверяем окружение внутри Docker контейнера:"
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Run Java Automation Tests') {
            agent {
                dockerContainer {
                    image 'eclipse-temurin:25-jdk-noble'
                }
            }
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    echo "Запуск автотестов на окружении: ${env.ENV}"
                    sh 'mvn test -Dspring.classformat.ignore=true'
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo "Публикация результатов в Allure на хостовом агенте..."
                allure includeProperties: false,
                       jdk: '',
                       properties: [],
                       reportBuildPolicy: 'ALWAYS',
                       results: [[path: 'target/allure-results']]
            }
        }
    }
}
