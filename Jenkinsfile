pipeline {
    tools {
        dockerTool 'all-docker'
    }

    agent any

    environment {
        ENV = 'staging'
    }

    stages {
        stage('Run Java 25 and Maven Tests inside Docker') {
            agent {
                docker {
                    image 'maven'
                    args '-v /var/run/docker.sock:/var/run/docker.sock -e TESTCONTAINERS_RYUK_DISABLED=true -v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                echo "Контейнер 'Maven + Java 25' успешно запущен"
                echo "Проверяем версии инструментов:"
                sh 'java -version'
                sh 'mvn -version'

                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    echo "Запуск автотестов..."
                    sh 'mvn test -Dspring.classformat.ignore=true -Dcucumber.plugin=io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm -Dallure.results.directory=target/allure-results'
                }
            }
        }

        stage('Generate Allure Report') {
            agent any
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
}
