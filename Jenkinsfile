pipeline {
    tools {
        dockerTool 'all-docker'
    }

    agent {
        docker {
            image 'eclipse-temurin:25-jdk-noble'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    environment {
        ENV = 'staging'
    }

    stages {
        stage('Initialize and Environment Check') {
            steps {
                echo "Проверяем окружение внутри Docker-контейнера:"
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Run Java Automation Tests') {
            steps {
                echo "Запуск автотестов на окружении: ${env.ENV}"
                sh 'mvn test -Dspring.classformat.ignore=true'
            }
        }
    }

    post {
        always {
            echo "Публикация результатов в Allure..."
            allure includeProperties: false,
                   jdk: '',
                   properties: [],
                   reportBuildPolicy: 'ALWAYS',
                   results: [[path: 'target/allure-results']]
        }
    }
}
