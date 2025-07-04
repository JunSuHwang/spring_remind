pipeline{
    agent any
    environment{
        SCRIPT_PATH = '/srv/spring_remind'
    }
    tools{
        gradle 'gradle 8.12.1'
    }
    stages{
        stage('Checkout') {
            steps{
                checkout scm
            }
        }
        stage('Prepare'){
            steps{
                sh 'gradle clean'
            }
        }
        stage('Replace Prod & Secret Properties'){
            steps{
                withCredentials([
                file(credentialsId: 'prod_yml', variable: 'prod_yml'),
                file(credentialsId: 'key_yml', variable: 'key_yml'),
                file(credentialsId: 'db_env', variable: 'db_env')
                ]) {
                    script{
                        sh '''
                            cp $prod_yml ./src/main/resources/application-prod.yml
                            cp $key_yml ./src/main/resources/application-key.yml
                            cp $db_env ./proj/.env
                        '''
                    }
                }
            }
        }
        stage('Build'){
            steps{
                sh 'gradle build -x test'
            }
        }
        // 테스트 미완성
        //stage('Test'){
            //steps{
                //sh 'gradle test'
            //}
        //}
        stage('Deploy'){
            steps{
                sh '''
                    cp -f ./proj/docker-compose-prod.yml ${SCRIPT_PATH}
                    cp -f ./proj/Dockerfile ${SCRIPT_PATH}
                    cp -f ./proj/deploy.sh ${SCRIPT_PATH}
                    cp -f ./proj/.env ${SCRIPT_PATH}
                    cp -rf ./proj/nginx ${SCRIPT_PATH}
                    cp -f ./build/libs/*.jar ${SCRIPT_PATH}
                    chmod +x ${SCRIPT_PATH}/deploy.sh
                    ${SCRIPT_PATH}/deploy.sh
                '''
            }
        }
    }
}