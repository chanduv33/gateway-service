pipeline {
    agent any
    tools {
        maven 'maven'
        'org.jenkinsci.plugins.docker.commons.tools.DockerTool' 'docker'
    }

    environment {
        dockerHome = tool 'docker'
        PATH = "${dockerHome}/bin:$PATH"
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage ('Build and Push') {
            steps{
            withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {

                sh "docker build . -t gateway:${env.BUILD_NUMBER}"
                sh "docker login -u $USERNAME -p $PASSWORD"
                sh "docker tag gateway:${env.BUILD_NUMBER} chanduv33/gateway-service:${env.BUILD_NUMBER}"
                sh "docker push chanduv33/gateway-service:${env.BUILD_NUMBER}"
            }
            }
        }
        stage ('Delpoy') {
            steps {
              script {
		               sh "../jenkins/deploy.sh \"${env.BUILD_NUMBER}\" /home/chandrasekharvemugadda/jenkins/gateway"
	            }   
            }           
        }
    }
}