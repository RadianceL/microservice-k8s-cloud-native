pipeline {
    agent any
	environment {
		harborUser = 'admin'
		harborPasswd = 'Harbor12345'
		HarborAddress = 'harbor.wielun.com'
		harborRepo = 'library'
	}
    stages {
        stage('git拉取代码') {
            steps {
				git credentialsId: '0c71c0f9-8277-493b-xxxx-540a9324cf08', url: 'https://jihulab.com/xxxx/java-demo.git'
            }
        }

        stage('maven编译') {
           steps {
                    sh '''JAVA_HOME=/usr/local/jdk
                    PATH=$PATH:$JAVA_HOME/bin
                    /usr/local/maven/bin/mvn clean package -Dmaven.test.skip=true'''
                }
        }
        stage('生成自定义镜像') {
           steps {
                    sh '''docker build -t ${JOB_NAME}:latest .'''
                }
        }
        stage('上传自定义镜像到harbor') {
           steps {
                    sh '''docker login -u ${harborUser} -p ${harborPasswd} ${HarborAddress}
                    docker tag ${JOB_NAME}:latest ${HarborAddress}/${harborRepo}/${JOB_NAME}:latest
                    docker push ${HarborAddress}/${harborRepo}/${JOB_NAME}:latest'''
                }
        }
        stage('发送yaml到k8s-master并部署') {
           steps {
					sshPublisher(publishers: [sshPublisherDesc(configName: 'k8s-master', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''
					/usr/local/bin/kubectl apply -f /tmp/${JOB_NAME}/pipeline.yaml''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '${JOB_NAME}', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'pipeline.yaml')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
                }
        }
    }
}
