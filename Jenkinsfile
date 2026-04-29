pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'robsonsqn/moneymoney'
        DOCKER_TAG = 'latest'

        DOCKER_CREDENTIALS_ID = 'dockerhub-cred'
        KUBECONFIG_CREDENTIALS_ID = 'k8s-kubeconfig'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker') {
            steps {
                script {
                    echo "Construindo a imagem Docker a partir do Dockerfile..."
                    sh "docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ."
                }
            }
        }

        stage('Push Docker') {
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    echo "Autenticando no Docker Hub..."
                    sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                    
                    echo "Enviando a imagem para o Docker Hub..."
                    sh "docker push ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}"
                }
            }
        }

       stage('Deploy K8s') {
            steps {
                withCredentials([file(credentialsId: 'k8s-kubeconfig', variable: 'KUBE_CONFIG')]) {
                    script {
                        echo "Baixando o executável do kubectl..."
                        sh 'curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"'
                        sh 'chmod +x ./kubectl'

                        echo "Aplicando os Manifestos no Cluster Kubernetes..."
                        sh './kubectl --kubeconfig $KUBE_CONFIG apply -f k8s/postgres-deployment.yaml'
                    }
                }
            }
        }
    }

    post {
        always {
            sh "docker logout"
            cleanWs()
        }
    }
}
