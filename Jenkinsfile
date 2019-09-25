node {
    checkout scm
    stage('Build Maven') {
        docker.image('maven:3-alpine').inside('-v $HOME/.m2:/root/.m2') {
            sh './scripts/build-maven.sh'
        }
    }
    stage('Build Docker Image') {
        docker.build("${env.JOB_NAME}:${env.BUILD_NUMBER}")
    }
    stage('Push to AWS'){
        sh "./scripts/aws-ecr-push.sh ${env.JOB_NAME}:${env.BUILD_NUMBER} /home/.aws/credentials"
    }
}