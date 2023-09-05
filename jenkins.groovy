pipeline {
    agent any

    stages {
        stage('Check Current Directory') {
            steps {
                script {
                    // 获取当前工作目录
                    def currentDir = pwd()
                    // 定义所需的目标路径
                    def targetDir = "/var/jenkins_home/inventory/github"
                    if (currentDir == targetDir) {
                        echo "当前路径正确：$currentDir"
                    } else {
                        error "当前路径不正确。期望路径为：$targetDir，但实际路径为：$currentDir"
                    }
                }
            }
        }

        stage('Check Remote Repository') {
            steps {
                script {
                    def repositoryURL = 'git@github.com:Vergilraven/stable-diffusion.git'
                    // def repositoryURL = 'https://github.com/yourusername/yourrepository.git'
                    def gitCommand = "git ls-remote ${repositoryURL}"
                    def result = sh(script: gitCommand, returnStatus: true)

                    if (result == 0) {
                        echo "收到触发条件,开始更新代码"
                        sh 'git pull origin master'
                        // 执行其他操作

                    } else {
                        error "仓库不存在或无法访问,开始执行python3脚本为您重新配置登录的权限配置"
                        echo '******************************开始克隆仓库代码******************************'
                        sh "git clone ${repositoryURL}"
                        // 执行其他操作或中断构建
                        // 或者关联其他测试脚本的items
                    }
                }
            }
        }
    }
}
