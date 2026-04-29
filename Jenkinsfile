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
            agent {
                docker { 
                    image 'bitnami/kubectl:latest' 
                    // Garante que ele acesse o arquivo kubeconfig gerado nos steps anteriores
                    args '-u root' 
                }
            }
            steps {
                withCredentials([file(credentialsId: 'sua-credencial-kubeconfig', variable: 'KUBE_CONFIG')]) {
                    sh 'kubectl --kubeconfig $KUBE_CONFIG apply -f k8s/postgres-deployment.yaml'
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
